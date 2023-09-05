package ua.zp.starinet.starinet_02072015;

import android.app.Activity;
import android.app.Fragment;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import ua.zp.starinet.starinet_02072015.model.DBTicketsAnswer;
import ua.zp.starinet.starinet_02072015.model.DBTicketsOnlyInfo;
import ua.zp.starinet.starinet_02072015.network.DataService;
import ua.zp.starinet.starinet_02072015.requests.GetTicketsAnswerRequest;
import ua.zp.starinet.starinet_02072015.requests.GetTicketsOnlyInfoRequest;

/**
 * Created by root on 19.08.15.
 */
public class TicketsAnswer extends Fragment {

    DBHelper dbHelper;
    private static final String TAG = "myLogs";
    SpiceManager TicketsOnlyInfoSpiceManager = new SpiceManager(DataService.class);
    SpiceManager TicketsAnswerSpiceManager = new SpiceManager(DataService.class);

    final int colorBLACK = 0;
    final int colorGREEN = 1;
    final int colorRED = 2;
    final int colorBLUE = 3;

    final int ticketsInfo = 0;
    final int ticketsAnswer = 1;
    int ticketsStatus = ticketsInfo;

    final int chekErrorOK = 0;
    final int chekErrorAnswer = 1;
    //final int chekErrorDelete = 2;
    //final int chekErrorAdd = 3;
    //final int chekErrorNo = 4;
    public int chekError = 5;

    String dbTicketsID;
    String priority;
    int dbID;
    int dbTicketsStatus;

    TextView txtTicketsN;
    TextView txtStatus;
    TextView txtCreated;
    TextView txtDepartment;
    TextView txtPriority;
    TextView txtUpdated;
    TextView txtSubject;
    TextView txtInfo;

