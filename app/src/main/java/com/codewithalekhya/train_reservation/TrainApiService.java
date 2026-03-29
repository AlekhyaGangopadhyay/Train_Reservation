package com.codewithalekhya.train_reservation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface TrainApiService {
    @GET("api/v3/trainBetweenStations")
    Call<TrainResponse> getTrainsBetweenStations(
        @Header("X-RapidAPI-Key") String apiKey,
        @Header("X-RapidAPI-Host") String apiHost,
        @Query("fromStationCode") String fromStationCode,
        @Query("toStationCode") String toStationCode,
        @Query("dateOfJourney") String dateOfJourney
    );

    @GET("api/v1/liveTrainStatus")
    Call<LiveStatusResponse> getLiveStatus(
        @Header("X-RapidAPI-Key") String apiKey,
        @Header("X-RapidAPI-Host") String apiHost,
        @Query("trainNo") String trainNo,
        @Query("startDay") int startDay
    );

    @GET
    Call<WeatherResponse> getWeather(@Url String url);

    @GET
    Call<NewsResponse> getNews(@Url String url);
}