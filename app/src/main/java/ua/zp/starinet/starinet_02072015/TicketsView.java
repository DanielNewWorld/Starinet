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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.RetrofitError;
import ua.zp.starinet.starinet_02072015.model.ArrayListDBTicketsView;
import ua.zp.starinet.starinet_02072015.model.DBTicketsUpdate;
import ua.zp.starinet.starinet_02072015.model.DBTicketsView;
import ua.zp.starinet.starinet_02072015.model.DBTicketsOnlyInfo;
import ua.zp.starinet.starinet_02072015.network.DataService;
import ua.zp.starinet.starinet_02072015.requests.GetTicketsOnlyInfoRequest;
import ua.zp.starinet.starinet_02072015.requests.GetTicketsUpdateRequest;
import ua.zp.starinet.starinet_02072015.requests.GetTicketsViewRequest;

/**
 * Created by root on 19.08.15.
 */
public class TicketsView extends Fragment {

    DBHelper dbHelper;
    private static final String TAG = "myLogs";
    SpiceManager TicketsViewSpiceManager = new SpiceManager(DataService.class);
    SpiceManager TicketsOnlyInfoSpiceManager = new SpiceManager(DataService.class);
    SpiceManager TicketsUpdateStatusPrioritySpiceManager = new SpiceManager(DataService.class);

    final int colorBLACK = 0;
    final int colorGREEN = 1;
    final int colorRED = 2;
    final int colorBLUE = 3;

    final int statusClose = -1;
    final int statusOther = -2;
    int status = statusOther;

    final int ticketsInfo = 0;

    final int chekErrorOK = 0;
    public int chekError = 5;

    //private CharSequence mTitle;
    //FragmentTransaction fTrans;
    //Info fragInfo;

    private RecyclerView rvView;
    private List<TicketsViewClass> ticketsView;

    //int dbID;
    String dbTicketsID;

    TextView txtTicketsN;
    TextView txtCreated;
    TextView txtDepartment;
    TextView txtUpdated;
    TextView txtSubject;
    TextView txtInfo;
    Spinner spnStatus;
    Spinner spnPriority;
    Button btnUpdate;

    String[] spnStatusName = { "Новый", "Закрыто (Решено)", "Ожидание ответа от абонента", "Ожидание ответа от оператора" };

