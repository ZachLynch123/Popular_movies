package com.zachary.lynch.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zachary.lynch.popularmovies.R;
import com.zachary.lynch.popularmovies.movies.MovieData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerAdapter extends RecyclerView.Adapter {
    private MovieData mMovieData;
    private Context mContext;

    public TrailerAdapter (Context context, MovieData movieData){
        mContext = context;
        mMovieData = movieData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_layout, parent, false);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //holder.

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.trailerListView) ListView mListView;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        public void bindTrailerNumber(){}

        @Override
        public void onClick(View v) {

        }
    }
}
