package com.zachary.lynch.popularmovies.adapters;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zachary.lynch.popularmovies.movies.MovieData;

public class DetailsAdapter extends BaseAdapter {
    private MovieData[] mMovieData;
    private Context mContext;


    public DetailsAdapter(Context context, MovieData[] movieData){
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
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
