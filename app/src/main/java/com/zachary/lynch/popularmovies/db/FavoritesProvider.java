package com.zachary.lynch.popularmovies.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.HashMap;

public class FavoritesProvider extends ContentProvider{
    static final  String PROVIDER_NAME = "com.zachary.lynch.popularmovies.db.FavoritesProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/moviefavorites";
    public static final Uri CONTENT_URL = Uri.parse(URL);

    static final String id = "id";
    public static final String movieName = "movie_name";
    public static final String releaseDate = "release_date";
    public static final String voteAverage = "vote_average";
    public static final String movieId = "movie_id";
    static final int uriCode = 1;

    private static HashMap<String, String > values;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "moviefavorites", uriCode);
    }
    private SQLiteDatabase db;
    static final String DATABASE_NAME = "myFavorites";
    public static final String TABLE_NAME = "favorite_movies";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE = "CREATE TABLE " + TABLE_NAME +
            " (id INTEGER PRIMARY KEY AUTOINCREMENT, movie_name TEXT NOT NULL, movie_id TEXT NOT NULL);";


    @Override
    public boolean onCreate() {
        DatabaseHelper dbHelper =  new DatabaseHelper(getContext());
        db = dbHelper.getWritableDatabase();

        if (db != null){
            return true;
        }else {
            return false;
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(TABLE_NAME);

        switch (uriMatcher.match(uri)){

            case uriCode:
                queryBuilder.setProjectionMap(values);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);

        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){

            case uriCode:
                return "vdn.android.cursor.dir/moviefavorites";
            default:
                throw new IllegalArgumentException("Unsupported Uri " + uri);

        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowId = db.insert(TABLE_NAME, null, values);

        if (rowId > 0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URL, rowId);

            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }else {
            Toast.makeText(getContext(), "didn't insert row", Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsDeleted = 0;
        switch (uriMatcher.match(uri)){

            case uriCode:
                rowsDeleted = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
    public static class DatabaseHelper extends SQLiteOpenHelper{
        public DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_DB_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}