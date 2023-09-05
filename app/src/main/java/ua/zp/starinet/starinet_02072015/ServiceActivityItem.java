package ua.zp.starinet.starinet_02072015;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Fragment;

/**
 * Created by root on 19.08.15.
 */
public class ServiceActivityItem extends Fragment {

    ImageView credit_sub;
    ImageView freeze_sub;
    ImageView inet_sub;
    ImageView ip_sub;
    ImageView mail_inform_sub;
    ImageView recalc_sub;
    //ImageView drweb_sub;
    //ImageView turbo_night_sub;
    ImageView shares_sub;

    FragmentTransaction fTrans;

    Credit_sub_item fragCreditSubItem;
    Freeze_sub_item fragFreezeSubItem;
    Inet_sub_item fragInetSubItem;
    Real_IP fragRealIPSubItem;
    EMAIL_inform_sub_item fragEMAILSubItem;
    ReCalc_sub_item fragReCalcSubItem;
    //DR_WEB_sub_item fragDrWEBSubItem;
    Turbo_night_sub_item fragTurboSubItem;
    Shares_sub_item fragSharesSubItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.service_menu, null);

        credit_sub = (ImageView) v.findViewById(R.id.smile_sub);
        freeze_sub = (ImageView) v.findViewById(R.id.freeze_sub);
        inet_sub = (ImageView) v.findViewById(R.id.inet_sub);
        ip_sub = (ImageView) v.findViewById(R.id.ip_sub);
        mail_inform_sub = (ImageView) v.findViewById(R.id.mail_inform_sub);
        recalc_sub = (ImageView) v.findViewById(R.id.recalc_sub);
        /*drweb_sub = (ImageView) v.findViewById(R.id.drweb_sub);*/
        //turbo_night_sub = (ImageView) v.findViewById(R.id.turbo_night_sub);
        shares_sub = (ImageView) v.findViewById(R.id.shares_sub);

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.smile_sub:
                        if (fragCreditSubItem==null) fragCreditSubItem = new Credit_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragCreditSubItem);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;

                    case R.id.freeze_sub:
                        if (fragFreezeSubItem==null) fragFreezeSubItem = new Freeze_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragFreezeSubItem);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;

                    case R.id.inet_sub:
                        if (fragInetSubItem==null) fragInetSubItem = new Inet_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragInetSubItem);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;

                    case R.id.ip_sub:
                        if (fragRealIPSubItem==null) fragRealIPSubItem = new Real_IP();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragRealIPSubItem);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;

                    case R.id.mail_inform_sub:
                        if (fragEMAILSubItem==null) fragEMAILSubItem = new EMAIL_inform_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragEMAILSubItem);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;

                    case R.id.recalc_sub:
                        if (fragReCalcSubItem==null) fragReCalcSubItem = new ReCalc_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragReCalcSubItem);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;

                    /*case R.id.drweb_sub:
                        if (fragDrWEBSubItem==null) fragDrWEBSubItem = new DR_WEB_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragDrWEBSubItem);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;*/

                    /*case R.id.turbo_night_sub:
                        if (fragTurboSubItem==null) fragTurboSubItem = new Turbo_night_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragTurboSubItem);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;*/

                    case R.id.shares_sub:
                        if (fragSharesSubItem==null) fragSharesSubItem = new Shares_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragSharesSubItem);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;
                }
            }
        };

        credit_sub.setOnClickListener(onClickListenerStart);
        freeze_sub.setOnClickListener(onClickListenerStart);
        inet_sub.setOnClickListener(onClickListenerStart);
        ip_sub.setOnClickListener(onClickListenerStart);
        mail_inform_sub.setOnClickListener(onClickListenerStart);
        recalc_sub.setOnClickListener(onClickListenerStart);
        //drweb_sub.setOnClickListener(onClickListenerStart);
        //turbo_night_sub.setOnClickListener(onClickListenerStart);
        shares_sub.setOnClickListener(onClickListenerStart);
        return v;
    }
}