    EditText edtText;
    Button btnToSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tickets_answer, null);

        txtTicketsN = (TextView) v.findViewById(R.id.txtTicketsN);
        txtStatus = (TextView) v.findViewById(R.id.txtStatus);
        txtCreated = (TextView) v.findViewById(R.id.txtCreated);
        txtDepartment = (TextView) v.findViewById(R.id.txtDepartment);
        txtPriority = (TextView) v.findViewById(R.id.txtPriority);
        txtUpdated = (TextView) v.findViewById(R.id.txtUpdated);
        txtSubject = (TextView) v.findViewById(R.id.txtSubject);
        txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        btnToSend = (Button) v.findViewById(R.id.btnToSend);
        edtText = (EditText) v.findViewById(R.id.edtText);

        txtInfo.setText("");

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
            int dbTicketsIDIndex = c.getColumnIndex("ticketsID");
            int dbTicketsStatustIndex = c.getColumnIndex("ticketsStatus");
            //int dbPasswordIndex = c.getColumnIndex("dbPassword");

            do {
                dbID = c.getInt(dbIDIndex);
                dbTicketsID = c.getString(dbTicketsIDIndex);
                dbTicketsStatus = c.getInt(dbTicketsStatustIndex);
                //dbPassword = c.getString(dbPasswordIndex);

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        c.close();

        // закрываем подключение к БД
        dbHelper.close();

        //Toast.makeText(getActivity(), String.valueOf(dbID), Toast.LENGTH_LONG).show();
        txtTicketsN.setText(dbTicketsID);

        if (dbTicketsStatus == -1) ticketsStatus = ticketsAnswer;
        TicketsOnlyInfoSpiceManager.execute(new GetTicketsOnlyInfoRequest(dbTicketsID, ticketsStatus), new GetTicketsOnlyInfoListener());

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtText.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity(), "Введите текст поля!", Toast.LENGTH_LONG).show();
                }
                else
                  TicketsAnswerSpiceManager.execute(new GetTicketsAnswerRequest(dbID, dbTicketsID, edtText.getText().toString()), new GetTicketsAnswerListener());
            }
        };
        btnToSend.setOnClickListener(onClickListenerStart);
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
            //Toast.makeText(getActivity(), "OK", Toast.LENGTH_LONG).show();

            //txtInfo.setText(String.valueOf(dbItems.chekError));

            //ByPinSpiceManager.shouldStop();

            chekError = dbItems.chekError;

            switch (chekError) {
                case chekErrorOK:
                    Log.d(TAG, "Тикет успешно открыт!" + chekError);
                    //Toast.makeText(getActivity(), "Тикет успешно открыт!", Toast.LENGTH_LONG).show();
                    //txtInfo.setText("Тикет успешно открыт!");

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

                        priority = "";
                        switch (dbItems.dbPriority) {
                            case 1:
                                priority = "Низкий";
                                txtPriority.setTextColor(Color.BLACK);
                                break;

                            case 2:
                                priority = "Нормальный";
                                txtPriority.setTextColor(Color.GREEN);
                                break;

                            case 3:
                                priority = "Срочный";
                                txtPriority.setTextColor(Color.YELLOW);
                                break;

                            case 4:
                                priority = "Критический";
                                txtPriority.setTextColor(Color.RED);
                                break;
                        }

                        switch (dbItems.dbStatus) {
                            case "new":
                                dbItems.dbStatus = "Новый";
                                txtStatus.setTextColor(Color.BLUE);
                                break;

                            case "close":
                                dbItems.dbStatus = "Закрыто (Решено)";
                                txtStatus.setTextColor(Color.RED);
                                break;

                            case "unswered":
                                dbItems.dbStatus = "Ожидание ответа от абонента";
                                txtStatus.setTextColor(Color.GREEN);
                                break;

                            case "onhold":
                                dbItems.dbStatus = "Ожидание ответа от оператора";
                                txtStatus.setTextColor(Color.YELLOW);
                                break;
                        }

                    txtStatus.setText(dbItems.dbStatus);
                    txtCreated.setText(dbItems.dbCreated);
                    txtDepartment.setText(dbItems.dbDepartment);
                    txtPriority.setText(priority);
                    txtUpdated.setText(dbItems.dbUpdated);
                    txtSubject.setText(dbItems.dbSubject);
                    break;

                case chekErrorAnswer:
                    Log.d(TAG, "Тикет успешно открыт!" + chekError);
                    Toast.makeText(getActivity(), "Тикет успешно открыт!", Toast.LENGTH_LONG).show();
                    txtInfo.setText("Тикет успешно открыт!");

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

                    priority = "";
                    switch (dbItems.dbPriority) {
                        case 1:
                            priority = "Низкий";
                            txtPriority.setTextColor(Color.BLACK);
                            break;

                        case 2:
                            priority = "Нормальный";
                            txtPriority.setTextColor(Color.GREEN);
                            break;

                        case 3:
                            priority = "Срочный";
                            txtPriority.setTextColor(Color.YELLOW);
                            break;

                        case 4:
                            priority = "Критический";
                            txtPriority.setTextColor(Color.RED);
                            break;
                    }

                    switch (dbItems.dbStatus) {
                        case "new":
                            dbItems.dbStatus = "Новый";
                            txtStatus.setTextColor(Color.BLUE);
                            break;

                        case "close":
                            dbItems.dbStatus = "Закрыто (Решено)";
                            txtStatus.setTextColor(Color.RED);
                            break;

                        case "unswered":
                            dbItems.dbStatus = "Ожидание ответа от абонента";
                            txtStatus.setTextColor(Color.GREEN);
                            break;

                        case "onhold":
                            dbItems.dbStatus = "Ожидание ответа от оператора";
                            txtStatus.setTextColor(Color.YELLOW);
                            break;
                    }

                    txtStatus.setText(dbItems.dbStatus);
                    txtCreated.setText(dbItems.dbCreated);
                    txtDepartment.setText(dbItems.dbDepartment);
                    txtPriority.setText(priority);
                    txtUpdated.setText(dbItems.dbUpdated);
                    txtSubject.setText(dbItems.dbSubject);
                    break;

                default:
                    Log.d(TAG, "Такого тикета нет! " + chekError);
                    Toast.makeText(getActivity(), "Такого тикета нет!", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    class GetTicketsAnswerListener implements RequestListener<DBTicketsAnswer> {
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
        public void onRequestSuccess(DBTicketsAnswer dbItems) {

            if (dbItems == null) {
                Toast.makeText(getActivity(), "Нет даных!", Toast.LENGTH_LONG).show();
                return;
            }

            //txtInfo.setText(String.valueOf(dbItems.chekError));

            //ByPinSpiceManager.shouldStop();


                chekError = dbItems.chekError;
            //chekError=chekErrorOK;
            //Log.d(TAG, "Error: " + chekError);

            switch (chekError) {
                case chekErrorOK:
                    Log.d(TAG, "Посмотрели переписку тикетов" + chekError);
                    Toast.makeText(getActivity(), "OK", Toast.LENGTH_LONG).show();
                    break;

                default:
                    Log.d(TAG, "Неизвестная ошибка: " + chekError);
                    Toast.makeText(getActivity(), "Неизвестная ошибка!", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // создаем объект
        TicketsAnswerSpiceManager.start(getActivity());
        TicketsOnlyInfoSpiceManager.start(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        TicketsAnswerSpiceManager.shouldStop();
        TicketsOnlyInfoSpiceManager.shouldStop();
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
