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
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by root on 24.08.15.
 */
public class Real_IP extends Fragment {

    DBHelper dbHelper;
    private static final String TAG = "myLogs";

    TextView txtIPIn;
    TextView txtIPReal;
    TextView txtIPRealStatus;
    TextView txtIPCost;
    TextView txtIPInfo;

    public int chekIPRealStatus=0;
    final int chekIPRealStatusNull=0;
    final int chekIPRealStatusOK=1;
    final int chekIPRealStatusBaburka=2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.real_ip, null);

        txtIPIn = (TextView) v.findViewById(R.id.txtIPIn);
        txtIPReal = (TextView) v.findViewById(R.id.txtIPReal);
        txtIPRealStatus = (TextView) v.findViewById(R.id.txtIPRealStatus);
        txtIPCost = (TextView) v.findViewById(R.id.txtIPCost);
        txtIPInfo = (TextView) v.findViewById(R.id.txtIPInfo);

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
            int dbIPIndex = c.getColumnIndex("dbIP");
            int dbIPRealIndex = c.getColumnIndex("dbIPReal");
            int dbIPRealStatusIndex = c.getColumnIndex("dbIPRealStatus");

            do {
                chekIPRealStatus = c.getInt(dbIPRealStatusIndex);
                if (chekIPRealStatus == chekIPRealStatusNull) {
                    txtIPRealStatus.setTextColor(Color.RED);
                    txtIPRealStatus.setText("Не подключена");
                    txtIPCost.setText("бесплатно");
                    txtIPReal.setText("нет");
                    txtIPInfo.setText("Заказать услугу можно по телефонам: \n" +
                                        "   (061) 270-53-76 \n" +
                                        "   (061) 221-01-03 \n" +
                                        "   (096) 80-44-225 \n" +
                                        "   или через HELP-центр в личном кабинете");
                }

                if (chekIPRealStatus == chekIPRealStatusOK) {
                    txtIPRealStatus.setTextColor(Color.GREEN);
                    txtIPRealStatus.setText("Подключена");
                    txtIPCost.setText("бесплатно");
                    txtIPReal.setText(c.getString(dbIPRealIndex));
                    txtIPInfo.setText("");
                }

                if (chekIPRealStatus == chekIPRealStatusBaburka) {
                    txtIPRealStatus.setTextColor(Color.GREEN);
                    txtIPRealStatus.setText("Подключена");
                    txtIPCost.setText("бесплатно");
                    txtIPReal.setText(c.getString(dbIPIndex));
                    txtIPInfo.setText("Для Вас эта услуга бесплатная и подключена автоматически!");
                }

                txtIPIn.setText(c.getString(dbIPIndex));

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        c.close();

        // закрываем подключение к БД
        dbHelper.close();
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
