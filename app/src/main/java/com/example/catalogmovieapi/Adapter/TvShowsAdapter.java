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
import com.example.catalogmovieapi.DetailFragments.DetailTvFragments;
import com.example.catalogmovieapi.ItemFragments.TvShowsFragment;
import com.example.catalogmovieapi.Model.TvShows;
import com.example.catalogmovieapi.R;

import java.util.ArrayList;

public class TvShowsAdapter extends RecyclerView.Adapter<TvShowsAdapter.TvShowsViewHolder> {
    private ArrayList<TvShows> mData = new ArrayList<>();

    private TvShowsFragment context;

    public TvShowsAdapter(TvShowsFragment context) {
        this.context = context;
    }

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

        tvShowsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.item_tv){
                    DetailTvFragments detailTvFragments = new DetailTvFragments();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(DetailTvFragments.EXTRA_TV, mData.get(i));
                    detailTvFragments.setArguments(bundle);
                    FragmentManager fragmentManager = context.getFragmentManager();
                    if (fragmentManager != null){
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.container_layout, detailTvFragments, DetailTvFragments.class.getSimpleName());
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
    class TvShowsViewHolder extends RecyclerView.ViewHolder {
        TextView title, release;
        ImageView poster;
        TvShowsViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_show_title);
            release = itemView.findViewById(R.id.tv_show_release);
            poster = itemView.findViewById(R.id.image_tv);
        }

        void bind(TvShows tv){
            title.setText(tv.getTvTitle());
            release.setText(tv.getTvRelease());
            Glide.with(itemView.getContext()).load("https://image.tmdb.org/t/p/w185" + tv.getTvPoster()).into(poster);

        }
    }
}
