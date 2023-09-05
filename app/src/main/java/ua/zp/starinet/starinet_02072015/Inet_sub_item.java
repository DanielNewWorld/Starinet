package ua.zp.starinet.starinet_02072015;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by root on 24.08.15.
 */
public class Inet_sub_item extends Fragment {

    DBHelper dbHelper;
    private static final String TAG = "myLogs";

    public int chekInetStatus;
    final int chekInetStatusRed=0;
    final int chekInetStatusGreen=1;

    TextView txtInetStatus;
    TextView txtTariff;
    TextView txtLogin;
    TextView txtIP;
    TextView txtMASK;
    TextView txtCost;
    TextView txtTariffCost;
    TextView txtChangeTariff;
    TextView txtPayments;

    FragmentTransaction fTrans;
    Change_tp_sub_item fragChangeTPSubItem;
    PaymentsActivityItem fragPayments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.inet_subitem, null);

        txtInetStatus = (TextView) v.findViewById(R.id.txtInetStatus);
        txtTariff = (TextView) v.findViewById(R.id.txtTariff);
        txtLogin = (TextView) v.findViewById(R.id.txtLogin);
        txtIP = (TextView) v.findViewById(R.id.txtIP);
        txtMASK = (TextView) v.findViewById(R.id.txtMASK);
        txtCost = (TextView) v.findViewById(R.id.txtCost);
        txtTariffCost = (TextView) v.findViewById(R.id.txtTariffCost);
        txtChangeTariff = (TextView) v.findViewById(R.id.txtChangeTariff);
        txtPayments = (TextView) v.findViewById(R.id.txtPayments);

        updateAccount();

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                switch (v.getId()) {
                    case R.id.txtTariffCost:
                        //вызываем сайт
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://starinet.zp.ua/tariff.php"));
                        startActivity(intent);
                        break;

                    case R.id.txtChangeTariff:
                        fTrans = getFragmentManager().beginTransaction();
                        if (fragChangeTPSubItem==null) fragChangeTPSubItem = new Change_tp_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragChangeTPSubItem);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;

                    case R.id.txtPayments:
                        fTrans = getFragmentManager().beginTransaction();
                        if (fragPayments==null) fragPayments = new PaymentsActivityItem();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragPayments);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;
                }
            }
        };
        txtTariffCost.setOnClickListener(onClickListenerStart);
        txtChangeTariff.setOnClickListener(onClickListenerStart);
        txtPayments.setOnClickListener(onClickListenerStart);
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
            int dbInetStatusIndex = c.getColumnIndex("dbInetStatus");
            int dbTariffIndex = c.getColumnIndex("dbTariff");
            int dbLoginIndex = c.getColumnIndex("dbLogin");
            int dbIPIndex = c.getColumnIndex("dbIP");
            int dbMASKIndex = c.getColumnIndex("dbMASK");
            int dbCostIndex = c.getColumnIndex("dbCost");

            do {
                chekInetStatus = c.getInt(dbInetStatusIndex);
                if (chekInetStatus == chekInetStatusRed) {
                    txtInetStatus.setTextColor(Color.RED);
                    txtInetStatus.setText("Выключен");
                }

                if (chekInetStatus == chekInetStatusGreen) {
                    txtInetStatus.setTextColor(Color.GREEN);
                    txtInetStatus.setText("Включен");
                }

                txtTariff.setText(c.getString(dbTariffIndex));
                txtLogin.setText(c.getString(dbLoginIndex));
                txtIP.setText(c.getString(dbIPIndex));
                txtMASK.setText(c.getString(dbMASKIndex));
                txtCost.setText(c.getString(dbCostIndex) + " грн / месяц");

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
