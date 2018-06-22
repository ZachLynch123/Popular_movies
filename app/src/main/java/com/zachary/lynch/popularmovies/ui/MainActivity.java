package com.zachary.lynch.popularmovies.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.zachary.lynch.popularmovies.ApiKey;
import com.zachary.lynch.popularmovies.R;
import com.zachary.lynch.popularmovies.adapter.FavoritesAdapter;
import com.zachary.lynch.popularmovies.adapter.GridAdapter;
import com.zachary.lynch.popularmovies.db.FavoritesProvider;
import com.zachary.lynch.popularmovies.movies.MovieData;
import com.zachary.lynch.popularmovies.movies.Reviews;
import com.zachary.lynch.popularmovies.movies.Trailers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

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
    public static final String TRAILER_ARRAY_LIST = "TRAILER_ARRAY_LIST";
    public static final String REVIEW_ARRAY_LIST = "REVIEW_ARRAY_LIST";
    public static final String POSITION = "POSITION";
    public static final Uri CONTENT_URL = FavoritesProvider.CONTENT_URL;

    private MovieData[] mMovieData;
    private String jsonTrailerDataStuff;
    private ArrayList<Trailers> mTrailers = new ArrayList<>();
    private ArrayList<Reviews> mReviews = new ArrayList<>();
    private boolean fromFavorites = false;

    @BindView(R.id.gridView)
    GridView mGridView;

    private final ApiKey apiKey = new ApiKey();


    private String movieUrl = "https://api.themoviedb.org/3/movie/popular?api_key=" + apiKey.getApiKey();
    private String baseTrailerUrl = "https://api.themoviedb.org/3/movie/";
    private String trailerUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

            try {
                getMovies(movieUrl);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

    }

    private void getMovies(String movie) throws IOException, JSONException {
        if (isNetworkAvailable()) {
            getMovieJson(movie);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    jsonTrailerDataStuff = null;
                    trailerUrl = baseTrailerUrl + mMovieData[position].getMovieId() + "?api_key=" + apiKey.getApiKey() + "&append_to_response=videos,reviews";
                    try {
                        getInfo();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    testReturn();
                    Context context = MainActivity.this;
                    Class destinationActivity = MovieDetailActivity.class;
                    Intent intent = new Intent(context, destinationActivity);
                    Bundle extras = new Bundle();
                    extras.putInt(POSITION, position);
                    extras.putString("MOVIE_ID", mMovieData[position].getMovieId());
                    extras.putParcelableArray(MOVIE_DATA, mMovieData);
                    extras.putParcelableArrayList(TRAILER_ARRAY_LIST, mTrailers);
                    extras.putParcelableArrayList(REVIEW_ARRAY_LIST,mReviews);
                    intent.putExtras(extras);
                    startActivity(intent);
                    // create a recyclerview for reviews, then work on database.
                    }
            });
        } else {
            Toast.makeText(this, "Network unavailable", Toast.LENGTH_LONG).show();
        }

    }

    private void testReturn() {

    }

    public void getMovieJson(String movie) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().
                url(movie)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.v(TAG, "Didn't WORK! " + e);
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
                response.body().close();

            }
        });
    }


    private void updateUi() {
        GridAdapter adapter = new GridAdapter(this, mMovieData);
        mGridView.setAdapter(adapter);
    }

    private MovieData[] getMovieData(String jsonData) throws JSONException, IOException {

        JSONObject movies = new JSONObject(jsonData);
        if (!fromFavorites) {
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
                System.out.println(movie.getMovieId() + "");
            }
            return movieData;
        }else{
            MovieData movie = new MovieData();
            MovieData[] oneMovie = new MovieData[1];

            movie.setTitle((movies.getString("original_title")));
            movie.setReleaseDate((movies.getString("release_date")));
            movie.setVoteAverage(movies.getInt("vote_average"));
            movie.setPlot(movies.getString("overview"));
            movie.setPosterImage(movies.getString("poster_path"));
            movie.setMovieId(movies.getInt("id"));
            oneMovie[0] = movie;
            return oneMovie;
        }
    }
    private void getInfo() throws JSONException{
        while (jsonTrailerDataStuff == null) {
            new TrailersHttp().execute(trailerUrl);
        }
        System.out.println("JSON STUFF " + jsonTrailerDataStuff);

        JSONObject trailers = new JSONObject(jsonTrailerDataStuff);
        JSONObject video = trailers.getJSONObject("videos");
        JSONArray videoArray = video.getJSONArray("results");
        JSONObject review = trailers.getJSONObject("reviews");
        JSONArray reviewArray = review.getJSONArray("results");
        // loop to get all trailers in the JSONArray
        mTrailers = getTrailersDataFromJson(videoArray);
        mReviews = getReviewsDataFromJson(reviewArray);
    }
    private class TrailersHttp extends AsyncTask<String, String, String >{
        @Override
        protected String doInBackground(String... strings) {
            while (jsonTrailerDataStuff == null) {
                final OkHttpClient client = new OkHttpClient();
                final Request request = new Request.Builder().
                        url(trailerUrl)
                        .build();
                try {
                    jsonTrailerDataStuff = client.newCall(request).execute().body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return jsonTrailerDataStuff;
        }
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

    private String getPopularUrl() {
        ApiKey apiKey = new ApiKey();
        // check if the network is available
        movieUrl = "http://api.themoviedb.org/3/movie/popular?api_key=" + apiKey.getApiKey();
        return movieUrl;
    }

    private String getTopRatedUrl() {
        ApiKey apiKey = new ApiKey();
        // check if the network is available
        movieUrl = "http://api.themoviedb.org/3/movie/top_rated?api_key=" + apiKey.getApiKey();
        return movieUrl;
    }
    private void getFavorites() {
        // query to get all items from db

        String [] projection = new String[]{"*"};
        Cursor cursor = getContentResolver().query(CONTENT_URL, projection, null,null,"movie_id");
        int i = 0;
        assert cursor != null;
        final String[] favoriteList = new String[cursor.getCount()];
        String [] moviePosters = new String[cursor.getCount()];
        final String [] movieId = new String [cursor.getCount()];
        if (cursor.moveToFirst()){

            do{
                String id = cursor.getString(cursor.getColumnIndex("movie_id"));
                String movieName = cursor.getString(cursor.getColumnIndex("movie_name"));
                String moviePoster = cursor.getString(cursor.getColumnIndex("movie_poster"));
                Log.v(TAG, "cursor results " + id + " " + movieName);
                favoriteList[i] = movieName;
                moviePosters[i] = moviePoster;
                movieId[i] = id;
                i++;
            }while (cursor.moveToNext());
            cursor.close();

        }
        FavoritesAdapter adapter = new FavoritesAdapter(this, favoriteList, moviePosters);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                testReturn();
                jsonTrailerDataStuff = null;
                trailerUrl = baseTrailerUrl + movieId[position] + "?api_key=" + apiKey.getApiKey() + "&append_to_response=videos,reviews";
                try {
                    getInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                testReturn();
                Context context = MainActivity.this;
                Class destinationActivity = MovieDetailActivity.class;
                Intent intent = new Intent(context, destinationActivity);
                Bundle extras = new Bundle();
                extras.putInt(POSITION, position);
                extras.putString("MOVIE_ID", movieId[position]);
                extras.putString("MOVIE_TITLE", favoriteList[position]);
                extras.putParcelableArray(MOVIE_DATA, mMovieData);
                extras.putParcelableArrayList(TRAILER_ARRAY_LIST, mTrailers);
                extras.putParcelableArrayList(REVIEW_ARRAY_LIST,mReviews);
                intent.putExtras(extras);
                startActivity(intent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.popular) {
            try {
                getMovies(getPopularUrl());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        if (id == R.id.vote) {
            try {
                getMovies(getTopRatedUrl());

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        if (id == R.id.favorite){
            getFavorites();
        }


        return super.onOptionsItemSelected(item);
    }


    private ArrayList<Trailers> getTrailersDataFromJson(JSONArray videoArray) throws JSONException {

         ArrayList<Trailers> results = new ArrayList<>();
         for (int i = 0; i < videoArray.length(); i++) {
             JSONObject trailer = videoArray.getJSONObject(i);
                 results.add(new Trailers(trailer));
             }
             return results;
         }



    private ArrayList<Reviews> getReviewsDataFromJson(JSONArray reviewArray) throws JSONException {
        ArrayList<Reviews> results = new ArrayList<>();
        for (int i = 0; i < reviewArray.length(); i++) {
            JSONObject review = reviewArray.getJSONObject(i);
            results.add(new Reviews(review));
        }

        return results;
    }
}