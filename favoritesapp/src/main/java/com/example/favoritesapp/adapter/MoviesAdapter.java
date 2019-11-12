package com.example.favoritesapp.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.favoritesapp.R;
import com.example.favoritesapp.detail.MovieDetailActivity;
import com.example.favoritesapp.model.Movies;

import java.util.ArrayList;

import static com.example.favoritesapp.contract.MovieDbContract.Columns.CONTENT_URI_MOVIE;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {


    private ArrayList<Movies> mData = new ArrayList<>();


    private ArrayList<Movies> getData() {
        return mData;
    }

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
    public void onBindViewHolder(@NonNull final MoviesAdapter.MoviesViewHolder moviesViewHolder, final int i) {
        moviesViewHolder.bind(mData.get(i));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView movieTitle, movieRelease;
        ImageView moviePhoto;
        MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.tv_movie_title);
            movieRelease = itemView.findViewById(R.id.tv_movie_release);
            moviePhoto = itemView.findViewById(R.id.image_movie);

            itemView.setOnClickListener(this);

        }

        void bind(Movies movies) {
            movieTitle.setText(movies.getMovieTitle());
            movieRelease.setText(movies.getMovieRelease());
            Glide.with(itemView.getContext()).load("https://image.tmdb.org/t/p/w185" + movies.getMoviePhoto()).into(moviePhoto);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Movies movies = mData.get(position);

            Intent intent = new Intent(itemView.getContext(), MovieDetailActivity.class);
            Uri uri = Uri.parse(CONTENT_URI_MOVIE + "/" + getData().get(position).getMovieId());
            intent.setData(uri);
            intent.putExtra(MovieDetailActivity.EXTRA_ITEM, movies);
            intent.putExtra(MovieDetailActivity.EXTRA_POSITION, position);
            v.getContext().startActivity(intent);

        }
    }
}
