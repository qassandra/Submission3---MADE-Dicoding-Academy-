package com.example.catalogmovieapi.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.example.catalogmovieapi.fragments.MoviesFragment;
import com.example.catalogmovieapi.fragments.TvShowsFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private int number;
    public ViewPagerAdapter(FragmentManager fm, int number) {
        super(fm);
        this.number = number;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MoviesFragment();
            case 1:
                return new TvShowsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return number;
    }
}
