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
public class StatistikaActivityItem extends Fragment {

    ImageView internet_sub;
    ImageView ip_sub;
    ImageView system_sub;

    FragmentTransaction fTrans;

    Internet_sub_item fragInternetSubItem;
    Debit_sub_item fragDebitSubItem;
    Info_sub_item fragInfoSubItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.statistika_menu, null);

        internet_sub = (ImageView) v.findViewById(R.id.internet_sub);
        ip_sub = (ImageView) v.findViewById(R.id.ip_sub);
        system_sub = (ImageView) v.findViewById(R.id.system_sub);

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                switch (v.getId()) {

                    case R.id.internet_sub:
                        if (fragInternetSubItem==null) fragInternetSubItem = new Internet_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragInternetSubItem);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;

                    case R.id.ip_sub:
                        if (fragDebitSubItem==null) fragDebitSubItem = new Debit_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragDebitSubItem);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;

                    case R.id.system_sub:
                        if (fragInfoSubItem==null) fragInfoSubItem = new Info_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragInfoSubItem);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;
                }
            }
        };

        internet_sub.setOnClickListener(onClickListenerStart);
        ip_sub.setOnClickListener(onClickListenerStart);
        system_sub.setOnClickListener(onClickListenerStart);
        return v;
    }
}
