package ua.zp.starinet.starinet_02072015;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.math.BigDecimal;
import java.util.Calendar;

import retrofit.RetrofitError;
import ua.zp.starinet.starinet_02072015.model.DBFreeze;
import ua.zp.starinet.starinet_02072015.network.DataService;
import ua.zp.starinet.starinet_02072015.requests.GetFreezeRequest;

/**
 * Created by root on 20.08.15.
 */
public class Freeze_sub_item extends Fragment {

    TextView txtStatus;
    TextView txtInfo;
    TextView txtInfo2;
    TextView txtInfo3;
    TextView txtBalance;
    TextView txtBalanceFreeze;
    TextView txtInfoOut;
    TextView txtInfoOut2;
    TextView txtVnimanie;
    TextView txtCostUsluga;

    Button btnFreeze;

    DBHelper dbHelper;
    private static final String TAG = "myLogs";
    SpiceManager FreezeSpiceManager = new SpiceManager(DataService.class);

    public int chekStatus=0;
    final int chekStatusBlock=1;
    final int chekStatusFreeze=2;
    final int chekStatusVirus=3;
    final int chekStatusActiven=4;
    public int chekStatusUPDATE = 0;

    final int chekErrorOKFreeze=0;
    final int chekErrorOKActive=1;
    final int chekErrorLimit=2;
    public int chekError=4;

