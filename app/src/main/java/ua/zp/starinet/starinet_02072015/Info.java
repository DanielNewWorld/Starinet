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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
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
public class Info extends Fragment {

    TextView txtInfo;
    TextView txtInfo2;

    String info = "";
    String info2 = "";

    final int colorBLACK = 0;
    final int colorGREEN = 1;
    final int colorRED = 2;
    final int colorBLUE = 3;
    int colorInfo = 0;
    int color2Info = 0;

    DBHelper dbHelper;
    private static final String TAG = "myLogs";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.info, null);

        txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        txtInfo2 = (TextView) v.findViewById(R.id.txtInfo2);

        txtInfo.setText("");
        txtInfo2.setText("");

        txtInfo.setTextColor(Color.BLACK);
        txtInfo2.setTextColor(Color.BLACK);

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
            int dbInfoIndex = c.getColumnIndex("info");
            int dbInfo2Index = c.getColumnIndex("info2");
            int dbColorIndex = c.getColumnIndex("color");
            int dbColor2Index = c.getColumnIndex("color2");

            do {
                info = c.getString(dbInfoIndex);
                info2 = c.getString(dbInfo2Index);
                colorInfo = c.getInt(dbColorIndex);
                color2Info = c.getInt(dbColor2Index);

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        c.close();

        // закрываем подключение к БД
        dbHelper.close();


        switch (colorInfo) {

            case colorGREEN:
                txtInfo.setTextColor(Color.GREEN);
                break;
            case colorRED:
                txtInfo.setTextColor(Color.RED);
                break;
            case colorBLACK:
                txtInfo.setTextColor(Color.BLACK);
                break;
            case colorBLUE:
                txtInfo.setTextColor(Color.BLUE);
                break;
        }

        switch (color2Info) {

            case colorGREEN:
                txtInfo2.setTextColor(Color.GREEN);
                break;
            case colorRED:
                txtInfo2.setTextColor(Color.RED);
                break;
            case colorBLACK:
                txtInfo2.setTextColor(Color.BLACK);
                break;
            case colorBLUE:
                txtInfo2.setTextColor(Color.BLUE);
                break;
        }

        txtInfo.setText(info);
        txtInfo2.setText(info2);

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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