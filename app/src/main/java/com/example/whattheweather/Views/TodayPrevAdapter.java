package com.example.whattheweather.Views;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.whattheweather.Fragments.PrevisionFragment;
import com.example.whattheweather.Fragments.TodayFragment;

import org.json.JSONObject;

public class TodayPrevAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    private JSONObject hourlyForecast;
    private JSONObject DailyForecast;
    public static Fragment[] fragments;

    public TodayPrevAdapter(FragmentManager fm, int numOfTabs, JSONObject hourlyForecast, JSONObject DailyForecast) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numOfTabs = numOfTabs;
        fragments = new Fragment[numOfTabs];
        this.hourlyForecast = hourlyForecast;
        this.DailyForecast = DailyForecast;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return TodayFragment.newInstance(this.hourlyForecast.toString());
            case 1:
                return PrevisionFragment.newInstance(this.DailyForecast.toString());
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

    public static Fragment[] getFragments() {
        return fragments;
    }
}
