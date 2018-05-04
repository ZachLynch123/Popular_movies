package com.zachary.lynch.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zachary.lynch.popularmovies.R;
import com.zachary.lynch.popularmovies.movies.MovieData;


public class MovieAdapter extends BaseAdapter {
    private MovieData[] mMovieData;
    private Context mContext;

    public MovieAdapter(Context context, MovieData[] movieData) {
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
        //ViewHolder holder;
/*
        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.movie_detail_item, null);
            holder = new ViewHolder();
            //TODO: create custom layout named "movie_detail_item"  and populate the views in this section

            view.setTag(holder);
        } else  {
            holder = (ViewHolder) view.getTag();
        }
        MovieData data = mMovieData[i];
        // populate views based on the position

        return view;
    }


    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
    private static class ViewHolder{
        ImageView iconImageView; //public by default
        TextView temperatureLabel;
        TextView dayLabel;
        ImageView imgCircleView;
    }
    */
return view;
    }
}