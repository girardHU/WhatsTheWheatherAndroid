package com.example.whattheweather.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whattheweather.R;
import com.example.whattheweather.Views.DaysForecastAdapter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrevisionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrevisionFragment extends Fragment {

    private static final String ARG_DATASTRING = "ARG_DATASTRING";

    private String dataString;
    private JsonObject dataObject;

    private RecyclerView recyclerView;
    private DaysForecastAdapter daysForecastAdapter;


    public PrevisionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param dataString Parameter 1.
     * @return A new instance of fragment PrevisionFragment.
     */
    public static PrevisionFragment newInstance(String dataString) {
        PrevisionFragment fragment = new PrevisionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATASTRING, dataString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dataString = getArguments().getString(ARG_DATASTRING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_prevision, container, false);

        this.recyclerView = v.findViewById(R.id.daysForecastRecyclerView);
        this.dataObject = new JsonParser().parse(this.dataString).getAsJsonObject();
        this.configureRecyclerView();


        return v;
    }

    private void configureRecyclerView() {
        this.daysForecastAdapter = new DaysForecastAdapter(this.dataObject);
        this.recyclerView.setAdapter(this.daysForecastAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }
}