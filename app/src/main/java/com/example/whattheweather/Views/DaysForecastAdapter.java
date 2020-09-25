package com.example.whattheweather.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whattheweather.R;
import com.google.gson.JsonObject;

public class DaysForecastAdapter extends RecyclerView.Adapter<DaysForecastViewHolder> {

    private JsonObject datas;

    public DaysForecastAdapter(JsonObject list) {
        this.datas = list;
    }

    @NonNull
    @Override
    public DaysForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_prevision, parent, false);
        return new DaysForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DaysForecastViewHolder holder, int position) {
        holder.weatherImageView.setImageResource(getWeatherResource(this.datas.get(String.valueOf(position + 1)).getAsJsonObject().get("weather").getAsString()));
        holder.textViewDay.setText("J + " + position + 1);
        holder.textViewTemp.setText(this.datas.get(String.valueOf(position + 1)).getAsJsonObject().get("temperature").getAsString());
        if (position == 3) {
            holder.divider.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return this.datas.size();
    }

    private int getWeatherResource(String weatherString) {
        switch(weatherString) {
            case "Rain":
                return R.drawable.rain;
            case "Clouds":
                return R.drawable.cloud;
            case "Clear":
                return R.drawable.sun;
            case "Storm":
                return R.drawable.storm;
            case "Snow":
                return R.drawable.snow;
            default:
                return R.drawable.question_mark;
        }
    }
}
