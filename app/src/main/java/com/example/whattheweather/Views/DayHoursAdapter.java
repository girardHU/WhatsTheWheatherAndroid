package com.example.whattheweather.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whattheweather.R;
import com.google.gson.JsonObject;

public class DayHoursAdapter extends RecyclerView.Adapter<DayHoursViewHolder> {

    private JsonObject datas;

    public DayHoursAdapter(JsonObject list) {
        this.datas = list;
        System.out.println("DAYHOURADAPTER : " + this.datas);
    }

    @NonNull
    @Override
    public DayHoursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_hours, parent, false);
        return new DayHoursViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayHoursViewHolder holder, int position) {
        holder.hoursTextView.setText(this.datas.get(String.valueOf(position)).getAsJsonObject().get("hour").getAsString() + "H");
        holder.tempTextView.setText(this.datas.get(String.valueOf(position)).getAsJsonObject().get("temperature").getAsInt() + "°C");
        if (position == getItemCount() - 1) {
            holder.dividerFrameLayout.setVisibility(View.GONE);
        } else if (position == 0) {
            holder.beginSpace.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return this.datas.size();
    }
}
