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

import retrofit.RetrofitError;
import ua.zp.starinet.starinet_02072015.model.DBCredit;
import ua.zp.starinet.starinet_02072015.network.DataService;
import ua.zp.starinet.starinet_02072015.requests.GetCreditRequest;

/**
 * Created by root on 20.08.15.
 */
public class Credit_sub_item extends Fragment {

    DBHelper dbHelper;
    private static final String TAG = "myLogs";

    Double dbBalance;

    TextView txtInfo;
    TextView txtInfo2;
    TextView txtInfo3;
    Button btnCredit;

    SpiceManager CreditSubSpiceManager = new SpiceManager(DataService.class);

    String dbAccountID;
    String dbBalanceRound;

    public int chekStatus=0;
    final int chekStatusBlock=1;
    final int chekStatusFreeze=2;
    final int chekStatusVirus=3;

    final int chekErrorOK=0;
    final int chekErrorDate=1;
    final int chekErrorLimit=2;
    final int chekErrorStatus=3;
    public int chekError=4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.credit_sub_item, null);

        txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        txtInfo2 = (TextView) v.findViewById(R.id.txtInfo2);
        txtInfo3 = (TextView) v.findViewById(R.id.txtInfo3);
        btnCredit = (Button) v.findViewById(R.id.btnCredit);

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.btnCredit:
                        // подключаемся к БД
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        Log.d(TAG, "--- Rows in mytable: ---");
                        // делаем запрос всех данных из таблицы mytable, получаем Cursor
                        Cursor c = db.query("mytable", null, null, null, null, null, null);

                        // ставим позицию курсора на первую строку выборки
                        // если в выборке нет строк, вернется false
                        if (c.moveToFirst()) {

                            // определяем номера столбцов по имени в выборке
                            int dbBalanceIndex = c.getColumnIndex("dbBalance");
                            int dbAccountIDIndex = c.getColumnIndex("dbAccountID");

                            do {
                                dbBalance = c.getDouble(dbBalanceIndex);
                                dbAccountID = c.getString(dbAccountIDIndex);

                                // переход на следующую строку
                                // а если следующей нет (текущая - последняя), то false - выходим из цикла
                            } while (c.moveToNext());
                        } else
                            Log.d(TAG, "0 rows");
                        c.close();

                        // закрываем подключение к БД
                        //dbHelper.close();

                        if (dbBalance > -1) {
                            txtInfo.setTextColor(Color.GREEN);
                            txtInfo.setText("Ваш баланс: " + dbBalance + " грн");
                            txtInfo2.setText("На данный момент у Вас нет необходимости использовать услугу 'Экспресс кредит'. Задолженности у Вас нет");
                            txtInfo3.setText("Спасибо за своевременную оплату услуг");
                        } else
                        {
                            double BalanceMinus = dbBalance * -1 + 0.49;
                            dbBalanceRound = String.valueOf(roundUp(BalanceMinus, 0));
                            CreditSubSpiceManager.execute(new GetCreditRequest(dbAccountID, dbBalanceRound), new GetCreditListener());
                        }
                        break;

                }
            }
        };
        btnCredit.setOnClickListener(onClickListenerStart);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // создаем объект
        CreditSubSpiceManager.start(activity);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(activity);
    }

    public BigDecimal roundUp(double value, int digits){
        return new BigDecimal(""+value).setScale(digits, BigDecimal.ROUND_HALF_UP);
    }

    class GetCreditListener implements RequestListener<DBCredit> {
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
        public void onRequestSuccess(DBCredit dbItems) {

            if (dbItems == null) {
                //Toast.makeText(MainActivity.this, "Нет даных!", Toast.LENGTH_LONG).show();
                return;
            }

            //txtInfo.setText(String.valueOf(dbItems.chekError));

            //ByPinSpiceManager.shouldStop();

            chekError = dbItems.chekError;
            chekStatus = dbItems.chekStatus;

            //chekError=chekErrorOK;
            //Log.d(TAG, "Error: " + chekError);

            switch (chekError) {
                case chekErrorOK:
                    //Log.d(TAG, "ID и PIN совпали:" + chekError);
                    txtInfo.setText("Ваш баланс: " + dbBalance + " грн");
                    txtInfo2.setText("Вы взяли кредит на сумму " + dbBalanceRound + " грн.");
                    txtInfo3.setText("Шестого числа он будет анулирован автоматически");
                    break;

                case chekErrorDate:
                    Log.d(TAG, "Кредит можно брать только до 5 числа текущего месяца!" + chekError);
                    txtInfo.setText("Кредит можно брать только до 5 числа текущего месяца!");
                    txtInfo2.setText("");
                    txtInfo3.setText("");
                    txtInfo.setTextColor(Color.RED);
                    break;

                case chekErrorLimit:
                    Log.d(TAG, "Вы уже взяли кредит!" + chekError);
                    txtInfo.setText("Вы уже взяли кредит!");
                    txtInfo2.setText("");
                    txtInfo3.setText("");
                    txtInfo.setTextColor(Color.RED);
                    break;

                case chekErrorStatus:
                    Log.d(TAG, "Статус: ");
                    txtInfo.setTextColor(Color.RED);
                    txtInfo2.setText("");
                    txtInfo3.setText("");

                    switch (chekStatus) {
                        case chekStatusBlock:
                            txtInfo.setText("Ваш аккаунт заблокирован! Вы не можете взять кредит");
                            break;

                        case chekStatusFreeze:
                            txtInfo.setText("Ваш аккаунт заморожен! Вы не можете взять кредит");
                            break;

                        case chekStatusVirus:
                            txtInfo.setText("Ваш аккаунт инфицирован! Вы не можете взять кредит");
                            break;
                    }
                    break;

                default:
                    Log.d(TAG, "Неизвестная ошибка: " + chekError);
                    txtInfo.setText("Неизвестная ошибка");
                    txtInfo2.setText("");
                    txtInfo3.setText("");
                    txtInfo.setTextColor(Color.RED);
                    break;
            }
        }
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
