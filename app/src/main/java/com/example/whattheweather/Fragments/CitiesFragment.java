package com.example.whattheweather.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whattheweather.R;
import com.example.whattheweather.db.City;
import com.example.whattheweather.db.CityDatabase;
import com.example.whattheweather.Views.CitiesAdapter;

import java.util.List;


public class CitiesFragment extends Fragment {

    private List<City> cities;
    private CitiesAdapter adapter;
    private View fragment;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.fragment = inflater.inflate(R.layout.fragment_cities, container, false);
        this.recyclerView = fragment.findViewById(R.id.fragment_cities_recycler_view);
        this.cities = CityDatabase.getDatabase(getActivity()).cityDao().getAll();
        this.configureRecyclerView();
        return fragment;
    }

    private void configureRecyclerView(){
        this.adapter = new CitiesAdapter(this.cities, getActivity());
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void newCityAdded(City city) {
        this.adapter.addCityFromList(city);
    }

}