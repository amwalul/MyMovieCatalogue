package com.example.amwa.mymoviecatalogue.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.amwa.mymoviecatalogue.R;
import com.example.amwa.mymoviecatalogue.entity.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private Cursor listMovies;
    private Context context;

    public FavoriteAdapter(Context context) {
        this.context = context;
    }

    public Cursor getListMovies() {
        return listMovies;
    }

    public void setListMovies(Cursor listMovies) {
        this.listMovies = listMovies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_items, viewGroup, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder favoriteViewHolder, int i) {
        final Movie movie = getItem(i);
        favoriteViewHolder.tvTitle.setText(movie.getTitle());
        favoriteViewHolder.tvOverview.setText(movie.getOverview());
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w185/" + movie.getPoster())
                .into(favoriteViewHolder.ivThumbnail);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (listMovies == null) return 0;
        return listMovies.getCount();
    }

    public Movie getItem(int position){
        if (!listMovies.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid");
        }
        return new Movie(listMovies);
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.tv_overview) TextView tvOverview;
        @BindView(R.id.iv_thumbnail) ImageView ivThumbnail;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
