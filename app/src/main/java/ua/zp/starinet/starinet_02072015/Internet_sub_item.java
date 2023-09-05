package ua.zp.starinet.starinet_02072015;

import android.app.Activity;
import android.app.Fragment;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import ua.zp.starinet.starinet_02072015.model.ArrayListDBInternetSub;
import ua.zp.starinet.starinet_02072015.model.DBInternetSub;
import ua.zp.starinet.starinet_02072015.network.DataService;
import ua.zp.starinet.starinet_02072015.requests.GetInternetSubRequest;

/**
 * Created by root on 24.08.15.
 */
public class Internet_sub_item extends Fragment {

    SpiceManager InternetSubSpiceManager = new SpiceManager(DataService.class);

    Button btnFind;
    TextView txtInfo;
    TextView txtNTitle;
    TextView txtDataTitle;
    TextView txtInternetTitle;
    TextView txtSummaTitle;
    TextView txtBalanceBeforeTitle;
    TextView txtBalanceAfterTitle;
    TextView txtTypeTitle;
    TextView txtCommentsTitle;

    TextView txtN;
    TextView txtData;
    TextView txtInternet;
    TextView txtSumma;
    TextView txtBalanceBefore;
    TextView txtBalanceAfter;
    TextView txtType;
    TextView txtComments;

    String txtNString;
    String txtDataString;
    String txtInternetString;
    String txtSummaString;
    String txtBalanceBeforeString;
    String txtBalanceAfterString;
    String txtTypeString;
    String txtCommentsString;

    Spinner spnMonth;
    Spinner spnGod;
    Spinner spnMonthEnd;
    Spinner spnGodEnd;

    String[] spnMonthName = { "январь", "февраль", "март", "апрель", "май", "июнь",
            "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь" };

    String[] spnGodName = { "2015", "2014", "2013", "2012", "2011", "2010"};

    String edtHomeString;
    String edtEndString;

    final int chekErrorOK=0;
    final int chekErrorDate=1;
    public int chekError=4;

    private static final String TAG = "myLogs";
    DBHelper dbHelper;

