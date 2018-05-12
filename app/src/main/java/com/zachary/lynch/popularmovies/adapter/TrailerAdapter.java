package com.zachary.lynch.popularmovies.adapter;

// recycler view
// doesn't work

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
/*
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private MovieData[] mMovieData;
    private Context mContext;

    public TrailerAdapter (Context context, MovieData[] movieData){
        mContext = context;
        mMovieData = movieData;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_layout, parent, false);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bindTrailerNumber(mMovieData[position]);
    }


    @Override
    public int getItemCount() {
        return mMovieData.length;
    }
    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.trailerNumber) TextView mTrailerNumber;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }
        public void bindTrailerNumber(MovieData data){
            mTrailerNumber.setText(data.getNumOfTrailers());

        }

        @Override
        public void onClick(View v) {

        }
    }
}
*/
