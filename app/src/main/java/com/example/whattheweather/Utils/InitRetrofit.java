package com.example.whattheweather.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InitRetrofit {

    private Retrofit retrofit;
    private static OpenWeatherApiService apiRouter;

    public InitRetrofit (){
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder().baseUrl("https://community-open-weather-map.p.rapidapi.com/").addConverterFactory(GsonConverterFactory.create(gson)).build();
        apiRouter = retrofit.create(OpenWeatherApiService.class);
    }

    public static OpenWeatherApiService getApiRouter() {
        return apiRouter;
    }

    public static void setApiRouter(OpenWeatherApiService apiRouter) {
        InitRetrofit.apiRouter = apiRouter;
    }
}
