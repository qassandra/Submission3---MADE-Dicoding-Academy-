package com.example.catalogmovieapi.DetailFragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.catalogmovieapi.Model.TvShows;
import com.example.catalogmovieapi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailTvFragments extends Fragment {
    public static String EXTRA_TV = "extra_tv";
    TextView title, releaseDate, tvVoteAverage, tvVoteCount, overview, tvDesc, voteAverage, votedBy, people;
    ImageView poster;
    private ProgressBar progressBar;
    public DetailTvFragments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_tv_fragments, container, false);

        title = view.findViewById(R.id.tv_show_title);
        releaseDate = view.findViewById(R.id.tv_show_release);
        tvVoteAverage = view.findViewById(R.id.tv_show_average);
        tvVoteCount = view.findViewById(R.id.tv_movie_number);
        poster = view.findViewById(R.id.image_tv);
        tvDesc = view.findViewById(R.id.tv_show_desc);
        overview = view.findViewById(R.id.overview);
        voteAverage = view.findViewById(R.id.tv_show_voteAverage);
        votedBy = view.findViewById(R.id.tv_show_votedBy);
        people = view.findViewById(R.id.tv_show_people);

        progressBar = view.findViewById(R.id.progressBarDetailTv);
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
                        TvShows tvShows = null;
                        if (getArguments() != null) {
                            tvShows = getArguments().getParcelable(EXTRA_TV);
                        }

                        String url = "https://image.tmdb.org/t/p/w185" + tvShows.getTvPoster();
                        String number = Integer.toString(tvShows.getTvVoteCount());

                        title.setText(tvShows.getTvTitle());
                        releaseDate.setText(tvShows.getTvRelease());
                        tvVoteAverage.setText(tvShows.getTvVote());
                        tvVoteCount.setText(number);
                        tvDesc.setText(tvShows.getTvOverview());
                        Glide.with(DetailTvFragments.this).load(url).into(poster);

                        overview.setText(R.string.overview);
                        voteAverage.setText(R.string.votes);
                        votedBy.setText(R.string.voted);
                        people.setText(R.string.people);

                        getActivity().setTitle(tvShows.getTvTitle());

                        progressBar.setVisibility(View.INVISIBLE);

                    }
                });
            }
        }).start();

        return view;
    }

}
