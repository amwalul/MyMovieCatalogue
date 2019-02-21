package com.example.amwa.mymoviecatalogue.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static android.provider.BaseColumns._ID;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.LANGUAGE;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.OVERVIEW;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.POSTER;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.RATING;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.TITLE;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.getColumnInt;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.getColumnString;

public class Movie implements Parcelable {
    private int id;
    private String title, rating, overview, poster, language, releaseDate;

    public Movie() {
    }

    public Movie(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.title = getColumnString(cursor, TITLE);
        this.overview = getColumnString(cursor, OVERVIEW);
        this.rating = getColumnString(cursor, RATING);
        this.poster = getColumnString(cursor, POSTER);
        this.language = getColumnString(cursor, LANGUAGE);
        this.releaseDate = getColumnString(cursor, RELEASE_DATE);
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        rating = in.readString();
        overview = in.readString();
        poster = in.readString();
        language = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(rating);
        dest.writeString(overview);
        dest.writeString(poster);
        dest.writeString(language);
        dest.writeString(releaseDate);
    }
}
