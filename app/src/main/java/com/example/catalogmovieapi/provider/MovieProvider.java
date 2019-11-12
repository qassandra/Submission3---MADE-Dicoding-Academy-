package com.example.catalogmovieapi.provider;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import androidx.annotation.NonNull;
import android.util.Log;

import com.example.catalogmovieapi.helper.MovieHelper;
import com.example.catalogmovieapi.helper.TvHelper;
import com.example.catalogmovieapi.fragments.FavoriteMovieFragment;

import java.util.Objects;

import static com.example.catalogmovieapi.contract.MovieDbContract.AUTHORITY;
import static com.example.catalogmovieapi.contract.MovieDbContract.Columns.CONTENT_URI_MOVIE;
import static com.example.catalogmovieapi.contract.MovieDbContract.TABLE_MOVIE;
import static com.example.catalogmovieapi.contract.TvDbContract.TABLE_TV;

@SuppressLint("Registered")
public class MovieProvider extends ContentProvider {
    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final int TV = 3;
    private static final int TV_ID = 4;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIE, MOVIE);
        sUriMatcher.addURI(AUTHORITY,
                TABLE_MOVIE + "/#",
                MOVIE_ID);
        sUriMatcher.addURI(AUTHORITY, TABLE_TV, TV);
        sUriMatcher.addURI(AUTHORITY,
                TABLE_TV + "/#",
                TV_ID);
    }

    private MovieHelper movieHelper;
    private TvHelper tvHelper;

    @Override
    public boolean onCreate() {
        movieHelper = MovieHelper.getInstance(getContext());
        tvHelper = TvHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(MovieProvider.class.getName(), "query: " + uri.getPath() + " uri nya " + sUriMatcher.match(uri));

        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                movieHelper.open();
                cursor = movieHelper.queryProvider();
                break;
            case MOVIE_ID:
                movieHelper.open();
                cursor = movieHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            case TV:
                tvHelper.open();
                cursor = tvHelper.queryProvider();
                break;
            case TV_ID:
                tvHelper.open();
                cursor = tvHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long added;
        if (sUriMatcher.match(uri) == MOVIE) {
            movieHelper.open();
            added = movieHelper.insertProvider(values);
        } else if (sUriMatcher.match(uri) == TV) {
            tvHelper.open();
            added = tvHelper.insertProvider(values);
        } else {
            added = 0;
        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI_MOVIE, new FavoriteMovieFragment.DataObserver(new Handler(), getContext()));

        return Uri.parse(CONTENT_URI_MOVIE + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int deleted;
        if (sUriMatcher.match(uri) == MOVIE_ID) {
            movieHelper.open();
            deleted = movieHelper.deleteProvider(uri.getLastPathSegment());
        }else if (sUriMatcher.match(uri) == TV_ID){
            tvHelper.open();
            deleted = tvHelper.deleteProvider(uri.getLastPathSegment());
        }
        else {
            deleted = 0;
        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI_MOVIE, new FavoriteMovieFragment.DataObserver(new Handler(), getContext()));

        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updated;
        if (sUriMatcher.match(uri) == MOVIE_ID) {
            movieHelper.open();
            updated = movieHelper.updateProvider(uri.getLastPathSegment(), values);
        } else if (sUriMatcher.match(uri) == TV_ID){
            tvHelper.open();
            updated = tvHelper.updateProvider(uri.getLastPathSegment(), values);
        }
        else {
            updated = 0;
        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI_MOVIE, new FavoriteMovieFragment.DataObserver(new Handler(), getContext()));

        return updated;
    }
}
