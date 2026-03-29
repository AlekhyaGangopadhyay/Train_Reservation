package com.codewithalekhya.train_reservation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LiveStatusActivity extends AppCompatActivity {

    private TextInputEditText etTrainNo;
    private ProgressBar progressBar;
    private MaterialCardView cardCurrentStatus;
    private TextView tvCurrentStation, tvDelayInfo, tvUpcomingTitle;
    private RecyclerView rvUpcoming;
    private TrainApiService apiService;

    // Use your actual API Key here
    private static final String API_KEY = "a73c3d4b52msh89f0c0e5602b4e3p1fe879jsn1a5f1d43a6ab";
    private static final String API_HOST = "irctc1.p.rapidapi.com";
    private static final String BASE_URL = "https://irctc1.p.rapidapi.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_status);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_live);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        etTrainNo = findViewById(R.id.et_train_no_live);
        progressBar = findViewById(R.id.pb_live);
        cardCurrentStatus = findViewById(R.id.card_current_status);
        tvCurrentStation = findViewById(R.id.tv_current_station);
        tvDelayInfo = findViewById(R.id.tv_delay_info);
        tvUpcomingTitle = findViewById(R.id.tv_upcoming_title);
        rvUpcoming = findViewById(R.id.rv_upcoming_stations);
        Button btnGetStatus = findViewById(R.id.btn_get_status);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(TrainApiService.class);

        rvUpcoming.setLayoutManager(new LinearLayoutManager(this));

        btnGetStatus.setOnClickListener(v -> {
            String trainNo = etTrainNo.getText().toString().trim();
            if (trainNo.isEmpty()) {
                Toast.makeText(this, "Enter Train Number", Toast.LENGTH_SHORT).show();
            } else {
                fetchLiveStatus(trainNo);
            }
        });
    }

    private void fetchLiveStatus(String trainNo) {
        progressBar.setVisibility(View.VISIBLE);
        cardCurrentStatus.setVisibility(View.GONE);
        tvUpcomingTitle.setVisibility(View.GONE);
        rvUpcoming.setVisibility(View.GONE);

        // startDay = 0 means today, 1 means yesterday
        apiService.getLiveStatus(API_KEY, API_HOST, trainNo, 0)
                .enqueue(new Callback<LiveStatusResponse>() {
            @Override
            public void onResponse(@NonNull Call<LiveStatusResponse> call, @NonNull Response<LiveStatusResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    displayStatus(response.body().getData());
                } else {
                    Toast.makeText(LiveStatusActivity.this, "Live status not found for this train", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LiveStatusResponse> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LiveStatusActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayStatus(LiveStatusResponse.LiveStatusData data) {
        cardCurrentStatus.setVisibility(View.VISIBLE);
        tvCurrentStation.setText(String.format("Current: %s", data.getCurrentStation()));
        tvDelayInfo.setText(String.format("Delay: %d mins | %s", data.getDelay(), data.getStatusAsOf()));

        if (data.getUpcomingStations() != null && !data.getUpcomingStations().isEmpty()) {
            tvUpcomingTitle.setVisibility(View.VISIBLE);
            rvUpcoming.setVisibility(View.VISIBLE);
            rvUpcoming.setAdapter(new StationStatusAdapter(data.getUpcomingStations()));
        }
    }
}