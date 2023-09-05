package ua.zp.starinet.starinet_02072015;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.app.Fragment;

/**
 * Created by root on 19.08.15.
 */
public class PaymentsActivityItem extends Fragment {

    ImageView cn_cards_sub;
    ImageView check_sub;
    ImageView friend_sub;
    ImageView terminals_sub;
    ImageView pb1_sub;
    ImageView beznal_sub;
    ImageView cards_sub;
    ImageView wm_sub;
    ImageView nsmep_sub;

    FragmentTransaction fTrans;

    By_pin_sub_item fragByPinSubItem;
    Friend_sub_item fragFriend;
    Terminals_sub_item fragTerminals;
    PB_sub_item fragPB;
    Beznal_sub_item fragBeznal;
    Visa_sub_item fragVisa;
    WM_sub_item fragWM;
    NSMEP_sub_item fragNSMEP;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.payments_menu, null);

        cn_cards_sub = (ImageView) v.findViewById(R.id.cn_cards_sub);
        check_sub = (ImageView) v.findViewById(R.id.check_sub);
        friend_sub = (ImageView) v.findViewById(R.id.friend_sub);
        terminals_sub = (ImageView) v.findViewById(R.id.terminals_sub);
        pb1_sub = (ImageView) v.findViewById(R.id.pb1_sub);
        beznal_sub = (ImageView) v.findViewById(R.id.beznal_sub);
        cards_sub = (ImageView) v.findViewById(R.id.cards_sub);
        wm_sub = (ImageView) v.findViewById(R.id.wm_sub);
        nsmep_sub = (ImageView) v.findViewById(R.id.nsmep_sub);

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.cn_cards_sub:
                        if (fragByPinSubItem==null) fragByPinSubItem = new By_pin_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragByPinSubItem);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;

                    case R.id.check_sub:
                        if (fragByPinSubItem==null) fragByPinSubItem = new By_pin_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragByPinSubItem);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;

                    case R.id.friend_sub:
                        if (fragFriend==null) fragFriend = new Friend_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragFriend);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;

                    case R.id.terminals_sub:
                        if (fragTerminals==null) fragTerminals = new Terminals_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragTerminals);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;

                    case R.id.pb1_sub:
                        if (fragPB==null) fragPB = new PB_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragPB);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;

                    case R.id.beznal_sub:
                        if (fragBeznal==null) fragBeznal = new Beznal_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragBeznal);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;

                    case R.id.cards_sub:
                        if (fragVisa==null) fragVisa = new Visa_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragVisa);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;

                    case R.id.wm_sub:
                        if (fragWM==null) fragWM = new WM_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragWM);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;

                    case R.id.nsmep_sub:
                        if (fragNSMEP==null) fragNSMEP = new NSMEP_sub_item();
                        fTrans = getFragmentManager().beginTransaction();
                        fTrans.replace(R.id.frgmCont, fragNSMEP);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;
                }
            }
        };

        cn_cards_sub.setOnClickListener(onClickListenerStart);
        check_sub.setOnClickListener(onClickListenerStart);
        friend_sub.setOnClickListener(onClickListenerStart);
        terminals_sub.setOnClickListener(onClickListenerStart);
        pb1_sub.setOnClickListener(onClickListenerStart);
        beznal_sub.setOnClickListener(onClickListenerStart);
        cards_sub.setOnClickListener(onClickListenerStart);
        wm_sub.setOnClickListener(onClickListenerStart);
        nsmep_sub.setOnClickListener(onClickListenerStart);
        return v;
    }
}
