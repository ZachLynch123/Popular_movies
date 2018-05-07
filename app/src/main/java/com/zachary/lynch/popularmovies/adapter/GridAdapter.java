package com.zachary.lynch.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zachary.lynch.popularmovies.R;
import com.zachary.lynch.popularmovies.movies.MovieData;


public class GridAdapter extends BaseAdapter {
    private MovieData[] mMovieData;
    private MovieData mSingleMovie;
    private Context mContext;

    public GridAdapter(Context context, MovieData[] movieData) {
        mMovieData = movieData;
        mContext = context;
    }


    @Override
    public int getCount() {
        return mMovieData.length;
    }

    @Override
    public Object getItem(int i) {
        return mMovieData[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.main_grid_layout, null);
            holder = new ViewHolder();
            //TODO: create custom layout named "movie_detail_item"  and find the views in this section
            holder.gridImageView = view.findViewById(R.id.gridImageView);
            holder.movieName = view.findViewById(R.id.movieName);
            holder.gridImageView.setScaleType(ImageView.ScaleType.FIT_XY);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        MovieData data = mMovieData[i];
        mSingleMovie = mMovieData[i];
        // populate views based on the position

        Picasso
                .with(mContext)
                .load(data.getPosterImage())
                .resize(6000,4000)
                .onlyScaleDown()
                .into(holder.gridImageView);
        holder.movieName.setText(data.getTitle());
        return view;
    }


    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    private static class ViewHolder {
        ImageView gridImageView; //public by default
        TextView movieName;

    }

}