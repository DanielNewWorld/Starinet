package ua.zp.starinet.starinet_02072015;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import retrofit.RetrofitError;
import ua.zp.starinet.starinet_02072015.model.DBTickets;
import ua.zp.starinet.starinet_02072015.network.DataService;
import ua.zp.starinet.starinet_02072015.requests.GetTicketsRequest;

/**
 * Created by root on 19.08.15.
 */
public class TicketsNewText extends Fragment {

    Button btnCreate;
    EditText edtSubject;
    EditText edtText;

    RadioButton radio_low;      //низкий
    RadioButton radio_normal;   //нормальный
    RadioButton radio_urgent;   //срочный
    RadioButton radio_critical; //критичекий

    DBHelper dbHelper;
    private static final String TAG = "myLogs";
    SpiceManager TicketsSpiceManager = new SpiceManager(DataService.class);

    final int colorBLACK = 0;
    final int colorGREEN = 1;
    final int colorRED = 2;
    final int colorBLUE = 3;

    final int chekErrorOK = 0;
    //final int chekErrorDate = 1;
    //final int chekErrorDelete = 2;
    //final int chekErrorAdd = 3;
    //final int chekErrorNo = 4;
    public int chekError = 5;

    int dbPriority;

    int dbID;
    String dbAccountID;
    String dbDepartment;

    private CharSequence mTitle;
    FragmentTransaction fTrans;
    Info fragInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tickets_new_text, null);

        btnCreate = (Button) v.findViewById(R.id.btnCreate);
        edtSubject = (EditText) v.findViewById(R.id.edtSubject);
        edtText = (EditText) v.findViewById(R.id.edtText);

        radio_low = (RadioButton) v.findViewById(R.id.radio_low);
        radio_normal = (RadioButton) v.findViewById(R.id.radio_normal);
        radio_urgent = (RadioButton) v.findViewById(R.id.radio_urgent);
        radio_critical = (RadioButton) v.findViewById(R.id.radio_critical);

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.btnCreate:

                        dbPriority = 0;

                        if (radio_low.isChecked()) {
                            dbPriority = 1;
                        }
                        if (radio_normal.isChecked()) {
                            dbPriority = 2;
                        }
                        if (radio_urgent.isChecked()) {
                            dbPriority = 3;
                        }
                        if (radio_critical.isChecked()) {
                            dbPriority = 4;
                        }

                        if (dbPriority == 0) {
                            Toast.makeText(getActivity(), "Выберите приоритет вопроса!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Выберите приоритет вопроса!");
                            break;
                        }

                        if (edtSubject.getText().toString().equals("") |
                                edtText.getText().toString().equals(""))
                        {
                            // Здесь код, если EditText пуст
                            Toast.makeText(getActivity(), "Введите все поля обязательные для заполнения!", Toast.LENGTH_LONG).show();
                            break;
                        }
                        else
                        {
                            //dbDepartment = "finance";
                            TicketsSpiceManager.execute(new GetTicketsRequest(dbID, edtSubject.getText().toString(),
                                    dbAccountID, dbDepartment, dbPriority, edtText.getText().toString()), new GetTicketsListener());
                        }
                        break;
                }
            }
        };
        btnCreate.setOnClickListener(onClickListenerStart);
        return v;
    }

    class GetTicketsListener implements RequestListener<DBTickets> {
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
        public void onRequestSuccess(DBTickets dbItems) {

            if (dbItems == null) {
                Toast.makeText(getActivity(), "Нет даных!", Toast.LENGTH_LONG).show();
                return;
            }

            //txtInfo.setText(String.valueOf(dbItems.chekError));

            //ByPinSpiceManager.shouldStop();

            chekError = dbItems.chekError;

            // создаем объект для создания и управления версиями БД
            dbHelper = new DBHelper(getActivity());
            // подключаемся к БД
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            switch (chekError) {
                case chekErrorOK:
                    Log.d(TAG, "Ваш тикет принят в обработку!" + chekError);
                    Toast.makeText(getActivity(), "Ваш тикет принят в обработку!", Toast.LENGTH_LONG).show();

                    // подготовим данные для вставки в виде пар: наименование столбца - значение
                    db.execSQL("UPDATE `mytable` SET `info`='" + "Ваш тикет принят в обработку!" + "'");
                    db.execSQL("UPDATE `mytable` SET `info2`='" + "" + "'");
                    db.execSQL("UPDATE `mytable` SET `color`='" + colorGREEN + "'");

                    // закрываем подключение к БД
                    dbHelper.close();

                    //mTitle = getString(R.string.title_section6);
                    if (fragInfo == null) fragInfo = new Info();
                    fTrans = getFragmentManager().beginTransaction();
                    fTrans.replace(R.id.lnrInfo, fragInfo);
                    fTrans.addToBackStack(null);
                    fTrans.commit();

                    break;

                default:
                    Log.d(TAG, "Неизвестная ошибка!" + chekError);
                    Toast.makeText(getActivity(), "Неизвестная ошибка!", Toast.LENGTH_LONG).show();

                    // подготовим данные для вставки в виде пар: наименование столбца - значение
                    db.execSQL("UPDATE `mytable` SET `info`='" + "Неизвестная ошибка!" + "'");
                    db.execSQL("UPDATE `mytable` SET `info2`='" + "" + "'");
                    db.execSQL("UPDATE `mytable` SET `color`='" + colorRED + "'");

                    // закрываем подключение к БД
                    dbHelper.close();

                    //mTitle = getString(R.string.title_section6);
                    if (fragInfo == null) fragInfo = new Info();
                    fTrans = getFragmentManager().beginTransaction();
                    fTrans.replace(R.id.lnrInfo, fragInfo);
                    fTrans.addToBackStack(null);
                    fTrans.commit();
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
            int dbDepartmentIndex = c.getColumnIndex("dbDepartment");
            //int dbPasswordIndex = c.getColumnIndex("dbPassword");

            do {
                dbID = c.getInt(dbIDIndex);
                dbAccountID = c.getString(dbAccountIDIndex);
                dbDepartment = c.getString(dbDepartmentIndex);
                //dbPassword = c.getString(dbPasswordIndex);

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        c.close();

        // закрываем подключение к БД
        dbHelper.close();

        //Toast.makeText(getActivity(), "chekError: " + String.valueOf(chekError) + "; status: " + String.valueOf(status), Toast.LENGTH_LONG).show();
        // создаем объект
        TicketsSpiceManager.start(activity);
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
