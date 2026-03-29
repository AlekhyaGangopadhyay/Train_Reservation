package com.codewithalekhya.train_reservation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StationStatusAdapter extends RecyclerView.Adapter<StationStatusAdapter.ViewHolder> {

    private final List<LiveStatusResponse.StationStatus> stations;

    public StationStatusAdapter(List<LiveStatusResponse.StationStatus> stations) {
        this.stations = stations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LiveStatusResponse.StationStatus station = stations.get(position);
        holder.text1.setText(station.getStationName());
        holder.text2.setText("ETA: " + station.getEta() + " | ETD: " + station.getEtd());
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1, text2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }
    }
}