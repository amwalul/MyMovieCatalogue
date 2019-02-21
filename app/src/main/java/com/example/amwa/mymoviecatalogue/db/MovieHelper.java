package com.example.amwa.mymoviecatalogue.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.amwa.mymoviecatalogue.entity.Movie;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.LANGUAGE;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.OVERVIEW;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.POSTER;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.RATING;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.TITLE;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.TABLE_NAME;

public class MovieHelper {
    private static String DATABASE_TABLE = TABLE_NAME;
    private Context context;
    private DatabaseHelper dataBaseHelper;
    private SQLiteDatabase database;

    public MovieHelper(Context context) {
        this.context = context;
    }

    public MovieHelper open() throws SQLException {
        dataBaseHelper = new DatabaseHelper(context);
        database = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dataBaseHelper.close();
    }

    public ArrayList<Movie> query() {
        ArrayList<Movie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null, null, null, null, null, _ID + " DESC", null);
        cursor.moveToFirst();
        Movie movie;
        if (cursor.getCount() > 0) {
            do {
                movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                movie.setRating(cursor.getString(cursor.getColumnIndexOrThrow(RATING)));
                movie.setPoster(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
                movie.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(LANGUAGE)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));

                arrayList.add(movie);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(Movie movie) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(TITLE, movie.getTitle());
        initialValues.put(OVERVIEW, movie.getOverview());
        initialValues.put(RATING, movie.getRating());
        initialValues.put(POSTER, movie.getPoster());
        initialValues.put(LANGUAGE, movie.getLanguage());
        initialValues.put(RELEASE_DATE, movie.getReleaseDate());
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public int update(Movie movie) {
        ContentValues args = new ContentValues();
        args.put(TITLE, movie.getTitle());
        args.put(OVERVIEW, movie.getOverview());
        args.put(RATING, movie.getRating());
        args.put(POSTER, movie.getPoster());
        args.put(LANGUAGE, movie.getLanguage());
        args.put(RELEASE_DATE, movie.getReleaseDate());
        return database.update(DATABASE_TABLE, args, _ID + "= '" + movie.getId() + "'", null);
    }

    public int delete(int id) {
        return database.delete(TABLE_NAME, _ID + " = '" + id + "'", null);
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null, _ID + " = ?", new String[]{id}, null, null, null, null);
    }

    public Cursor queryProvider(){
        return database.query(DATABASE_TABLE,null,null,null,null,null,_ID + " DESC");
    }

    public long insertProvider(ContentValues values){
        return database.insert(DATABASE_TABLE,null, values);
    }

    public int updateProvider(String id,ContentValues values){
        return database.update(DATABASE_TABLE,values,_ID +" = ?",new String[]{id} );
    }

    public int deleteProvider(String id){
        return database.delete(DATABASE_TABLE,_ID + " = ?", new String[]{id});
    }
}
