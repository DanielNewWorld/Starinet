package ua.zp.starinet.starinet_02072015;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import android.app.FragmentTransaction;

public class StartActivityItem extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
                   RVAdapter.OnCardClikedListener,
                   RVAdapterTicketsView.OnTicketsAnswerClikedListener{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    FragmentTransaction fTrans;

    ServiceActivityItem fragService;
    HomeActivityItem fragHome;
    PaymentsActivityItem fragPayments;
    StatistikaActivityItem fragStatistika;
    SetupActivityItem fragSetup;
    TicketsActivityItem fragTickets;
    ContactActivityItem fragContact;
    AccountSubItem fragAccountSub;

    TicketsView fragTicketsView;
    TicketsAnswer fragTicketsAnswer;

    private static long back_pressed;

    DBHelper dbHelper;
    private static final String TAG = "myLogs";

    int dbID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_menu);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        TextView txtDataNews = (TextView) findViewById(R.id.txtDataNews);
        TextView txtNews = (TextView) findViewById(R.id.txtNews);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Log.d(TAG, "--- Rows in mytable: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("mytable", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int dbDataNewsIndex = c.getColumnIndex("dbDataNews");
            int dbNewsIndex = c.getColumnIndex("dbNews");

            do {
                txtDataNews.setText(c.getString(dbDataNewsIndex));
                txtNews.setText(c.getString(dbNewsIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(TAG, "0 rows");
        c.close();

        // закрываем подключение к БД
        dbHelper.close();

    }

    @Override
    public void onCardCliked(String ticketsID) {
        //Toast.makeText(Start.this, String.valueOf(viewindex.getId()), Toast.LENGTH_SHORT).show();

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("UPDATE `mytable` SET `ticketsID`='" + ticketsID + "'");
        // закрываем подключение к БД
        dbHelper.close();

        //mTitle = getString(persons.get(viewindex.getId()).imageURLAndroid);
        fTrans = getFragmentManager().beginTransaction();
        if (fragTicketsView == null) fragTicketsView = new TicketsView();
        fTrans.replace(R.id.frgmCont, fragTicketsView);
        fTrans.addToBackStack(null);
        fTrans.commit();
    }

    @Override
    public void onTicketsAnswerCliked(int status) {
        //Toast.makeText(StartActivityItem.this, "Написать ответ", Toast.LENGTH_SHORT).show();

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("UPDATE `mytable` SET `ticketsStatus`=" + status);
        // закрываем подключение к БД
        dbHelper.close();

        //mTitle = getString(persons.get(viewindex.getId()).imageURLAndroid);
        fTrans = getFragmentManager().beginTransaction();
        if (fragTicketsAnswer == null) fragTicketsAnswer = new TicketsAnswer();
        fTrans.replace(R.id.frgmCont, fragTicketsAnswer);
        fTrans.addToBackStack(null);
        fTrans.commit();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        fTrans = getFragmentManager().beginTransaction();
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                if (fragHome==null) fragHome = new HomeActivityItem();
                fTrans.replace(R.id.frgmCont, fragHome);
                break;

            case 2:
                mTitle = getString(R.string.title_section2);
                if (fragService==null) fragService = new ServiceActivityItem();
                fTrans.replace(R.id.frgmCont, fragService);
                break;

            case 3:
                mTitle = getString(R.string.title_section3);
                if (fragPayments==null) fragPayments = new PaymentsActivityItem();
                fTrans.replace(R.id.frgmCont, fragPayments);
                break;

            case 4:
                mTitle = getString(R.string.title_section4);
                if (fragStatistika==null) fragStatistika = new StatistikaActivityItem();
                fTrans.replace(R.id.frgmCont, fragStatistika);
                break;

            case 5:
                mTitle = getString(R.string.title_section5);
                if (fragSetup==null) fragSetup = new SetupActivityItem();
                fTrans.replace(R.id.frgmCont, fragSetup);
                break;

            case 6:
                mTitle = getString(R.string.title_section6);
                if (fragTickets==null) fragTickets = new TicketsActivityItem();
                fTrans.replace(R.id.frgmCont, fragTickets);
                break;

            case 7:
                mTitle = getString(R.string.title_section7);
                if (fragContact==null) fragContact = new ContactActivityItem();
                fTrans.replace(R.id.frgmCont, fragContact);
                break;

        }
        //fTrans.addToBackStack(null);
        fTrans.commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.account) {
            if (fragAccountSub==null) fragAccountSub = new AccountSubItem();
            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.frgmCont, fragAccountSub);
            fTrans.addToBackStack(null);
            fTrans.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((StartActivityItem) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    @Override
    public void onBackPressed() {

        if (back_pressed + 2000 > System.currentTimeMillis())
        {
            //    super.onBackPressed();
            moveTaskToBack(true);
            finish();
            System.runFinalizersOnExit(true);
            System.exit(0);
        }
        else
            Toast.makeText(getBaseContext(), "Нажмите еще раз, чтобы выйти!",
                    Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
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

//вызываем сайт
//Intent intent;
//intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://starinet.zp.ua"));
//startActivity(intent);

//набирает номер телефона
//intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:123456"));
//startActivity(intent);

//показываем на карте
//intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:000"));
//startActivity(intent);

//finish();