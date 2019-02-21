package com.example.amwa.mymoviecatalogue.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class MovieItems implements Parcelable {
    private String title, rating, overview, poster, language, releaseDate;

    public MovieItems(JSONObject object) {
        try {
            this.title = object.getString("title");
            this.overview = object.getString("overview");
            this.rating = object.getString("vote_average");
            this.poster = object.getString("poster_path");
            this.language = object.getString("original_language");
            this.releaseDate = object.getString("release_date");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected MovieItems(Parcel in) {
        title = in.readString();
        rating = in.readString();
        overview = in.readString();
        poster = in.readString();
        language = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<MovieItems> CREATOR = new Creator<MovieItems>() {
        @Override
        public MovieItems createFromParcel(Parcel in) {
            return new MovieItems(in);
        }

        @Override
        public MovieItems[] newArray(int size) {
            return new MovieItems[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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
        dest.writeString(title);
        dest.writeString(rating);
        dest.writeString(overview);
        dest.writeString(poster);
        dest.writeString(language);
        dest.writeString(releaseDate);
    }
}
