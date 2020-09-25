package com.example.whattheweather.Utils;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface OpenWeatherApiService {
    @Headers({"x-rapidapi-host: community-open-weather-map.p.rapidapi.com", "x-rapidapi-key: bc8360e4eemshe288df5b860a1bbp1e74ebjsn2cca2b4f3c61"})
    @GET("weather")
    Call<JsonObject> getTodaysWeather(
            @Query("q") String city,
            @Query("units") String units,
            @Query("mode") String mode);

    @Headers({"x-rapidapi-host: community-open-weather-map.p.rapidapi.com", "x-rapidapi-key: bc8360e4eemshe288df5b860a1bbp1e74ebjsn2cca2b4f3c61"})
    @GET("forecast")
    Call<JsonObject> getForecast(
            @Query("q") String city,
            @Query("units") String units,
            @Query("mode") String mode
            );
}