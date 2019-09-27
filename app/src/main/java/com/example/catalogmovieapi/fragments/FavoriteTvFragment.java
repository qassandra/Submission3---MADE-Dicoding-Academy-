package com.example.catalogmovieapi.fragments;


import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.catalogmovieapi.LoadTvCallback;
import com.example.catalogmovieapi.R;
import com.example.catalogmovieapi.adapter.TvShowsAdapter;
import com.example.catalogmovieapi.model.TvShows;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.catalogmovieapi.db.MappingHelperTv.mapCursorArrayListTv;
import static com.example.catalogmovieapi.db.TvDbContract.Columns.CONTENT_URI_TV;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteTvFragment extends Fragment implements LoadTvCallback {
    private ProgressBar progressBar;
    private TvShowsAdapter adapter;
    private static final String EXTRA_STATE = "EXTRA_STATE";

    public FavoriteTvFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_tv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar);
        RecyclerView rvFavorites = view.findViewById(R.id.tv_category);
        rvFavorites.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFavorites.setHasFixedSize(true);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, getContext());
        Objects.requireNonNull(getActivity()).getContentResolver().registerContentObserver(CONTENT_URI_TV, true, myObserver);

        adapter = new TvShowsAdapter();
        rvFavorites.setAdapter(adapter);

        if (savedInstanceState == null) {
            new LoadTvAsync(getContext(), this).execute();
        } else {
            ArrayList<TvShows> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setData(list);
            }
        }
    }

    @Override
    public void preExecute() {
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void postExecute(Cursor tvShows) {
        progressBar.setVisibility(View.INVISIBLE);

        ArrayList<TvShows> listTv = mapCursorArrayListTv(tvShows);
        if (listTv.size() > 0) {
            adapter.setData(listTv);
        } else {
            adapter.setData(new ArrayList<TvShows>());
        }

    }

    private static class LoadTvAsync extends AsyncTask<Void, Void, Cursor> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadTvCallback> weakCallback;

        private LoadTvAsync(Context context, LoadTvCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(CONTENT_URI_TV, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor tvShows) {
            super.onPostExecute(tvShows);
            weakCallback.get().postExecute(tvShows);
        }
    }

    public static class DataObserver extends ContentObserver {
        final Context context;
        DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadTvAsync(getContext(), this).execute();
    }
}
