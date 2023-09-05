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
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import retrofit.RetrofitError;
import ua.zp.starinet.starinet_02072015.model.DBPass;
import ua.zp.starinet.starinet_02072015.network.DataService;
import ua.zp.starinet.starinet_02072015.requests.GetPassRequest;

/**
 * Created by root on 24.08.15.
 */
public class ChangePass_sub_item  extends Fragment {

    EditText edtPass;
    EditText edtNewPass;
    EditText edtNewPassTwo;

    Button btnChange;

    DBHelper dbHelper;
    private static final String TAG = "myLogs";
    SpiceManager PassSpiceManager = new SpiceManager(DataService.class);

    final int chekErrorOK = 0;
    //final int chekErrorDate = 1;
    //final int chekErrorDelete = 2;
    //final int chekErrorAdd = 3;
    //final int chekErrorNo = 4;
    public int chekError = 5;

    final int statusChange = 0;
    //final int statusDelete = 1;
    final int statusInfo = 2;
    public int status = 2;

    int dbID;
    String dbPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.changepass_sub_item, null);

        edtPass = (EditText) v.findViewById(R.id.edtPass);
        edtNewPass = (EditText) v.findViewById(R.id.edtNewPass);
        edtNewPassTwo = (EditText) v.findViewById(R.id.edtNewPassTwo);
        btnChange = (Button) v.findViewById(R.id.btnChange);

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.btnChange:
                        if (edtPass.getText().toString().equals("") |
                                edtNewPass.getText().toString().equals("") |
                                edtNewPassTwo.getText().toString().equals(""))
                        {
                            // Здесь код, если EditText пуст
                            Toast.makeText(getActivity(), "Введите все поля обязательные для заполнения!", Toast.LENGTH_LONG).show();
                            break;
                        }
                        else
                        {
                            if (edtNewPass.getText().toString().equals(edtNewPassTwo.getText().toString())) {
                                if (edtPass.getText().toString().equals(dbPassword)) {
                                    if (edtPass.length() > 3 & edtPass.length() < 65) {
                                        PassSpiceManager.execute(new GetPassRequest(dbID, edtNewPass.getText().toString()), new GetPassListener());
                                        //Toast.makeText(getActivity(), "Сменили!", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(getActivity(), "Длина пароля от 4 до 64 символов!", Toast.LENGTH_LONG).show();
                                        break;
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "Вы ввели неверный текущий пароль!", Toast.LENGTH_LONG).show();
                                    break;
                                }
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Поля нового пароля не совпадают!", Toast.LENGTH_LONG).show();
                                break;
                            }
                        }
                        break;

                }
            }
        };
        btnChange.setOnClickListener(onClickListenerStart);
        return v;
    }

    class GetPassListener implements RequestListener<DBPass> {
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
        public void onRequestSuccess(DBPass dbItems) {

            if (dbItems == null) {
                Toast.makeText(getActivity(), "Нет даных!", Toast.LENGTH_LONG).show();
                return;
            }

            //txtInfo.setText(String.valueOf(dbItems.chekError));

            //ByPinSpiceManager.shouldStop();

            chekError = dbItems.chekError;

            switch (chekError) {
                case chekErrorOK:
                    Log.d(TAG, "Пароль сменен!" + chekError);
                    Toast.makeText(getActivity(), "Пароль сменен!", Toast.LENGTH_LONG).show();
                    break;

                default:
                    Log.d(TAG, "Неизвестная ошибка!" + chekError);
                    Toast.makeText(getActivity(), "Неизвестная ошибка!", Toast.LENGTH_LONG).show();
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
            int dbPasswordIndex = c.getColumnIndex("dbPassword");

            do {
                dbID = c.getInt(dbIDIndex);
                dbPassword = c.getString(dbPasswordIndex);

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
        PassSpiceManager.start(activity);
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
