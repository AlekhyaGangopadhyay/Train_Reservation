package com.codewithalekhya.train_reservation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlatformBookingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platform_booking);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_platform);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        TextInputEditText etStation = findViewById(R.id.et_platform_station);
        Button btnBook = findViewById(R.id.btn_book_platform);

        btnBook.setOnClickListener(v -> {
            String station = etStation.getText().toString().trim();
            if (station.isEmpty()) {
                Toast.makeText(this, "Enter Station Name", Toast.LENGTH_SHORT).show();
            } else {
                savePlatformBooking(station);
                Toast.makeText(this, "Platform Ticket Booked for " + station, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, BookedTicketsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void savePlatformBooking(String station) {
        SharedPreferences userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String currentEmail = userPrefs.getString("current_user_email", "");
        
        SharedPreferences sharedPreferences = getSharedPreferences("TrainPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        
        String key = "booked_tickets_" + currentEmail;
        String json = sharedPreferences.getString(key, null);
        List<BookedTicket> ticketList;
        if (json == null) {
            ticketList = new ArrayList<>();
        } else {
            Type type = new TypeToken<List<BookedTicket>>() {}.getType();
            ticketList = gson.fromJson(json, type);
        }

        long timestamp = System.currentTimeMillis();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(new Date(timestamp));
        
        BookedTicket newTicket = new BookedTicket(
                "N/A", 
                "N/A", 
                "Platform Ticket", 
                "PLT-001",
                station, 
                "Entry Only", 
                "Valid for 12 hours", 
                10.0, 
                currentDate,
                timestamp,
                true
        );
        
        ticketList.add(newTicket);
        sharedPreferences.edit().putString(key, gson.toJson(ticketList)).apply();
    }
}