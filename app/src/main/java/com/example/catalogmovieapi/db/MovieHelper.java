package com.example.catalogmovieapi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.catalogmovieapi.model.Movies;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.catalogmovieapi.db.MovieDbContract.Columns.MOVIE_ID;
import static com.example.catalogmovieapi.db.MovieDbContract.TABLE_MOVIE;

public class MovieHelper {
    private static final String DATABASE_TABLE = TABLE_MOVIE;
    private final DatabaseHelper databaseHelper;

    private static MovieHelper INSTANCE;

    private SQLiteDatabase database;

    private final Context mContext;

    public MovieHelper(Context context) {
        mContext = context;
        this.databaseHelper = new DatabaseHelper(context);
    }

    public ArrayList<Movies> queryAll(){
        ArrayList<Movies> list = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(
                MovieDbContract.Columns.CONTENT_URI_MOVIE,
                null, null, null, null);

        if (cursor != null){
            if (cursor.getCount() > 0 ){
                cursor.moveToFirst();
                do {
                    list.add(new Movies(cursor));
                    cursor.moveToNext();
                } while (!cursor.isAfterLast());
            }
            cursor.close();
        }
        return list;
    }

    public static MovieHelper getInstance(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new MovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null
                , MOVIE_ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , MOVIE_ID + " ASC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, MOVIE_ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, MOVIE_ID + " = ?", new String[]{id});
    }
}
