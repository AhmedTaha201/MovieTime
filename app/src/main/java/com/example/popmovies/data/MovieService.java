package com.example.popmovies.data;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Taha on 2/21/2018.
 */

public interface MovieService {

    //The query parameter used to get movie with its revies and videos
    String APPEND_PARAMETERS = "videos,reviews";

    @GET("{searchFor}")
        //searchFor could be {popular} or {top_rated}
    Call<MoviesData.MoviesListWrapper> getMovieList(@Path("searchFor") String searchFor, @Query("api_key") String apiKey);

    @GET("{id}")
    Call<Movie> getFullMovieWithID(@Path("id") String id, @Query("append_to_response") String append, @Query("api_key") String apiKey);

    @GET ("{id}/videos")
    Call<MoviesData.TrailerWrapper> getMovieVideos(@Path("id") String id, @Query("api_key") String apiKey);

    @GET ("{id}/reviews")
    Call<MoviesData.ReviewWrapper> getMovieReviews(@Path("id") String id, @Query("api_key") String apiKey);
}
