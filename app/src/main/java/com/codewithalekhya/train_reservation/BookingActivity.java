package com.codewithalekhya.train_reservation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import java.util.Random;

public class BookingActivity extends AppCompatActivity {

    private Train train;
    private TextInputEditText etPassengerName, etPassengerAge;
    private RadioGroup rgClass;
    private TextView tvFinalFare;
    private double currentFare = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        train = (Train) getIntent().getSerializableExtra("train");
        String bookingType = getIntent().getStringExtra("booking_type");

        MaterialToolbar toolbar = findViewById(R.id.toolbar_booking);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        TextView tvName = findViewById(R.id.tv_booking_train_name);
        TextView tvRoute = findViewById(R.id.tv_booking_route);
        TextView tvSeatInfo = findViewById(R.id.tv_seat_info);
        rgClass = findViewById(R.id.rg_class);
        etPassengerName = findViewById(R.id.et_p_name);
        etPassengerAge = findViewById(R.id.et_p_age);
        tvFinalFare = findViewById(R.id.tv_final_fare);
        Button btnConfirm = findViewById(R.id.btn_confirm_final);

        if (train != null) {
            tvName.setText(String.format(Locale.getDefault(), "%s (%s)", train.getTrainName(), train.getTrainNumber()));
            tvRoute.setText(String.format(Locale.getDefault(), "%s to %s\nDeparture: %s", 
                    train.getFromStation(), train.getToStation(), train.getDepartureTime()));
            
            currentFare = train.getPrice();
            updateFareDisplay();

            if ("RESERVED".equals(bookingType)) {
                tvSeatInfo.setVisibility(View.VISIBLE);
                String seat = String.format(Locale.getDefault(), "B%d-%d", 
                        new Random().nextInt(5) + 1, new Random().nextInt(64) + 1);
                tvSeatInfo.setText(String.format(Locale.getDefault(), "Seat Assigned: %s", seat));
            }
        }

        rgClass.setOnCheckedChangeListener((group, checkedId) -> {
            if (train == null) return;
            double multiplier = 1.0;
            if (checkedId == R.id.rb_sl) multiplier = 1.0;
            else if (checkedId == R.id.rb_3a) multiplier = 2.5;
            else if (checkedId == R.id.rb_2a) multiplier = 3.5;
            else if (checkedId == R.id.rb_1a) multiplier = 5.0;
            
            currentFare = train.getPrice() * multiplier;
            updateFareDisplay();
        });

        btnConfirm.setOnClickListener(v -> {
            if (etPassengerName.getText() == null || etPassengerAge.getText() == null) return;

            String name = etPassengerName.getText().toString().trim();
            String age = etPassengerAge.getText().toString().trim();

            if (name.isEmpty() || age.isEmpty()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
            } else {
                saveBooking(name, age);
                Toast.makeText(this, "Booking Successful!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, BookedTicketsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateFareDisplay() {
        tvFinalFare.setText(String.format(Locale.getDefault(), "Total Fare: ₹%.2f", currentFare));
    }

    private void saveBooking(String name, String age) {
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
        
        RadioButton selectedRb = findViewById(rgClass.getCheckedRadioButtonId());
        String travelClass = selectedRb != null ? selectedRb.getText().toString() : "GEN";

        BookedTicket newTicket = new BookedTicket(
                name, 
                age, 
                train.getTrainName() + " [" + travelClass + "]", 
                train.getTrainNumber(),
                train.getFromStation(), 
                train.getToStation(), 
                train.getDepartureTime(), 
                currentFare, 
                currentDate,
                timestamp,
                false
        );
        
        ticketList.add(newTicket);
        trainPrefs.edit().putString(key, gson.toJson(ticketList)).apply();
    }
}