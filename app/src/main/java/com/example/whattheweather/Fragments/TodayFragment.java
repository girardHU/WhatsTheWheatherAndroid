package com.example.whattheweather.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whattheweather.R;
import com.example.whattheweather.Views.DayHoursAdapter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayFragment extends Fragment {

    private static final String ARG_HOURSDATASTRING = "hoursDataString";

    private String hoursDataString;
    private JsonObject dataObject;

    private RecyclerView recyclerView;
    private DayHoursAdapter dayHoursAdapter;


    public TodayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param hoursDataString Parameter 1.
     * @return A new instance of fragment TodayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodayFragment newInstance(String hoursDataString) {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_HOURSDATASTRING, hoursDataString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hoursDataString = getArguments().getString(ARG_HOURSDATASTRING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_today, container, false);
        this.recyclerView = v.findViewById(R.id.dayHoursRecyclerView);
        this.dataObject = new JsonParser().parse(this.hoursDataString).getAsJsonObject();
        this.configureRecyclerView();


        return v;
    }

    private void configureRecyclerView() {
        this.dayHoursAdapter = new DayHoursAdapter(this.dataObject);
        this.recyclerView.setAdapter(this.dayHoursAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }
}