package com.example.popmovies.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
* This class is to be used with Retrofit only to get the data
* Since the returned response from the API is an object containing the list of movies, We need to get the data object first
* and then we get the movies list
* The same for videos and reviews*/

public class MoviesData {

    private static final String JSON_RESULTS_ARRAY = "results";

    public class MoviesListWrapper {

        @SerializedName(JSON_RESULTS_ARRAY)
        @Expose
        private List<Movie> moviesList;

        public List<Movie> getMoviesList() {
            return moviesList;
        }
    }

    public class TrailerWrapper {
        @SerializedName(JSON_RESULTS_ARRAY)
        @Expose
        private List<Movie.Trailer> trailerList;

        public List<Movie.Trailer> getTrailerList() {
            return trailerList;
        }
    }

    public class ReviewWrapper{

        @SerializedName(JSON_RESULTS_ARRAY)
        @Expose
        private List<Review> reviewList;

        public List<Review> getReviewList() {
            return reviewList;
        }
    }
}
