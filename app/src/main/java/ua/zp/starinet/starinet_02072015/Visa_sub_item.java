package ua.zp.starinet.starinet_02072015;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;

import ua.zp.starinet.starinet_02072015.network.DataService;

/**
 * Created by root on 24.08.15.
 */
public class Visa_sub_item extends Fragment {

    Button btnVisa;
    EditText edtCount;

    Integer oplata;

    DBHelper dbHelper;
    private static final String TAG = "myLogs";
    SpiceManager TicketsSpiceManager = new SpiceManager(DataService.class);

    final int chekErrorOK = 0;
    //final int chekErrorDate = 1;
    //final int chekErrorDelete = 2;
    //final int chekErrorAdd = 3;
    //final int chekErrorNo = 4;
    public int chekError = 5;

    int dbID;

    private CharSequence mTitle;
    FragmentTransaction fTrans;
    Info fragInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.visa_sub_item, null);

        btnVisa = (Button) v.findViewById(R.id.btnVisa);
        edtCount = (EditText) v.findViewById(R.id.edtCount);

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
            int dbIDIndex = c.getColumnIndex("dbID");
            //int dbAccountIDIndex = c.getColumnIndex("dbAccountID");
            //int dbDepartmentIndex = c.getColumnIndex("dbDepartment");
            //int dbPasswordIndex = c.getColumnIndex("dbPassword");

            do {
                dbID = c.getInt(dbIDIndex);
                //dbAccountID = c.getString(dbAccountIDIndex);
                //dbDepartment = c.getString(dbDepartmentIndex);
                //dbPassword = c.getString(dbPasswordIndex);

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        c.close();

        // закрываем подключение к БД
        dbHelper.close();

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.btnVisa:

                        if (edtCount.getText().toString().equals(""))
                        {
                            // Здесь код, если EditText пуст
                            Toast.makeText(getActivity(), "Введите поля обязательные для заполнения!", Toast.LENGTH_LONG).show();
                            break;
                        }
                        else
                        {
                            oplata = new Integer(edtCount.getText().toString());

                            if (oplata < 10) {
                                Toast.makeText(getActivity(), "Минимальная сумма оплаты: 10 грн", Toast.LENGTH_LONG).show();
                                break;
                            }
                        }
                        break;

                }
            }
        };
        btnVisa.setOnClickListener(onClickListenerStart);
        return v;
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
