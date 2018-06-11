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
    public static final Uri CONTENT_URI = Uri.parse("content://com.zachary.lynch.popularmovies.db.ContentProvder/" + FavoriteDbHelper.TABLE_NAME + "/");
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
        builder.setTables(FavoriteDbHelper.TABLE_NAME);
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
        long _id = db.insert(FavoriteDbHelper.TABLE_NAME, null, values);
        if (_id > -1){
            Uri newUri = ContentUris.withAppendedId(CONTENT_URI, _id);
            mContext.getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        throw new SQLiteException("Insert failed for Uri: " + uri);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numDeleted;
        numDeleted = db.delete(FavoritesDatabaseContract.FavoritesEntry.TABLE_NAME, selection, selectionArgs);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                FavoritesDatabaseContract.FavoritesEntry.COLUMN_MOVIE_ID + "'");
        // deletes all elements in table
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                FavoritesDatabaseContract.FavoritesEntry.TABLE_NAME + "'");
        return numDeleted;
        /*
        public int delete(Uri uri, String selection, String[] selectionArgs){
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		int numDeleted;
		switch(match){
			case FLAVOR:
				numDeleted = db.delete(
						FlavorsContract.FlavorEntry.TABLE_FLAVORS, selection, selectionArgs);
				// reset _ID
				db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
						FlavorsContract.FlavorEntry.TABLE_FLAVORS + "'");
				break;
			case FLAVOR_WITH_ID:
				numDeleted = db.delete(FlavorsContract.FlavorEntry.TABLE_FLAVORS,
						FlavorsContract.FlavorEntry._ID + " = ?",
						new String[]{String.valueOf(ContentUris.parseId(uri))});
				// reset _ID
				db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
						FlavorsContract.FlavorEntry.TABLE_FLAVORS + "'");

				break;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}

		return numDeleted;
         */
        /*
        // Codes for the UriMatcher //////
	private static final int FLAVOR = 100;
	private static final int FLAVOR_WITH_ID = 200;
	////////

	private static UriMatcher buildUriMatcher(){
		// Build a UriMatcher by adding a specific code to return based on a match
		// It's common to use NO_MATCH as the code for this case.
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		final String authority = FlavorsContract.CONTENT_AUTHORITY;

		// add a code for each type of URI you want
		matcher.addURI(authority, FlavorsContract.FlavorEntry.TABLE_FLAVORS, FLAVOR);
		matcher.addURI(authority, FlavorsContract.FlavorEntry.TABLE_FLAVORS + "/#", FLAVOR_WITH_ID);

		return matcher;
	}
         */

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
