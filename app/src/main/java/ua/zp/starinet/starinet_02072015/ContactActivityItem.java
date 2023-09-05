package ua.zp.starinet.starinet_02072015;

import android.app.Activity;
import android.content.Intent;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by root on 19.08.15.
 */
public class ContactActivityItem extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contact_menu, null);

/*        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                switch (v.getId()) {

                    //case R.id.help_menu_item:
//                        intent = new Intent(ContactActivityItem.this, TicketsActivityItem.class);
//                        startActivity(intent);
//                        break;
                }
            }
        };*/
    }

}
