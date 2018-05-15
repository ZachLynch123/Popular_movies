package com.zachary.lynch.popularmovies.adapter;

// recycler view
// doesn't work

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zachary.lynch.popularmovies.R;
import com.zachary.lynch.popularmovies.movies.MovieData;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder> {
    private Parcelable[] trailerParse;
    private MovieData[] mTrailerData;
    private int i = 1;

    private Context mContext;

    public DetailsAdapter(Context context, Parcelable[] parcelables) {
        trailerParse = parcelables;
        mContext = context;
        mTrailerData = Arrays.copyOf(parcelables, parcelables.length, MovieData[].class);

    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_layout, parent, false);
        DetailsViewHolder viewHolder = new DetailsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {
        holder.bindDetails(mTrailerData[position]);

    }


    @Override
    public int getItemCount() {
        return mTrailerData.length;
    }

    public class  DetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.trailerNumber) TextView mTrailerNumber;
        private String mTrailer;




        public DetailsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bindDetails(MovieData movieData){
            mTrailerNumber.setText(String.format(mContext.getString(R.string.watch_trailer), i));
            mTrailer = movieData.getMovieTrailer();
            i++;
        }

        @Override
        public void onClick(View view) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mTrailer));
            mContext.startActivity(webIntent);

        }
    }

}