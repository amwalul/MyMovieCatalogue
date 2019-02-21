package com.example.amwa.mymoviecatalogue.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class NowPlayingFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<MovieItems>> {
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.rv_movie) RecyclerView rvMovie;

    private MovieAdapter adapter;
    private ArrayList<MovieItems> mData;

    private final String fragmentName = "NOW_PLAYING";
    private final String EXTRA_STATE = "EXTRA_STATE";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_now_playing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        adapter = new MovieAdapter(getActivity());
        adapter.notifyDataSetChanged();

        rvMovie.setHasFixedSize(true);
        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovie.setAdapter(adapter);

        if (savedInstanceState == null) getLoaderManager().initLoader(0, null, NowPlayingFragment.this);
        else adapter.setData(savedInstanceState.<MovieItems>getParcelableArrayList(EXTRA_STATE));

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
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getData());
    }

    @NonNull
    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int i, @Nullable Bundle bundle) {
        progressBar.setVisibility(View.VISIBLE);
        return new MyAsyncTaskLoader(getActivity(), fragmentName);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<MovieItems>> loader, ArrayList<MovieItems> movieItems) {
        adapter.setData(movieItems);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<MovieItems>> loader) {
        adapter.setData(null);
    }
}
