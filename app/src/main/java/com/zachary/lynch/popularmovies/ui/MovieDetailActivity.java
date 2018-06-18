package com.zachary.lynch.popularmovies.ui;

import android.content.ContentResolver;
import android.content.ContentValues;

import android.database.Cursor;
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
import com.zachary.lynch.popularmovies.adapter.ReviewsAdapter;
import com.zachary.lynch.popularmovies.db.FavoriteDbHelper;
import com.zachary.lynch.popularmovies.db.FavoritesProvider;
import com.zachary.lynch.popularmovies.movies.MovieData;
import com.zachary.lynch.popularmovies.movies.Reviews;
import com.zachary.lynch.popularmovies.movies.Trailers;

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
    public static final Uri CONTENT_URL = Uri.parse("content://com.zachary.lynch.popularmovies.favoritesprovider." +
            "FavoritesProvider/moviefavorites");

    public ContentResolver mResolver;
    private MovieData[] test;
    private MovieData mMovieData;
    private int position;
    private ArrayList<Trailers> mTrailersArrayList;
    private ArrayList<Reviews> mReviewsArrayList;

    @BindView(R.id.movie)
    TextView mTitle;
    @BindView(R.id.favorite_btn)
    Button mFavorites;
    @BindView(R.id.poster)
    ImageView mPoster;
    @BindView(R.id.release_date)
    TextView mReleaseDate;
    @BindView(R.id.votes)
    TextView mVotes;
    @BindView(R.id.plot)
    TextView mPlot;
    @BindView(R.id.posterTop)
    ImageView mPosterTop;
    @BindView(R.id.trailers)
    RecyclerView mRecyclerView;
    @BindView(R.id.review)
    RecyclerView mReviewsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();

        try {
            assert extras != null;
            position = extras.getInt(MainActivity.POSITION);
            Parcelable[] parcelables = extras.getParcelableArray(MainActivity.MOVIE_DATA);
            test = Arrays.copyOf(parcelables, parcelables.length, MovieData[].class);
            mTrailersArrayList = extras.getParcelableArrayList(MainActivity.TRAILER_ARRAY_LIST);
            mReviewsArrayList = extras.getParcelableArrayList(MainActivity.REVIEW_ARRAY_LIST);
            //reviewParse = extras.getParcelableArrayList(MainActivity.REVIEW_ARRAY_LIST);
            mMovieData = test[position];
            //trailerParse = this.getIntent().getExtras().getParcelableArray(MainActivity.TRAILER_ARRAY_LIST);
        } catch (RuntimeException e) {
            Log.v(TAG, "Unmarshalling unknown type code 2556014 at offset 8448? More like fuck you");
        }


        // id = this.getIntent().getExtras().getLong("id", 1L);



       /* position = extras.getInt("Position");

        assert parcelables != null;

        test2 = Arrays.copyOf(trailerParse, parcelables.length, MovieData[].class);
        Log.v(TAG, "" + test2[position]);
        Log.v(TAG, reviewParse[position] +"");
        mFavorites.setText(R.string.add_favorites);
*/
        updateUi();
        mFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // edit if statement to check if the movie is already in database
                if (mFavorites.getText().equals("Add Fav")) {
                    addToFavorites(mMovieData.getTitle(), mMovieData.getReleaseDate(), mMovieData.getVoteAverage());
                    changeButtonText();
                } else {
                    deleteFromFavorites(mMovieData.getTitle());
                    changeButtonText();
                }

            }
        });
    }

    private void changeButtonText() {
        if (mFavorites.getText().equals("Add Fav")) {
            mFavorites.setText(R.string.remove_favorites);
        } else {
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
        DetailsAdapter adapter = new DetailsAdapter(this, mTrailersArrayList);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(this, mReviewsArrayList);
        mReviewsView.setAdapter(reviewsAdapter);

        RecyclerView.LayoutManager reviewLayoutManager = new LinearLayoutManager(this);
        mReviewsView.setLayoutManager(reviewLayoutManager);

    }
    // creating an options menu to add and remove movies from favorites.
    private void deleteFromFavorites(String title){
        String movieToDelete = title;
        long movieDeleted = mResolver.delete(CONTENT_URL,"movieName = ?", new String[]{movieToDelete});
        Log.v(TAG, "From deleteFromFavorites " + movieToDelete);
        getFavorites();

    }

    private void getFavorites() {
        // query to get all items from db
        String [] projection = new String[]{"id", "name", "releaseDate", "voteAverage"};
        Cursor cursor = mResolver.query(CONTENT_URL, projection, null,null,null);

        String favoriteList = "";
        if (cursor.moveToFirst()){

            do{
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String movieName = cursor.getString(cursor.getColumnIndex("movieName"));
                String realeaseDate = cursor.getString(cursor.getColumnIndex("releaseDate"));
                String voteAverage = cursor.getString(cursor.getColumnIndex("voteAverage"));
                favoriteList = favoriteList + id + ": " + movieName + "\n" + realeaseDate + "\n" +
                        voteAverage;
            }while (cursor.moveToNext());
        }
        Log.v(TAG, "getFavorites " + favoriteList);
    }

    private void addToFavorites(String title, String releaseDate, int voteAverage){
        ContentValues values = new ContentValues();
        values.put(FavoritesProvider.movieName, title);
        values.put(FavoritesProvider.releaseDate, releaseDate);
        values.put(FavoritesProvider.voteAverage, voteAverage);
        Uri uri = getContentResolver().insert(FavoritesProvider.CONTENT_URL, values);

        Toast.makeText(getBaseContext(), "New movie added at, " + uri.toString(),
                Toast.LENGTH_SHORT).show();
    }
}













