package com.codewithalekhya.train_reservation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookedTicketsAdapter extends RecyclerView.Adapter<BookedTicketsAdapter.ViewHolder> {

    private List<BookedTicket> tickets;

    public BookedTicketsAdapter(List<BookedTicket> tickets) {
        this.tickets = tickets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booked_ticket, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookedTicket ticket = tickets.get(position);
        holder.tvTrainName.setText(ticket.getTrainName() + " (" + ticket.getTrainNumber() + ")");
        holder.tvPassengerName.setText("Passenger: " + ticket.getPassengerName() + " (Age: " + ticket.getPassengerAge() + ")");
        holder.tvRoute.setText(ticket.getFromStation() + " to " + ticket.getToStation());
        holder.tvDate.setText("Booking Date: " + ticket.getBookingDate());
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTrainName, tvPassengerName, tvRoute, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTrainName = itemView.findViewById(R.id.tv_booked_train_name);
            tvPassengerName = itemView.findViewById(R.id.tv_booked_passenger_name);
            tvRoute = itemView.findViewById(R.id.tv_booked_route);
            tvDate = itemView.findViewById(R.id.tv_booked_date);
        }
    }
}