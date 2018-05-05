package com.zachary.lynch.popularmovies.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zachary.lynch.popularmovies.R;
import com.zachary.lynch.popularmovies.adapters.GridAdapter;
import com.zachary.lynch.popularmovies.movies.MovieData;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActicity extends Activity {
    private MovieData mMovieData;
    @BindView(R.id.movie_name) TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        mMovieData = intent.getParcelableExtra(MainActivity.MOVIE_DATA);
        mTextView.setText(mMovieData.getTitle());

        //i.putExtra("parcelable_extra", (Parcelable) myParcelableObject);






    }
}
