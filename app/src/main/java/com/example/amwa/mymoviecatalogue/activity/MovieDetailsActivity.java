package com.example.amwa.mymoviecatalogue.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.amwa.mymoviecatalogue.entity.MovieItems;
import com.example.amwa.mymoviecatalogue.R;
import com.example.amwa.mymoviecatalogue.db.MovieHelper;
import com.example.amwa.mymoviecatalogue.entity.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.CONTENT_URI;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.LANGUAGE;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.OVERVIEW;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.POSTER;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.RATING;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.TITLE;

public class MovieDetailsActivity extends AppCompatActivity {
    @BindView(R.id.iv_poster) ImageView ivPoster;
    @BindView(R.id.btn_favorite) Button btnFavorite;
    @BindView(R.id.tv_title_details) TextView tvTitle;
    @BindView(R.id.tv_rating) TextView tvRating;
    @BindView(R.id.tv_language) TextView tvLanguage;
    @BindView(R.id.tv_release) TextView tvRelease;
    @BindView(R.id.tv_overview) TextView tvOverview;

    public final static String EXTRA_ITEM = "EXTRA_ITEM";
    public final static String EXTRA_FAVORITE_ITEM = "EXTRA_FAVORITE_ITEM";
    final static String imageUrl = "https://image.tmdb.org/t/p/w185/";

    private boolean isAdded = false;
    public static int REQUEST_UPDATE = 200;
    public static int RESULT_DELETE = 301;

    private Movie movie;
    private MovieItems items;
    private MovieHelper movieHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        items = getIntent().getParcelableExtra(EXTRA_ITEM);

        movieHelper = new MovieHelper(this);
        movieHelper.open();

        movie = getIntent().getParcelableExtra(EXTRA_FAVORITE_ITEM);

        if (movie != null){
            isAdded = true;
            btnFavorite.setText(getString(R.string.remove_from_favorite));

            getSupportActionBar().setTitle(movie.getTitle());
            Glide.with(this)
                    .load(imageUrl + movie.getPoster())
                    .into(ivPoster);
            tvTitle.setText(movie.getTitle());
            tvRating.setText(movie.getRating());
            tvLanguage.setText(movie.getLanguage().toUpperCase());
            tvRelease.setText(movie.getReleaseDate());
            tvOverview.setText(movie.getOverview());
        } else {
            btnFavorite.setText(getString(R.string.add_to_favorite));

            getSupportActionBar().setTitle(items.getTitle());
            Glide.with(this)
                    .load(imageUrl + items.getPoster())
                    .into(ivPoster);
            tvTitle.setText(items.getTitle());
            tvRating.setText(items.getRating());
            tvLanguage.setText(items.getLanguage().toUpperCase());
            tvRelease.setText(items.getReleaseDate());
            tvOverview.setText(items.getOverview());
        }

        Uri uri = getIntent().getData();
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) movie = new Movie(cursor);
                cursor.close();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (movieHelper != null) {
            movieHelper.close();
        }
    }

    @OnClick(R.id.btn_favorite)
    public void onFavoriteClick() {
        if (isAdded) {
            getContentResolver().delete(Uri.parse(CONTENT_URI + "/" + movie.getId()), null, null);
            Toast.makeText(getApplicationContext(), getString(R.string.item_deleted), Toast.LENGTH_SHORT).show();
            setResult(RESULT_DELETE);
            finish();
        } else {
            ContentValues values = new ContentValues();
            values.put(TITLE, items.getTitle());
            values.put(POSTER, items.getPoster());
            values.put(RATING, items.getRating());
            values.put(LANGUAGE, items.getLanguage());
            values.put(RELEASE_DATE, items.getReleaseDate());
            values.put(OVERVIEW, items.getOverview());

            getContentResolver().insert(CONTENT_URI, values);
            Toast.makeText(getApplicationContext(), getString(R.string.item_added), Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
