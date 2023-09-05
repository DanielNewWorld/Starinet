package ua.zp.starinet.starinet_02072015;

/**
 * Created by root on 04.08.15.
 */

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RVAdapterShares extends RecyclerView.Adapter<RVAdapterShares.PersonViewHolder> {

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView txtName;
        TextView txtDataStart;
        TextView txtDataEnd;
        TextView txtStatus;

        PersonViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtDataStart = (TextView) itemView.findViewById(R.id.txtDataStart);
            txtDataEnd = (TextView) itemView.findViewById(R.id.txtDataEnd);
            txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
        }
    }

    List<SharesRVClass> shares;

    RVAdapterShares(List<SharesRVClass> shares){
        this.shares = shares;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_shares, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.txtName.setText(shares.get(i).sharesName);
        personViewHolder.txtDataStart.setText(shares.get(i).sharesdataStart);
        personViewHolder.txtDataEnd.setText(shares.get(i).sharesdataEnd);
        personViewHolder.txtStatus.setText(shares.get(i).sharesStatus);
    }

    @Override
    public int getItemCount() {
        return shares.size();
    }
}