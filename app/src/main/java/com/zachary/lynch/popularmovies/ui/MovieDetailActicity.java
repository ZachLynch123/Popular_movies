package com.zachary.lynch.popularmovies.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zachary.lynch.popularmovies.R;
import com.zachary.lynch.popularmovies.adapters.MovieAdapter;
import com.zachary.lynch.popularmovies.movies.MovieData;

import java.util.Arrays;

import butterknife.ButterKnife;

public class MovieDetailActicity extends Activity {
    private MovieData[] mMovieData;
    // bind gridview


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail_acticity);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.MOVIE_DATA);
        mMovieData = Arrays.copyOf(parcelables, parcelables.length, MovieData[].class);

        MovieAdapter adapter = new MovieAdapter(this, mMovieData);
        //mGridView.setAdapter(adapter)

        // set an onclick listener for the items in the grid?
    }
}
