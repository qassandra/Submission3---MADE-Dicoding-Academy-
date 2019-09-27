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

import com.example.catalogmovieapi.LoadMoviesCallback;
import com.example.catalogmovieapi.R;
import com.example.catalogmovieapi.adapter.MoviesAdapter;
import com.example.catalogmovieapi.model.Movies;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.catalogmovieapi.db.MappingHelper.mapCursorToArrayList;
import static com.example.catalogmovieapi.db.MovieDbContract.Columns.CONTENT_URI_MOVIE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMovieFragment extends Fragment implements LoadMoviesCallback {
    private ProgressBar progressBar;
    private MoviesAdapter movieAdapter;
    private static final String EXTRA_STATE = "EXTRA_STATE";


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar);
        RecyclerView rvFavorites = view.findViewById(R.id.movie_category);
        rvFavorites.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFavorites.setHasFixedSize(true);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, getContext());
        Objects.requireNonNull(getActivity()).getContentResolver().registerContentObserver(CONTENT_URI_MOVIE, true, myObserver);

        movieAdapter = new MoviesAdapter();
        rvFavorites.setAdapter(movieAdapter);

        if (savedInstanceState == null) {
            new LoadMoviesAsync(getContext(), this).execute();
        } else {
            ArrayList<Movies> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                movieAdapter.setData(list);
            }
        }
    }

    public FavoriteMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false);
    }
    private static class LoadMoviesAsync extends AsyncTask<Void, Void, Cursor> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMoviesCallback> weakCallback;

        private LoadMoviesAsync(Context context, LoadMoviesCallback callback) {
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
            return context.getContentResolver().query(CONTENT_URI_MOVIE, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor movies) {
            super.onPostExecute(movies);
            weakCallback.get().postExecute(movies);
        }
    }

    @Override
    public void preExecute() {
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void postExecute(Cursor movies) {
        progressBar.setVisibility(View.INVISIBLE);

        ArrayList<Movies> listNotes = mapCursorToArrayList(movies);
        if (listNotes.size() > 0) {
            movieAdapter.setData(listNotes);
        } else {
            movieAdapter.setData(new ArrayList<Movies>());
        }
    }

    public static class DataObserver extends ContentObserver {
        final Context context;
        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadMoviesAsync(getContext(), this).execute();
    }
}
