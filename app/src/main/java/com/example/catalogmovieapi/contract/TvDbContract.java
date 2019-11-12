package com.example.catalogmovieapi.contract;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class TvDbContract {
    private static final String AUTHORITY = "com.example.catalogmovieapi";
    private static final String SCHEME = "content";
    public static String TABLE_TV = "tv";

    public static final class Columns implements BaseColumns {
        public static final String TV_ID = "tvId";
        public static final String TITLE = "title";
        public static final String RELEASE_DATE = "releaseDate";
        public static final String OVERVIEW = "overview";
        public static final String AVERAGE = "average";
        public static final String POSTER = "poster";
        public static final String COUNT = "count";

        public static final Uri CONTENT_URI_TV = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_TV)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }
}
