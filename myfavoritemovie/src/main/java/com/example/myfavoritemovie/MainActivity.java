package com.example.myfavoritemovie;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import static com.example.myfavoritemovie.DatabaseContract.MovieColumns.CONTENT_URI;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvMovie;
    private FavoriteAdapter adapter;
    private TextView tvNoItems;
    private Cursor list;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvNoItems = findViewById(R.id.tv_no_items);

        rvMovie = findViewById(R.id.rv_movie);
        rvMovie.setHasFixedSize(true);
        rvMovie.setLayoutManager(new LinearLayoutManager(context));

        adapter = new FavoriteAdapter(this);
        adapter.setListMovies(list);
        rvMovie.setAdapter(adapter);

        new LoadMovieAsync().execute();
    }

    private class LoadMovieAsync extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            list = cursor;
            adapter.setListMovies(list);
            adapter.notifyDataSetChanged();

            if (list.getCount() == 0) tvNoItems.setVisibility(View.VISIBLE);
            else tvNoItems.setVisibility(View.GONE);
        }
    }
}
