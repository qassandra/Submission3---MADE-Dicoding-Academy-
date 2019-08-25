package com.example.catalogmovieapi.DetailFragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.catalogmovieapi.Model.Movies;
import com.example.catalogmovieapi.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailMovieFragment extends Fragment {
    public static final String EXTRA_MOVIES = "extra_movies";
    private TextView movieTitle, movieRelease, movieAverage, moviePeople, movieDesc, overview, voteAverage, voteCount, tvPeople;
    private ImageView moviePoster;

    private ProgressBar progressBar;


    public DetailMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_movie, container, false);
        movieTitle = view.findViewById(R.id.tv_show_title);
        movieRelease = view.findViewById(R.id.tv_show_release);
        movieAverage = view.findViewById(R.id.tv_show_average);
        moviePeople = view.findViewById(R.id.tv_movie_number);
        moviePoster = view.findViewById(R.id.image_tv);
        movieDesc = view.findViewById(R.id.tv_show_desc);

        overview = view.findViewById(R.id.overview);
        voteAverage = view.findViewById(R.id.tv_movie_voteAverage);
        voteCount = view.findViewById(R.id.tv_movie_votedBy);
        tvPeople = view.findViewById(R.id.tv_movie_people);

        progressBar = view.findViewById(R.id.progressBarDetail);
        progressBar.setVisibility(View.VISIBLE);

        final Handler handler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Movies movies = null;
                        if (getArguments() != null) {
                            movies = getArguments().getParcelable(EXTRA_MOVIES);
                        }

                        String url = "https://image.tmdb.org/t/p/w185" + movies.getMoviePhoto();
                        String people = Integer.toString(movies.getMoviePeople());

                        movieTitle.setText(movies.getMovieTitle());
                        movieRelease.setText(movies.getMovieRelease());
                        movieAverage.setText(movies.getMovieAverage());
                        moviePeople.setText(people);
                        movieDesc.setText(movies.getMovieDescription());
                        Glide.with(DetailMovieFragment.this).load(url).into(moviePoster);

                        overview.setText(R.string.overview);
                        voteAverage.setText(R.string.votes);
                        voteCount.setText(R.string.voted);
                        tvPeople.setText(R.string.people);

                        getActivity().setTitle(movies.getMovieTitle());

                        progressBar.setVisibility(View.INVISIBLE);

                    }
                });
            }
        }).start();
        return view;
    }

}
