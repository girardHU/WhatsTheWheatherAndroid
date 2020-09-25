package com.example.whattheweather.Views;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whattheweather.R;

public class DayHoursViewHolder extends RecyclerView.ViewHolder {

    public ConstraintLayout parentConstraintLayout;
    public TextView hoursTextView;
    public TextView tempTextView;
    public FrameLayout dividerFrameLayout;
    public Space beginSpace;
    public Space space1;
    public Space space2;



    public DayHoursViewHolder(@NonNull View view) {
        super(view);

        this.parentConstraintLayout = view.findViewById(R.id.parentConstraintLayout);
        this.dividerFrameLayout = view.findViewById(R.id.frameLayoutSpacerHours);
        this.hoursTextView = view.findViewById(R.id.hoursTextView);
        this.tempTextView = view.findViewById(R.id.tempTextView);
        this.beginSpace = view.findViewById(R.id.beginSpace);
        this.space1 = view.findViewById(R.id.space01Hours);
        this.space2 = view.findViewById(R.id.space02Hours);
    }
}
