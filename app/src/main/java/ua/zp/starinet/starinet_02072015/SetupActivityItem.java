package ua.zp.starinet.starinet_02072015;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by root on 19.08.15.
 */
public class SetupActivityItem extends Fragment {

    ImageView speed_sub;
    ImageView change_pass_sub;

    FragmentTransaction fTrans;

    Change_tp_sub_item fragChangeTPSubItem;
    ChangePass_sub_item fragChangePassSubItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.setup_menu, null);

        speed_sub = (ImageView) v.findViewById(R.id.speed_sub);
        change_pass_sub = (ImageView) v.findViewById(R.id.change_pass_sub);

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.speed_sub:
                        if (fragChangeTPSubItem==null) fragChangeTPSubItem = new Change_tp_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragChangeTPSubItem);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;

                    case R.id.change_pass_sub:
                        if (fragChangePassSubItem==null) fragChangePassSubItem = new ChangePass_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragChangePassSubItem);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;
                }
            }
        };
        speed_sub.setOnClickListener(onClickListenerStart);
        change_pass_sub.setOnClickListener(onClickListenerStart);
        return v;
    }
}
