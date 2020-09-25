package com.example.whattheweather.Views;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whattheweather.R;

public class DaysForecastViewHolder extends RecyclerView.ViewHolder {

    public ConstraintLayout parentConstraintLayout;
    public FrameLayout dividerFrameLayout;
    public ImageView weatherImageView;
    public TextView textViewDay;
    public TextView textViewTemp;
    public View divider;
    public Space space2;
    public Space space3;



    public DaysForecastViewHolder(@NonNull View view) {
        super(view);

        this.parentConstraintLayout = view.findViewById(R.id.parentConstraintLayout);
        this.dividerFrameLayout = view.findViewById(R.id.dividerFrameLayout);
        this.weatherImageView = view.findViewById(R.id.weatherImageView);
        this.textViewDay = view.findViewById(R.id.textViewDay);
        this.textViewTemp = view.findViewById(R.id.textViewTemp);
        this.divider = view.findViewById(R.id.divider);
        this.space2 = view.findViewById(R.id.space2);
        this.space3 = view.findViewById(R.id.space3);
    }
}
