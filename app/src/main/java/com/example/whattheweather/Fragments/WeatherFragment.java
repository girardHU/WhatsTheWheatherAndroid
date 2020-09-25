package com.example.whattheweather.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.whattheweather.Utils.InitRetrofit;
import com.example.whattheweather.R;

import com.example.whattheweather.Views.TodayPrevAdapter;
import com.google.android.material.tabs.TabLayout;

import com.example.whattheweather.db.City;
import com.example.whattheweather.db.CityDatabase;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WeatherFragment extends Fragment {

    private  Context context;

    private View view;
    private City city;
    private TextView cityName;
    private TextView cityTemp;
    private TextView cityTempMinMax;
    private TextView currentTime;
    private TextView cityHumidity;
    private TextView cityPressure;
    private TextView cityWind;
    private JSONObject forecastDataObject;
    private JSONObject hourlyDataObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        this.view = view;
        this.cityName = view.findViewById(R.id.curCityNameTextView);
        this.cityTemp = view.findViewById(R.id.currentCityTempCurTextView);
        this.cityTempMinMax = view.findViewById(R.id.currentCityTempMinMaxTextView);
        this.currentTime = view.findViewById(R.id.curCityTimestampTextView);
        this.cityHumidity = view.findViewById(R.id.humidityTextView);
        this.cityPressure = view.findViewById(R.id.pressureTextView);
        this.cityWind = view.findViewById(R.id.windTextView);
        this.forecastDataObject = new JSONObject();
        this.hourlyDataObject = new JSONObject();
        this.getCurrentCity();

        return view;
    }

    private Context getAppContext() {
        return this.context;
    }

    public void getCurrentCity() {
        this.city = CityDatabase.getDatabase(getActivity()).cityDao().getCurrentCity();
        if(city != null) {
            this.cityName.setText(this.city.getName());
            this.getCurrentData(this.city.getName());
            this.getForecastData(this.city.getName());
        }
        else {
            this.cityName.setText("No city selected");
        }
    }

    public void getCurrentData(String CityName) {
        InitRetrofit retrofit = new InitRetrofit();

        Call<JsonObject> call = retrofit.getApiRouter().getTodaysWeather(CityName, "metric", "json");
        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject apiResponse = response.body();

                    JsonObject main = apiResponse.get("main").getAsJsonObject();
                    JsonObject wind = apiResponse.get("wind").getAsJsonObject();
                    double temp = main.get("temp").getAsDouble();

                    SimpleDateFormat sdformat = new SimpleDateFormat("E dd MMM yyyy HH:mm:ss");
                    Date today = new java.util.Date(Long.parseLong(String.valueOf(apiResponse.get("dt").getAsInt() *1000L)));
                    currentTime.setText(sdformat.format(today));

                    cityTemp.setText((int) temp + "°C / " + (int) (temp * 1.8 + 32) + "°F");
                    cityTempMinMax.setText( main.get("temp_min").getAsInt() + "°C/" + main.get("temp_max").getAsInt() + "°C");

                    cityHumidity.setText("Precipit. : " + main.get("humidity").getAsInt() + "%");
                    cityPressure.setText("Press. : " + main.get("pressure").getAsInt() + " hPa");
                    cityWind.setText(wind.get("speed").getAsInt() + " m.s-1, " + wind.get("deg").getAsInt() + "°");

                }
                else {
                    System.out.println("Request Error : " + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("Network Error : " + t.getLocalizedMessage());
            }
        });
    }

    public void getForecastData(String CityName) {
        InitRetrofit retrofit = new InitRetrofit();

        Call<JsonObject> call = InitRetrofit.getApiRouter().getForecast(CityName, "metric", "json");
        call.enqueue(new Callback<JsonObject>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject apiResponse = response.body();

                    SimpleDateFormat hourlyFormatParsable = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat hourlyFormatComparable = new SimpleDateFormat("HH");
                    SimpleDateFormat sdformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                    SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat apiFormatComparable = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdformatClear = new SimpleDateFormat("yyyy-MM-dd");
                    Date today = new java.util.Date(Long.parseLong(String.valueOf(System.currentTimeMillis())));

                    JSONObject tempJsonObj;

                    JsonArray list = apiResponse.get("list").getAsJsonArray();
                    try {
                        int i = 0;
                        int temp;
                        for (JsonElement elem : list) {
                            tempJsonObj = new JSONObject();
                            if (0 == apiFormatComparable.format(apiFormat.parse(elem.getAsJsonObject().get("dt_txt").getAsString())).compareTo(apiFormatComparable.format(today))) {
                                Date date = hourlyFormatParsable.parse(elem.getAsJsonObject().get("dt_txt").getAsString());
                                temp = elem.getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsInt();
                                tempJsonObj.put("hour", hourlyFormatComparable.format(date));
                                tempJsonObj.put("temperature", temp);
                                hourlyDataObject.put(String.valueOf(i), tempJsonObj);

                            }
                            i += 1;
                        }

                    } catch (ParseException | JSONException e) {
                        e.printStackTrace();
                    }

                    // PREVISION 5 JOURS
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(today);
                    calendar.add(Calendar.DATE, 1);
                    Date cursor = calendar.getTime();
                    JSONObject tempObj;
                    String mainWeather;
                    int y = 1;
                    int temp = 0;

                    try {
                        for (JsonElement elem : list) {
                            tempObj = new JSONObject();
                            mainWeather = "";

                            if (0 == apiFormatComparable.format(apiFormat.parse(elem.getAsJsonObject().get("dt_txt").getAsString())).compareTo(apiFormatComparable.format(cursor))) {
                                temp += elem.getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsInt();
                            } else if (0 < apiFormatComparable.format(apiFormat.parse(elem.getAsJsonObject().get("dt_txt").getAsString())).compareTo(apiFormatComparable.format(cursor))) {
                                temp /= 8;
                                mainWeather = elem.getAsJsonObject().get("weather").getAsJsonArray().get(0).getAsJsonObject().get("main").getAsString();
                                tempObj.put("temperature", temp + "°C / " + (int) (temp * 1.8 + 32) + "°F");
                                tempObj.put("weather", mainWeather);
                                forecastDataObject.put(String.valueOf(y), tempObj);

                                calendar.setTime(cursor);
                                calendar.add(Calendar.DATE, 1);
                                cursor = calendar.getTime();
                                y++;
                            }

                        }

                    } catch (ParseException | JSONException e) {
                        e.printStackTrace();
                    }
                    setupPreviewViewPager();
                } else {
                    System.out.println("Request Error : " + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("Network Error : " + t.getLocalizedMessage());
            }
        });
    }

    private void setupPreviewViewPager() {
        final TabLayout tabLayoutTodayPrev = view.findViewById(R.id.tabBarTodayPrev);
        final ViewPager viewPagerTodayPrev  = view.findViewById(R.id.viewPagerTodayPrev);
        final PagerAdapter todayPrevAdapter = new TodayPrevAdapter(getFragmentManager(), tabLayoutTodayPrev.getTabCount(), hourlyDataObject, forecastDataObject);

        viewPagerTodayPrev.setAdapter(todayPrevAdapter);


        tabLayoutTodayPrev.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerTodayPrev.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPagerTodayPrev.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                Fragment frag = TodayPrevAdapter.fragments[position];
                String fgTag = null;
                if (frag instanceof TodayFragment) {
                    fgTag = "TodayFragment";
                    getFragmentManager().findFragmentByTag(fgTag);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(frag);
                    ft.attach(frag);
                    ft.commit();
                } else if (frag instanceof PrevisionFragment) {
                    fgTag = "PrevisionFragment";
                    getFragmentManager().findFragmentByTag(fgTag);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(frag);
                    ft.attach(frag);
                    ft.commit();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) { };
        });
    }
}