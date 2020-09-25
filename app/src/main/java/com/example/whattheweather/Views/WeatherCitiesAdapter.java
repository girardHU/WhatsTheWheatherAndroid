package com.example.whattheweather.Views;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.whattheweather.Fragments.CitiesFragment;
import com.example.whattheweather.Fragments.WeatherFragment;

public class WeatherCitiesAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    public static Fragment[] fragments;

    public WeatherCitiesAdapter(FragmentManager fm, int numOfTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numOfTabs = numOfTabs;
        fragments = new Fragment[numOfTabs];
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new WeatherFragment();
            case 1:
                return new CitiesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        fragments[position]  = createdFragment;
        return createdFragment;
    }
}
