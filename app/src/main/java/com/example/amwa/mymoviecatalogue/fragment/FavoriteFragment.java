package com.example.amwa.mymoviecatalogue.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.amwa.mymoviecatalogue.listener.ItemClickSupport;
import com.example.amwa.mymoviecatalogue.activity.MovieDetailsActivity;
import com.example.amwa.mymoviecatalogue.R;
import com.example.amwa.mymoviecatalogue.adapter.FavoriteAdapter;
import com.example.amwa.mymoviecatalogue.entity.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.amwa.mymoviecatalogue.activity.MovieDetailsActivity.REQUEST_UPDATE;
import static com.example.amwa.mymoviecatalogue.activity.MovieDetailsActivity.RESULT_DELETE;
import static com.example.amwa.mymoviecatalogue.db.DatabaseContract.MovieColumns.CONTENT_URI;

public class FavoriteFragment extends Fragment {
    @BindView(R.id.rv_movie) RecyclerView rvMovie;
    @BindView(R.id.tv_no_items) TextView tvNoItems;

    private FavoriteAdapter adapter;
    private Cursor list;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, view);
        Context context = view.getContext();

        rvMovie.setHasFixedSize(true);
        rvMovie.setLayoutManager(new LinearLayoutManager(context));

        adapter = new FavoriteAdapter(getActivity());
        adapter.setListMovies(list);
        rvMovie.setAdapter(adapter);

        new LoadMovieAsync().execute();

        ItemClickSupport.addTo(rvMovie).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                final Movie movie = adapter.getItem(position);
                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                intent.putExtra(MovieDetailsActivity.EXTRA_FAVORITE_ITEM, movie);
                startActivityForResult(intent, MovieDetailsActivity.REQUEST_UPDATE);
            }
        });

        return view;
    }

    private class LoadMovieAsync extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_UPDATE) {
            if (resultCode == RESULT_DELETE) {
                new LoadMovieAsync().execute();
            }
        }
    }
}
