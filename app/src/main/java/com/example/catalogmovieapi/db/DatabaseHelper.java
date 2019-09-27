package com.example.catalogmovieapi.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.catalogmovieapi.db.MovieDbContract.TABLE_MOVIE;
import static com.example.catalogmovieapi.db.TvDbContract.TABLE_TV;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dbmoviecatalog";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_MOVIE = String.format("CREATE TABLE %s"
            + " (%s INTEGER PRIMARY KEY," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s INTEGER NOT NULL)",
            TABLE_MOVIE,
            MovieDbContract.Columns.MOVIE_ID,
            MovieDbContract.Columns.TITLE,
            MovieDbContract.Columns.RELEASE_DATE,
            MovieDbContract.Columns.OVERVIEW,
            MovieDbContract.Columns.POSTER,
            MovieDbContract.Columns.AVERAGE,
            MovieDbContract.Columns.COUNT
            );

    private static final String SQL_CREATE_TABLE_TV = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s INTEGER NOT NULL)",
            TABLE_TV,
            TvDbContract.Columns.TV_ID,
            TvDbContract.Columns.TITLE,
            TvDbContract.Columns.RELEASE_DATE,
            TvDbContract.Columns.OVERVIEW,
            TvDbContract.Columns.POSTER,
            TvDbContract.Columns.AVERAGE,
            TvDbContract.Columns.COUNT
    );



    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
        db.execSQL(SQL_CREATE_TABLE_TV);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TV);
        onCreate(db);
    }
}
