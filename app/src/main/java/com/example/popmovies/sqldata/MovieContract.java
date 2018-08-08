package com.example.popmovies.sqldata;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {
    public static final String AUTHORITY = "com.example.popmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    //Favourite movies table
    public static final class MOVIE_ENTRY implements BaseColumns{

        public static final Uri BASE_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static final String TABLE_NAME = "favourites";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_RATE = "rate";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POSTER_PATH = "poster";
        public static final String COLUMN_BACK_POSTER = "posterBack";
        public static final String COLUMN_VOTE_COUNT = "voteCount";
        public static final String COLUMN_GENRES = "genres";

    }
}
