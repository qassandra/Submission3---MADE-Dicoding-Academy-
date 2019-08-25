package com.example.catalogmovieapi.Adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.catalogmovieapi.DetailFragments.DetailMovieFragment;
import com.example.catalogmovieapi.Model.Movies;
import com.example.catalogmovieapi.ItemFragments.MoviesFragment;
import com.example.catalogmovieapi.R;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private MoviesFragment context;

    public MoviesAdapter(MoviesFragment context) {
        this.context = context;
    }

    private ArrayList<Movies> mData = new ArrayList<>();

    public void setData(ArrayList<Movies> items){
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        return new MoviesViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.MoviesViewHolder moviesViewHolder, final int i) {
        moviesViewHolder.bind(mData.get(i));

        moviesViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.item_movie){
                    DetailMovieFragment detailMovieFragment = new DetailMovieFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(DetailMovieFragment.EXTRA_MOVIES, mData.get(i));
                    detailMovieFragment.setArguments(bundle);
                    FragmentManager fragmentManager = context.getFragmentManager();
                    if (fragmentManager != null){
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.container_layout, detailMovieFragment, DetailMovieFragment.class.getSimpleName());
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder {
        TextView movieTitle, movieRelease;
        ImageView moviePhoto;
        MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.tv_show_title);
            movieRelease = itemView.findViewById(R.id.tv_show_release);
            moviePhoto = itemView.findViewById(R.id.image_tv);

        }

        void bind(Movies movies) {
            movieTitle.setText(movies.getMovieTitle());
            movieRelease.setText(movies.getMovieRelease());
            Glide.with(itemView.getContext()).load("https://image.tmdb.org/t/p/w185" + movies.getMoviePhoto()).into(moviePhoto);
        }

    }
}
