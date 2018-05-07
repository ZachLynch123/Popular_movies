package com.zachary.lynch.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.zachary.lynch.popularmovies.ApiKey;
import com.zachary.lynch.popularmovies.R;
import com.zachary.lynch.popularmovies.adapter.GridAdapter;
import com.zachary.lynch.popularmovies.movies.MovieData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

    private TextView mTextView;
    private MovieData[] mMovieData;
    @BindView(R.id.gridView)
    GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mTextView = findViewById(R.id.text);
        // check if the network is available
        ApiKey apiKey = new ApiKey();
        String movieUrl = "https://api.themoviedb.org/3/discover/movie?api_key=" +
                apiKey.getApiKey() + "&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1";
        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder().
                    url(movieUrl)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextView.setText("Failed");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
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
                    Class destinationActivity = MovieDetailActicity.class;
                    //Toast.makeText(MainActivity.this, position, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, destinationActivity);
                    intent.putExtra(MOVIE_DATA, mMovieData[position]);
                    startActivity(intent);
                }
            });

        } else {
            Toast.makeText(this, "Network unavailable", Toast.LENGTH_LONG).show();
            mTextView.setText("Different Issue");
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
            movieData[i] = movie;
        }

        return movieData;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }
}


