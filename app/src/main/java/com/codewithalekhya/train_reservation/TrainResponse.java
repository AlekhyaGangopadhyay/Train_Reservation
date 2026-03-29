package com.codewithalekhya.train_reservation;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TrainResponse {
    @SerializedName("status")
    private boolean status;
    
    @SerializedName("data")
    private List<Train> data;

    public boolean isStatus() {
        return status;
    }

    public List<Train> getData() {
        return data;
    }
}