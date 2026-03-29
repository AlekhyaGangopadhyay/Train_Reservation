package com.codewithalekhya.train_reservation;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.content.SharedPreferences;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BookedTicketsActivity extends AppCompatActivity {

    private static final long EXPIRY_TIME_MS = 12 * 60 * 60 * 1000; // 12 hours

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_tickets);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_tickets);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.rv_booked_tickets);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<BookedTicket> tickets = getAndCleanBookedTickets();
        BookedTicketsAdapter adapter = new BookedTicketsAdapter(tickets);
        recyclerView.setAdapter(adapter);
    }

    private List<BookedTicket> getAndCleanBookedTickets() {
        SharedPreferences sharedPreferences = getSharedPreferences("TrainPrefs", MODE_PRIVATE);
        SharedPreferences userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String currentEmail = userPrefs.getString("current_user_email", "");
        
        String json = sharedPreferences.getString("booked_tickets_" + currentEmail, null);
        if (json == null) {
            return new ArrayList<>();
        }
        
        Gson gson = new Gson();
        Type type = new TypeToken<List<BookedTicket>>() {}.getType();
        List<BookedTicket> tickets = gson.fromJson(json, type);
        
        long currentTime = System.currentTimeMillis();
        boolean changed = false;
        
        Iterator<BookedTicket> iterator = tickets.iterator();
        while (iterator.hasNext()) {
            BookedTicket ticket = iterator.next();
            if (ticket.isTemporary() && (currentTime - ticket.getBookingTimestamp() > EXPIRY_TIME_MS)) {
                iterator.remove();
                changed = true;
            }
        }
        
        if (changed) {
            sharedPreferences.edit().putString("booked_tickets_" + currentEmail, gson.toJson(tickets)).apply();
        }
        
        return tickets;
    }
}