package ua.zp.starinet.starinet_02072015;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import retrofit.RetrofitError;
import ua.zp.starinet.starinet_02072015.model.ArrayListDBTicketsAll;
import ua.zp.starinet.starinet_02072015.model.DBTickets;
import ua.zp.starinet.starinet_02072015.model.DBTicketsAll;
import ua.zp.starinet.starinet_02072015.network.DataService;
import ua.zp.starinet.starinet_02072015.requests.GetTicketsAllRequest;
import ua.zp.starinet.starinet_02072015.requests.GetTicketsRequest;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 19.08.15.
 */
public class TicketsAll extends Fragment {

    DBHelper dbHelper;
    private static final String TAG = "myLogs";
    SpiceManager TicketsAllSpiceManager = new SpiceManager(DataService.class);

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

    int dbID;

    private RecyclerView rv;
    private List<Tickets> tickets;

    String priority;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tickets_all, null);

        rv=(RecyclerView)v.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

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
        //dbHelper.close();

        TicketsAllSpiceManager.execute(new GetTicketsAllRequest(dbID), new GetTicketsAllListener());

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        };
        //btnCreate.setOnClickListener(onClickListenerStart);
        return v;
    }

    class GetTicketsAllListener implements RequestListener<ArrayListDBTicketsAll> {
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
        public void onRequestSuccess(ArrayListDBTicketsAll dbItems) {

            if (dbItems == null) {
                //Toast.makeText(MainActivity.this, "Нет даных!", Toast.LENGTH_LONG).show();
                return;
            }

            //txtInfo.setText(String.valueOf(dbItems.chekError));

            //ByPinSpiceManager.shouldStop();

            for (DBTicketsAll i: dbItems) {
                chekError = i.chekError;
            }
            //chekError=chekErrorOK;
            //Log.d(TAG, "Error: " + chekError);

            switch (chekError) {
                case chekErrorOK:
                    Log.d(TAG, "Посмотрели все тикеты" + chekError);
                    //Toast.makeText(getActivity(), "OK", Toast.LENGTH_LONG).show();

                    tickets = new ArrayList<>();

                    for (DBTicketsAll i: dbItems) {

                        switch (i.dbDepartment) {
                            case "finance":
                                i.dbDepartment = "Финансовые вопросы";
                                break;

                            case "technical":
                                i.dbDepartment = "Технические вопросы";
                                break;

                            case "abuse":
                                i.dbDepartment = "Другие жалобы";
                                break;

                            case "wishes":
                                i.dbDepartment = "Пожелания и предложения";
                                break;

                            case "offline":
                                i.dbDepartment = "Расторжение договора";
                                break;
                        }

                        priority = "";
                        switch (i.dbPriority) {
                            case 1:
                                priority = "Низкий";
                                break;

                            case 2:
                                priority = "Нормальный";
                                break;

                            case 3:
                                priority = "Срочный";
                                break;

                            case 4:
                                priority = "Критический";
                                break;
                        }

                        switch (i.dbStatus) {
                            case "new":
                                i.dbStatus = "Новый";
                                break;

                            case "close":
                                i.dbStatus = "Закрыто (Решено)";
                                break;

                            case "unswered":
                                i.dbStatus = "Ожидание ответа от абонента";
                                break;

                            case "onhold":
                                i.dbStatus = "Ожидание ответа от оператора";
                                break;
                        }

                        tickets.add(new Tickets(i.dbTicketsID, i.dbSubject, i.dbCreated,i.dbUpdated,i.dbDepartment,i.dbStatus,priority, dbID));
                    }

                    RVAdapter adapter = new RVAdapter(tickets);
                    rv.setAdapter(adapter);

                    break;

                /*case chekErrorDate:
                    Log.d(TAG, "За указанный период списания не найдены" + chekError);

                    break;*/

                default:
                    Log.d(TAG, "Вы ещё не создавали тикеты: " + chekError);
                    Toast.makeText(getActivity(), "Вы ещё не создавали тикеты!", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // создаем объект
        TicketsAllSpiceManager.start(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        TicketsAllSpiceManager.shouldStop();
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
