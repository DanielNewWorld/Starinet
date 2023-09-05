package ua.zp.starinet.starinet_02072015;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ua.zp.starinet.starinet_02072015.model.DbItem;


/**
 * Created by root on 19.08.15.
 */
public class AccountSubItem extends Fragment {

    TextView txtAccountIDActive;
    TextView txtAccountID;
    TextView txtFIO;
    TextView txtBalance;
    TextView txtStatus;
    TextView txtStreet_id;
    TextView txtCityID;
    TextView txtTel;
    TextView txtTelMob;
    TextView txtDates;
    TextView txtCredit;
    TextView txtPayments;
    TextView txtStatusTitle;

    public int chekStatus;
    final int chekStatusBlock=1;
    final int chekStatusFreeze=2;
    final int chekStatusVirus=3;
    final int chekStatusActiven=4;

    DBHelper dbHelper;
    private static final String TAG = "myLogs";
    FragmentTransaction fTrans;
    PaymentsActivityItem fragPayments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account_users_subitem, null);

        txtAccountIDActive = (TextView) v.findViewById(R.id.txtAccountIDActive);
        txtAccountID = (TextView) v.findViewById(R.id.txtAccountID);
        txtFIO = (TextView) v.findViewById(R.id.txtFIO);
        txtBalance = (TextView) v.findViewById(R.id.txtBalance);
        txtStatus = (TextView) v.findViewById(R.id.txtStatus);
        txtStreet_id = (TextView) v.findViewById(R.id.txtStreet_id);
        txtCityID = (TextView) v.findViewById(R.id.txtCityID);
        txtTel = (TextView) v.findViewById(R.id.txtTel);
        txtTelMob = (TextView) v.findViewById(R.id.txtTelMob);
        txtDates = (TextView) v.findViewById(R.id.txtDates);
        txtCredit = (TextView) v.findViewById(R.id.txtCredit);
        txtPayments = (TextView) v.findViewById(R.id.txtPayments);
        txtStatusTitle = (TextView) v.findViewById(R.id.txtStatusTitle);

        updateAccount();

        TextView txtPayments = (TextView) v.findViewById(R.id.txtPayments);
        txtPayments.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                fTrans = getFragmentManager().beginTransaction();
                if (fragPayments==null) fragPayments = new PaymentsActivityItem();
                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.frgmCont, fragPayments);
                fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });

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
            int dbAccountIDIndex = c.getColumnIndex("dbAccountID");
            int chekStatusIndex = c.getColumnIndex("chekStatus");
            int dbCity_idIndex = c.getColumnIndex("dbCity_id");
            int dbStreet_idIndex = c.getColumnIndex("dbStreet_id");
            int dbFIOIndex = c.getColumnIndex("dbFIO");
            int dbTelIndex = c.getColumnIndex("dbTel");
            int dbTelMobIndex = c.getColumnIndex("dbTelMob");
            int dbDatesIndex = c.getColumnIndex("dbDates");
            int dbBalanceIndex = c.getColumnIndex("dbBalance");
            int dbCreditIndex = c.getColumnIndex("dbCredit");

            do {
                txtAccountIDActive.setText("Ваш лицевой счет № " + c.getString(dbAccountIDIndex));

                chekStatus = c.getInt(chekStatusIndex);
                switch (chekStatus) {
                    case chekStatusBlock:
                        txtStatus.setTextColor(Color.RED);
                        txtStatusTitle.setTextColor(Color.RED);
                        txtStatus.setText("Заблокирован");
                        txtStatusTitle.setText("Заблокирован");
                        break;

                    case chekStatusFreeze:
                        txtStatus.setTextColor(Color.BLUE);
                        txtStatusTitle.setTextColor(Color.BLUE);
                        txtStatus.setText("Заморожен");
                        txtStatusTitle.setText("Заморожен");
                        break;

                    case chekStatusVirus:
                        txtStatus.setTextColor(Color.RED);
                        txtStatusTitle.setTextColor(Color.RED);
                        txtStatus.setText("Инфицирован");
                        txtStatusTitle.setText("Инфицирован");
                        break;

                    case chekStatusActiven:
                        txtStatus.setTextColor(Color.GREEN);
                        txtStatusTitle.setTextColor(Color.GREEN);
                        txtStatus.setText("Активен");
                        txtStatusTitle.setText("Активен");
                        break;
                }

                txtAccountID.setText(c.getString(dbAccountIDIndex));
                txtCityID.setText(c.getString(dbCity_idIndex));
                txtStreet_id.setText(c.getString(dbStreet_idIndex));
                txtFIO.setText(c.getString(dbFIOIndex));
                txtTel.setText(c.getString(dbTelIndex));
                txtTelMob.setText(c.getString(dbTelMobIndex));
                txtDates.setText(c.getString(dbDatesIndex));
                txtBalance.setText(c.getString(dbBalanceIndex) + " грн");
                txtCredit.setText("-" + c.getString(dbCreditIndex) + ".00 грн");

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
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}