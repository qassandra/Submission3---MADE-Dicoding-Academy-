package com.example.catalogmovieapi.ItemFragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.catalogmovieapi.Adapter.MoviesAdapter;
import com.example.catalogmovieapi.Adapter.TvShowsAdapter;
import com.example.catalogmovieapi.Model.TvShows;
import com.example.catalogmovieapi.R;
import com.example.catalogmovieapi.ViewModel.ViewModelTv;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowsFragment extends Fragment {
    private TvShowsAdapter adapter;
    private ProgressBar progressBar;



    public TvShowsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tv_shows,container,false);
        getActivity().setTitle(R.string.tv_shows);
        adapter = new TvShowsAdapter(this);
        RecyclerView rvTv = view.findViewById(R.id.tv_category);
        rvTv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rvTv.setAdapter(adapter);
        rvTv.setHasFixedSize(true);

        progressBar = view.findViewById(R.id.progressBarTv);

        ViewModelTv viewModelTv = ViewModelProviders.of(this).get(ViewModelTv.class);
        viewModelTv.getTv().observe(this, getTelevision);
        viewModelTv.setListTv();
        showLoading(true);
        return view;
    }

    private void showLoading(boolean b) {
        if (b){
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private Observer<ArrayList<TvShows>> getTelevision = new Observer<ArrayList<TvShows>>() {
        @Override
        public void onChanged(@Nullable ArrayList<TvShows> tvShows) {
            if (tvShows != null){
                adapter.setData(tvShows);
            }
            showLoading(false);
        }
    };

}
