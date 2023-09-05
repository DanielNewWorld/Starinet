package ua.zp.starinet.starinet_02072015;

/**
 * Created by root on 24.09.15.
 */

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class HomeActivityItem extends Fragment {

    private static final String TAG = "myLogs";
    FragmentTransaction fTrans;

    AccountSubItem fragAccountSub;
    By_pin_sub_item fragCardsSub, fragCheckSub;
    StatistikaActivityItem fragStatistika;
    Change_tp_sub_item fragChangeTPSubItem;
    Credit_sub_item fragCreditSubItem;
    Freeze_sub_item fragFreezeSubItem;
    Friend_sub_item fragFriendSubItem;
    ContactActivityItem fragContact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_menu, null);

        ImageView account_sub = (ImageView) v.findViewById(R.id.account_sub);
        ImageView cards_sub = (ImageView) v.findViewById(R.id.cards_sub);
        ImageView check_sub = (ImageView) v.findViewById(R.id.check_sub);
        ImageView stat_sub = (ImageView) v.findViewById(R.id.stat_sub);
        ImageView speed_sub = (ImageView) v.findViewById(R.id.speed_sub);
        ImageView smile_sub = (ImageView) v.findViewById(R.id.smile_sub);
        ImageView freeze_sub = (ImageView) v.findViewById(R.id.freeze_sub);
        ImageView friend_sub = (ImageView) v.findViewById(R.id.friend_sub);
        ImageView contact_sub = (ImageView) v.findViewById(R.id.contact_sub);

        account_sub.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fragAccountSub==null) fragAccountSub = new AccountSubItem();
                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.frgmCont, fragAccountSub);
                fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });

        cards_sub.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fragCardsSub == null) fragCardsSub = new By_pin_sub_item();
                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.frgmCont, fragCardsSub);
                fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });

        check_sub.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fragCheckSub == null) fragCheckSub = new By_pin_sub_item();
                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.frgmCont, fragCheckSub);
                fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });

        stat_sub.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fragStatistika == null) fragStatistika = new StatistikaActivityItem();
                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.frgmCont, fragStatistika);
                fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });

        speed_sub.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fragChangeTPSubItem == null) fragChangeTPSubItem = new Change_tp_sub_item();
                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.frgmCont, fragChangeTPSubItem);
                fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });

        smile_sub.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fragCreditSubItem == null) fragCreditSubItem = new Credit_sub_item();
                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.frgmCont, fragCreditSubItem);
                fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });

        freeze_sub.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fragFreezeSubItem == null) fragFreezeSubItem = new Freeze_sub_item();
                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.frgmCont, fragFreezeSubItem);
                fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });

        friend_sub.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fragFriendSubItem == null) fragFriendSubItem = new Friend_sub_item();
                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.frgmCont, fragFriendSubItem);
                fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });

        contact_sub.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (fragContact == null) fragContact = new ContactActivityItem();
                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.frgmCont, fragContact);
                fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });

        return v;
    }
}