    String[] spnPriorityName = { "Низкий", "Нормальный", "Срочный", "Критический" };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tickets_view, null);

        txtTicketsN = (TextView) v.findViewById(R.id.txtTicketsN);
        txtCreated = (TextView) v.findViewById(R.id.txtCreated);
        txtDepartment = (TextView) v.findViewById(R.id.txtDepartment);
        txtUpdated = (TextView) v.findViewById(R.id.txtUpdated);
        txtSubject = (TextView) v.findViewById(R.id.txtSubject);
        txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        spnStatus = (Spinner) v.findViewById(R.id.spnStatus);
        spnPriority = (Spinner) v.findViewById(R.id.spnPriority);
        btnUpdate = (Button) v.findViewById(R.id.btnUpdate);

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spnStatusName);

        // присваиваем адаптер списку
        spnStatus.setAdapter(adapter);
        spnStatus.setPrompt("Установите статус");
        spnStatus.setSelection(0);

        spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // создаем адаптер
        ArrayAdapter<String> adapterPriority = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, spnPriorityName);

        // присваиваем адаптер списку
        spnPriority.setAdapter(adapterPriority);
        spnPriority.setPrompt("Установите приоритет");
        spnPriority.setSelection(1);

        spnPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rvView = (RecyclerView)v.findViewById(R.id.rvView);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvView.setLayoutManager(llm);
        rvView.setHasFixedSize(true);

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
            int dbTicketsIDIndex = c.getColumnIndex("ticketsID");

            do {
                dbTicketsID = c.getString(dbTicketsIDIndex);

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        c.close();

        // закрываем подключение к БД
        dbHelper.close();

        //Toast.makeText(getActivity(),"View: " + String.valueOf(dbID), Toast.LENGTH_LONG).show();
        txtTicketsN.setText("№" + dbTicketsID);
        txtInfo.setText("");

        TicketsOnlyInfoSpiceManager.execute(new GetTicketsOnlyInfoRequest(dbTicketsID, ticketsInfo), new GetTicketsOnlyInfoListener());

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TicketsUpdateStatusPrioritySpiceManager.execute(new GetTicketsUpdateRequest(dbTicketsID, spnStatus.getSelectedItemPosition(),
                                                                                                         spnPriority.getSelectedItemPosition() + 1), new GetTicketsUpdateListener());
            }
        };
        btnUpdate.setOnClickListener(onClickListenerStart);
        return v;
    }

    class GetTicketsOnlyInfoListener implements RequestListener<DBTicketsOnlyInfo> {
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
        public void onRequestSuccess(DBTicketsOnlyInfo dbItems) {

            if (dbItems == null) {
                Toast.makeText(getActivity(), "Нет даных!", Toast.LENGTH_LONG).show();
                return;
            }

            //ByPinSpiceManager.shouldStop();

            chekError = dbItems.chekError;

            switch (chekError) {
                case chekErrorOK:
                    Log.d(TAG, "Посмотрели выбранный тикет" + chekError);
                    //Toast.makeText(getActivity(), "OK", Toast.LENGTH_LONG).show();

                        switch (dbItems.dbDepartment) {
                            case "finance":
                                dbItems.dbDepartment = "Финансовые вопросы";
                                break;

                            case "technical":
                                dbItems.dbDepartment = "Технические вопросы";
                                break;

                            case "abuse":
                                dbItems.dbDepartment = "Другие жалобы";
                                break;

                            case "wishes":
                                dbItems.dbDepartment = "Пожелания и предложения";
                                break;

                            case "offline":
                                dbItems.dbDepartment = "Расторжение договора";
                                break;
                        }

                        //String priority = "";
                        switch (dbItems.dbPriority) {
                            case 1:
                                //priority = "Низкий";
                                spnPriority.setSelection(0);
                                break;

                            case 2:
                                //priority = "Нормальный";
                                spnPriority.setSelection(1);
                                break;

                            case 3:
                                //priority = "Срочный";
                                spnPriority.setSelection(2);
                                break;

                            case 4:
                                //priority = "Критический";
                                spnPriority.setSelection(3);
                                break;
                        }

                        switch (dbItems.dbStatus) {
                            case "new":
                                spnStatus.setSelection(0);
                                status = statusOther;
                                break;

                            case "close":
                                dbItems.dbStatus = "Закрыто (Решено)";
                                spnStatus.setSelection(1);
                                status = statusClose;
                                break;

                            case "unswered":
                                dbItems.dbStatus = "Ожидание ответа от абонента";
                                spnStatus.setSelection(2);
                                status = statusOther;
                                break;

                            case "onhold":
                                dbItems.dbStatus = "Ожидание ответа от оператора";
                                spnStatus.setSelection(3);
                                status = statusOther;
                                break;
                        }

                    txtCreated.setText(dbItems.dbCreated);
                    txtDepartment.setText(dbItems.dbDepartment);
                    txtUpdated.setText(dbItems.dbUpdated);
                    txtSubject.setText(dbItems.dbSubject);

                    TicketsViewSpiceManager.execute(new GetTicketsViewRequest(dbTicketsID), new GetTicketsViewListener());
                    break;

                default:
                    Log.d(TAG, "Такого тикета нет! " + chekError);
                    Toast.makeText(getActivity(), "Такого тикета нет!", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    class GetTicketsViewListener implements RequestListener<ArrayListDBTicketsView> {
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
        public void onRequestSuccess(ArrayListDBTicketsView dbItems) {

            if (dbItems == null) {
                Toast.makeText(getActivity(), "Нет даных!", Toast.LENGTH_LONG).show();
                return;
            }

            for (DBTicketsView i: dbItems) {
                chekError = i.chekError;
            }

            switch (chekError) {
                case chekErrorOK:
                    Log.d(TAG, "Посмотрели переписку тикетов" + chekError);
                    //Toast.makeText(getActivity(), "OK", Toast.LENGTH_LONG).show();

                    ticketsView = new ArrayList<>();
                    for (DBTicketsView i: dbItems) {
                        ticketsView.add(new TicketsViewClass(i.dbCreated, i.dbText, i.dbAdminID));
                    }
                    ticketsView.add(new TicketsViewClass("", "", status));

                    RVAdapterTicketsView adapter = new RVAdapterTicketsView(ticketsView);
                    rvView.setAdapter(adapter);

                    break;

                default:
                    Log.d(TAG, "Неизвестная ошибка: " + chekError);
                    Toast.makeText(getActivity(), "Неизвестная ошибка!", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    class GetTicketsUpdateListener implements RequestListener<DBTicketsUpdate> {
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
        public void onRequestSuccess(DBTicketsUpdate dbItems) {

            if (dbItems == null) {
                Toast.makeText(getActivity(), "Нет даных!", Toast.LENGTH_LONG).show();
                return;
            }

            //Toast.makeText(getActivity(), "Ответ", Toast.LENGTH_LONG).show();

            //ByPinSpiceManager.shouldStop();

            chekError = dbItems.chekError;

            switch (chekError) {
                case chekErrorOK:
                    Log.d(TAG, "Обновили статус и приоритет" + chekError);
                    //Toast.makeText(getActivity(), "OK", Toast.LENGTH_LONG).show();

                    //String priority = "";
                    switch (dbItems.dbPriority) {
                        case 1:
                            //priority = "Низкий";
                            spnPriority.setSelection(0);
                            break;

                        case 2:
                            //priority = "Нормальный";
                            spnPriority.setSelection(1);
                            break;

                        case 3:
                            //priority = "Срочный";
                            spnPriority.setSelection(2);
                            break;

                        case 4:
                            //priority = "Критический";
                            spnPriority.setSelection(3);
                            break;
                    }

                    switch (dbItems.dbStatus) {
                        case "new":
                            spnStatus.setSelection(0);
                            status = statusOther;
                            break;

                        case "close":
                            dbItems.dbStatus = "Закрыто (Решено)";
                            spnStatus.setSelection(1);
                            status = statusClose;
                            break;

                        case "unswered":
                            dbItems.dbStatus = "Ожидание ответа от абонента";
                            spnStatus.setSelection(2);
                            status = statusOther;
                            break;

                        case "onhold":
                            dbItems.dbStatus = "Ожидание ответа от оператора";
                            spnStatus.setSelection(3);
                            status = statusOther;
                            break;
                    }

                    txtInfo.setText("Обновленно!");
                    break;

                default:
                    Log.d(TAG, "Такого тикета нет! " + chekError);
                    Toast.makeText(getActivity(), "Такого тикета нет!", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // создаем объект
        TicketsViewSpiceManager.start(getActivity());
        TicketsOnlyInfoSpiceManager.start(getActivity());
        TicketsUpdateStatusPrioritySpiceManager.start(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();

        if (TicketsViewSpiceManager.isStarted()) {
            TicketsViewSpiceManager.shouldStop();}

        if (TicketsOnlyInfoSpiceManager.isStarted()) {
            TicketsOnlyInfoSpiceManager.shouldStop();}

        if (TicketsUpdateStatusPrioritySpiceManager.isStarted()) {
            TicketsUpdateStatusPrioritySpiceManager.shouldStop();}
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
