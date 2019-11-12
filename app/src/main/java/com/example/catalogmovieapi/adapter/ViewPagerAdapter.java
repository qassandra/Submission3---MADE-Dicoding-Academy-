package com.example.catalogmovieapi.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
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
