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
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.apache.commons.lang3.ObjectUtils;

import retrofit.RetrofitError;
import ua.zp.starinet.starinet_02072015.model.DBEMail;
import ua.zp.starinet.starinet_02072015.network.DataService;
import ua.zp.starinet.starinet_02072015.requests.GetEMailRequest;

/**
 * Created by root on 24.08.15.
 */
public class EMAIL_inform_sub_item extends Fragment {

    TextView txtInfo;
    TextView txtInfo2;
    TextView txtInfo3;
    TextView txtInfo4;
    TextView txtInfo5;

    Button btnAdd;
    EditText edtEMail;

    DBHelper dbHelper;
    private static final String TAG = "myLogs";
    SpiceManager EMailSpiceManager = new SpiceManager(DataService.class);

    final int chekErrorOK = 0;
    final int chekErrorNoActive = 1;
    final int chekErrorDelete = 2;
    final int chekErrorAdd = 3;
    final int chekErrorNo = 4;
    public int chekError = 5;

    final int statusAdd = 0;
    final int statusDelete = 1;
    final int statusInfo = 2;
    public int status = 2;

    int dbID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.email_sub_item, null);

        btnAdd = (Button) v.findViewById(R.id.btnAdd);
        edtEMail = (EditText) v.findViewById(R.id.edtEMail);

        txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        txtInfo2 = (TextView) v.findViewById(R.id.txtInfo2);
        txtInfo3 = (TextView) v.findViewById(R.id.txtInfo3);
        txtInfo4 = (TextView) v.findViewById(R.id.txtInfo4);
        txtInfo5 = (TextView) v.findViewById(R.id.txtInfo5);

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

        EMailSpiceManager.execute(new GetEMailRequest(dbID, "", statusInfo), new GetEMailListener());

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.btnAdd:

                        if (edtEMail.getText().toString().equals("") & status == statusAdd)
                        {
                            // Здесь код, если EditText пуст
                            Toast.makeText(getActivity(), "Введите ваш эл. адрес почты", Toast.LENGTH_LONG).show();
                            break;
                        }
                        else
                        {
                            // если есть текст, то здесь другой код
                            EMailSpiceManager.execute(new GetEMailRequest(dbID, edtEMail.getText().toString(), status), new GetEMailListener());
                        }
                        break;

                }
            }
        };
        btnAdd.setOnClickListener(onClickListenerStart);
        return v;
    }

    class GetEMailListener implements RequestListener<DBEMail> {
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
        public void onRequestSuccess(DBEMail dbItems) {

            if (dbItems == null) {
                Toast.makeText(getActivity(), "Нет даных!", Toast.LENGTH_LONG).show();
                return;
            }

            //txtInfo.setText(String.valueOf(dbItems.chekError));

            //ByPinSpiceManager.shouldStop();

            chekError = dbItems.chekError;

            switch (chekError) {
                case chekErrorOK:
                    Log.d(TAG, "Услуга подключена!" + chekError);
                    Toast.makeText(getActivity(), "Услуга подключена!", Toast.LENGTH_LONG).show();
                    txtInfo.setText("Услуга подключена!");
                    txtInfo.setTextColor(Color.GREEN);
                    txtInfo2.setText("");
                    txtInfo3.setText("E-mail: " + dbItems.dbEMail);
                    txtInfo4.setText("");
                    edtEMail.setVisibility(View.INVISIBLE);
                    btnAdd.setText("Удалить E-mail");
                    btnAdd.setVisibility(View.VISIBLE);
                    txtInfo5.setText("");
                    status = statusDelete;
                    break;

                case chekErrorNoActive:
                    Log.d(TAG, "услуга не активирована, проверьте почту и активируйте услугу." + chekError);
                    Toast.makeText(getActivity(), "услуга не активирована, проверьте почту и активируйте услугу.", Toast.LENGTH_LONG).show();
                    txtInfo.setText("Услуга не активирована!");
                    txtInfo.setTextColor(Color.RED);
                    txtInfo2.setText("Проверьте почту и активируйте услугу");
                    txtInfo3.setText("E-mail: " + dbItems.dbEMail);
                    edtEMail.setVisibility(View.INVISIBLE);
                    btnAdd.setVisibility(View.INVISIBLE);
                    txtInfo4.setText("Обратите внимание!\nЕсли Вы не получили письмо с активацией в течение 5 минут, проверьте не попало ли письмо в СПАМ на Вашем почтовом сервере.\n" +
                            "Если письма все же нет, то удалите E-mail адрес и добавьте его снова.");
                    txtInfo5.setText("");
                    status = statusInfo;
                    break;

                case chekErrorNo:
                    Log.d(TAG, "Услуга не подключена" + chekError);
                    Toast.makeText(getActivity(), "Услуга не подключена", Toast.LENGTH_LONG).show();
                    txtInfo.setText("Услуга не подключена");
                    txtInfo.setTextColor(Color.RED);
                    txtInfo2.setText("Добавьте свой email для включения услуги");
                    txtInfo3.setText("Добавить email");
                    txtInfo4.setText("Введите Ваш E-mail*:");
                    edtEMail.setVisibility(View.VISIBLE);
                    btnAdd.setText("Добавить");
                    btnAdd.setVisibility(View.VISIBLE);
                    txtInfo5.setText("* электронный адрес будет проверен на ликвидность");
                    status = statusAdd;
                    break;

                case chekErrorDelete:
                    Log.d(TAG, "Ваш E-mail удален!" + chekError);
                    Toast.makeText(getActivity(), "Ваш E-mail удален!", Toast.LENGTH_LONG).show();
                    txtInfo.setText("Ваш E-mail удален!");
                    txtInfo.setTextColor(Color.GREEN);
                    txtInfo2.setText("");
                    txtInfo3.setText("");
                    txtInfo4.setText("");
                    edtEMail.setVisibility(View.INVISIBLE);
                    btnAdd.setVisibility(View.INVISIBLE);
                    txtInfo5.setText("");
                    status = statusInfo;
                    break;

                case chekErrorAdd:
                    Log.d(TAG, "Ваш E-mail добавлен!" + chekError);
                    Toast.makeText(getActivity(), "Ваш E-mail добавлен!", Toast.LENGTH_LONG).show();
                    txtInfo.setText("Ваш E-mail добавлен!");
                    txtInfo.setTextColor(Color.GREEN);
                    txtInfo2.setText("");
                    txtInfo3.setText("");
                    txtInfo4.setText("");
                    edtEMail.setVisibility(View.INVISIBLE);
                    btnAdd.setVisibility(View.INVISIBLE);
                    txtInfo5.setText("");
                    status = statusInfo;
                    break;

                default:
                    Log.d(TAG, "Неизвестная ошибка!" + chekError);
                    Toast.makeText(getActivity(), "Неизвестная ошибка!", Toast.LENGTH_LONG).show();
                    txtInfo.setText("Неизвестная ошибка!");
                    txtInfo.setTextColor(Color.RED);
                    txtInfo2.setText("");
                    txtInfo3.setText("");
                    txtInfo4.setText("");
                    txtInfo5.setText("");
                    edtEMail.setVisibility(View.INVISIBLE);
                    btnAdd.setVisibility(View.INVISIBLE);
                    status = statusInfo;
                    break;
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // создаем объект
        EMailSpiceManager.start(activity);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(activity);
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
