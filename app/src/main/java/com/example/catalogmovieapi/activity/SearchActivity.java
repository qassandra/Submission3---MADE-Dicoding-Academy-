package com.example.catalogmovieapi.activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.catalogmovieapi.R;
import com.example.catalogmovieapi.adapter.MoviesAdapter;
import com.example.catalogmovieapi.adapter.TvShowsAdapter;
import com.example.catalogmovieapi.model.Movies;
import com.example.catalogmovieapi.model.TvShows;
import com.example.catalogmovieapi.view.ViewModelMovie;
import com.example.catalogmovieapi.view.ViewModelTv;

import java.util.ArrayList;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {
    static final String KEYWORD = "keyword";
    static final String INDEX = "index";
    private String QUERY;
    private String LANGUAGE;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private ViewModelMovie viewModelMovie;
    private ViewModelTv viewModelTv;
    private MoviesAdapter moviesAdapter;
    private TvShowsAdapter tvShowsAdapter;
    private LinearLayout noResult;
    TextView txtNull;
    RecyclerView rvSearch;
    private final Observer<ArrayList<Movies>> getMovies = new Observer<ArrayList<Movies>>() {
        @Override
        public void onChanged(@Nullable ArrayList<Movies> movies) {
            if (movies != null){
                moviesAdapter.setData(movies);

            }
        }
    };
    private final Observer<ArrayList<TvShows>> getTv = new Observer<ArrayList<TvShows>>() {
        @Override
        public void onChanged(ArrayList<TvShows> tvShows) {
            if (tvShows != null){
                tvShowsAdapter.setData(tvShows);
                if (tvShowsAdapter.getItemCount() == 0){
                    rvSearch.setVisibility(View.GONE);
                } else {
                    showNull();
                }

            }
        }
    };

    private void showNull() {
        txtNull = findViewById(R.id.textNull);
        noResult.setVisibility(View.VISIBLE);
        txtNull.setText(("\""+QUERY + "\" " + getResources().getString(R.string.no_result)));

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_layout);
        rvSearch = findViewById(R.id.rv_search);
        noResult = findViewById(R.id.null_layout);
        QUERY = getIntent().getStringExtra(KEYWORD);
        String CATEGORY = getIntent().getStringExtra(INDEX);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (CATEGORY.equals("0")){
            setMovieModel();
            setMovieData();
            setMovieView();
        } else {
            setTvModel();
            setTvData();
            setTvView();
        }
        setToolbar();


    }

    private void setTvView() {
        tvShowsAdapter = new TvShowsAdapter();
        rvSearch.setLayoutManager(new LinearLayoutManager(this));
        rvSearch.setAdapter(tvShowsAdapter);

    }

    private void setTvData() {
        viewModelTv.searchTv(QUERY);
    }

    private void setTvModel() {
        viewModelTv = new ViewModelProvider(this).get(ViewModelTv.class);
        viewModelTv.getTv().observe(this, getTv);
    }

    private void setMovieView() {
        moviesAdapter = new MoviesAdapter();
        rvSearch.setLayoutManager(new LinearLayoutManager(this));
        rvSearch.setAdapter(moviesAdapter);

    }

    private void setMovieData() {
        viewModelMovie.searchMovie(QUERY);

    }

    private void setMovieModel() {
        viewModelMovie = new ViewModelProvider(this).get(ViewModelMovie.class);
        viewModelMovie.getMovies().observe(this, getMovies);


    }

    private void setToolbar() {
       if (getSupportActionBar() != null){
           getSupportActionBar().setTitle(getString(R.string.result) + " \""+QUERY+"\"");
           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           getSupportActionBar().setDisplayShowHomeEnabled(true);


       }

    }
}
