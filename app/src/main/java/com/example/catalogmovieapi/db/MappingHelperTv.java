package com.example.catalogmovieapi.db;

import android.database.Cursor;

import com.example.catalogmovieapi.model.TvShows;

import java.util.ArrayList;

import static com.example.catalogmovieapi.db.TvDbContract.Columns.AVERAGE;
import static com.example.catalogmovieapi.db.TvDbContract.Columns.COUNT;
import static com.example.catalogmovieapi.db.TvDbContract.Columns.OVERVIEW;
import static com.example.catalogmovieapi.db.TvDbContract.Columns.POSTER;
import static com.example.catalogmovieapi.db.TvDbContract.Columns.RELEASE_DATE;
import static com.example.catalogmovieapi.db.TvDbContract.Columns.TITLE;
import static com.example.catalogmovieapi.db.TvDbContract.Columns.TV_ID;

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
