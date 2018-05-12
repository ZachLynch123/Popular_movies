package com.zachary.lynch.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    private MovieData[] mMovieData;
    @BindView(R.id.gridView)
    GridView mGridView;
    private final ApiKey apiKey = new ApiKey();

    private String movieUrl = "http://api.themoviedb.org/3/movie/popular?api_key=" + apiKey.getApiKey();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        getMovies(movieUrl);
    }
    private void getMovies(String movie){
        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder().
                    url(movie)
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
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Context context = MainActivity.this;
                    Class destinationActivity = MovieDetailActivity.class;
                    //Toast.makeText(MainActivity.this, position, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, destinationActivity);
                    intent.putExtra(MOVIE_DATA, mMovieData[position]);
                    startActivity(intent);
                }
            });

        } else {
            Toast.makeText(this, "Network unavailable", Toast.LENGTH_LONG).show();
        }

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


}


