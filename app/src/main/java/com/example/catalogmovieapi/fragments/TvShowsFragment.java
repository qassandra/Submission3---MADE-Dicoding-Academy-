package com.example.catalogmovieapi.fragments;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.catalogmovieapi.adapter.TvShowsAdapter;
import com.example.catalogmovieapi.model.TvShows;
import com.example.catalogmovieapi.R;
import com.example.catalogmovieapi.view.ViewModelTv;

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
        adapter = new TvShowsAdapter();
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
