package com.example.catalogmovieapi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.catalogmovieapi.db.TvDbContract.Columns.TV_ID;
import static com.example.catalogmovieapi.db.TvDbContract.TABLE_TV;

public class TvHelper {
    private static final String DATABASE_TABLE = TABLE_TV;
    private final DatabaseHelper databaseHelper;

    private static TvHelper INSTANCE;

    private SQLiteDatabase database;

    private TvHelper(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public static TvHelper getInstance(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new TvHelper(context);
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
                , TV_ID + " = ?"
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
                , TV_ID + " ASC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, TV_ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, TV_ID + " = ?", new String[]{id});
    }
}
