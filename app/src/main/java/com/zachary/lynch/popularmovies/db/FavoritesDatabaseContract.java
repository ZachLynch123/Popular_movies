package com.zachary.lynch.popularmovies.db;


import android.provider.BaseColumns;

class FavoritesDatabaseContract {
        static final class FavoritesEntry implements BaseColumns {
            static final String TABLE_NAME = "favorites";
            static final String COLUMN_MOVIE_TITLE = "movie_title";
            static final String  COLUMN_MOVIE_ID = "movie_id";
            static final String COLUMN_MOVIE_PLOT = "movie_plot";
            static final String COLUMN_MOVIE_RATING = "movie_rating";
            static final String COLUMN_MOVIE_POSTER = "movie_poster";
            static final String COLUMN_MOVIE_RELEASE_DATE = "movie_release_date";

        }
    }
