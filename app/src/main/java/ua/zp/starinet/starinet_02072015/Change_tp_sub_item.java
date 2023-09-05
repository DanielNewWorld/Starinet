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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import retrofit.RetrofitError;
import ua.zp.starinet.starinet_02072015.model.DBChangeTP;
import ua.zp.starinet.starinet_02072015.network.DataService;
import ua.zp.starinet.starinet_02072015.requests.GetChangeTPRequest;

/**
 * Created by root on 20.08.15.
 */
public class Change_tp_sub_item extends Fragment {

    TextView txtNameTarif;
    TextView txtCost;
    TextView txtVsego;
    TextView txtBalance;
    TextView txtInfo;

    Button btnNext;

    RadioButton radio_Star20;
    RadioButton radio_Star40;
    RadioButton radio_Star70;
    RadioButton radio_Star100;

    DBHelper dbHelper;
    private static final String TAG = "myLogs";

    String dbTariff;
    String dbCost;
    String dbBalance;
    String dbAccountID;
    String dbChangeTP;
    String dbChangeTPName;

    SpiceManager ChangeTPSubSpiceManager = new SpiceManager(DataService.class);

    final int chekErrorOK=0;
    final int chekErrorTarif=1;
    public int chekError=20;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.changetp_subitem, null);

        txtNameTarif = (TextView) v.findViewById(R.id.txtNameTarif);
        txtCost = (TextView) v.findViewById(R.id.txtCost);
        txtVsego = (TextView) v.findViewById(R.id.txtVsego);
        txtBalance = (TextView) v.findViewById(R.id.txtBalance);
        txtInfo = (TextView) v.findViewById(R.id.txtInfo);

        btnNext = (Button) v.findViewById(R.id.btnNext);

        radio_Star20 = (RadioButton) v.findViewById(R.id.radio_Star20);
        radio_Star40 = (RadioButton) v.findViewById(R.id.radio_Star40);
        radio_Star70 = (RadioButton) v.findViewById(R.id.radio_Star70);
        radio_Star100 = (RadioButton) v.findViewById(R.id.radio_Star100);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(getActivity());
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Log.d(TAG, "--- Rows in mytable: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("mytable", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int dbTariffIndex = c.getColumnIndex("dbTariff");
            int dbCostIndex = c.getColumnIndex("dbCost");
            int dbBalanceIndex = c.getColumnIndex("dbBalance");
            int dbAccountIDIndex = c.getColumnIndex("dbAccountID");

            do {
                dbTariff = c.getString(dbTariffIndex);
                dbCost = c.getString(dbCostIndex);
                dbBalance = c.getString(dbBalanceIndex);
                dbAccountID = c.getString(dbAccountIDIndex);

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        c.close();

        // закрываем подключение к БД
        dbHelper.close();

        txtNameTarif.setText(dbTariff);
        txtCost.setText(dbCost);
        txtVsego.setText(dbCost);
        txtBalance.setText(dbBalance + " грн");

        // создаем объект
        ChangeTPSubSpiceManager.start(getActivity());

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.btnNext:
                        //if () {                     } //проверка баланса и стоимости тарифа

                        dbChangeTP="0";
                        dbChangeTPName="не выбран";

                        if (radio_Star20.isChecked()) {
                            dbChangeTP="22";
                            dbChangeTPName="Star20";
                        }
                        if (radio_Star40.isChecked()) {
                            dbChangeTP="23";
                            dbChangeTPName="Star40";
                        }
                        if (radio_Star70.isChecked()) {
                            dbChangeTP="24";
                            dbChangeTPName="Star70";
                        }
                        if (radio_Star100.isChecked()) {
                            dbChangeTP="25";
                            dbChangeTPName="Star100";
                        }

                        if (dbChangeTP=="0") {
                            Toast.makeText(getActivity(), "Выберите тарифный план", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Выберите тарифный план");
                            txtInfo.setTextColor(Color.GREEN);
                            txtInfo.setText("Выберите тарифный план");
                            break;
                        }

                        ChangeTPSubSpiceManager.execute(new GetChangeTPRequest(dbAccountID, dbChangeTP), new GetChangeTPListener());
                        break;

                }
            }
        };
        btnNext.setOnClickListener(onClickListenerStart);
        return v;
    }

    class GetChangeTPListener implements RequestListener<DBChangeTP> {
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
        public void onRequestSuccess(DBChangeTP dbItems) {

            if (dbItems == null) {
                Toast.makeText(getActivity(), "Нет ответа от сервера!", Toast.LENGTH_LONG).show();
                return;
            }

            //ByPinSpiceManager.shouldStop();

            chekError = dbItems.chekError;

            switch (chekError) {
                case chekErrorOK:
                    Log.d(TAG, "Заявка принята" + chekError);
                    txtInfo.setTextColor(Color.GREEN);
                    txtInfo.setText("Ваша заявка принята в обработку! Ваш тарифный план со следующего месяца будет сменен на " + dbChangeTPName);
                    break;

                case chekErrorTarif:
                    Log.d(TAG, "Вы не можете сменить тарифный план на " + dbChangeTP);
                    txtInfo.setText("Вы не можете сменить тарифный план на " + dbChangeTP);
                    txtInfo.setTextColor(Color.RED);
                    break;

                default:
                    Log.d(TAG, "Неизвестная ошибка: " + chekError);
                    txtInfo.setText("Неизвестная ошибка");
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