    double dbCost;
    String dbTariff;
    String dbAccountID;
    double dbBalance;
    double balanceFreeze;
    double balanceItogo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.freeze_subitem, null);

        txtStatus = (TextView) v.findViewById(R.id.txtStatus);
        txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        txtInfo2 = (TextView) v.findViewById(R.id.txtInfo2);
        txtInfo3 = (TextView) v.findViewById(R.id.txtInfo3);
        txtBalance = (TextView) v.findViewById(R.id.txtBalance);
        txtBalanceFreeze = (TextView) v.findViewById(R.id.txtBalanceFreeze);
        txtInfoOut = (TextView) v.findViewById(R.id.txtInfoOut);
        txtInfoOut2 = (TextView) v.findViewById(R.id.txtInfoOut2);
        txtVnimanie = (TextView) v.findViewById(R.id.txtVnimanie);
        txtCostUsluga = (TextView) v.findViewById(R.id.txtCostUsluga);

        btnFreeze = (Button) v.findViewById(R.id.btnFreeze);

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Log.d(TAG, "--- Rows in mytable: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("mytable", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int dbStatusIndex = c.getColumnIndex("chekStatus");
            int dbTariffIndex = c.getColumnIndex("dbTariff");
            int dbBalanceIndex = c.getColumnIndex("dbBalance");
            int dbCostIndex = c.getColumnIndex("dbCost");
            int dbAccountIDIndex = c.getColumnIndex("dbAccountID");

            do {
                chekStatus = c.getInt(dbStatusIndex);
                dbTariff = c.getString(dbTariffIndex);
                dbBalance = c.getDouble(dbBalanceIndex);
                dbCost = c.getDouble(dbCostIndex);
                dbAccountID = c.getString(dbAccountIDIndex);

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        c.close();

        // закрываем подключение к БД
        //dbHelper.close();

        Calendar myCalendar = (Calendar) Calendar.getInstance().clone();
        //myCalendar.set(your_year, your_month, 1);
        int max_date = myCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int this_day = myCalendar.get(Calendar.DAY_OF_MONTH);

        switch (chekStatus) {
            case chekStatusBlock:
                txtStatus.setText("Заблокирован");
                txtStatus.setTextColor(Color.RED);
                txtVnimanie.setText("");
                txtBalance.setText("");
                txtInfo.setText("");
                txtInfo2.setText("");
                txtInfo3.setText("");
                txtCostUsluga.setText("");
                txtBalanceFreeze.setText("");
                txtInfoOut.setText("Ваш лицевой счет Заблокирован");
                txtInfoOut2.setText("Вам запрещено использовать данную услугу!");
                txtInfoOut.setTextColor(Color.RED);
                txtInfoOut2.setTextColor(Color.RED);
                btnFreeze.setVisibility(View.INVISIBLE);
                break;

            case chekStatusFreeze:
                txtStatus.setText("Заморожен");
                txtStatus.setTextColor(Color.BLUE);
                txtVnimanie.setText("Внимание!");
                txtInfo2.setText("После нажатия на кнопку:");
                txtInfo3.setText("В результате на счету будет:");
                txtCostUsluga.setText("Стоимость услуги: 0 грн/мес");
                btnFreeze.setText("Разморозить");
                balanceFreeze = dbCost / max_date * (max_date - this_day);
                txtBalance.setText("Ваш текущий баланс: " + String.valueOf(roundUp(dbBalance,2)) + " грн");
                txtInfo.setText("По тарифу '" + dbTariff + "' на счет будет возвращено: " + String.valueOf(roundUp(balanceFreeze, 2)) + " грн");
                balanceItogo = dbBalance - balanceFreeze;
                txtBalanceFreeze.setText(String.valueOf(roundUp(balanceItogo, 2)) + " грн");
                btnFreeze.setVisibility(View.VISIBLE);
                chekStatusUPDATE = chekStatusActiven;
                break;

            case chekStatusVirus:
                txtVnimanie.setText("");
                txtBalance.setText("");
                txtInfo.setText("");
                txtInfo2.setText("");
                txtInfo3.setText("");
                txtCostUsluga.setText("");
                txtBalanceFreeze.setText("");
                txtStatus.setText("Инфицирован");
                txtStatus.setTextColor(Color.RED);
                txtInfoOut.setText("Ваш лицевой счет Инфицирован");
                txtInfoOut2.setText("Вам запрещено использовать данную услугу!");
                txtInfoOut.setTextColor(Color.RED);
                txtInfoOut2.setTextColor(Color.RED);
                btnFreeze.setVisibility(View.INVISIBLE);
                break;

            case chekStatusActiven:
                txtStatus.setText("Активен");
                txtStatus.setTextColor(Color.GREEN);
                txtVnimanie.setText("Внимание!");
                txtInfo2.setText("После нажатия на кнопку:");
                txtInfo3.setText("В результате на счету будет:");
                txtCostUsluga.setText("Стоимость услуги: 0 грн/мес");
                balanceFreeze = dbCost / max_date * (max_date - this_day);
                txtBalance.setText("Ваш текущий баланс: " + String.valueOf(roundUp(dbBalance,2)) + " грн");
                txtInfo.setText("По тарифу '" + dbTariff + "' на счет будет возвращено: " + String.valueOf(roundUp(balanceFreeze, 2)) + " грн");
                balanceItogo = dbBalance + balanceFreeze;
                txtBalanceFreeze.setText(String.valueOf(roundUp(balanceItogo, 2)) + " грн");
                btnFreeze.setText("Заморозить");
                btnFreeze.setVisibility(View.VISIBLE);
                chekStatusUPDATE = chekStatusFreeze;
                break;
        }

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.btnFreeze:
                        FreezeSpiceManager.execute(new GetFreezeRequest(dbAccountID, chekStatusUPDATE, balanceItogo), new GetFreezeListener());
                        break;
                }
            }
        };
        btnFreeze.setOnClickListener(onClickListenerStart);
        return v;
    }

    public BigDecimal roundUp(double value, int digits){
        return new BigDecimal(""+value).setScale(digits, BigDecimal.ROUND_HALF_UP);
    }

    class GetFreezeListener implements RequestListener<DBFreeze> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            RetrofitError cause = (RetrofitError) spiceException.getCause();
            if (cause == null ||
                    cause.isNetworkError() ||
                    cause.getResponse() == null) {
                Toast.makeText(getActivity(), "Нет доступа к Интернету", Toast.LENGTH_LONG).show();
            }

            return;
        }

        @Override
        public void onRequestSuccess(DBFreeze dbItems) {

            if (dbItems == null) {
                Toast.makeText(getActivity(), "Нет даных!", Toast.LENGTH_LONG).show();
                return;
            }

            //txtInfo.setText(String.valueOf(dbItems.chekError));

            //ByPinSpiceManager.shouldStop();

            chekError = dbItems.chekError;

            switch (chekError) {
                case chekErrorOKFreeze:
                    Log.d(TAG, "Ваш счет заморожен!" + chekError);
                    txtVnimanie.setText("");
                    txtInfo.setText("");
                    txtInfo2.setText("");
                    txtInfo3.setText("");
                    txtCostUsluga.setText("");
                    txtBalanceFreeze.setText("");
                    txtStatus.setText("Заморожен");
                    txtStatus.setTextColor(Color.BLUE);
                    txtInfoOut.setText("");
                    txtInfoOut2.setText("Ваш счет заморожен!");
                    txtInfoOut2.setTextColor(Color.BLUE);
                    txtBalance.setText("Ваш текущий баланс: " + String.valueOf(roundUp(balanceItogo,2)) + " грн");
                    btnFreeze.setVisibility(View.INVISIBLE);
                    // подключаемся к БД
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    // подготовим данные для вставки в виде пар: наименование столбца - значение
                    db.execSQL("UPDATE `mytable` SET `dbBalance`='"+balanceItogo+"'");
                    db.execSQL("UPDATE `mytable` SET `chekStatus`='"+chekStatusFreeze+"'");

                    // закрываем подключение к БД
                    //dbHelper.close();
                    break;

                case chekErrorOKActive:
                    Log.d(TAG, "Ваш счет разморожен!" + chekError);
                    txtVnimanie.setText("");
                    txtInfo.setText("");
                    txtInfo2.setText("");
                    txtInfo3.setText("");
                    txtCostUsluga.setText("");
                    txtBalanceFreeze.setText("");
                    txtStatus.setText("Активен");
                    txtStatus.setTextColor(Color.GREEN);
                    txtInfoOut.setText("");
                    txtInfoOut2.setText("Ваш счет разморожен!");
                    txtInfoOut2.setTextColor(Color.GREEN);
                    txtBalance.setText("Ваш текущий баланс: " + String.valueOf(roundUp(balanceItogo,2)) + " грн");
                    btnFreeze.setVisibility(View.INVISIBLE);
                    // подключаемся к БД
                    SQLiteDatabase db1 = dbHelper.getWritableDatabase();

                    // подготовим данные для вставки в виде пар: наименование столбца - значение
                    db1.execSQL("UPDATE `mytable` SET `dbBalance`='"+balanceItogo+"'");
                    db1.execSQL("UPDATE `mytable` SET `chekStatus`='"+chekStatusActiven+"'");

                    // закрываем подключение к БД
                    //dbHelper.close();
                    break;

                case chekErrorLimit:
                    Log.d(TAG, "В этом месяце, Вы уже не можете использовать услугу 'Заморозка счета'!" + chekError);
                    txtVnimanie.setText("");
                    txtInfo.setText("");
                    txtInfo2.setText("");
                    txtInfo3.setText("");
                    txtCostUsluga.setText("");
                    txtBalanceFreeze.setText("");
                    txtStatus.setText("");
                    txtInfoOut.setText("");
                    txtInfoOut2.setText("В этом месяце, Вы уже не можете использовать услугу 'Заморозка счета'!");
                    txtInfoOut2.setTextColor(Color.RED);
                    txtBalance.setText("");
                    btnFreeze.setVisibility(View.INVISIBLE);
                    break;

                default:
                    Log.d(TAG, "Неизвестная ошибка: " + chekError);
                    Toast.makeText(getActivity(), "Неизвестная ошибка: " + chekError, Toast.LENGTH_LONG).show();
                    txtVnimanie.setText("");
                    txtInfo.setText("");
                    txtInfo2.setText("");
                    txtInfo3.setText("");
                    txtCostUsluga.setText("");
                    txtBalanceFreeze.setText("");
                    txtStatus.setText("");
                    txtInfoOut.setText("");
                    txtInfoOut2.setText("Неизвестная ошибка");
                    txtInfoOut2.setTextColor(Color.RED);
                    txtBalance.setText("");
                    btnFreeze.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // создаем объект
        FreezeSpiceManager.start(activity);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(activity);
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
