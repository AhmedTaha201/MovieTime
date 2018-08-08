package com.example.popmovies.sqldata;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieProvider extends ContentProvider {

    //Constants used by the UriMatcher
    public static final int MOVIES = 500;
    public static final int MOVIES_ID = 501;

    private SQLiteOpenHelper mDbHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    //A helper method to build The UriMatcher
    public static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        matcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIES_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                try {
                    Cursor cursor = db.query(MovieContract.MOVIE_ENTRY.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                    cursor.setNotificationUri(getContext().getContentResolver(), MovieContract.MOVIE_ENTRY.BASE_URI);

                    return cursor;
                } catch (UnsupportedOperationException e) {
                    e.printStackTrace();
                    return null;
                }

            case MOVIES_ID:
                String id = uri.getLastPathSegment();
                String selectionString = MovieContract.MOVIE_ENTRY.COLUMN_MOVIE_ID+ "=?";
                String[] selectionArgsString = new String[]{id};

                Cursor cursor = db.query(MovieContract.MOVIE_ENTRY.TABLE_NAME, projection, selectionString, selectionArgsString, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;

            default:
                throw new IllegalArgumentException();
        }

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnedUri = null;

        switch (match) {
            case MOVIES:
                try {
                    long id = db.insert(MovieContract.MOVIE_ENTRY.TABLE_NAME, null, values);
                    if (id > 0) {
                        returnedUri = ContentUris.withAppendedId(MovieContract.MOVIE_ENTRY.BASE_URI, id);
                        getContext().getContentResolver().notifyChange(uri, null);
                        return returnedUri;
                    }
                } catch (UnsupportedOperationException e) {
                    e.printStackTrace();
                    return returnedUri;
                }
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                try {
                    int deletedRows = db.delete(MovieContract.MOVIE_ENTRY.TABLE_NAME, null, null);
                    getContext().getContentResolver().notifyChange(uri, null);
                    return deletedRows;
                } catch (UnsupportedOperationException e) {
                    e.printStackTrace();
                    return 0;
                }
            case MOVIES_ID:
                try {
                    String id = uri.getLastPathSegment();
                    String selectionString = MovieContract.MOVIE_ENTRY.COLUMN_MOVIE_ID + "=?";
                    String[] selectionArgsString = new String[]{id};


                    int deletedRows = db.delete(MovieContract.MOVIE_ENTRY.TABLE_NAME, selectionString, selectionArgsString);
                    getContext().getContentResolver().notifyChange(MovieContract.MOVIE_ENTRY.BASE_URI, null);
                    return deletedRows;
                }catch (UnsupportedOperationException e){
                    e.printStackTrace();
                    return 0;
                }
            default:
                throw new IllegalArgumentException();

        }

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
