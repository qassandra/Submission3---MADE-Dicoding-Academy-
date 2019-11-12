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
import com.example.favoritesapp.detail.TvDetailActivity;
import com.example.favoritesapp.model.TvShows;

import java.util.ArrayList;

import static com.example.favoritesapp.contract.TvDbContract.Columns.CONTENT_URI_TV;

public class TvShowsAdapter extends RecyclerView.Adapter<TvShowsAdapter.TvShowsViewHolder> {
    private ArrayList<TvShows> mData = new ArrayList<>();

    public void setData(ArrayList<TvShows> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TvShowsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tv, viewGroup, false);
        return new TvShowsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowsAdapter.TvShowsViewHolder tvShowsViewHolder, final int i) {
        tvShowsViewHolder.bind(mData.get(i));


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    class TvShowsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, release;
        ImageView poster;
        TvShowsViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_movie_title);
            release = itemView.findViewById(R.id.tv_movie_release);
            poster = itemView.findViewById(R.id.image_movie);

            itemView.setOnClickListener(this);
        }

        void bind(TvShows tv){
            title.setText(tv.getTvTitle());
            release.setText(tv.getTvRelease());
            Glide.with(itemView.getContext()).load("https://image.tmdb.org/t/p/w185" + tv.getTvPoster()).into(poster);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            TvShows tvShows = mData.get(position);

            Intent intent = new Intent(itemView.getContext(), TvDetailActivity.class);
            Uri uri = Uri.parse(CONTENT_URI_TV + "/" + getData().get(position).getId());
            intent.setData(uri);
            intent.putExtra(TvDetailActivity.EXTRA_ITEM, tvShows);
            intent.putExtra(TvDetailActivity.EXTRA_POSITION, position);
            v.getContext().startActivity(intent);

        }
    }

    private ArrayList<TvShows> getData() {
        return mData;
    }
}
