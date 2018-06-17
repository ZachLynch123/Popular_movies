package com.zachary.lynch.popularmovies.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zachary.lynch.popularmovies.R;
import com.zachary.lynch.popularmovies.movies.MovieData;
import com.zachary.lynch.popularmovies.movies.Trailers;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsViewHolder> {
    private ArrayList<Trailers> mTrailersArrayList;
    private Context mContext;

    public DetailsAdapter(Context context, ArrayList<Trailers> trailersArrayList) {
        mTrailersArrayList = trailersArrayList;
        mContext = context;

    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_layout, parent, false);
        return new DetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailsViewHolder holder, int position) {
        holder.bindDetails(mTrailersArrayList.get(position));

    }


    @Override
    public int getItemCount() {
        return mTrailersArrayList.size();
    }

    public class DetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.trailerNumber)
        TextView mTrailerNumber;
        private String mTrailer;


        public DetailsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bindDetails(Trailers trailers) {
            mTrailer = trailers.getTrailer();
            if (mTrailer != null) {
                mTrailerNumber.setText(trailers.getName());
            } else {
                Toast.makeText(mContext, "this is weird", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onClick(View view) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mTrailer));
            mContext.startActivity(webIntent);

        }
    }
}


