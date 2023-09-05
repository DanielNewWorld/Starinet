package ua.zp.starinet.starinet_02072015;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import retrofit.RetrofitError;
import ua.zp.starinet.starinet_02072015.model.DBByPin;
import ua.zp.starinet.starinet_02072015.network.ByPinNetwork;
import ua.zp.starinet.starinet_02072015.network.DataService;
import ua.zp.starinet.starinet_02072015.requests.GetByPinRequest;

public class Test extends Activity {

    SpiceManager ByPinSpiceManager = new SpiceManager(DataService.class);

    Button buyCards;
    EditText edtNomer;
    EditText edtPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        buyCards = (Button) findViewById(R.id.buyCards);
        edtNomer = (EditText) findViewById(R.id.edtNomer);
        edtPin = (EditText) findViewById(R.id.edtPin);

        ByPinSpiceManager.start(this);

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.buyCards:
                        //ByPinSpiceManager.execute(new GetByPinRequest(edtNomer.getText().toString(), edtPin.getText().toString()), new GetByPinListener());
                        break;

                }
            }
        };
        buyCards.setOnClickListener(onClickListenerStart);
    }

    class GetByPinListener implements RequestListener<DBByPin> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            RetrofitError cause = (RetrofitError) spiceException.getCause();
            if (cause == null ||
                    cause.isNetworkError() ||
                    cause.getResponse() == null) {
                //Toast.makeText(By_pin_sub_item.this, "Нет доступа к Интернету", Toast.LENGTH_LONG).show();
            }

            return;
        }
        @Override
        public void onRequestSuccess(DBByPin dbItems) {

            if (dbItems == null) {
                //Toast.makeText(MainActivity.this, "Нет даных!", Toast.LENGTH_LONG).show();
                return;
            }

            edtNomer.setText(String.valueOf(dbItems.chekError));

            ByPinSpiceManager.shouldStop();

        /*chekError = dbItems.chekError;

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
                cv.put("dbAccountID", dbItems.dbAccountID);
                cv.put("dbFIO", dbItems.dbFIO);
                cv.put("dbStatus", dbItems.dbStatus);
                cv.put("dbStreet_id", dbItems.dbStreet_id);
                cv.put("dbCity_id", dbItems.dbCity_id);
                cv.put("dbTel", dbItems.dbTel);
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
        }*/
        }

    }


}
