package com.example.catalogmovieapi.fragments;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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

import com.example.catalogmovieapi.adapter.MoviesAdapter;
import com.example.catalogmovieapi.model.Movies;
import com.example.catalogmovieapi.R;
import com.example.catalogmovieapi.view.ViewModelMovie;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {
    private MoviesAdapter adapter;
    private RecyclerView rvMovies;
    public ProgressBar progressBar;
    ViewModelMovie viewModelMovie;

    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        adapter = new MoviesAdapter();
        rvMovies = view.findViewById(R.id.movie_category);
        rvMovies.setLayoutManager(new LinearLayoutManager(this.getContext()));

        rvMovies.setHasFixedSize(true);
        progressBar = view.findViewById(R.id.progressBar);

        viewModelMovie = new ViewModelProvider(this).get(ViewModelMovie.class);
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
            if (movies.size() > 0){
                adapter.setData(movies);
                rvMovies.setAdapter(adapter);
                showLoading(false);
            }

        }
    };

    public void showLoading(boolean b) {
        if (b) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }


    private void loadMovies(){

    }
}
