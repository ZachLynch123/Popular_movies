package com.zachary.lynch.popularmovies.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zachary.lynch.popularmovies.R;

public class FavoritesAdapter extends BaseAdapter{
    private Context mContext;
    private String [] mFavoriteList;
    private String [] mMoviePosters;

    public FavoritesAdapter(Context context, String[] favoriteList, String [] moviePosters){
        mContext = context;
        mFavoriteList = favoriteList;
        mMoviePosters = moviePosters;
    }


    @Override
    public int getCount() {
        return mFavoriteList.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.main_grid_layout, null);
            holder = new ViewHolder();
            holder.gridImageView = view.findViewById(R.id.gridImageView);
            holder.movieName = view.findViewById(R.id.movieName);
            holder.gridImageView.setScaleType(ImageView.ScaleType.FIT_XY);


            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        String movieNames = mFavoriteList[i];
        String posterImage = mMoviePosters[i];

        Picasso
                .with(mContext)
                .load(posterImage)
                .resize(6000, 4000)
                .onlyScaleDown()
                .into(holder.gridImageView);

        /*Picasso
                .with(mContext)
                .load(posterImage)
                .resize(6000, 500)
                .onlyScaleDown()
                .into(holder.gridImageView);*/

        holder.movieName.setText(movieNames);


        return view;
    }

    private static class ViewHolder {
        ImageView gridImageView;
        TextView movieName;
    }
}
