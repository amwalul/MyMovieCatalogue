package com.example.amwa.mymoviecatalogue.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.amwa.mymoviecatalogue.R;
import com.example.amwa.mymoviecatalogue.entity.MovieItems;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private ArrayList<MovieItems> mData = new ArrayList<>();
    private Context context;

    public MovieAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<MovieItems> getData() {
        return mData;
    }

    public void setData(ArrayList<MovieItems> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_items, viewGroup, false);
        return new ViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final MovieItems movieItems = mData.get(i);
        viewHolder.textViewTitle.setText(movieItems.getTitle());
        viewHolder.textViewOverview.setText(movieItems.getOverview());
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w185/" + movieItems.getPoster())
                .into(viewHolder.imageViewthumbnail);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title) TextView textViewTitle;
        @BindView(R.id.tv_overview) TextView textViewOverview;
        @BindView(R.id.iv_thumbnail) ImageView imageViewthumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}