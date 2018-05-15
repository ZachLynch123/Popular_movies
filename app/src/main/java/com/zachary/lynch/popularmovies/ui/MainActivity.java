package com.zachary.lynch.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.zachary.lynch.popularmovies.ApiKey;
import com.zachary.lynch.popularmovies.R;
import com.zachary.lynch.popularmovies.adapter.GridAdapter;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String MOVIE_DATA = "MOVIE_DATA";
    public static final String TRAILER_DATA = "TRAILER_DATA";

    private MovieData[] mMovieData;
    private MovieData[] mTrailerData;
    @BindView(R.id.gridView) GridView mGridView;
    private final ApiKey apiKey = new ApiKey();

    private String movieUrl = "http://api.themoviedb.org/3/movie/popular?api_key=" + apiKey.getApiKey();
    private String trailerUrl = "https://api.themoviedb.org/3/movie/299536/videos?api_key=" + apiKey.getApiKey() + "&language=en-US";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getMovies(movieUrl);
        getTrailers(trailerUrl);
    }


    private void getMovies(String movie){
        if (isNetworkAvailable()) {
            getMovieJson(movie);
            getTrailers(trailerUrl);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Context context = MainActivity.this;
                    Class destinationActivity = MovieDetailActivity.class;
                    //Toast.makeText(MainActivity.this, position, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, destinationActivity);
                    Bundle extras = new Bundle();
                    extras.putInt("Position", position);

                    extras.putParcelableArray(MOVIE_DATA, mMovieData);
                    extras.putParcelableArray(TRAILER_DATA, mTrailerData);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });
            }else {
            Toast.makeText(this, "Network unavailable", Toast.LENGTH_LONG).show();
        }

    }
    public void getMovieJson(String movie){
        OkHttpClient client = new OkHttpClient();
         Request request = new Request.Builder().
                url(movie)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.v(TAG, "Didn't WORK! ");
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
                        mMovieData = getMovieData(jsonData);
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
    }




    private void updateUi() {
        GridAdapter adapter = new GridAdapter(this, mMovieData);
        mGridView.setAdapter(adapter);


    }

    private MovieData[] getMovieData(String jsonData) throws JSONException {
        JSONObject movies = new JSONObject(jsonData);
        JSONArray movieDetails = movies.getJSONArray("results");
        MovieData[] movieData = new MovieData[movieDetails.length()];
        for (int i = 0; i < movieDetails.length(); i++) {
            JSONObject singleMovie = movieDetails.getJSONObject(i);
            MovieData movie = new MovieData();
            movie.setTitle((singleMovie.getString("title")));
            Log.v(TAG, "JsonAgain: " + movie.getPosterImage());
            movie.setReleaseDate((singleMovie.getString("release_date")));
            movie.setVoteAverage(singleMovie.getInt("vote_average"));
            movie.setPlot(singleMovie.getString("overview"));
            movie.setPosterImage(singleMovie.getString("poster_path"));
            movie.setMovieId(singleMovie.getInt("id"));
            movieData[i] = movie;
        }
        Log.v(TAG, "FORM MAIN ACTIVITY " + Arrays.toString(movieData));

        return movieData;
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
    private String getPopularUrl(){
        ApiKey apiKey = new ApiKey();
        // check if the network is available
        movieUrl = "http://api.themoviedb.org/3/movie/popular?api_key=" + apiKey.getApiKey();
        return movieUrl;
    }
    private String getTopRatedUrl(){
        ApiKey apiKey = new ApiKey();
        // check if the network is available
        movieUrl = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + apiKey.getApiKey();
        return movieUrl;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.popular){
           getMovies(getPopularUrl());
        }
        if (id == R.id.vote){
           getMovies(getTopRatedUrl());
        }

        return super.onOptionsItemSelected(item);
    }
    private void getTrailers(String trailerUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().
                url(trailerUrl)
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
                        mTrailerData = getTrailerData(jsonData);
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
    }

    private MovieData[] getTrailerData(String jsonData) throws JSONException {
        JSONObject idResults = new JSONObject(jsonData);
        JSONArray results = idResults.getJSONArray("results");
        MovieData[] trailerData = new  MovieData[results.length()];
        for (int i = 0; i < results.length(); i++){
            JSONObject singleKey = results.getJSONObject(i);
            MovieData data = new MovieData();
            data.setMovieTrailer(singleKey.getString("key"));
            trailerData[i] = data;
            Log.v(TAG, "form loop " + data.getMovieTrailer());
            data.setNumOfTrailers(results.length());
            Log.v(TAG, "num of trailers " + data.getNumOfTrailers());
        }
        return trailerData ;
    }

}






