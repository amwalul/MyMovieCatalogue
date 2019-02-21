package com.example.amwa.mymoviecatalogue.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.example.amwa.mymoviecatalogue";
    public static final String SCHEME = "content";
    public static final String TABLE_NAME = "favorite";

    public static final class MovieColumns implements BaseColumns {
        public static final String TITLE = "title";
        public static final String OVERVIEW = "overview";
        public static final String RATING = "rating";
        public static final String POSTER = "poster";
        public static final String LANGUAGE = "language";
        public static final String RELEASE_DATE = "release_date";
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
}
