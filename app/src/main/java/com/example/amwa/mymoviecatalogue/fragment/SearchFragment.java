package com.example.amwa.mymoviecatalogue.fragment;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.amwa.mymoviecatalogue.listener.ItemClickSupport;
import com.example.amwa.mymoviecatalogue.activity.MovieDetailsActivity;
import com.example.amwa.mymoviecatalogue.entity.MovieItems;
import com.example.amwa.mymoviecatalogue.loader.MyAsyncTaskLoader;
import com.example.amwa.mymoviecatalogue.R;
import com.example.amwa.mymoviecatalogue.adapter.MovieAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<MovieItems>> {
    @BindView(R.id.edt_movie) EditText edtMovie;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.rv_movie) RecyclerView rvMovie;

    private MovieAdapter adapter;
    private ArrayList<MovieItems> mData;

    private final String fragmentName = "SEARCH";
    private static final String EXTRA_STATE = "EXTRA_STATE";
    public static final String EXTRAS_MOVIE = "EXTRAS_MOVIE";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        adapter = new MovieAdapter(getActivity());
        adapter.notifyDataSetChanged();

        rvMovie.setHasFixedSize(true);
        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovie.setAdapter(adapter);

        String movie = edtMovie.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRAS_MOVIE, movie);

        if (savedInstanceState != null) adapter.setData(savedInstanceState.<MovieItems>getParcelableArrayList(EXTRA_STATE));
        else getLoaderManager().initLoader(0, bundle, SearchFragment.this);
        mData = adapter.getData();
        ItemClickSupport.addTo(rvMovie).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                final MovieItems movieItems = mData.get(position);
                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                intent.putExtra(MovieDetailsActivity.EXTRA_ITEM, movieItems);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getData());
    }


    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int id, Bundle args) {
        String title = "";
        if (args != null) title = args.getString(EXTRAS_MOVIE);
        progressBar.setVisibility(View.VISIBLE);

        return new MyAsyncTaskLoader(getActivity(), fragmentName, title);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItems>> loader, ArrayList<MovieItems> data) {
        adapter.setData(data);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItems>> loader) {
        adapter.setData(null);
    }

    @OnClick(R.id.btn_search)
    public void onSearchClick() {
        String movie = edtMovie.getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString(EXTRAS_MOVIE, movie);
        getLoaderManager().restartLoader(0, bundle, SearchFragment.this);
    }
}
