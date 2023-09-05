package ua.zp.starinet.starinet_02072015;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by root on 24.08.15.
 */
public class Terminals_sub_item extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.terminals_sub_item, null);

        View.OnClickListener onClickListenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                switch (v.getId()) {

                    //case R.id.start_menu_item:
                    //    intent = new Intent(AccountSubItem.this, .class);
                    //    startActivity(intent);
                    //    break;

                }
            }
        };
        //start_menu_item.setOnClickListener(onClickListenerStart);
        return v;
    }
}
