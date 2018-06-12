package com.zachary.lynch.popularmovies.ui;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zachary.lynch.popularmovies.R;
import com.zachary.lynch.popularmovies.adapter.DetailsAdapter;
import com.zachary.lynch.popularmovies.db.ContentProvder;
import com.zachary.lynch.popularmovies.db.FavoriteDbHelper;
import com.zachary.lynch.popularmovies.movies.MovieData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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
    private MovieData[] test;
    private MovieData mMovieData;
    private int position;
    private Parcelable[] trailerParse;
    private MovieData[] test2;
    private int i;
    private MovieData mMovieTrailers;
    private String mColumnId;

    @BindView(R.id.movie) TextView mTitle;
    @BindView(R.id.favorite_btn) Button mFavorites;
    @BindView(R.id.poster) ImageView mPoster;
    @BindView(R.id.release_date) TextView mReleaseDate;
    @BindView(R.id.votes) TextView mVotes;
    @BindView(R.id.plot) TextView mPlot;
    @BindView(R.id.posterTop) ImageView mPosterTop;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();

        assert extras != null;
        position = extras.getInt("Position");
        Parcelable[] parcelables = extras.getParcelableArray(MainActivity.MOVIE_DATA);
        trailerParse = extras.getParcelableArray(MainActivity.TRAILER_DATA);

        test = Arrays.copyOf(parcelables, parcelables.length, MovieData[].class);

        test2 = Arrays.copyOf(trailerParse, parcelables.length, MovieData[].class);
        mMovieData = test[position];
        Log.v(TAG, "" + test2[position]);
        Log.v(TAG, mMovieTrailers +"");
        mFavorites.setText(R.string.add_favorites);

        updateUi();
        mFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFavorites.getText().equals("Add Fav")){
                    addToFavorites(mMovieData.getTitle(), mMovieData.getMovieId(), mMovieData.getPlot(), mMovieData.getReleaseDate(), mMovieData.getVoteAverage());
                    changeButtonText();
                }else {
                    deleteFromFavorites(mMovieData.getTitle());
                    changeButtonText();
                }

            }
        });
    }

    private void changeButtonText() {
        if (mFavorites.getText().equals("Add Fav")){
            mFavorites.setText(R.string.remove_favorites);
        }else{
            mFavorites.setText(R.string.add_favorites);
        }

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
        DetailsAdapter adapter = new DetailsAdapter(this, trailerParse);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }
    // creating an options menu to add and remove movies from favorites.




    private void deleteFromFavorites(String title) {
        Uri x = ContentProvder.CONTENT_URI.buildUpon().appendPath(FavoriteDbHelper.COLUMN_MOVIE_ID).build();
        int uri = getContentResolver().delete(
                ContentProvder.CONTENT_URI.buildUpon().appendPath(FavoriteDbHelper.COLUMN_MOVIE_ID).build(),
                null, null);

        Toast.makeText(this, uri + "", Toast.LENGTH_LONG).show();

    }

    private void addToFavorites(String title, String movieId, String plot, String releaseDate, int voteAverage) {
        ContentValues values = new ContentValues();
        values.put(FavoriteDbHelper.COLUMN_MOVIE_TITLE, title);
        values.put(FavoriteDbHelper.COLUMN_MOVIE_ID, movieId);
        values.put(FavoriteDbHelper.COLUMN_MOVIE_PLOT, plot);
        values.put(FavoriteDbHelper.COLUMN_MOVIE_RELEASE_DATE, releaseDate);
        values.put(FavoriteDbHelper.COLUMN_MOVIE_RATING, String.valueOf(voteAverage));
        Uri uri = getContentResolver().insert(ContentProvder.CONTENT_URI, values);
        if (uri != null) {
             Toast.makeText(this, uri.toString(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}











