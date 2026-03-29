package com.codewithalekhya.train_reservation;

import java.io.Serializable;

public class BookedTicket implements Serializable {
    private String passengerName;
    private String passengerAge;
    private String trainName;
    private String trainNumber;
    private String fromStation;
    private String toStation;
    private String departureTime;
    private double price;
    private String bookingDate;
    private long bookingTimestamp;
    private boolean isTemporary;

    public BookedTicket(String passengerName, String passengerAge, String trainName, String trainNumber, 
                        String fromStation, String toStation, String departureTime, double price, 
                        String bookingDate, long bookingTimestamp, boolean isTemporary) {
        this.passengerName = passengerName;
        this.passengerAge = passengerAge;
        this.trainName = trainName;
        this.trainNumber = trainNumber;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.departureTime = departureTime;
        this.price = price;
        this.bookingDate = bookingDate;
        this.bookingTimestamp = bookingTimestamp;
        this.isTemporary = isTemporary;
    }

    public String getPassengerName() { return passengerName; }
    public String getPassengerAge() { return passengerAge; }
    public String getTrainName() { return trainName; }
    public String getTrainNumber() { return trainNumber; }
    public String getFromStation() { return fromStation; }
    public String getToStation() { return toStation; }
    public String getDepartureTime() { return departureTime; }
    public double getPrice() { return price; }
    public String getBookingDate() { return bookingDate; }
    public long getBookingTimestamp() { return bookingTimestamp; }
    public boolean isTemporary() { return isTemporary; }
}