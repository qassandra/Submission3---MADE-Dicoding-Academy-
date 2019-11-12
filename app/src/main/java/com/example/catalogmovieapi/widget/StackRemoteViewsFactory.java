package com.example.catalogmovieapi.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.example.catalogmovieapi.R;
import com.example.catalogmovieapi.model.Movies;

import java.util.concurrent.ExecutionException;

import static com.bumptech.glide.request.target.Target.SIZE_ORIGINAL;
import static com.example.catalogmovieapi.contract.MovieDbContract.Columns.CONTENT_URI_MOVIE;

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context context;
    private Cursor cursor;
    private Movies movies;
    private int appWidgetId;


    public StackRemoteViewsFactory(Context applicationContext, Intent intent) {
        context = applicationContext;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        cursor = context.getContentResolver().query(CONTENT_URI_MOVIE, null
                , null, null, null);

    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) {
            cursor.close();
        }
        final long identityToken = Binder.clearCallingIdentity();

        cursor = context.getContentResolver().query(CONTENT_URI_MOVIE, null
                , null, null, null);
        Binder.restoreCallingIdentity(identityToken);

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        movies = getItem(position);
        String url = "https://image.tmdb.org/t/p/w185" + movies.getMoviePhoto();
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .into(SIZE_ORIGINAL, SIZE_ORIGINAL)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        remoteViews.setImageViewBitmap(R.id.image_widget, bitmap);

        Bundle extras = new Bundle();
        extras.putInt(FavoriteWidget.EXTRA_ITEM, position);
        Intent intent = new Intent();
        intent.putExtras(extras);

        remoteViews.setOnClickFillInIntent(R.id.image_widget, intent);
        return remoteViews;
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
    private Movies getItem(int position) {
        if (!cursor.moveToPosition(position)){
            throw new IllegalStateException("Failed");
        }
        return new Movies(cursor);
    }

}