    String dbAccountID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.internet_sub_item, null);

        btnFind = (Button) v.findViewById(R.id.btnFind);
        txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        txtN = (TextView) v.findViewById(R.id.txtN);
        txtData = (TextView) v.findViewById(R.id.txtData);
        txtInternet= (TextView) v.findViewById(R.id.txtInternet);
        txtSumma = (TextView) v.findViewById(R.id.txtSumma);
        txtBalanceBefore = (TextView) v.findViewById(R.id.txtBalanceBefore);
        txtBalanceAfter = (TextView) v.findViewById(R.id.txtBalanceAfter);
        txtType = (TextView) v.findViewById(R.id.txtType);
        txtComments = (TextView) v.findViewById(R.id.txtComments);

        txtNTitle = (TextView) v.findViewById(R.id.txtNTitle);
        txtDataTitle = (TextView) v.findViewById(R.id.txtDataTitle);
        txtInternetTitle = (TextView) v.findViewById(R.id.txtInternetTitle);
        txtSummaTitle = (TextView) v.findViewById(R.id.txtSummaTitle);
        txtBalanceBeforeTitle = (TextView) v.findViewById(R.id.txtBalanceBeforeTitle);
        txtBalanceAfterTitle = (TextView) v.findViewById(R.id.txtBalanceAfterTitle);
        txtTypeTitle = (TextView) v.findViewById(R.id.txtTypeTitle);
        txtCommentsTitle = (TextView) v.findViewById(R.id.txtCommentsTitle);

        spnMonth = (Spinner) v.findViewById(R.id.spnMonth);
        spnMonthEnd = (Spinner) v.findViewById(R.id.spnMonthEnd);
        spnGod = (Spinner) v.findViewById(R.id.spnGod);
        spnGodEnd = (Spinner) v.findViewById(R.id.spnGodEnd);

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spnMonthName);

        // присваиваем адаптер списку
        spnMonth.setAdapter(adapter);
        spnMonth.setPrompt("Выберите месяц");
        spnMonth.setSelection(0);

        spnMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // создаем адаптер
        ArrayAdapter<String> adapterGod = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spnGodName);

        // присваиваем адаптер списку
        spnGod.setAdapter(adapterGod);
        spnGod.setPrompt("Выберите год");
        spnGod.setSelection(0);

        spnGod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // создаем адаптер
        ArrayAdapter<String> adapterMonthEnd = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spnMonthName);

        // присваиваем адаптер списку
        spnMonthEnd.setAdapter(adapterMonthEnd);
        spnMonthEnd.setPrompt("Выберите месяц");
        spnMonthEnd.setSelection(0);

        spnMonthEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // создаем адаптер
        ArrayAdapter<String> adapterGodEnd = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spnGodName);

        // присваиваем адаптер списку
        spnGodEnd.setAdapter(adapterGodEnd);
        spnGodEnd.setPrompt("Выберите год");
        spnGodEnd.setSelection(0);

        spnGodEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.btnFind:

                        // подключаемся к БД
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        Log.d(TAG, "--- Rows in mytable: ---");
                        // делаем запрос всех данных из таблицы mytable, получаем Cursor
                        Cursor c = db.query("mytable", null, null, null, null, null, null);

                        // ставим позицию курсора на первую строку выборки
                        // если в выборке нет строк, вернется false
                        if (c.moveToFirst()) {

                            // определяем номера столбцов по имени в выборке
                            int dbAccountIndex = c.getColumnIndex("dbAccountID");
                            //int dbLoginIndex = c.getColumnIndex("dbLogin");
                            //int dbBalanceIndex = c.getColumnIndex("dbBalance");

                            do {
                                dbAccountID = c.getString(dbAccountIndex);
                                //dbLogin = c.getString(dbLoginIndex);
                                //dbBalance = c.getDouble(dbBalanceIndex);

                                // переход на следующую строку
                                // а если следующей нет (текущая - последняя), то false - выходим из цикла
                            } while (c.moveToNext());
                        } else
                            Log.d(TAG, "0 rows");
                        c.close();

                        // закрываем подключение к БД
                        //dbHelper.close();

                        int spnMonthItem = spnMonth.getSelectedItemPosition() + 1;
                        int spnMonthEndItem = spnMonthEnd.getSelectedItemPosition() + 1;

                        edtHomeString = spnGod.getSelectedItem().toString() + "." + spnMonthItem + ".0";
                        edtEndString = spnGodEnd.getSelectedItem().toString() + "." + spnMonthEndItem + ".31 23:59:59";

                        InternetSubSpiceManager.execute(new GetInternetSubRequest(edtHomeString, edtEndString, dbAccountID), new GetInternetListener());
                        break;

                }
            }
        };
        btnFind.setOnClickListener(onClickListenerStart);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // создаем объект
        InternetSubSpiceManager.start(activity);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(activity);
    }

    class GetInternetListener implements RequestListener<ArrayListDBInternetSub> {
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
        public void onRequestSuccess(ArrayListDBInternetSub dbItems) {

            if (dbItems == null) {
                //Toast.makeText(MainActivity.this, "Нет даных!", Toast.LENGTH_LONG).show();
                return;
            }

            //txtInfo.setText(String.valueOf(dbItems.chekError));

            //ByPinSpiceManager.shouldStop();

            for (DBInternetSub i: dbItems) {
                chekError = i.chekError;
            }
            //chekError=chekErrorOK;
            //Log.d(TAG, "Error: " + chekError);

            switch (chekError) {
                case chekErrorOK:
                    Log.d(TAG, "ID и PIN совпали:" + chekError);
                    txtInfo.setTextColor(Color.GREEN);
                    txtInfo.setText("Период с " + spnMonth.getSelectedItem().toString() + " " + spnGod.getSelectedItem().toString()
                                    + " по " + spnMonthEnd.getSelectedItem().toString() + " " + spnGodEnd.getSelectedItem().toString());

                    txtNTitle.setText("№");
                    txtDataTitle.setText("Дата/время");
                    txtInternetTitle.setText("Интернет");
                    txtSummaTitle.setText("Сумма");
                    txtBalanceBeforeTitle.setText("Баланс до");
                    txtBalanceAfterTitle.setText("Баланс после");
                    txtTypeTitle.setText("Тип платежа");
                    txtCommentsTitle.setText("Примечания");

                    txtNString="";
                    txtDataString="";
                    txtInternetString="";
                    txtSummaString="";
                    txtBalanceBeforeString="";
                    txtBalanceAfterString="";
                    txtTypeString="";
                    txtCommentsString="";

                    int j=1;
                    for (DBInternetSub i : dbItems) {
                        txtNString = txtNString + j + "\n"; j=j+1;
                        txtDataString = txtDataString + i.dbDateTime + "\n";
                        txtInternetString = txtInternetString + String.valueOf(roundUp (i.dbInternet,2)) + "\n";
                        txtSummaString = txtSummaString + i.dbSumma + "\n";
                        txtBalanceBeforeString = txtBalanceBeforeString + i.dbBalanceBefore + "\n";
                        txtBalanceAfterString = txtBalanceAfterString + i.dbBalanceAfter + "\n";
                        txtTypeString = txtTypeString + i.dbType + "\n";
                        txtCommentsString = txtCommentsString + i.dbComments + "\n";
                    }

                    txtN.setText(txtNString);
                    txtData.setText(txtDataString);
                    txtInternet.setText(txtInternetString);
                    txtSumma.setText(txtSummaString);
                    txtBalanceBefore.setText(txtBalanceBeforeString);
                    txtBalanceAfter.setText(txtBalanceAfterString);
                    txtType.setText(txtTypeString);
                    txtComments.setText(txtCommentsString);

                    // подключаемся к БД
                    /*SQLiteDatabase db = dbHelper.getWritableDatabase();

                    // подготовим данные для вставки в виде пар: наименование столбца - значение
                    dbBalance = dbBalance + dbItems.dbNominal;
                    db.execSQL("UPDATE `mytable` SET `dbBalance`='"+dbBalance+"'");*/

                    // закрываем подключение к БД
                    //dbHelper.close();
                    break;

                case chekErrorDate:
                    Log.d(TAG, "За указанный период платежи не найдены" + chekError);
                    txtInfo.setText("За указанный период платежи не найдены");
                    txtInfo.setTextColor(Color.RED);

                    txtNString="";
                    txtDataString="";
                    txtInternetString="";
                    txtSummaString="";
                    txtBalanceBeforeString="";
                    txtBalanceAfterString="";
                    txtTypeString="";
                    txtCommentsString="";

                    txtN.setText(txtNString);
                    txtData.setText(txtDataString);
                    txtInternet.setText(txtInternetString);
                    txtSumma.setText(txtSummaString);
                    txtBalanceBefore.setText(txtBalanceBeforeString);
                    txtBalanceAfter.setText(txtBalanceAfterString);
                    txtType.setText(txtTypeString);
                    txtComments.setText(txtCommentsString);
                    break;

                default:
                    Log.d(TAG, "Неизвестная ошибка: " + chekError);
                    txtInfo.setText("Неизвестная ошибка");
                    txtInfo.setTextColor(Color.RED);

                    txtNString="";
                    txtDataString="";
                    txtInternetString="";
                    txtSummaString="";
                    txtBalanceBeforeString="";
                    txtBalanceAfterString="";
                    txtTypeString="";
                    txtCommentsString="";

                    txtN.setText(txtNString);
                    txtData.setText(txtDataString);
                    txtInternet.setText(txtInternetString);
                    txtSumma.setText(txtSummaString);
                    txtBalanceBefore.setText(txtBalanceBeforeString);
                    txtBalanceAfter.setText(txtBalanceAfterString);
                    txtType.setText(txtTypeString);
                    txtComments.setText(txtCommentsString);
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
