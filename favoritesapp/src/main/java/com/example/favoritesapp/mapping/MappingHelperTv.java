package com.example.favoritesapp.mapping;

import android.database.Cursor;

import com.example.favoritesapp.model.TvShows;

import java.util.ArrayList;

import static com.example.favoritesapp.contract.TvDbContract.Columns.AVERAGE;
import static com.example.favoritesapp.contract.TvDbContract.Columns.COUNT;
import static com.example.favoritesapp.contract.TvDbContract.Columns.OVERVIEW;
import static com.example.favoritesapp.contract.TvDbContract.Columns.POSTER;
import static com.example.favoritesapp.contract.TvDbContract.Columns.RELEASE_DATE;
import static com.example.favoritesapp.contract.TvDbContract.Columns.TITLE;
import static com.example.favoritesapp.contract.TvDbContract.Columns.TV_ID;

public class MappingHelperTv {
    public static ArrayList<TvShows> mapCursorArrayListTv(Cursor tvCursor){
        ArrayList<TvShows> tvShows = new ArrayList<>();

        while (tvCursor.moveToNext()){
            int tvId = tvCursor.getInt(tvCursor.getColumnIndexOrThrow(TV_ID));
            String title = tvCursor.getString(tvCursor.getColumnIndexOrThrow(TITLE));
            String releaseDate = tvCursor.getString(tvCursor.getColumnIndexOrThrow(RELEASE_DATE));
            String overview = tvCursor.getString(tvCursor.getColumnIndexOrThrow(OVERVIEW));
            String poster = tvCursor.getString(tvCursor.getColumnIndexOrThrow(POSTER));
            String average = tvCursor.getString(tvCursor.getColumnIndexOrThrow(AVERAGE));
            int count = tvCursor.getInt(tvCursor.getColumnIndexOrThrow(COUNT));
            tvShows.add(new TvShows(tvId, title, releaseDate, overview, poster, average, count));
        }

        return tvShows;
    }
}
