package com.zachary.lynch.popularmovies.db;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ContentProvder extends ContentProvider{
    static final Uri CONTENT_URI = Uri.parse("content://com.zachary.lynch.popularmovies.db/" + FavoritesDatabaseContract.FavoritesEntry.TABLE_NAME);
    private Context mContext;
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        mContext = getContext();
        FavoriteDbHelper favoriteDbHelper = new FavoriteDbHelper(mContext);
        db = favoriteDbHelper.getWritableDatabase();
        return db != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] columns, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FavoritesDatabaseContract.FavoritesEntry.TABLE_NAME);
        return builder.query(db, columns, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long _id = db.insert(FavoritesDatabaseContract.FavoritesEntry.TABLE_NAME, null, values);
        if (_id > -1){
            Uri newUri = ContentUris.withAppendedId(CONTENT_URI, _id);
            mContext.getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        throw new SQLiteException("Insert failed for Uri: " + uri);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
