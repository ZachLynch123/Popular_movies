package com.zachary.lynch.popularmovies.ui;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zachary.lynch.popularmovies.ApiKey;
import com.zachary.lynch.popularmovies.R;
import com.zachary.lynch.popularmovies.adapter.TrailerAdapter;
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
    @BindView(R.id.trailerListView) ListView mTrailerListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        ApiKey apiKey = new ApiKey();

        Intent intent = getIntent();

        mMovieData = intent.getParcelableExtra(MainActivity.MOVIE_DATA);
        updateUi();

        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder().
                    url("https://api.themoviedb.org/3/movie/299536/videos?api_key=" + apiKey.getApiKey() + "&language=en-US")
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        //noinspection ConstantConditions
                        String jsonData = response.body().string();
                        if (response.isSuccessful()) {
                            Log.v(TAG, "From JSON" + jsonData);
                            mMovieTrailers = getMovieTrailers(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateUi();
                                }
                            });
                        }
                    } catch (IOException | JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }

                }
            });

        }}

    private MovieData [] getMovieTrailers(String jsonData) throws JSONException {
        JSONObject idResults = new JSONObject(jsonData);
        JSONArray results = idResults.getJSONArray("results");
        MovieData[] trailerData = new  MovieData[results.length()];
        for (int i = 0; i < results.length(); i++){
            JSONObject singleKey = results.getJSONObject(i);
            MovieData data = new MovieData();
            data.setMovieTrailer(singleKey.getString("key"));
            trailerData[i] = data;
            Log.v(TAG, "form loop " + data);
        }
        Log.v(TAG, "From Trailers " + Arrays.toString(trailerData));

        return trailerData ;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
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
