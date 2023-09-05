package ua.zp.starinet.starinet_02072015;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import ua.zp.starinet.starinet_02072015.model.ArrayListDBShares;
import ua.zp.starinet.starinet_02072015.model.DBShares;
import ua.zp.starinet.starinet_02072015.network.DataService;
import ua.zp.starinet.starinet_02072015.requests.GetSharesRequest;

/**
 * Created by root on 24.08.15.
 */
public class Shares_sub_item extends Fragment {

    private static final String TAG = "myLogs";
    DBHelper dbHelper;
    SpiceManager SharesSpiceManager = new SpiceManager(DataService.class);

    int dbID;

    final int chekErrorOK = 0;
    public int chekError = 5;

    private RecyclerView rvShares;
    private List<SharesRVClass> sharesRV;

    final int colorBLACK = 0;
    final int colorGREEN = 1;
    final int colorRED = 2;
    final int colorBLUE = 3;

    private CharSequence mTitle;
    FragmentTransaction fTrans;
    Info fragInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.shares_sub_item, null);

        rvShares = (RecyclerView) v.findViewById(R.id.rvShares);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvShares.setLayoutManager(llm);
        rvShares.setHasFixedSize(true);

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

            do {
                dbID = c.getInt(dbIDIndex);

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        c.close();

        // закрываем подключение к БД
        dbHelper.close();

        SharesSpiceManager.execute(new GetSharesRequest(dbID), new GetSharesListener());

        return v;
    }

    class GetSharesListener implements RequestListener<ArrayListDBShares> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            RetrofitError cause = (RetrofitError) spiceException.getCause();
            if (cause == null ||
                    cause.isNetworkError() ||
                    cause.getResponse() == null) {
                Toast.makeText(getActivity(), "Нет доступа к Интернету", Toast.LENGTH_LONG).show();
            }
            else {
                switch (cause.getResponse().getStatus()) {
                    case 401:
                        Toast.makeText(getActivity(), "Нет доступа к серверу!", Toast.LENGTH_LONG).show();
                        break;
                }
            }
            return;
        }

        @Override
        public void onRequestSuccess(ArrayListDBShares dbItems) {

            if (dbItems == null) {
                Toast.makeText(getActivity(), "Нет даных!", Toast.LENGTH_LONG).show();
                return;
            }

            for (DBShares i: dbItems) {
                chekError = i.chekError;
            }

            switch (chekError) {
                case chekErrorOK:
                    Log.d(TAG, "Посмотрели акции" + chekError);
                    //Toast.makeText(getActivity(), "OK", Toast.LENGTH_LONG).show();

                    sharesRV = new ArrayList<>();
                    String status="";
                    for (DBShares i: dbItems) {
                        switch (i.sharesStatus) {
                            case 1:
                                status = "Активная";
                                break;

                            case 0:
                                status = "Неактивная";
                                break;
                        }

                        sharesRV.add(new SharesRVClass(i.sharesName, i.sharesdataStart, i.sharesdataEnd, status));
                    }

                    RVAdapterShares adapter = new RVAdapterShares(sharesRV);
                    rvShares.setAdapter(adapter);

                    break;

                default:
                    Log.d(TAG, "Акций нет! " + chekError);
                    Toast.makeText(getActivity(), "Акций нет!", Toast.LENGTH_LONG).show();

                    // создаем объект для создания и управления версиями БД
                    dbHelper = new DBHelper(getActivity());
                    // подключаемся к БД
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    // подготовим данные для вставки в виде пар: наименование столбца - значение
                    db.execSQL("UPDATE `mytable` SET `info`='" + "Вы еще не участвовали ни в одной из проводимых акций" + "'");
                    db.execSQL("UPDATE `mytable` SET `info2`='" + "Следите за новостями и акциями на главном сайте компании!" + "'");
                    db.execSQL("UPDATE `mytable` SET `color`='" + colorGREEN + "'");
                    db.execSQL("UPDATE `mytable` SET `color2`='" + colorBLACK + "'");

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

        // создаем объект
        SharesSpiceManager.start(activity);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (SharesSpiceManager.isStarted()) {
            SharesSpiceManager.shouldStop();}

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
