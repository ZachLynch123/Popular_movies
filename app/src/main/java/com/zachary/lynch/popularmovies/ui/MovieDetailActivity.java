package com.zachary.lynch.popularmovies.ui;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zachary.lynch.popularmovies.ApiKey;
import com.zachary.lynch.popularmovies.R;
import com.zachary.lynch.popularmovies.movies.MovieData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieDetailActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    private MovieData mMovieData;
    private MovieData [] mMovieTrailers;

    @BindView(R.id.movie) TextView mTitle;
    @BindView(R.id.poster) ImageView mPoster;
    @BindView(R.id.release_date) TextView mReleaseDate;
    @BindView(R.id.votes) TextView mVotes;
    @BindView(R.id.plot) TextView mPlot;
    @BindView(R.id.posterTop) ImageView mPosterTop;
    @BindView(R.id.trailerRecycler) RecyclerView mTrailerRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        mMovieData = intent.getParcelableExtra(MainActivity.MOVIE_DATA);

        updateUi();
    }

    private void updateUi() {
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
