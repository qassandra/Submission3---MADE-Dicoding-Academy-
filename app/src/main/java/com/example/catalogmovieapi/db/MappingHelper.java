package com.example.catalogmovieapi.db;

import android.database.Cursor;

import com.example.catalogmovieapi.model.Movies;

import java.util.ArrayList;

import static com.example.catalogmovieapi.db.MovieDbContract.Columns.AVERAGE;
import static com.example.catalogmovieapi.db.MovieDbContract.Columns.COUNT;
import static com.example.catalogmovieapi.db.MovieDbContract.Columns.MOVIE_ID;
import static com.example.catalogmovieapi.db.MovieDbContract.Columns.OVERVIEW;
import static com.example.catalogmovieapi.db.MovieDbContract.Columns.POSTER;
import static com.example.catalogmovieapi.db.MovieDbContract.Columns.RELEASE_DATE;
import static com.example.catalogmovieapi.db.MovieDbContract.Columns.TITLE;

public class MappingHelper {
    public static ArrayList<Movies> mapCursorToArrayList(Cursor movieCursor) {

        ArrayList<Movies> movieList = new ArrayList<>();

        while (movieCursor.moveToNext()) {
            int movieId = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(MOVIE_ID));
            String title = movieCursor.getString(movieCursor.getColumnIndexOrThrow(TITLE));
            String releaseDate = movieCursor.getString(movieCursor.getColumnIndexOrThrow(RELEASE_DATE));
            String overview = movieCursor.getString(movieCursor.getColumnIndexOrThrow(OVERVIEW));
            String poster = movieCursor.getString(movieCursor.getColumnIndexOrThrow(POSTER));
            String average = movieCursor.getString(movieCursor.getColumnIndexOrThrow(AVERAGE));
            int count = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(COUNT));
            movieList.add(new Movies(movieId, title, releaseDate, overview, poster, average, count));
        }
        return movieList;
    }

}
