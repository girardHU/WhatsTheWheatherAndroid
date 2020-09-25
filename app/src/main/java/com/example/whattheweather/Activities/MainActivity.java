package com.example.whattheweather.Activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.whattheweather.R;
import com.example.whattheweather.Utils.InitRetrofit;
import com.example.whattheweather.Views.WeatherCitiesAdapter;
import com.example.whattheweather.db.City;
import com.example.whattheweather.db.CityDatabase;
import com.example.whattheweather.Fragments.CitiesFragment;
import com.example.whattheweather.Fragments.WeatherFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private  Context context;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.constraintLayout = findViewById(R.id.mainConstraintLayout);

        final TabLayout tabLayoutWeatherCities = findViewById(R.id.tabBarWeatherCities);
        final ViewPager viewPagerWeatherCities  = findViewById(R.id.viewPagerWeatherCities);

        final PagerAdapter weatherCitiesAdapter = new WeatherCitiesAdapter(getSupportFragmentManager(), tabLayoutWeatherCities.getTabCount());

        viewPagerWeatherCities.setAdapter(weatherCitiesAdapter);

        tabLayoutWeatherCities.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerWeatherCities.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPagerWeatherCities.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                Fragment frag = WeatherCitiesAdapter.fragments[position];
                String fgTag = null;
                if (frag instanceof WeatherFragment) {
                    fgTag = "WeatherFragment";
                    System.out.println(fgTag);
                    getSupportFragmentManager().findFragmentByTag(fgTag);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.detach(frag);
                    ft.attach(frag);
                    ft.commit();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        final EditText searchbar = findViewById(R.id.citySearchbar);
        searchbar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (textView.getText() != null && textView.getText().toString() != "") {
                    checkCityByNameInAPI("" + textView.getText().toString());
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchbar.getWindowToken(), 0);
                    searchbar.setText("");
                    constraintLayout.requestFocus();
                    cityLoad();
                    return true;
                }
                return false;
            }
        });
    }

    private Context getAppContext() {
        return this.context;
    }

    public void checkCityByName(JsonObject apiResponse) {
        String name = apiResponse.get("name").getAsString();
        City city = CityDatabase.getDatabase(this.getAppContext()).cityDao().findByName(name);
        if(city == null)
            this.addInDatabase(apiResponse);
        else {
            Snackbar snack = Snackbar.make(this.constraintLayout, "Already in your list", Snackbar.LENGTH_SHORT);
            snack.show();
        }
    }

    public void checkCityByNameInAPI(String name) {
        InitRetrofit retrofit = new InitRetrofit();
        Call<JsonObject> call = retrofit.getApiRouter().getTodaysWeather(name, "metric", "json");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject apiResponse = response.body();
                    System.out.println(apiResponse);
                    checkCityByName(apiResponse);
                }
                else {
                    Snackbar snack = Snackbar.make(constraintLayout, "Cannot find this city", Snackbar.LENGTH_SHORT);
                    snack.show();
                    System.out.println("Request Error : " + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Snackbar snack = Snackbar.make(constraintLayout, "Network error", Snackbar.LENGTH_SHORT);
                snack.show();
                System.out.println("Network Error : " + t.getLocalizedMessage());
            }
        });
    }

    public void addInDatabase(JsonObject apiResponse) {
        String name = apiResponse.get("name").getAsString();
        JsonObject sys  = apiResponse.getAsJsonObject("sys");
        String country = sys.get("country").getAsString();
        cityResetCurrentValue();
        citySave(name, country);
        Snackbar snack = Snackbar.make(constraintLayout, "City successfully added", Snackbar.LENGTH_SHORT);
        snack.show();
        // TODO: rediriger vers WeatherFragment
    }

    public void refreshFragment(String fgTag) {
        Fragment frg = getSupportFragmentManager().findFragmentByTag(fgTag);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    public void cityDrop() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                CityDatabase.getDatabase(getAppContext()).cityDao().deleteAll();
            }
        });
    }

    public void cityResetCurrentValue() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                CityDatabase.getDatabase(getAppContext()).cityDao().resetCurrentValue();
            }
        });
    }

    public void citySave(String name, String country) {
        Date date = new Date();
        final City city = new City(false, date.getTime(), name, country);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                CityDatabase.getDatabase(getAppContext()).cityDao().insertAll(city);
            }
        });
        CitiesFragment frag = (CitiesFragment) WeatherCitiesAdapter.fragments[1];
        frag.newCityAdded(city);
    }

    public void cityLoad() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<City> cities = CityDatabase.getDatabase(getAppContext()).cityDao().getAll();
            }
        });
    }
}