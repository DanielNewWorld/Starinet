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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {

    public interface OnCardClikedListener{
        public void onCardCliked(String ticketsID);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView txtTicketsN;
        TextView txtStatus;
        TextView txtCreated;
        TextView txtDepartment;
        TextView txtPriority;
        TextView txtUpdated;
        TextView txtSubject;

        PersonViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            txtTicketsN = (TextView) itemView.findViewById(R.id.txtTicketsN);
            txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
            txtCreated = (TextView) itemView.findViewById(R.id.txtCreated);
            txtDepartment = (TextView) itemView.findViewById(R.id.txtDepartment);
            txtPriority = (TextView) itemView.findViewById(R.id.txtPriority);
            txtUpdated = (TextView) itemView.findViewById(R.id.txtUpdated);
            txtSubject = (TextView) itemView.findViewById(R.id.txtSubject);


            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnCardClikedListener listner = (OnCardClikedListener) itemView.getContext();
                    if (listner != null) {
                        listner.onCardCliked(txtTicketsN.getText().toString()); //передаем в Star
                    }
                }
            });

        }
    }

    List<Tickets> tickets;

    RVAdapter(List<Tickets> tickets){
        this.tickets = tickets;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tickets, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.txtTicketsN.setText(tickets.get(i).dbTicketsID);
        personViewHolder.txtStatus.setText(tickets.get(i).dbStatus);
        personViewHolder.txtCreated.setText(tickets.get(i).dbCreated);
        personViewHolder.txtDepartment.setText(tickets.get(i).dbDepartment);
        personViewHolder.txtPriority.setText(tickets.get(i).dbPriority);
        personViewHolder.txtUpdated.setText(tickets.get(i).dbUpdated);
        personViewHolder.txtSubject.setText(tickets.get(i).dbSubject);

        switch (tickets.get(i).dbPriority) {
            case "Низкий":
                personViewHolder.txtPriority.setTextColor(Color.BLACK);
                break;

            case "Нормальный":
                personViewHolder.txtPriority.setTextColor(Color.GREEN);
                break;

            case "Срочный":
                personViewHolder.txtPriority.setTextColor(Color.YELLOW);
                break;

            case "Критический":
                personViewHolder.txtPriority.setTextColor(Color.RED);
                break;
        }

        switch (tickets.get(i).dbStatus) {
            case "Новый":
                personViewHolder.txtStatus.setTextColor(Color.BLUE);
                break;

            case "Закрыто (Решено)":
                personViewHolder.txtStatus.setTextColor(Color.RED);
                break;

            case "Ожидание ответа от абонента":
                personViewHolder.txtStatus.setTextColor(Color.GREEN);
                break;

            case "Ожидание ответа от оператора":
                personViewHolder.txtStatus.setTextColor(Color.YELLOW);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }
}