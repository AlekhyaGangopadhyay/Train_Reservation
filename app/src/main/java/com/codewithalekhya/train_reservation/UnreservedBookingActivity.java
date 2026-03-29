package com.codewithalekhya.train_reservation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UnreservedBookingActivity extends AppCompatActivity {

    private String from, to;
    private int adultCount = 1;
    private int childCount = 0;
    private int baseFare = 25; 
    private TextView tvAdultCount, tvChildCount, tvTotalFare, tvFromDisplay, tvToDisplay;
    private RadioGroup rgTrainType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unreserved_booking);

        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");

        MaterialToolbar toolbar = findViewById(R.id.toolbar_unreserved);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        tvFromDisplay = findViewById(R.id.tv_from_display);
        tvToDisplay = findViewById(R.id.tv_to_display);
        tvAdultCount = findViewById(R.id.tv_adult_count);
        tvChildCount = findViewById(R.id.tv_child_count);
        tvTotalFare = findViewById(R.id.tv_total_fare);
        rgTrainType = findViewById(R.id.rg_train_type);

        tvFromDisplay.setText(from != null ? from : "SOURCE");
        tvToDisplay.setText(to != null ? to : "DESTINATION");

        findViewById(R.id.btn_adult_plus).setOnClickListener(v -> {
            if (adultCount < 4) { adultCount++; updateUI(); }
        });

        findViewById(R.id.btn_adult_minus).setOnClickListener(v -> {
            if (adultCount > 1) { adultCount--; updateUI(); }
        });

        findViewById(R.id.btn_child_plus).setOnClickListener(v -> {
            if (childCount < 4) { childCount++; updateUI(); }
        });

        findViewById(R.id.btn_child_minus).setOnClickListener(v -> {
            if (childCount > 0) { childCount--; updateUI(); }
        });

        rgTrainType.setOnCheckedChangeListener((group, checkedId) -> {
            baseFare = (checkedId == R.id.rb_mail_exp) ? 45 : 25;
            updateUI();
        });

        findViewById(R.id.btn_book_unres_now).setOnClickListener(v -> {
            saveUnreservedBooking();
            Toast.makeText(this, "Ticket Booked Successfully!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, BookedTicketsActivity.class));
            finish();
        });

        updateUI();
    }

    private void updateUI() {
        tvAdultCount.setText(String.valueOf(adultCount));
        tvChildCount.setText(String.valueOf(childCount));
        int total = (adultCount * baseFare) + (childCount * (baseFare / 2));
        tvTotalFare.setText("Fare: ₹ " + total);
    }

    private void saveUnreservedBooking() {
        SharedPreferences userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String currentEmail = userPrefs.getString("current_user_email", "");
        
        SharedPreferences trainPrefs = getSharedPreferences("TrainPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        
        String key = "booked_tickets_" + currentEmail;
        String json = trainPrefs.getString(key, null);
        List<BookedTicket> ticketList;
        if (json == null) {
            ticketList = new ArrayList<>();
        } else {
            Type type = new TypeToken<List<BookedTicket>>() {}.getType();
            ticketList = gson.fromJson(json, type);
        }

        long timestamp = System.currentTimeMillis();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(new Date(timestamp));
        int total = (adultCount * baseFare) + (childCount * (baseFare / 2));

        BookedTicket newTicket = new BookedTicket(
                "Adult: " + adultCount + ", Child: " + childCount,
                "N/A",
                "Unreserved Journey",
                "GEN-E-TKT",
                from,
                to,
                "Valid for 12 hours",
                (double) total,
                currentDate,
                timestamp,
                true
        );

        ticketList.add(newTicket);
        trainPrefs.edit().putString(key, gson.toJson(ticketList)).apply();
    }
}