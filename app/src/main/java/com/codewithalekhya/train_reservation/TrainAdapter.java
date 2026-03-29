package com.codewithalekhya.train_reservation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

public class TrainAdapter extends RecyclerView.Adapter<TrainAdapter.TrainViewHolder> {

    private List<Train> trainList;
    private final OnTrainClickListener listener;

    public interface OnTrainClickListener {
        void onTrainClick(Train train);
    }

    public TrainAdapter(List<Train> trainList, OnTrainClickListener listener) {
        this.trainList = trainList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_train, parent, false);
        return new TrainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainViewHolder holder, int position) {
        Train train = trainList.get(position);
        holder.tvTrainName.setText(train.getTrainName());
        holder.tvTrainNumber.setText(train.getTrainNumber());
        holder.tvDepartureTime.setText(train.getDepartureTime());
        holder.tvArrivalTime.setText(train.getArrivalTime());
        holder.tvFromStation.setText(train.getFromStation());
        holder.tvToStation.setText(train.getToStation());
        holder.tvPrice.setText(String.format(Locale.getDefault(), "₹%.0f", train.getPrice()));
        holder.tvSeats.setText(String.format(Locale.getDefault(), "AVL %d", train.getAvailableSeats()));

        holder.itemView.setOnClickListener(v -> listener.onTrainClick(train));
    }

    @Override
    public int getItemCount() {
        return trainList.size();
    }

    public void updateList(List<Train> newList) {
        this.trainList = newList;
        notifyDataSetChanged();
    }

    static class TrainViewHolder extends RecyclerView.ViewHolder {
        TextView tvTrainName, tvTrainNumber, tvDepartureTime, tvArrivalTime, tvFromStation, tvToStation, tvPrice, tvSeats;

        public TrainViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTrainName = itemView.findViewById(R.id.tv_train_name);
            tvTrainNumber = itemView.findViewById(R.id.tv_train_number);
            tvDepartureTime = itemView.findViewById(R.id.tv_departure_time);
            tvArrivalTime = itemView.findViewById(R.id.tv_arrival_time);
            tvFromStation = itemView.findViewById(R.id.tv_from_station);
            tvToStation = itemView.findViewById(R.id.tv_to_station);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvSeats = itemView.findViewById(R.id.tv_seats);
        }
    }
}