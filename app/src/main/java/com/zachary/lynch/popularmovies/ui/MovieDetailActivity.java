package com.zachary.lynch.popularmovies.ui;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentValues;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zachary.lynch.popularmovies.R;
import com.zachary.lynch.popularmovies.adapter.DetailsAdapter;
import com.zachary.lynch.popularmovies.adapter.ReviewsAdapter;
import com.zachary.lynch.popularmovies.db.FavoritesProvider;
import com.zachary.lynch.popularmovies.movies.MovieData;
import com.zachary.lynch.popularmovies.movies.Reviews;
import com.zachary.lynch.popularmovies.movies.Trailers;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    // D:\Udacity\PopularMovies\app\src\main\java\com\zachary\lynch\popularmovies\db\FavoritesProvider.java
    public static final Uri CONTENT_URL = FavoritesProvider.CONTENT_URL;

    public ContentResolver mResolver;
    private MovieData[] test;
    private MovieData mMovieData;
    private int position;
    private String mMovieId;
    private String mMovieName;
    private int index = -1;
    private ArrayList<Trailers> mTrailersArrayList;
    private ArrayList<Reviews> mReviewsArrayList;
    private boolean fromReviews = false;



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

    @TargetApi(Build.VERSION_CODES.O)
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
            mMovieId = extras.getString("MOVIE_ID");
            mMovieName = extras.getString("MOVIE_TITLE");
            test = Arrays.copyOf(parcelables, parcelables.length, MovieData[].class);
            mTrailersArrayList = extras.getParcelableArrayList(MainActivity.TRAILER_ARRAY_LIST);
            mReviewsArrayList = extras.getParcelableArrayList(MainActivity.REVIEW_ARRAY_LIST);
            //reviewParse = extras.getParcelableArrayList(MainActivity.REVIEW_ARRAY_LIST);
            for (int i = 0; i < test.length; i++){
                if (test[i].getTitle().equals(mMovieName)){
                    index = i;
                    break;
                }
            }
            mMovieData = test[index];
            Log.v(TAG,  " " + test[index].getMovieId());
            //trailerParse = this.getIntent().getExtras().getParcelableArray(MainActivity.TRAILER_ARRAY_LIST);
        } catch (RuntimeException e) {
            Log.v(TAG, "Unmarshalling unknown type code 2556014 at offset 8448");
        }
        checkIfRowExists();
        updateUi();
        mFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // edit if statement to check if the movie is already in database
                if (mFavorites.getText().equals("Add Favorite")) {
                    addToFavorites(mMovieData.getTitle(), mMovieId, mMovieData.getPosterImage());
                }
                 else {
                    deleteFromFavorites();
                }

            }
        });
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


    private void getFavorites() {
        // query to get all items from db
        String [] projection = new String[]{"id", "movie_name", "movie_id"};
        Cursor cursor = mResolver.query(CONTENT_URL, projection, null,null,null);

        String favoriteList = "";
        if (cursor.moveToFirst()){

            do{
                String id = cursor.getString(cursor.getColumnIndex("movie_id"));
                String movieName = cursor.getString(cursor.getColumnIndex("movie_name"));
            }while (cursor.moveToNext());
        }
        Log.v(TAG, "getFavorites " + favoriteList);
    }

    private void addToFavorites(String title, String movieId, String moviePoster){
        testReturn();
        ContentValues values = new ContentValues();
        values.put(FavoritesProvider.movieName, title);
        values.put(FavoritesProvider.movieId, movieId);
        values.put(FavoritesProvider.moviePoster, moviePoster);
        Uri x = getBaseContext().getContentResolver().insert(CONTENT_URL, values);
        Toast.makeText(getBaseContext(), "New movie added at, " + x.toString(),
                Toast.LENGTH_SHORT).show();
        mFavorites.setText("Remove Favorite");
    }

    private void deleteFromFavorites() {
        testReturn();

        int x = getBaseContext().getContentResolver().delete(CONTENT_URL, "movie_name = ?", new String[]{mMovieData.getTitle()});
        Toast.makeText(getBaseContext(), "number of movies deleted, " + x,
                Toast.LENGTH_SHORT).show();
        mFavorites.setText("Add Favorite");
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkIfRowExists(){
        String [] projection = new String[]{"*"};
        Cursor cursor = getContentResolver().query(CONTENT_URL, projection, "movie_name = ?",new String[]{mMovieData.getTitle()},null);
        if (cursor.moveToFirst()){
            cursor.close();
            mFavorites.setText("Remove Favorite");
            return true;
        }else{
            cursor.close();
            mFavorites.setText("Add Favorite");
            return false;
        }
        /*
        FavoritesProvider.DatabaseHelper dbHelper = new FavoritesProvider.DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + FavoritesProvider.TABLE_NAME + " WHERE " + FavoritesProvider.movieName +
                " = " + title;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0){
            cursor.close();
            mFavorites.setText("Add Favorite");
            return false;
        }
        cursor.close();
        mFavorites.setText("Remove Favorite");
        */

    }
    private void testReturn(){

    }
}













