package com.codewithalekhya.train_reservation;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {
    @SerializedName("main")
    public Main main;
    @SerializedName("weather")
    public Weather[] weather;

    public static class Main {
        @SerializedName("temp")
        public float temp;
    }

    public static class Weather {
        @SerializedName("description")
        public String description;
    }
}