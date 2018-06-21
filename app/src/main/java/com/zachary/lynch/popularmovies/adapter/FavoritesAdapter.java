package com.zachary.lynch.popularmovies.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zachary.lynch.popularmovies.R;

public class FavoritesAdapter extends BaseAdapter{
    private Context mContext;
    private String [] mFavoriteList;

    public FavoritesAdapter(Context context, String[] favoriteList){
        mContext = context;
        mFavoriteList = favoriteList;
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
            view = LayoutInflater.from(mContext).inflate(R.layout.favorite_layout, null);
            holder = new ViewHolder();
            holder.movieName = view.findViewById(R.id.favorite_movie_name);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        String movieNames = mFavoriteList[i];
        holder.movieName.setText(movieNames);


        return view;
    }

    private static class ViewHolder {
        TextView movieName;
    }
}
