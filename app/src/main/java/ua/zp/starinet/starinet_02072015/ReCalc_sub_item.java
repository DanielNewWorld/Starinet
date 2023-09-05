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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import retrofit.RetrofitError;
import ua.zp.starinet.starinet_02072015.model.ArrayListDBReCalcSub;
import ua.zp.starinet.starinet_02072015.model.DBReCalcSub;
import ua.zp.starinet.starinet_02072015.network.DataService;
import ua.zp.starinet.starinet_02072015.requests.GetReCalcSubRequest;

/**
 * Created by root on 24.08.15.
 */
public class ReCalc_sub_item extends Fragment {

    TextView txtData;
    TextView txtPeriod;
    TextView txtVopros;
    TextView txtTel;
    TextView txtStatus;
    TextView txtInfo;

    Button btnReCalc;

    EditText edtFIO;
    EditText edtTel;
    EditText edtComment;

    Spinner spnMonth;
    Spinner spnGod;
    Spinner spnMonthEnd;
    Spinner spnGodEnd;
    Spinner spnDay;
    Spinner spnDayEnd;

    LinearLayout lnrAdd;

    String[] spnMonthName = { "январь", "февраль", "март", "апрель", "май", "июнь",
            "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь" };
    String[] spnGodName = { "2015", "2014", "2013", "2012", "2011", "2010"};
    String[] spnDayName = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};

    DBHelper dbHelper;
    private static final String TAG = "myLogs";
    SpiceManager ReCalcSpiceManager = new SpiceManager(DataService.class);

    final int chekErrorOK = 0;
    final int chekErrorDate = 1;
    //final int chekErrorDelete = 2;
    final int chekErrorAdd = 3;
    //final int chekErrorNo = 4;
    public int chekError = 5;

    int dbID;
    String strData;
    String strPeriod;
    String strVopros;
    String strTel;
    String strStatus;
    String dbAccountID;

    String edtHomeString;
    String edtEndString;

    final int statusAdd = 0;
    //final int statusDelete = 1;
    final int statusInfo = 2;
    public int status = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recalc_sub_item, null);

        txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        txtData = (TextView) v.findViewById(R.id.txtData);
        txtPeriod = (TextView) v.findViewById(R.id.txtPeriod);
        txtVopros = (TextView) v.findViewById(R.id.txtVopros);
        txtTel = (TextView) v.findViewById(R.id.txtTel);
        txtStatus = (TextView) v.findViewById(R.id.txtStatus);
        btnReCalc = (Button) v.findViewById(R.id.btnReCalc);

        edtFIO = (EditText) v.findViewById(R.id.edtFIO);
        edtTel = (EditText) v.findViewById(R.id.edtTel);
        edtComment = (EditText) v.findViewById(R.id.edtComment);

        spnMonth = (Spinner) v.findViewById(R.id.spnMonth);
        spnMonthEnd = (Spinner) v.findViewById(R.id.spnMonthEnd);
        spnGod = (Spinner) v.findViewById(R.id.spnGod);
        spnGodEnd = (Spinner) v.findViewById(R.id.spnGodEnd);
        spnDay = (Spinner) v.findViewById(R.id.spnDay);
        spnDayEnd = (Spinner) v.findViewById(R.id.spnDayEnd);

        lnrAdd = (LinearLayout) v.findViewById(R.id.lnrAdd);

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

        // создаем адаптер
        ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spnDayName);

        // присваиваем адаптер списку
        spnDay.setAdapter(adapterDay);
        spnDay.setPrompt("Выберите день");
        spnDay.setSelection(0);

        spnDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // создаем адаптер
        ArrayAdapter<String> adapterDayEnd = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spnDayName);

        // присваиваем адаптер списку
        spnDayEnd.setAdapter(adapterDayEnd);
        spnDayEnd.setPrompt("Выберите день");
        spnDayEnd.setSelection(0);

        spnDayEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                    case R.id.btnReCalc:

                        if (edtFIO.getText().toString().equals("") |
                            edtTel.getText().toString().equals("") |
                            edtComment.getText().toString().equals(""))
                        {
                            // Здесь код, если EditText пуст
                            Toast.makeText(getActivity(), "Введите все поля обязательные для заполнения!", Toast.LENGTH_LONG).show();
                            break;
                        }
                        else
                        {
                            // если есть текст, то здесь другой код
                            int spnMonthItem = spnMonth.getSelectedItemPosition() + 1;
                            int spnMonthEndItem = spnMonthEnd.getSelectedItemPosition() + 1;

                            edtHomeString = spnGod.getSelectedItem().toString() + "." + spnMonthItem + "." + spnDay.getSelectedItem().toString();
                            edtEndString = spnGodEnd.getSelectedItem().toString() + "." + spnMonthEndItem + "." + spnDayEnd.getSelectedItem().toString();

                            //edtHomeString = "2015-05-01";
                            //edtEndString = "2015-05-15";
                            ReCalcSpiceManager.execute(new GetReCalcSubRequest(dbID, statusAdd, dbAccountID,
                                    edtFIO.getText().toString(),
                                    edtTel.getText().toString(),
                                    edtHomeString, edtEndString,
                                    edtComment.getText().toString()), new GetReCalcListener());
                        }
                        break;

                }
            }
        };
        btnReCalc.setOnClickListener(onClickListenerStart);
        return v;
    }

    class GetReCalcListener implements RequestListener<ArrayListDBReCalcSub> {
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
        public void onRequestSuccess(ArrayListDBReCalcSub dbItems) {

            if (dbItems == null) {
                Toast.makeText(getActivity(), "Нет даных!", Toast.LENGTH_LONG).show();
                return;
            }

            //txtInfo.setText(String.valueOf(dbItems.chekError));

            //ByPinSpiceManager.shouldStop();

            //chekError = chekErrorOK;

            for (DBReCalcSub i: dbItems) {
                chekError = i.chekError;
            }
            //Log.d(TAG, "Error: " + chekError);

            Toast.makeText(getActivity(),"chekError: " + String.valueOf(chekError) + "; status: " + String.valueOf(status), Toast.LENGTH_LONG).show();
            switch (chekError) {
                case chekErrorOK:
                    //Log.d(TAG, "ID и PIN совпали:" + chekError);

                    strData = "";
                    strPeriod = "";
                    strVopros = "";
                    strTel = "";
                    strStatus = "";

                    for (DBReCalcSub i : dbItems) {
                        strData = strData + i.dbData + "\n";
                        strPeriod = strPeriod + i.dbPeriod + "\n";
                        strVopros = strVopros + i.dbMessage + "\n";
                        strTel = strTel + i.dbTel + "\n";
                        strStatus = strStatus + i.dbStatus + "\n";
                    }

                    txtData.setText(strData);
                    txtData.setTextColor(Color.BLACK);
                    txtPeriod.setText(strPeriod);
                    txtVopros.setText(strVopros);
                    txtTel.setText(strTel);
                    txtStatus.setText(strStatus);
                    break;

                case chekErrorAdd:
                    Toast.makeText(getActivity(), "Ваша заявка на перерасчет принята в обработку", Toast.LENGTH_LONG).show();
                    txtData.setText("Ваша заявка на перерасчет принята в обработку!");
                    txtData.setTextColor(Color.GREEN);
                    lnrAdd.setVisibility(View.INVISIBLE);
                    break;

                default:
                    Log.d(TAG, "Заявок на перерасчет не найдено" + chekError);
                    txtData.setText("Заявок на перерасчет не найдено");
                    txtData.setTextColor(Color.RED);
                    txtPeriod.setText("");
                    txtTel.setText("");
                    txtStatus.setText("");
                    break;
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        chekError = 5;

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(activity);
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Log.d(TAG, "--- Rows in mytable: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("mytable", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int dbIDIndex = c.getColumnIndex("dbID");
            int dbAccountIDIndex = c.getColumnIndex("dbAccountID");

            do {
                dbID = c.getInt(dbIDIndex);
                dbAccountID = c.getString(dbAccountIDIndex);

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        c.close();

        // закрываем подключение к БД
        dbHelper.close();

        Toast.makeText(getActivity(),"chekError: " + String.valueOf(chekError) + "; status: " + String.valueOf(status), Toast.LENGTH_LONG).show();
        // создаем объект
        ReCalcSpiceManager.start(activity);
        ReCalcSpiceManager.execute(new GetReCalcSubRequest(dbID, statusInfo, "", "", "", "", "", ""), new GetReCalcListener());
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
