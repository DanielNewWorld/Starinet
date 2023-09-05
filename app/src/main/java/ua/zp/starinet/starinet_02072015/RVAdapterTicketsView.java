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

public class RVAdapterTicketsView extends RecyclerView.Adapter<RVAdapterTicketsView.PersonViewHolder> {

    public interface OnTicketsAnswerClikedListener{
        public void onTicketsAnswerCliked(int status);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView txtTextData;
        TextView txtText;
        TextView txtAbonOperator;

        int status = 0;

        PersonViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            txtTextData = (TextView) itemView.findViewById(R.id.txtTextData);
            txtText = (TextView) itemView.findViewById(R.id.txtText);
            txtAbonOperator = (TextView) itemView.findViewById(R.id.txtAbonOperator);

            txtText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnTicketsAnswerClikedListener listner = (OnTicketsAnswerClikedListener) itemView.getContext();
                    if (listner != null & status == -1) {
                        listner.onTicketsAnswerCliked(status); //передаем в Star
                    }

                    if (listner != null & status == -2) {
                        listner.onTicketsAnswerCliked(status); //передаем в Star
                    }
                }
            });

        }
    }

    List<TicketsViewClass> ticketsView;

    RVAdapterTicketsView(List<TicketsViewClass> ticketsView){
        this.ticketsView = ticketsView;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tickets_view, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
      personViewHolder.txtTextData.setText(ticketsView.get(i).dbCreated);
      personViewHolder.txtText.setText(ticketsView.get(i).dbText);

        switch (ticketsView.get(i).dbAdminID) {
            case 0:
                personViewHolder.txtAbonOperator.setTextColor(Color.GREEN);
                personViewHolder.txtAbonOperator.setText("Вы написали:");
                break;

            case -1:
                personViewHolder.txtAbonOperator.setTextColor(Color.RED);
                personViewHolder.txtAbonOperator.setText("Вопрос решен. Тикет закрыт");
                personViewHolder.txtText.setText("Если хотите продолжить обсуждение темы, нажмите на это сообщение");
                personViewHolder.txtText.setTextColor(Color.BLUE);
                personViewHolder.status = -1;
                break;

            case -2:
                personViewHolder.txtAbonOperator.setTextColor(Color.GREEN);
                personViewHolder.txtAbonOperator.setText("Написать ответ");
                personViewHolder.txtText.setText("Если хотите написать ответ, нажмите на это сообщение");
                personViewHolder.txtText.setTextColor(Color.BLUE);
                personViewHolder.status = -2;
                break;

            default:
                personViewHolder.txtAbonOperator.setTextColor(Color.RED);
                personViewHolder.txtAbonOperator.setText("Оператор №" + ticketsView.get(i).dbAdminID + " ответил Вам:");
                break;

        }
    }

    @Override
    public int getItemCount() {
        return ticketsView.size();
    }
}