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
import java.util.Objects;

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
    private String baseTrailerUrl = "https://api.themoviedb.org/3/movie/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getMovies(movieUrl);
    }


    private void getMovies(String movie){
        if (isNetworkAvailable()) {
            getMovieJson(movie);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    getTrailers(baseTrailerUrl, mMovieData[position].getMovieId());
                    Context context = MainActivity.this;
                    Class destinationActivity = MovieDetailActivity.class;
/*Had to add this line of code because mTrailerData was always null on first click. So I called the method until it didn't return null.
UNLESS it was run through the debugger. That was the only time mTrailerData didn't return null on first click.
Also, when clicked, it doesn't change the value of mTrailerData's MovieId on first click.
 */
                    while (mTrailerData == null){
                        getTrailers(baseTrailerUrl, mMovieData[position].getMovieId());
                    }
                    Log.v(TAG, "Trailer Movie Id: " + mTrailerData[position].getMovieId());
                    Log.v(TAG, "mMovieData Movie Id: " + mMovieData[position].getMovieId());
                    if (mTrailerData != null && mMovieData[position].getMovieId().equals(mTrailerData[position].getMovieId())) {
                        //Toast.makeText(MainActivity.this, position, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, destinationActivity);
                        Bundle extras = new Bundle();
                        extras.putInt("Position", position);
                        Log.v(TAG, mMovieData[position].getMovieId());
                        Log.v(TAG, mTrailerData[position].getMovieId());

                        extras.putParcelableArray(MOVIE_DATA, mMovieData);
                        extras.putParcelableArray(TRAILER_DATA, mTrailerData);
                        intent.putExtras(extras);
                        startActivity(intent);
                    } else{
                        Toast.makeText(MainActivity.this, "mTrailerData null", Toast.LENGTH_SHORT).show();
                    }
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
    private void getTrailers(String trailerUrl, String movieId) {

        trailerUrl = trailerUrl + movieId + "/videos?api_key=" + apiKey.getApiKey();

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
        int movieId = idResults.getInt("id");
        JSONArray results = idResults.getJSONArray("results");
        MovieData[] trailerData = new  MovieData[results.length()];
        for (int i = 0; i < results.length(); i++){
            JSONObject singleKey = results.getJSONObject(i);
            MovieData data = new MovieData();
            data.setMovieTrailer(singleKey.getString("key"));
            data.setMovieId(movieId);
            trailerData[i] = data;
            data.setNumOfTrailers(results.length());
        }
        return trailerData ;
    }

}






/*package com.zachary.lynch.popularmovies.ui;

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
import java.util.ArrayList;
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
    private ArrayList<MovieData> allMovieData = new ArrayList<>();

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
            getMovieJson(movie);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    getTrailers(mMovieData[position].getMovieId(), position);
                    Context context = MainActivity.this;
                    Class destinationActivity = MovieDetailActivity.class;
                    //Toast.makeText(MainActivity.this, position, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, destinationActivity);
                    Log.v(TAG , "allMovieData " + allMovieData.get(position).getMovieTrailer());
                    allMovieData.toArray();

                    if (allMovieData != null) {
                        Bundle extras = new Bundle();
                        extras.putInt("Position", position);
                        extras.putParcelableArrayList(MOVIE_DATA, allMovieData);
                       // extras.putParcelableArray(TRAILER_DATA, mTrailerData);
                        intent.putExtras(extras);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "mTrailerData is still null", Toast.LENGTH_LONG).show();
                    }
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
            movie.setReleaseDate((singleMovie.getString("release_date")));
            movie.setVoteAverage(singleMovie.getInt("vote_average"));
            movie.setPlot(singleMovie.getString("overview"));
            movie.setPosterImage(singleMovie.getString("poster_path"));
            movie.setMovieId(singleMovie.getInt("id"));
            movieData[i] = movie;
            allMovieData.add(movie);
        }

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
            allMovieData.clear();
            getMovies(getPopularUrl());
        }
        if (id == R.id.vote){
            allMovieData.clear();
            getMovies(getTopRatedUrl());

        }

        return super.onOptionsItemSelected(item);
    }
    // passing in movie id to get the proper trailers for each movie based on movie id. look at phone
    //photos to get reviews
    private MovieData[] getTrailers(String movieId, final int position) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().
                url("https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + apiKey.getApiKey())
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
                        mTrailerData = getTrailerData(jsonData);
                        Log.v(TAG, "trailerData + " + mTrailerData);
                        Log.v(TAG,"trailerData + "+ allMovieData.get(position).getTitle());
                    }
                } catch (IOException | JSONException e) {
                    Log.e(TAG, "Exception caught: ", e);
                }

            }
        });
        return mTrailerData;
    }

    private MovieData[] getTrailerData(String jsonData) throws JSONException {
        JSONObject idResults = new JSONObject(jsonData);
        JSONArray results = idResults.getJSONArray("results");
        MovieData[] trailerData = new  MovieData[results.length()];
        ArrayList<MovieData> trailers =  new ArrayList<>(Arrays.asList(new MovieData[0]));
        for (int i = 0; i < results.length(); i++){
            JSONObject singleKey = results.getJSONObject(i);
            MovieData data = new MovieData();
            data.setMovieTrailer(singleKey.getString("key"));
            data.setNumOfTrailers(results.length());
            trailerData[i] = data;
            Log.v(TAG, "trailerData[i]? " + trailerData[i].getMovieTrailer());

            trailers.add(data);
            allMovieData.add(i, data);
            Log.v(TAG, trailers.get(i).getMovieTrailer() + " Movie ID " + trailers.get(i).getNumOfTrailers());
            Log.v(TAG, "trailers in AllMovieData? " + allMovieData.get(0).getMovieTrailer());

        }
        return trailerData;

    }
}

//TODO: Create a method that handles OkHTTP calls, pass the "different" parts of the url as parameters, like the movie id for movie trailers
// return the jsonData = response.body().string() and set something like mTrailerData = getTrailerData(jsonData)
//TODO: Reformat the URL parameters to pass in both movieTrailer and Reviews (look at photos in phone for more info

//TODO: Still have to structuore the database and make a content provider. Look at udacity videos for help
*/


