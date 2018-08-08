package com.example.popmovies.sqldata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.popmovies.sqldata.MovieContract.MOVIE_ENTRY;


public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies";
    private static final int DATABASE_VERSION = 2;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + MOVIE_ENTRY.TABLE_NAME + " ( "
                + MOVIE_ENTRY._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MOVIE_ENTRY.COLUMN_TITLE + " TEXT NOT NULL, "
                + MOVIE_ENTRY.COLUMN_YEAR + " TEXT, "
                + MOVIE_ENTRY.COLUMN_RATE + " TEXT, "
                + MOVIE_ENTRY.COLUMN_DURATION + " TEXT, "
                + MOVIE_ENTRY.COLUMN_OVERVIEW + " TEXT, "
                + MOVIE_ENTRY.COLUMN_MOVIE_ID + " TEXT, "
                + MOVIE_ENTRY.COLUMN_POSTER_PATH + " TEXT, "
                + MOVIE_ENTRY.COLUMN_BACK_POSTER + " TEXT, "
                + MOVIE_ENTRY.COLUMN_VOTE_COUNT + " TEXT, "
                + MOVIE_ENTRY.COLUMN_GENRES + " TEXT"
                + " );";

        db.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +  MOVIE_ENTRY.TABLE_NAME);
        onCreate(db);
    }
}
