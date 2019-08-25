package com.example.catalogmovieapi.ItemFragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.catalogmovieapi.Adapter.MoviesAdapter;
import com.example.catalogmovieapi.Model.Movies;
import com.example.catalogmovieapi.R;
import com.example.catalogmovieapi.ViewModel.ViewModelMovie;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {
    private MoviesAdapter adapter;
    public ProgressBar progressBar;

    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        getActivity().setTitle(R.string.movies);
        adapter = new MoviesAdapter(this);
        RecyclerView rvMovies = view.findViewById(R.id.movie_category);
        rvMovies.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rvMovies.setAdapter(adapter);
        rvMovies.setHasFixedSize(true);
        progressBar = view.findViewById(R.id.progressBar);

        ViewModelMovie viewModelMovie = ViewModelProviders.of(this).get(ViewModelMovie.class);
        viewModelMovie.getMovies().observe(this, getMovie);
        viewModelMovie.setMovies();
        showLoading(true);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private Observer<ArrayList<Movies>> getMovie = new Observer<ArrayList<Movies>>() {
        @Override
        public void onChanged(@Nullable ArrayList<Movies> movies) {
            if (movies != null){
                adapter.setData(movies);
            }
            showLoading(false);
        }
    };

    public void showLoading(boolean b) {
        if (b) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

}
