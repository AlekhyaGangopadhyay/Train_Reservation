package com.codewithalekhya.train_reservation;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReservedBookingActivity extends AppCompatActivity {

    private TrainAdapter adapter;
    private TextInputEditText etFrom, etTo;
    private ProgressBar progressBar;
    private TrainApiService apiService;
    private TextView tvSelectedDate;
    private Calendar calendar;
    private LinearLayout llDateChips;
    private ChipGroup cgClass;

    // IMPORTANT: Use your actual RapidAPI Key here
    private static final String API_KEY = "a73c3d4b52msh89f0c0e5602b4e3p1fe879jsn1a5f1d43a6ab";
    private static final String API_HOST = "irctc1.p.rapidapi.com";
    private static final String BASE_URL = "https://irctc1.p.rapidapi.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserved_booking);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_reserved);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        etFrom = findViewById(R.id.et_from_res);
        etTo = findViewById(R.id.et_to_res);
        Button btnSearch = findViewById(R.id.btn_search_res);
        RecyclerView rvTrains = findViewById(R.id.rv_trains_res);
        progressBar = findViewById(R.id.pb_res);
        FloatingActionButton fabSwap = findViewById(R.id.fab_swap);
        tvSelectedDate = findViewById(R.id.tv_selected_date);
        Spinner spinnerQuota = findViewById(R.id.spinner_quota);
        llDateChips = findViewById(R.id.ll_date_chips);
        cgClass = findViewById(R.id.cg_class);

        calendar = Calendar.getInstance();
        updateDateLabel();
        setupDateChips();
        setupClassChips();

        findViewById(R.id.iv_calendar).setOnClickListener(v -> showDatePicker());
        tvSelectedDate.setOnClickListener(v -> showDatePicker());

        fabSwap.setOnClickListener(v -> {
            if (etFrom.getText() == null || etTo.getText() == null) return;
            String fromText = etFrom.getText().toString();
            String toText = etTo.getText().toString();
            etFrom.setText(toText);
            etTo.setText(fromText);
        });

        String[] quotas = {"General", "Ladies", "Lower berth", "Person with disability", "Duty pass", "Tatkal", "Premium Tatkal"};
        ArrayAdapter<String> quotaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, quotas);
        quotaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQuota.setAdapter(quotaAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(TrainApiService.class);

        adapter = new TrainAdapter(new ArrayList<>(), train -> {
            Intent intent = new Intent(this, BookingActivity.class);
            intent.putExtra("train", train);
            intent.putExtra("booking_type", "RESERVED");
            startActivity(intent);
        });

        rvTrains.setLayoutManager(new LinearLayoutManager(this));
        rvTrains.setAdapter(adapter);

        btnSearch.setOnClickListener(v -> {
            if (etFrom.getText() == null || etTo.getText() == null) return;
            String from = etFrom.getText().toString().trim().toUpperCase();
            String to = etTo.getText().toString().trim().toUpperCase();
            if (from.isEmpty() || to.isEmpty()) {
                Toast.makeText(this, "Enter station codes", Toast.LENGTH_SHORT).show();
            } else {
                fetchTrains(from, to);
            }
        });
    }

    private void setupDateChips() {
        llDateChips.removeAllViews();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM", Locale.getDefault());
        Calendar tempCal = (Calendar) calendar.clone();
        tempCal.add(Calendar.DAY_OF_MONTH, 1); 

        for (int i = 0; i < 3; i++) {
            Chip chip = new Chip(this);
            String dateStr = sdf.format(tempCal.getTime());
            final long timeInMillis = tempCal.getTimeInMillis();
            chip.setText(dateStr);
            chip.setOnClickListener(v -> {
                calendar.setTimeInMillis(timeInMillis);
                updateDateLabel();
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 0, 0);
            llDateChips.addView(chip, params);
            tempCal.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private void setupClassChips() {
        String[] classes = {"All", "2S", "VS", "SL", "CC", "VC", "3E", "3A", "FC", "2A", "EC", "EV", "1A", "EA"};
        for (String cls : classes) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_chip_choice, cgClass, false);
            chip.setText(cls);
            if (cls.equals("All")) chip.setChecked(true);
            cgClass.addView(chip);
        }
    }

    private void showDatePicker() {
        DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
            setupDateChips();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dpd.show();
    }

    private void updateDateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM", Locale.getDefault());
        if (isToday(calendar)) {
            tvSelectedDate.setText("Today");
        } else if (isTomorrow(calendar)) {
            tvSelectedDate.setText("Tomorrow");
        } else {
            tvSelectedDate.setText(sdf.format(calendar.getTime()));
        }
    }
    
    private boolean isToday(Calendar cal) {
        Calendar today = Calendar.getInstance();
        return cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
               cal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR);
    }

    private boolean isTomorrow(Calendar cal) {
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
        return cal.get(Calendar.YEAR) == tomorrow.get(Calendar.YEAR) &&
               cal.get(Calendar.DAY_OF_YEAR) == tomorrow.get(Calendar.DAY_OF_YEAR);
    }

    private void fetchTrains(String from, String to) {
        if (API_KEY.startsWith("YOUR_")) {
            Toast.makeText(this, "API Key missing. Showing Mock Data.", Toast.LENGTH_SHORT).show();
            loadMockData(from, to);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        findViewById(R.id.rv_trains_res).setVisibility(View.GONE);
        
        // Format date for API: YYYY-MM-DD
        SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateOfJourney = apiDateFormat.format(calendar.getTime());

        apiService.getTrainsBetweenStations(API_KEY, API_HOST, from, to, dateOfJourney)
                .enqueue(new Callback<TrainResponse>() {
            @Override
            public void onResponse(@NonNull Call<TrainResponse> call, @NonNull Response<TrainResponse> response) {
                progressBar.setVisibility(View.GONE);
                findViewById(R.id.rv_trains_res).setVisibility(View.VISIBLE);
                
                if (response.isSuccessful() && response.body() != null) {
                    List<Train> trains = response.body().getData();
                    if (trains != null && !trains.isEmpty()) {
                        adapter.updateList(trains);
                    } else {
                        Toast.makeText(ReservedBookingActivity.this, "No live trains found. Check codes/date.", Toast.LENGTH_SHORT).show();
                        loadMockData(from, to);
                    }
                } else {
                    Toast.makeText(ReservedBookingActivity.this, "API Error: " + response.code() + ". Check Key.", Toast.LENGTH_SHORT).show();
                    loadMockData(from, to);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TrainResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                findViewById(R.id.rv_trains_res).setVisibility(View.VISIBLE);
                Toast.makeText(ReservedBookingActivity.this, "Network Failure. Showing Mock Data.", Toast.LENGTH_SHORT).show();
                loadMockData(from, to);
            }
        });
    }

    private void loadMockData(String from, String to) {
        List<Train> mockTrains = new ArrayList<>();
        mockTrains.add(new Train("12435", "Rajdhani Express", from, to, "16:00", "08:00", 50, 1500));
        mockTrains.add(new Train("12002", "Shatabdi Express", from, to, "06:00", "14:00", 30, 1200));
        mockTrains.add(new Train("22436", "Vande Bharat", from, to, "06:00", "14:00", 20, 1800));
        adapter.updateList(mockTrains);
        findViewById(R.id.rv_trains_res).setVisibility(View.VISIBLE);
    }
}