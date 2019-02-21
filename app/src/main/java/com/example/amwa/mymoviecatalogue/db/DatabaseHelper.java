package com.example.amwa.mymoviecatalogue.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.LANGUAGE;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.OVERVIEW;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.POSTER;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.RATING;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.TITLE;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.TABLE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "dbmoviecatalogue";
    private static  final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_NOTE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL);",
            TABLE_NAME,
            _ID,
            TITLE,
            OVERVIEW,
            RATING,
            POSTER,
            LANGUAGE,
            RELEASE_DATE
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
