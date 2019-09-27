package com.example.catalogmovieapi;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.example.catalogmovieapi.db.MovieDbContract;
import com.example.catalogmovieapi.db.MovieHelper;
import com.example.catalogmovieapi.model.Movies;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<Movies> list = new ArrayList<>();
    private MovieHelper helper;
    private Context context;
    private Movies movies;
    private int appWidgetId;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        helper = new MovieHelper(context);
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        getMovie();
    }

    @Override
    public void onDataSetChanged() {
        getMovie();

    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d(StackRemoteViewsFactory.class.getSimpleName(), "getViewAt: " + getCount());
        if (getCount() > 0)
        {
            Movies favoriteMovie = list.get(position);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_item);

            views.setTextViewText(R.id.movie_title, movies.getMovieTitle());
            String url = movies.getMoviePhoto() != null ? String.format("https://image.tmdb.org/t/p/w185", movies.getMoviePhoto()) : null;
            Bitmap bitmap = null;
            try {
            bitmap = Glide.with(context).asBitmap().load(url).submit().get();
            } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            }

            Bundle extras = new Bundle();
            extras.putInt(MovieWidget.INTENT_ITEM, movies.getMovieId());

            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            views.setImageViewBitmap(R.id.image_movie, bitmap);
            views.setTextViewText(R.id.movie_title, favoriteMovie.getMovieTitle());
            views.setOnClickFillInIntent(R.id.image_movie, fillInIntent);
            return views;
        }
        return null;
    }

    private void getMovie() {
        helper.open();
        list = helper.queryAll();
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
