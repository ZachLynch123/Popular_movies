package com.zachary.lynch.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zachary.lynch.popularmovies.R;
import com.zachary.lynch.popularmovies.movies.Reviews;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {
    private ArrayList<Reviews> mReviewsList;
    private Context mContext;


    public ReviewsAdapter(Context context, ArrayList<Reviews> reviewsList ) {
        mReviewsList = reviewsList;
        mContext = context;


    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_layout, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        holder.BindReviews(mReviewsList.get(position));

    }

    @Override
    public int getItemCount() {
        return mReviewsList.size();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.author) TextView mAuthorView;
        @BindView(R.id.content) TextView mContentView;

        private String mAuthor;
        private String mContent;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        public void BindReviews(Reviews reviews){
            mAuthor = reviews.getAuthor();
            mContent = reviews.getContent();
            mAuthorView.setText(mAuthor);
            mContentView.setText(mContent);

        }

    }
}
