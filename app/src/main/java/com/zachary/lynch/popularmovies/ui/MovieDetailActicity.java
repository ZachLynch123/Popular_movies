package com.zachary.lynch.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zachary.lynch.popularmovies.R;
import com.zachary.lynch.popularmovies.movies.MovieData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActicity extends AppCompatActivity {
    private static final String TAG = MovieDetailActicity.class.getSimpleName();
    private MovieData mMovieData;


    @BindView(R.id.movie) TextView mTitle;
    @BindView(R.id.poster) ImageView mPoster;
    @BindView(R.id.release_date) TextView mReleaseDate;
    @BindView(R.id.votes) TextView mVotes;
    @BindView(R.id.plot) TextView mPlot;
    @BindView(R.id.posterTop) ImageView mPosterTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        mMovieData = intent.getParcelableExtra(MainActivity.MOVIE_DATA);

        mTitle.setText(mMovieData.getTitle());
        mReleaseDate.setText(mMovieData.getReleaseDate());
        mVotes.setText(mMovieData.getVoteAverage() + "");
        mPlot.setText(mMovieData.getPlot());

        Picasso
                .with(this)
                .load(mMovieData.getPosterImage())
                .fit()
                .centerCrop()
                .into(mPoster);
        Picasso
                .with(this)
                .load(mMovieData.getPosterImage())
                .resize(6000, 4000)
                .onlyScaleDown()
                .into(mPosterTop);









    }
}
