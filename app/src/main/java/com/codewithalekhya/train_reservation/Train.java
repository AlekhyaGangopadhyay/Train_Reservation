package com.codewithalekhya.train_reservation;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Train implements Serializable {
    @SerializedName("train_number")
    private String trainNumber;
    
    @SerializedName("train_name")
    private String trainName;
    
    @SerializedName("from_sta")
    private String departureTime;
    
    @SerializedName("to_sta")
    private String arrivalTime;
    
    @SerializedName("from_station_name")
    private String fromStation;
    
    @SerializedName("to_station_name")
    private String toStation;
    
    private int availableSeats = 50;
    private double price = 750.0;

    public Train(String trainNumber, String trainName, String fromStation, String toStation, 
                 String departureTime, String arrivalTime, int availableSeats, double price) {
        this.trainNumber = trainNumber;
        this.trainName = trainName;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.availableSeats = availableSeats;
        this.price = price;
    }

    public String getTrainNumber() { return trainNumber; }
    public String getTrainName() { return trainName; }
    public String getFromStation() { return fromStation != null ? fromStation : "N/A"; }
    public String getToStation() { return toStation != null ? toStation : "N/A"; }
    public String getDepartureTime() { return departureTime != null ? departureTime : "00:00"; }
    public String getArrivalTime() { return arrivalTime != null ? arrivalTime : "00:00"; }
    public int getAvailableSeats() { return availableSeats; }
    public double getPrice() { return price; }

    public synchronized boolean bookSeats(int count) {
        if (availableSeats >= count) {
            availableSeats -= count;
            return true;
        }
        return false;
    }
}