package ua.zp.starinet.starinet_02072015;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.math.BigDecimal;

import retrofit.RetrofitError;
import ua.zp.starinet.starinet_02072015.model.DbItem;
import ua.zp.starinet.starinet_02072015.network.DataService;
import ua.zp.starinet.starinet_02072015.requests.GetWallpaperRequest;

public class MainActivity extends Activity {

    Animation anim;
    Animation animup;

    public String editLogin;
    public String editPass;

    //public int chekStatus=0;
    final int chekErrorOK=0;
    final int chekErrorLoginPass=1;
    public int chekError=2;

    TextView txtLoginPass;
    EditText txtEditLogin;
    EditText txtEditPass;
    CheckBox seePass;
    CheckBox saveLoginPass;
    ImageView imgLoadBD;

    Button btnNext;
    Button noPass;
    ImageView imgLogo;

    private static final String TAG = "myLogs";
    SpiceManager mSpiceManager = new SpiceManager(DataService.class);

    SharedPreferences sPref;
    private static long back_pressed;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.logo_start);
        Log.d(TAG, "Новая сессия");

        imgLogo = (ImageView) findViewById(R.id.imageView);
        anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.logo);
        imgLogo.startAnimation(anim);

        //задержка в секундах
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                setContentView(R.layout.screen_start);

                txtEditLogin = (EditText) findViewById(R.id.txtEditLogin);
                txtEditPass = (EditText) findViewById(R.id.txtEditPass);
                txtLoginPass = (TextView) findViewById(R.id.txtLoginPass);
                seePass = (CheckBox) findViewById(R.id.seePass);
                saveLoginPass = (CheckBox) findViewById(R.id.saveLoginPass);
                btnNext = (Button) findViewById(R.id.buttonNext);
                noPass = (Button) findViewById(R.id.noPass);

                animup = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alphaup);
                txtEditLogin.startAnimation(animup);
                txtEditPass.startAnimation(animup);
                seePass.startAnimation(animup);
                btnNext.startAnimation(animup);
                txtLoginPass.startAnimation(animup);
                noPass.startAnimation(animup);
                saveLoginPass.startAnimation(animup);

                //читаем логин и пароль
                sPref = getPreferences(MODE_PRIVATE);
                txtEditLogin.setText(sPref.getString("Login", ""));
                txtEditPass.setText(sPref.getString("Pass", ""));
                Log.d(TAG, "логин и пароль прочитан");
                //

                View.OnClickListener onClickListenerNextSee = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.seePass:

                                if (seePass.isChecked()) {
                                    txtEditPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                } else {
                                    txtEditPass.setInputType(129);
                                }
                                break;

                            case R.id.buttonNext:

                                editLogin = txtEditLogin.getText().toString();
                                editPass = txtEditPass.getText().toString();

                                if (txtEditLogin.getText().toString().equals("") | txtEditPass.getText().toString().equals(""))
                                {
                                    // Здесь код, если EditText пуст
                                    Toast.makeText(MainActivity.this, "Введите логин и пароль", Toast.LENGTH_LONG).show();
                                    break;
                                }
                                else
                                {
                                    // если есть текст, то здесь другой код
                                    btnNext.setVisibility(View.INVISIBLE);

                                    imgLoadBD = (ImageView) findViewById(R.id.imgLoadBD);
                                    imgLoadBD.setBackgroundResource(R.drawable.anim_loader_db);
                                    imgLoadBD.setVisibility(View.VISIBLE);

                                    AnimationDrawable animLoadDB = (AnimationDrawable) imgLoadBD.getBackground();
                                    animLoadDB.start();

                                    if (saveLoginPass.isChecked()) {
                                        //Записываем логин и пароль
                                        SharedPreferences.Editor ed = sPref.edit();
                                        ed.putString("Login", txtEditLogin.getText().toString());
                                        ed.putString("Pass", txtEditPass.getText().toString());
                                        ed.commit();
                                        Log.d(TAG, "логин и пароль записан");
                                        //
                                    } else {
                                        SharedPreferences.Editor ed = sPref.edit();
                                        ed.putString("Login", "");
                                        ed.putString("Pass", "");
                                        ed.commit();
                                        Log.d(TAG, "логин и пароль не записан т.к. не нажат чекбокс");
                                    }

                                    mSpiceManager.execute(new GetWallpaperRequest(editLogin, editPass), new GetWallpaperListener());
                                }
                                break;

                            case R.id.noPass:
                                //Toast.makeText(MainActivity.this, "Тестовый вход", Toast.LENGTH_LONG).show();
                                Intent intent;
                                intent = new Intent(MainActivity.this, StartActivityItem.class);
                                startActivity(intent);
                                break;

                        }
                    }
                };
                btnNext.setOnClickListener(onClickListenerNextSee);
                seePass.setOnClickListener(onClickListenerNextSee);
                noPass.setOnClickListener(onClickListenerNextSee);
            }
        }, 0); //3000

    };

        @Override
        protected void onStart() {
            super.onStart();
            mSpiceManager.start(this);
        }

        @Override
        protected void onStop() {
            super.onStop();
            mSpiceManager.shouldStop();
        }

        class GetWallpaperListener implements RequestListener<DbItem> {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                RetrofitError cause = (RetrofitError)spiceException.getCause();
                if ( cause == null ||
                        cause.isNetworkError() ||
                        cause.getResponse() == null) {
                    Toast.makeText(MainActivity.this, "Нет доступа к Интернету", Toast.LENGTH_LONG).show();
                    AnimationDrawable animLoadDB = (AnimationDrawable) imgLoadBD.getBackground();
                    animLoadDB.stop();
                    imgLoadBD.setVisibility(View.INVISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                }

                return;
            }

            @Override
            public void onRequestSuccess(DbItem dbItems) {

                    if (dbItems==null) {
                        Toast.makeText(MainActivity.this, "Нет даных!", Toast.LENGTH_LONG).show();
                        return;
                    }

                chekError = dbItems.chekError;

                Log.d(TAG, "Error: " + chekError);

                AnimationDrawable animLoadDB = (AnimationDrawable) imgLoadBD.getBackground();
                animLoadDB.stop();
                imgLoadBD.setVisibility(View.INVISIBLE);

                switch (chekError) {
                    case chekErrorOK:
                        Log.d(TAG, "пароль и логин совпал:" + chekError);

                        // создаем объект для создания и управления версиями БД
                        dbHelper = new DBHelper(MainActivity.this);

                        // создаем объект для данных
                        ContentValues cv = new ContentValues();

                        // подключаемся к БД
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        // подготовим данные для вставки в виде пар: наименование столбца - значение
                        cv.put("dbID", dbItems.dbID);
                        cv.put("dbAccountID", dbItems.dbAccountID);
                        cv.put("dbFIO", dbItems.dbFIO);
                        cv.put("dbStreet_id", dbItems.dbStreet_id);
                        cv.put("dbCity_id", dbItems.dbCity_id);
                        cv.put("dbTel", dbItems.dbTel);
                        cv.put("dbDepartment", "0");
                        cv.put("info", "");
                        cv.put("info2", "");
                        cv.put("color", 0);
                        cv.put("color2", 0);
                        cv.put("ticketsID", "");
                        cv.put("ticketsStatus", 0);
                        cv.put("dbTelMob", dbItems.dbTelMob);
                        cv.put("dbDates", dbItems.dbDates);
                        cv.put("dbCredit", dbItems.dbCredit);
                        cv.put("dbDataNews", dbItems.dbDataNews);
                        cv.put("dbNews", dbItems.dbNews);
                        cv.put("chekStatus", dbItems.chekStatus);
                        cv.put("dbBalance", String.valueOf(roundUp(dbItems.dbBalance, 2)));
                        cv.put("dbTariff", dbItems.dbTariff);
                        cv.put("dbInetStatus", dbItems.dbInetStatus);
                        cv.put("dbLogin", dbItems.dbLogin);
                        cv.put("dbPassword", dbItems.dbPassword);
                        cv.put("dbIP", dbItems.dbIP);
                        cv.put("dbMASK", dbItems.dbMASK);
                        cv.put("dbCost", dbItems.dbCost);
                        cv.put("dbIPReal", dbItems.dbIPReal);
                        cv.put("dbIPRealStatus", dbItems.dbIPRealStatus);
                        cv.put("dbISP", dbItems.dbISP);

                        // вставляем запись и получаем ее ID
                        long rowID = db.insert("mytable", null, cv);
                        Log.d(TAG, "row inserted, ID = " + rowID);

                        // закрываем подключение к БД
                        dbHelper.close();

                        Intent intent;
                        intent = new Intent(MainActivity.this, StartActivityItem.class);
                        startActivity(intent);
                        break;

                    case chekErrorLoginPass:
                        Toast.makeText(MainActivity.this, "Невырный логин или пароль", Toast.LENGTH_LONG).show();
                        btnNext.setVisibility(View.VISIBLE);
                        Log.d(TAG, "логин или пароль не совпал: "+chekError);
                        break;

                    default:
                        Toast.makeText(MainActivity.this, "Неизвестная ошибка", Toast.LENGTH_LONG).show();
                        btnNext.setVisibility(View.VISIBLE);
                        Log.d(TAG, "неизвестная ошибка: "+chekError);
                        break;
                }
            }
        }

    public BigDecimal roundUp(double value, int digits){
        return new BigDecimal(""+value).setScale(digits, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public void onBackPressed() {

        if (back_pressed + 2000 > System.currentTimeMillis())
        {
            //    super.onBackPressed();
            moveTaskToBack(true);
            finish();
            System.runFinalizersOnExit(true);
            System.exit(0);
        }
        else
            Toast.makeText(getBaseContext(), "Нажмите еще раз, чтобы выйти!",
                    Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, getString(R.string.name_BD), null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "dbID integer,"
                    + "ticketsStatus integer,"
                    + "color integer,"
                    + "color2 integer,"
                    + "ticketsID text,"
                    + "dbAccountID text,"
                    + "dbFIO text,"
                    + "dbStreet_id text,"
                    + "dbCity_id text,"
                    + "dbTel text,"
                    + "dbDepartment text,"
                    + "info text,"
                    + "info2 text,"
                    + "dbTelMob text,"
                    + "dbDates text,"
                    + "dbCredit text,"
                    + "dbDataNews text,"
                    + "dbNews text,"
                    + "chekStatus text,"
                    + "dbInetStatus integer,"
                    + "dbTariff text,"
                    + "dbLogin text,"
                    + "dbPassword text,"
                    + "dbIP text,"
                    + "dbMASK text,"
                    + "dbCost integer,"
                    + "dbIPReal text,"
                    + "dbIPRealStatus integer,"
                    + "dbISP integer,"
                    + "dbBalance double" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}