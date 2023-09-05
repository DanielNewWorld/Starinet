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

import java.math.BigDecimal;

import retrofit.RetrofitError;
import ua.zp.starinet.starinet_02072015.model.DBFriend;
import ua.zp.starinet.starinet_02072015.network.DataService;
import ua.zp.starinet.starinet_02072015.requests.GetFriendRequest;

/**
 * Created by root on 20.08.15.
 */
public class Friend_sub_item extends Fragment {

    EditText edtAccount;
    EditText edtPerevod;
    EditText edtComment;

    Button btnOplata;

    TextView txtInfo;
    TextView txtInfo2;
    TextView txtInfo3;

    DBHelper dbHelper;
    private static final String TAG = "myLogs";
    SpiceManager FriendSpiceManager = new SpiceManager(DataService.class);

    final int chekErrorOK = 0;
    final int chekErrorAccount = 1;
    //final int chekErrorLimit=2;
    //final int chekErrorStatus=3;
    public int chekError = 4;

    String dbAccountID;
    String accountIDOplata;
    String comment;
    double dbBalance;
    Integer oplata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.friend_subitem, null);

        edtAccount = (EditText) v.findViewById(R.id.edtAccount);
        edtPerevod = (EditText) v.findViewById(R.id.edtPerevod);
        edtComment = (EditText) v.findViewById(R.id.edtComment);

        btnOplata = (Button) v.findViewById(R.id.btnOplata);

        txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        txtInfo2 = (TextView) v.findViewById(R.id.txtInfo2);
        txtInfo3 = (TextView) v.findViewById(R.id.txtInfo3);

        edtComment.setText("Перевод средств на лицевой счет №" + edtAccount.getText());

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.btnOplata:
                        try {
                            oplata = new Integer(edtPerevod.getText().toString());

                            if (oplata < 5) {
                                Toast.makeText(getActivity(), "Минимальная сумма перевода: 5 грн", Toast.LENGTH_LONG).show();
                                break;
                            }

                            // подключаемся к БД
                            SQLiteDatabase db = dbHelper.getWritableDatabase();

                            Log.d(TAG, "--- Rows in mytable: ---");
                            // делаем запрос всех данных из таблицы mytable, получаем Cursor
                            Cursor c = db.query("mytable", null, null, null, null, null, null);

                            // ставим позицию курсора на первую строку выборки
                            // если в выборке нет строк, вернется false
                            if (c.moveToFirst()) {

                                // определяем номера столбцов по имени в выборке
                                //int dbStatusIndex = c.getColumnIndex("chekStatus");
                                //int dbTariffIndex = c.getColumnIndex("dbTariff");
                                int dbBalanceIndex = c.getColumnIndex("dbBalance");
                                //int dbCostIndex = c.getColumnIndex("dbCost");
                                int dbAccountIDIndex = c.getColumnIndex("dbAccountID");

                                do {
                                    //chekStatus = c.getInt(dbStatusIndex);
                                    //dbTariff = c.getString(dbTariffIndex);
                                    dbBalance = c.getDouble(dbBalanceIndex);
                                    //dbCost = c.getDouble(dbCostIndex);
                                    dbAccountID = c.getString(dbAccountIDIndex);

                                    // переход на следующую строку
                                    // а если следующей нет (текущая - последняя), то false - выходим из цикла
                                } while (c.moveToNext());
                            } else
                                Log.d(TAG, "0 rows");
                            c.close();

                            // закрываем подключение к БД
                            //dbHelper.close();

                            if (oplata >= dbBalance) {
                                Toast.makeText(getActivity(), "Сумма перевода не может превышать сумму Вашего баланса", Toast.LENGTH_LONG).show();
                                break;
                            }

                        } catch (NumberFormatException e) {
                            System.err.println("Вы неверно ввели сумму оплаты");
                            Toast.makeText(getActivity(), "Вы неверно ввели сумму оплаты", Toast.LENGTH_LONG).show();
                            txtInfo.setText("Вы неверно ввели сумму оплаты");
                            txtInfo.setTextColor(Color.RED);
                            txtInfo2.setText("");
                            txtInfo3.setText("");
                            break;
                        }

                        accountIDOplata = edtAccount.getText().toString();
                        comment = edtComment.getText().toString();
                        FriendSpiceManager.execute(new GetFriendRequest(dbAccountID, accountIDOplata, oplata, comment), new GetFriendListener());
                        break;

                }
            }
        };
        btnOplata.setOnClickListener(onClickListenerStart);
        return v;
    }

    public BigDecimal roundUp(double value, int digits){
        return new BigDecimal(""+value).setScale(digits, BigDecimal.ROUND_HALF_UP);
    }

    class GetFriendListener implements RequestListener<DBFriend> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            RetrofitError cause = (RetrofitError) spiceException.getCause();
            if (cause == null ||
                    cause.isNetworkError() ||
                    cause.getResponse() == null) {
                Toast.makeText(getActivity(), "Нет доступа к Интернету", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onRequestSuccess(DBFriend dbItems) {

            if (dbItems == null) {
                Toast.makeText(getActivity(), "Нет даных!", Toast.LENGTH_LONG).show();
                return;
            }

            //txtInfo.setText(String.valueOf(dbItems.chekError));

            //ByPinSpiceManager.shouldStop();

            chekError = dbItems.chekError;

            switch (chekError) {
                case chekErrorOK:
                    Log.d(TAG, "Платеж успешно проведен!" + chekError);
                    Toast.makeText(getActivity(), "Платеж успешно проведен!", Toast.LENGTH_LONG).show();
                    txtInfo.setText("Платеж успешно проведен!");
                    txtInfo.setTextColor(Color.GREEN);
                    txtInfo2.setText("");
                    txtInfo3.setText("");
                    // подключаемся к БД
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    // подготовим данные для вставки в виде пар: наименование столбца - значение
                    //db.execSQL("UPDATE `mytable` SET `dbBalance`='"+balanceItogo+"'");
                    //db.execSQL("UPDATE `mytable` SET `chekStatus`='"+chekStatusFreeze+"'");

                    // закрываем подключение к БД
                    //dbHelper.close();
                    break;

                case chekErrorAccount:
                    Log.d(TAG, "Номер счета получателя введен неверно!" + chekError);
                    Toast.makeText(getActivity(), "Номер счета получателя введен неверно!", Toast.LENGTH_LONG).show();
                    txtInfo.setText("Номер счета получателя введен неверно!");
                    txtInfo.setTextColor(Color.RED);
                    txtInfo2.setText("");
                    txtInfo3.setText("");
                    break;

                default:
                    Log.d(TAG, "Неизвестная ошибка!" + chekError);
                    Toast.makeText(getActivity(), "Неизвестная ошибка!", Toast.LENGTH_LONG).show();
                    txtInfo.setText("Неизвестная ошибка!");
                    txtInfo.setTextColor(Color.RED);
                    txtInfo2.setText("");
                    txtInfo3.setText("");
                    break;
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // создаем объект
        FriendSpiceManager.start(activity);

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
