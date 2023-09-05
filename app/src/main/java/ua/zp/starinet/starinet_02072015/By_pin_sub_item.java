package ua.zp.starinet.starinet_02072015;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.math.BigDecimal;

import retrofit.RetrofitError;
import ua.zp.starinet.starinet_02072015.model.DBByPin;
import ua.zp.starinet.starinet_02072015.network.DataService;
import ua.zp.starinet.starinet_02072015.requests.GetByPinRequest;

/**
 * Created by root on 19.08.15.
 */
public class By_pin_sub_item extends Fragment {

    SpiceManager ByPinSpiceManager = new SpiceManager(DataService.class);

    Button buyCards;
    EditText edtNomer;
    EditText edtPin;
    TextView txtInfo;
    TextView txtInfo2;

    final int chekErrorOK=0;
    final int chekErrorIDPin=1;
    final int chekErrorDateExp=2;
    final int chekErrorUsed=3;
    public int chekError=4;

    private static final String TAG = "myLogs";

    DBHelper dbHelper;
    String dbIP;
    String dbAccountID;
    String dbLogin;
    double dbBalance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bypin_subitem, null);

        buyCards = (Button) v.findViewById(R.id.buyCards);
        edtNomer = (EditText) v.findViewById(R.id.edtNomer);
        edtPin = (EditText) v.findViewById(R.id.edtPin);
        txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        txtInfo2 = (TextView) v.findViewById(R.id.txtInfo2);

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.buyCards:

                        // подключаемся к БД
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        Log.d(TAG, "--- Rows in mytable: ---");
                        // делаем запрос всех данных из таблицы mytable, получаем Cursor
                        Cursor c = db.query("mytable", null, null, null, null, null, null);

                        // ставим позицию курсора на первую строку выборки
                        // если в выборке нет строк, вернется false
                        if (c.moveToFirst()) {

                            // определяем номера столбцов по имени в выборке
                            int dbIPIndex = c.getColumnIndex("dbIP");
                            int dbAccountIndex = c.getColumnIndex("dbAccountID");
                            int dbLoginIndex = c.getColumnIndex("dbLogin");
                            int dbBalanceIndex = c.getColumnIndex("dbBalance");

                            do {
                                dbIP = c.getString(dbIPIndex);
                                dbAccountID = c.getString(dbAccountIndex);
                                dbLogin = c.getString(dbLoginIndex);
                                dbBalance = c.getDouble(dbBalanceIndex);

                                // переход на следующую строку
                                // а если следующей нет (текущая - последняя), то false - выходим из цикла
                            } while (c.moveToNext());
                        } else
                            Log.d(TAG, "0 rows");
                        c.close();

                        // закрываем подключение к БД
                        //dbHelper.close();

                        ByPinSpiceManager.execute(new GetByPinRequest(edtNomer.getText().toString(),
                                                                      edtPin.getText().toString(),
                                                                      dbIP, dbAccountID, dbLogin, dbBalance), new GetByPinListener());
                        break;

                }
            }
        };
        buyCards.setOnClickListener(onClickListenerStart);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // создаем объект
        ByPinSpiceManager.start(activity);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(activity);
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

            //edtNomer.setText(String.valueOf(dbItems.chekError));

            //ByPinSpiceManager.shouldStop();

        chekError = dbItems.chekError;

            Log.d(TAG, "Error: " + chekError);

        switch (chekError) {
            case chekErrorOK:
                Log.d(TAG, "ID и PIN совпали:" + chekError);
                txtInfo.setText("Ваш счет пополнен на " + dbItems.dbNominal + " грн");
                txtInfo2.setText("");
                txtInfo.setTextColor(Color.GREEN);
                txtInfo2.setTextColor(Color.GREEN);

                // подключаемся к БД
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                // подготовим данные для вставки в виде пар: наименование столбца - значение
                dbBalance = dbBalance + dbItems.dbNominal;
                db.execSQL("UPDATE `mytable` SET `dbBalance`='"+dbBalance+"'");

                // закрываем подключение к БД
                //dbHelper.close();
                break;

            case chekErrorIDPin:
                Log.d(TAG, "ID или PIN не совпал: " + chekError);
                txtInfo.setText("Не верный номер карты: " + dbItems.dbID + " или PIN код: " + dbItems.dbPIN);
                txtInfo2.setText("Проверьте данные или попробуйте другую карту");
                txtInfo.setTextColor(Color.RED);
                txtInfo2.setTextColor(Color.RED);
                break;

            case chekErrorDateExp:
                Log.d(TAG, "DateExp: " + chekError);
                txtInfo.setText("Дата использования карты или чека истекла: " + dbItems.dbDateExp);
                txtInfo2.setText("Пожалуйста, обратитесь в Help-центр");
                txtInfo.setTextColor(Color.RED);
                txtInfo2.setTextColor(Color.RED);
                break;

            case chekErrorUsed:
                Log.d(TAG, "Used: " + chekError);
                txtInfo.setText("Эта карта или чек уже была использована: " + dbItems.dbDateUse);
                txtInfo2.setText("Пожалуйста, обратитесь в Help-центр");
                txtInfo.setTextColor(Color.RED);
                txtInfo2.setTextColor(Color.RED);
                break;

            default:
                Log.d(TAG, "Неизвестная ошибка: " + chekError);
                txtInfo.setText("Неизвестная ошибка");
                txtInfo2.setText("Проверьте данные или попробуйте другую карту");
                txtInfo.setTextColor(Color.RED);
                txtInfo2.setTextColor(Color.RED);
                break;
        }
        }
    }

    public BigDecimal roundUp(double value, int digits){
        return new BigDecimal(""+value).setScale(digits, BigDecimal.ROUND_HALF_UP);
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
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
