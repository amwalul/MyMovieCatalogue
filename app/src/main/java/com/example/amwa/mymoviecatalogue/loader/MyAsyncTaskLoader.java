package com.example.amwa.mymoviecatalogue.loader;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import com.example.amwa.mymoviecatalogue.BuildConfig;
import com.example.amwa.mymoviecatalogue.entity.MovieItems;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MyAsyncTaskLoader extends AsyncTaskLoader<ArrayList<MovieItems>> {
    private ArrayList<MovieItems> mData;
    private boolean mHasResult = false;
    private String title;
    private String fragmentName;
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;

    public MyAsyncTaskLoader(final Context context, String fragmentName, String title) {
        super(context);

        onContentChanged();
        this.title = title;
        this.fragmentName = fragmentName;
    }

    public MyAsyncTaskLoader(final Context context, String fragmentName) {
        super(context);

        onContentChanged();
        this.fragmentName = fragmentName;
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged()) forceLoad();
        else if (mHasResult) deliverResult(mData);
    }

    @Override
    public void deliverResult(@Nullable ArrayList<MovieItems> data) {
        mData = data;
        mHasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mHasResult) {
            mData = null;
            mHasResult = false;
        }
    }

    @Nullable
    @Override
    public ArrayList<MovieItems> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();
        final ArrayList<MovieItems> movieList = new ArrayList<>();
        String url = "";
        String nowPlayingUrl = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + API_KEY + "&language=en-US";
        String upComingUrl = "https://api.themoviedb.org/3/movie/upcoming?api_key=" + API_KEY + "&language=en-US";
        String searchListUrl = "https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY + "&language=en-US";
        String searchUrl = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=en-US&query=" + title;

        if (fragmentName.equals("NOW_PLAYING")) url = nowPlayingUrl;
        else if (fragmentName.equals("UPCOMING")) url = upComingUrl;
        else if (TextUtils.isEmpty(title) && fragmentName.equals("SEARCH")) url = searchListUrl;
        else url = searchUrl;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movie = list.getJSONObject(i);
                        MovieItems movieItems = new MovieItems(movie);
                        movieList.add(movieItems);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        return movieList;
    }
}
