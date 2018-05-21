package com.zachary.lynch.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    public FavoriteDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " +
                FavoritesDatabaseContract.FavoritesEntry.TABLE_NAME + " (" +
                FavoritesDatabaseContract.FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoritesDatabaseContract.FavoritesEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL," +
                FavoritesDatabaseContract.FavoritesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                FavoritesDatabaseContract.FavoritesEntry.COLUMN_MOVIE_PLOT + " TEXT NOT NULL," +
                FavoritesDatabaseContract.FavoritesEntry.COLUMN_MOVIE_RATING + " TEXT NOT NULL," +
                FavoritesDatabaseContract.FavoritesEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL," +
                FavoritesDatabaseContract.FavoritesEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_FAVORITES_TABLE);

        // team treehouse data storage with SQLITE 00:42:18


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoritesDatabaseContract.FavoritesEntry.TABLE_NAME);
        onCreate(db);
    }
}
