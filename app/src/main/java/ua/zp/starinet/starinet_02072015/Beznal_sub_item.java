package ua.zp.starinet.starinet_02072015;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;

/**
 * Created by root on 24.08.15.
 */
public class Beznal_sub_item extends Fragment {

    DBHelper dbHelper;
    private static final String TAG = "myLogs";

    TextView txtFIO;
    TextView txtSuma;
    TextView txtAccount;
    TextView txtPDV;
    TextView txtVsego;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.beznal_sub_item, null);

        txtSuma = (TextView) v.findViewById(R.id.txtSuma);
        txtFIO = (TextView) v.findViewById(R.id.txtFIO);
        txtAccount = (TextView) v.findViewById(R.id.txtAccount);
        txtPDV = (TextView) v.findViewById(R.id.txtPDV);
        txtVsego = (TextView) v.findViewById(R.id.txtVsego);

        updateAccount();

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                switch (v.getId()) {

                    //case R.id.start_menu_item:
                    //    intent = new Intent(AccountSubItem.this, .class);
                    //    startActivity(intent);
                    //    break;

                }
            }
        };
        //start_menu_item.setOnClickListener(onClickListenerStart);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(activity);
    }

    public void updateAccount() {

        // создаем объект для данных
        ContentValues cv = new ContentValues();

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Log.d(TAG, "--- Rows in mytable: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("mytable", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            //int dbInetStatusIndex = c.getColumnIndex("dbInetStatus");
            int dbFIOIndex = c.getColumnIndex("dbFIO");
            int dbAccountIndex = c.getColumnIndex("dbAccountID");
            int dbCostTariffIndex = c.getColumnIndex("dbCost");
            int Vsego = c.getInt(dbCostTariffIndex);
            double PDV = 20*Vsego/100;
            double Suma = Vsego-PDV;

            do {
                txtFIO.setText(c.getString(dbFIOIndex));
                txtAccount.setText(c.getString(dbAccountIndex));
                txtSuma.setText(String.valueOf(roundUp(Suma, 2)));
                txtPDV.setText(String.valueOf(roundUp(PDV,2)));
                txtVsego.setText(String.valueOf(Vsego));

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        c.close();

        // закрываем подключение к БД
        dbHelper.close();
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
