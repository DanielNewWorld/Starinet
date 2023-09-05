package ua.zp.starinet.starinet_02072015;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.widget.Toast;

/**
 * Created by root on 19.08.15.
 */
public class TicketsActivityItem extends Fragment {

    ImageView imgTicket;
    ImageView imgTicketAll;

    private CharSequence mTitle;
    FragmentTransaction fTrans;

    TicketsSelect fragTicketsSelect;
    TicketsAll fragTicketsAll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tickets_menu, null);

        imgTicket = (ImageView) v.findViewById(R.id.imgTicket);
        imgTicketAll = (ImageView) v.findViewById(R.id.imgTicketAll);

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.imgTicket:
                        //Toast.makeText(getActivity(), "OK", Toast.LENGTH_LONG).show();
                        fTrans = getFragmentManager().beginTransaction();
                        //mTitle = getString(R.string.title_section6);
                        if (fragTicketsSelect==null) fragTicketsSelect = new TicketsSelect();
                        fTrans.replace(R.id.frgmCont, fragTicketsSelect);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;

                    case R.id.imgTicketAll:
                        //Toast.makeText(getActivity(), "OK", Toast.LENGTH_LONG).show();
                        fTrans = getFragmentManager().beginTransaction();
                        //mTitle = getString(R.string.title_section6);
                        if (fragTicketsAll==null) fragTicketsAll = new TicketsAll();
                        fTrans.replace(R.id.frgmCont, fragTicketsAll);
                        fTrans.addToBackStack(null);
                        fTrans.commit();
                        break;
                }
            }
        };
        imgTicket.setOnClickListener(onClickListenerStart);
        imgTicketAll.setOnClickListener(onClickListenerStart);

        return v;
    }
}
