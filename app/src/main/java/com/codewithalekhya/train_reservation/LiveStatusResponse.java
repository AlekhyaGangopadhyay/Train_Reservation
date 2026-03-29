package com.codewithalekhya.train_reservation;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LiveStatusResponse {
    @SerializedName("status")
    private boolean status;
    
    @SerializedName("data")
    private LiveStatusData data;

    public boolean isStatus() { return status; }
    public LiveStatusData getData() { return data; }

    public static class LiveStatusData {
        @SerializedName("current_station_name")
        private String currentStation;
        
        @SerializedName("status_as_of")
        private String statusAsOf;
        
        @SerializedName("delay_minutes")
        private int delay;
        
        @SerializedName("upcoming_stations")
        private List<StationStatus> upcomingStations;

        public String getCurrentStation() { return currentStation; }
        public String getStatusAsOf() { return statusAsOf; }
        public int getDelay() { return delay; }
        public List<StationStatus> getUpcomingStations() { return upcomingStations; }
    }

    public static class StationStatus {
        @SerializedName("station_name")
        private String stationName;
        
        @SerializedName("eta")
        private String eta;
        
        @SerializedName("etd")
        private String etd;

        public String getStationName() { return stationName; }
        public String getEta() { return eta; }
        public String getEtd() { return etd; }
    }
}