package com.example.whattheweather.Views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whattheweather.R;
import com.example.whattheweather.db.City;
import com.example.whattheweather.db.CityDatabase;

import java.util.List;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesViewHolder> {

    public List<City> cities;
    public Activity activity;

    public CitiesAdapter(List<City> cities, Activity activity) {
        this.cities = cities;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CitiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_cities_item, parent, false);
        return new CitiesViewHolder(view);
    }

    public void deleteCityFromList(int position) {
        this.cities.remove(position);
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, this.getItemCount());
    }

    public void addCityFromList(City city) {
        this.cities.add(city);
        this.notifyItemInserted(this.getItemCount() + 1);
        this.notifyItemRangeChanged(this.getItemCount() + 1, this.getItemCount());
    }

    private void setCities() {
        this.cities = CityDatabase.getDatabase(activity).cityDao().getAll();
    }

    @Override
    public void onBindViewHolder(CitiesViewHolder viewHolder, final int position) {
        viewHolder.textView.setText(cities.get(position).getName());
        if(cities.get(position).getCurrent() == true) {
            viewHolder.layout.setBackgroundColor(Color.parseColor("#e3e3e3"));
        }
        else
            viewHolder.layout.setBackgroundColor(0);

        new CitiesViewHolder.DownloadImageTask((viewHolder.flag)).execute("https://www.countryflags.io/" + cities.get(position).getCountry() + "/flat/64.png");

        viewHolder.trash.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteCityFromDB(cities.get(position).getName());
                deleteCityFromList(position);
            }
        });
        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                changeCurrentCityFromDB(cities.get(position).getName());
                setCities();
                notifyDataSetChanged();
            }
        });
    }

    private void deleteCityFromDB(String name) {
        CityDatabase.getDatabase(activity).cityDao().deleteByName(name);
    }

    private void changeCurrentCityFromDB(String name) {
        CityDatabase.getDatabase(activity).cityDao().resetAllCurrentState();
        CityDatabase.getDatabase(activity).cityDao().changeCurrentState(name);
    }

    @Override
    public int getItemCount() {
        return this.cities.size();
    }
}