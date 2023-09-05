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
import android.widget.ImageView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import retrofit.RetrofitError;
import ua.zp.starinet.starinet_02072015.network.DataService;

/**
 * Created by root on 24.08.15.
 */
public class Turbo_night_sub_item extends Fragment {

    ImageView imgTurboDay;
    ImageView imgTurboMonth;

    DBHelper dbHelper;
    private static final String TAG = "myLogs";
    SpiceManager TurboNightSpiceManager = new SpiceManager(DataService.class);

    final int chekErrorOK = 0;
    //final int chekErrorDate = 1;
    //final int chekErrorDelete = 2;
    //final int chekErrorAdd = 3;
    //final int chekErrorNo = 4;
    public int chekError = 5;

    //final int statusAdd = 0;
    //final int statusDelete = 1;
    final int statusInfo = 2;
    public int status = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.turbo_night_sub_item, null);

        imgTurboDay = (ImageView) v.findViewById(R.id.imgTurboDay);
        imgTurboMonth = (ImageView) v.findViewById(R.id.imgTurboMonth);

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.imgTurboDay:
                        break;

                    case R.id.imgTurboMonth:
                        break;

                }
            }
        };
        imgTurboDay.setOnClickListener(onClickListenerStart);
        imgTurboMonth.setOnClickListener(onClickListenerStart);
        return v;
    }

   /* class GetReCalcListener implements RequestListener<ArrayListDBReCalcSub> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            RetrofitError cause = (RetrofitError) spiceException.getCause();
            if (cause == null ||
                    cause.isNetworkError() ||
                    cause.getResponse() == null) {
                Toast.makeText(getActivity(), "Нет доступа к Интернету", Toast.LENGTH_LONG).show();
            }

            switch (cause.getResponse().getStatus()) {
                case HttpStatus.SC_UNAUTHORIZED:
                    Toast.makeText(getActivity(), "Неправильный логин или пароль", Toast.LENGTH_LONG).show();
                    break;
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
    }*/

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
            //int dbIDIndex = c.getColumnIndex("dbID");
            //int dbAccountIDIndex = c.getColumnIndex("dbAccountID");

            do {
                //dbID = c.getInt(dbIDIndex);
                //dbAccountID = c.getString(dbAccountIDIndex);

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        c.close();

        // закрываем подключение к БД
        dbHelper.close();

        Toast.makeText(getActivity(), "chekError: " + String.valueOf(chekError) + "; status: " + String.valueOf(status), Toast.LENGTH_LONG).show();
        // создаем объект
        //TurboNightSpiceManager.start(activity);
        //ReCalcSpiceManager.execute(new GetReCalcSubRequest(dbID, statusInfo, "", "", "", "", "", ""), new GetReCalcListener());
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
