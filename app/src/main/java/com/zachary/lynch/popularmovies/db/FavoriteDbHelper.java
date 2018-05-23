package com.zachary.lynch.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class FavoriteDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;
   public static final String TABLE_NAME = FavoritesDatabaseContract.FavoritesEntry.TABLE_NAME;
   public static final String COLUMN_ID = "_ID";
   public static final String COLUMN_MOVIE_TITLE = "MOVIE_TITLE";
   public static final String COLUMN_MOVIE_ID = "MOVIE_ID";
   public static final String COLUMN_MOVIE_PLOT = "PLOT";
   public static final String COLUMN_MOVIE_RATING = "RATING";
   public static final String COLUMN_MOVIE_RELEASE_DATE = "RELEASE_DATE";
   private static final String DB_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_MOVIE_TITLE + " TEXT NOT NULL," +
            COLUMN_MOVIE_ID + " TEXT NOT NULL," +
            COLUMN_MOVIE_PLOT + " TEXT NOT NULL," +
            COLUMN_MOVIE_RATING + " TEXT NOT NULL," +
            COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL);";
    private SQLiteDatabase database;


    public FavoriteDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);

        // team treehouse data storage with SQLITE 00:42:18


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
