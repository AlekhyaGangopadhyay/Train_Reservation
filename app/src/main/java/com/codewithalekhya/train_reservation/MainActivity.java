package com.codewithalekhya.train_reservation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView tvWeatherTemp, tvWeatherDesc, tvRailNewsTitle, tvRailNewsDesc;
    private TrainApiService apiService;

    // --- API KEYS (Get these for free) ---
    // 1. OpenWeatherMap: https://openweathermap.org/api (Free tier available)
    private static final String WEATHER_API_KEY = "37e4601d913cc0054925147f04dd8d43";
    // 2. NewsAPI: https://newsapi.org/ (Free tier for developers)
    private static final String NEWS_API_KEY = "8c23fbeb2d92426aa297f184cf47bff0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        TextView tvWelcome = findViewById(R.id.tv_user_name_home);
        tvWeatherTemp = findViewById(R.id.tv_weather_temp);
        tvWeatherDesc = findViewById(R.id.tv_weather_desc);
        tvRailNewsTitle = findViewById(R.id.tv_rail_news_title);
        tvRailNewsDesc = findViewById(R.id.tv_rail_news_desc);

        SharedPreferences userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String currentEmail = userPrefs.getString("current_user_email", "");
        String userName = userPrefs.getString("name_" + currentEmail, "Traveler");
        tvWelcome.setText(userName);

        setupRetrofit();
        fetchWeatherData();
        fetchRailNews();

        MaterialCardView cardReserved = findViewById(R.id.card_reserved);
        MaterialCardView cardUnreserved = findViewById(R.id.card_unreserved);
        MaterialCardView cardPlatform = findViewById(R.id.card_platform);
        MaterialCardView cardMyBookings = findViewById(R.id.card_my_bookings);

        cardReserved.setOnClickListener(v -> startActivity(new Intent(this, ReservedBookingActivity.class)));
        cardUnreserved.setOnClickListener(v -> showUnreservedDialog());
        cardPlatform.setOnClickListener(v -> startActivity(new Intent(this, PlatformBookingActivity.class)));
        cardMyBookings.setOnClickListener(v -> startActivity(new Intent(this, BookedTicketsActivity.class)));
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.example.com/") // Base URL is required but overridden by @Url
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(TrainApiService.class);
    }

    private void fetchWeatherData() {
        if (WEATHER_API_KEY.startsWith("YOUR_")) return;

        // Updated to fetch weather for Kolkata, IN
        String weatherUrl = String.format("https://api.openweathermap.org/data/2.5/weather?q=Kolkata,IN&units=metric&appid=%s", WEATHER_API_KEY);
        
        apiService.getWeather(weatherUrl).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvWeatherTemp.setText(String.format(Locale.getDefault(), "%.1f°C", response.body().main.temp));
                    tvWeatherDesc.setText(response.body().weather[0].description);
                }
            }
            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {}
        });
    }

    private void fetchRailNews() {
        if (NEWS_API_KEY.startsWith("YOUR_")) return;

        String newsUrl = String.format("https://newsapi.org/v2/everything?q=Indian+Railways&sortBy=publishedAt&apiKey=%s", NEWS_API_KEY);
        
        apiService.getNews(newsUrl).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().articles.isEmpty()) {
                    NewsResponse.Article article = response.body().articles.get(0);
                    tvRailNewsTitle.setText(article.title);
                    tvRailNewsDesc.setText(article.description);
                }
            }
            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {}
        });
    }

    private void showUnreservedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_station_input, null);
        builder.setView(view);

        TextInputEditText etFrom = view.findViewById(R.id.et_dialog_from);
        TextInputEditText etTo = view.findViewById(R.id.et_dialog_to);
        Button btnNext = view.findViewById(R.id.btn_dialog_next);

        AlertDialog dialog = builder.create();
        btnNext.setOnClickListener(v -> {
            String from = etFrom.getText().toString().trim();
            String to = etTo.getText().toString().trim();
            if (!from.isEmpty() && !to.isEmpty()) {
                Intent intent = new Intent(this, UnreservedBookingActivity.class);
                intent.putExtra("from", from);
                intent.putExtra("to", to);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_my_bookings) {
            startActivity(new Intent(this, BookedTicketsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}