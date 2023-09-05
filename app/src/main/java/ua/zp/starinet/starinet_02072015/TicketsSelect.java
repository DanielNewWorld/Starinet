package ua.zp.starinet.starinet_02072015;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * Created by root on 19.08.15.
 */
public class TicketsSelect extends Fragment {

    Button btnType;

    String dbDepartment;

    RadioButton radio_finance;
    RadioButton radio_tehnical;
    RadioButton radio_other;
    RadioButton radio_offers;
    RadioButton radio_dogovor;

    private CharSequence mTitle;
    FragmentTransaction fTrans;
    TicketsNewText fragTicketsNewText;

    private static final String TAG = "myLogs";
    DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tickets_new, null);

        btnType = (Button) v.findViewById(R.id.btnType);

        radio_finance = (RadioButton) v.findViewById(R.id.radio_finance);
        radio_tehnical = (RadioButton) v.findViewById(R.id.radio_tehnical);
        radio_other = (RadioButton) v.findViewById(R.id.radio_other);
        radio_offers = (RadioButton) v.findViewById(R.id.radio_offers);
        radio_dogovor = (RadioButton) v.findViewById(R.id.radio_dogovor);

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.btnType:

                        dbDepartment="0";

                        if (radio_finance.isChecked()) {
                            dbDepartment="finance"; // Финансовые вопросы
                        }
                        if (radio_tehnical.isChecked()) {
                            dbDepartment="technical"; // Технические вопросы
                        }
                        if (radio_other.isChecked()) {
                            dbDepartment="abuse"; // Другие жалобы
                        }
                        if (radio_offers.isChecked()) {
                            dbDepartment="wishes"; // Пожелания и предложения
                        }
                        if (radio_dogovor.isChecked()) {
                            dbDepartment="offline"; // Расторжение договора
                        }

                        if (dbDepartment.equals("0")) {
                            Toast.makeText(getActivity(), "Выберите раздел", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Выберите раздел");
                            break;
                        }
                        else {
                            // создаем объект для создания и управления версиями БД
                            dbHelper = new DBHelper(getActivity());
                            // подключаемся к БД
                            SQLiteDatabase db = dbHelper.getWritableDatabase();

                            // подготовим данные для вставки в виде пар: наименование столбца - значение
                            db.execSQL("UPDATE `mytable` SET `dbDepartment`='"+dbDepartment+"'");

                            // закрываем подключение к БД
                            dbHelper.close();

                            //Toast.makeText(getActivity(), "OK", Toast.LENGTH_LONG).show();
                            //mTitle = getString(R.string.title_section6);
                            fTrans = getFragmentManager().beginTransaction();
                            if (fragTicketsNewText == null) fragTicketsNewText = new TicketsNewText();
                            fTrans.replace(R.id.frgmCont, fragTicketsNewText);
                            fTrans.addToBackStack(null);
                            fTrans.commit();
                        }
                        break;
                }
            }
        };
        btnType.setOnClickListener(onClickListenerStart);
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
