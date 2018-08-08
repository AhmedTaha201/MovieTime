package com.example.popmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.popmovies.adapters.ReviewAdapter;
import com.example.popmovies.adapters.TrailerAdapter;
import com.example.popmovies.data.Movie;
import com.example.popmovies.data.MovieService;
import com.example.popmovies.data.MoviesData;
import com.example.popmovies.data.Review;
import com.example.popmovies.sqldata.MovieContract;
import com.example.popmovies.utils.Utils;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.onTrailerClickListener, ReviewAdapter.onReviewClickListener {

    private static final String TAG = "DetailActivity";

    public static final String REVIEW_EXTRA_KEY = "reviewww";

    //Used to save the current movie in a bundle onSaveInstanceState
    private final static String MOVIE_BUNDLE_KEY = "BUNDLE_MOVIE";

    Movie mCurrentMovie;

    Uri mMovieUri;

    TrailerAdapter mTrailerAdapter;

    List<Movie.Trailer> mTrailerList;

    ReviewAdapter mReviewAdapter;

    List<Review> mReviewList;

    String mMovieId;

    @BindView(R.id.tv_title)
    TextView mTitle;

    @BindView(R.id.poster)
    ImageView mPoster;

    @BindView(R.id.tv_rate)
    TextView mRateTextView;

    @BindView(R.id.rate_bar)
    RatingBar mRateBar;

    @BindView(R.id.vote_count)
    TextView mVoteCount;

    @BindView(R.id.shine_heart)
    ShineButton mShineButton;

    @BindView(R.id.tv_genres)
    TextView mGenreTV;

    @BindView(R.id.tv_quote)
    TextView mQuoteTV;

    @BindView(R.id.divider2)
    View mQuoteDivider;

    @BindView(R.id.tv_overview)
    TextView mOverview;

    @BindView(R.id.tv_duration)
    TextView mDurationTV;

    @BindView(R.id.tv_budget)
    TextView mBudgetTV;

    @BindView(R.id.tv_revenue)
    TextView mRevenueTV;

    @BindView(R.id.trailer_recycler_view)
    RecyclerView mTrailerRecyclerView;

    @BindView(R.id.review_recycler_view)
    RecyclerView mReviewRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_BUNDLE_KEY)) {
            mCurrentMovie = savedInstanceState.getParcelable(MOVIE_BUNDLE_KEY);
        } else {

            //Getting the Movie object from the intent
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra(MainActivity.MOVIE_EXTRA_NAME)) {
                mCurrentMovie = intent.getParcelableExtra(MainActivity.MOVIE_EXTRA_NAME);
            }
        }

        if (mCurrentMovie != null) {

            mMovieId = mCurrentMovie.getTmdbId();
            mMovieUri = ContentUris.withAppendedId(MovieContract.MOVIE_ENTRY.BASE_URI, Long.parseLong(mMovieId));
            String apiKey = getString(R.string.API_KEY);

            /**/
            populateViews();
            /**/


            /* Getting Trailers and reviews*/

            //Retrofit object to get reviews and trailers and other movie details
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Utils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            //Movies service
            MovieService service = retrofit.create(MovieService.class);


            //Fetching additional data
            Call<Movie> movieCall = service.getFullMovieWithID(mMovieId, MovieService.APPEND_PARAMETERS, apiKey);
            movieCall.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    mCurrentMovie = response.body();

                    if (mCurrentMovie == null) return;
                    //Other data - Duration, Budget, ...
                    mCurrentMovie.setGenresIds(Utils.getGenresIdList(mCurrentMovie.getGenreList()));
                    populateViews();

                    //Trailers
                    mTrailerList = mCurrentMovie.getTrailerList();
                    mTrailerAdapter.setTrailerList(mTrailerList);

                    //Reviews
                    mReviewList = mCurrentMovie.getReviewList();
                    mReviewAdapter.setReviewList(mReviewList);

                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {
                    Log.e(TAG, "Failed to get additional data for the movie");
                    t.printStackTrace();
                }
            });


        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE_BUNDLE_KEY, mCurrentMovie);
    }

    //Populating views in detail activity
    private void populateViews() {

        //Populating views
        if (mCurrentMovie != null) {
            //Title
            final String movieTitle = mCurrentMovie.getTitle();
            mTitle.setText(movieTitle);
            setTitle(movieTitle);

            //Rate
            final String movieRate = mCurrentMovie.getRate();
            mRateTextView.setText(movieRate);
            Float floatRate = Float.valueOf(movieRate) / 2;
            mRateBar.setRating(floatRate);

            // Vote count
            final String movieVoteCount = mCurrentMovie.getVoteCount();
            mVoteCount.setText(movieVoteCount);

            //Release date
            final String movieYear = mCurrentMovie.getReleaseDate().substring(0, 4);
            mTitle.append(getString(R.string.year_string, movieYear));

            //Duration
            final String duration = Utils.getDurationString(this ,mCurrentMovie.getRuntime());
            mDurationTV.setText(duration);

            //TagLine
            final String tagLine = mCurrentMovie.getTagLine();
            if (tagLine == null || TextUtils.isEmpty(tagLine)) {
                mQuoteTV.setVisibility(View.GONE);
                mQuoteDivider.setVisibility(View.GONE);
            } else {
                mQuoteTV.setVisibility(View.VISIBLE);
                mQuoteDivider.setVisibility(View.VISIBLE);
                mQuoteTV.setText(tagLine);
            }

            //Budget
            String budget = mCurrentMovie.getBudget();
            mBudgetTV.setText(budget == null || TextUtils.isEmpty(budget) ? "N/A" : Utils.getMoneyAmountString(this, Integer.parseInt(budget)));

            //Revenue
            String revenue = mCurrentMovie.getRevenue();
            mRevenueTV.setText(revenue == null || TextUtils.isEmpty(revenue) ? "N/A" : Utils.getMoneyAmountString(this, Integer.parseInt(revenue)));

            //Genres
            String genreString = mCurrentMovie.getGenreString();
            if (genreString == null) {
                genreString = Utils.getGenresString(mCurrentMovie.getGenresIds());
            }
            mGenreTV.setText(genreString);
            //Overview
            final String movieOverView = mCurrentMovie.getOverView();
            mOverview.setText(movieOverView);

            //Poster
            final String moviePoster = mCurrentMovie.getPosterUrl();
            String fullUrl = Utils.getPosterUrl(moviePoster, true);

            Picasso.with(this)
                    .load(fullUrl)
                    .error(R.drawable.placeholder1)
                    .placeholder(R.drawable.placeholder1)
                    .into(mPoster);

            mPoster.setContentDescription(getString(R.string.a11y_main_poster, movieTitle));

            //Shine button initialization
            mShineButton.setChecked(isFavourite());

            mShineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFavourite()) {
                        int deleted = getContentResolver().delete(mMovieUri, null, null);
                        if (deleted == 1) { //Deleted successfully
                            mShineButton.setChecked(false);
                        }
                    } else {
                        //Add the movie to favourites
                        ContentValues cv = makeMovieValues(mCurrentMovie);


                        Uri addedUri = getContentResolver().insert(MovieContract.MOVIE_ENTRY.BASE_URI, cv);
                        mShineButton.setChecked(addedUri != null);
                    }
                }
            });
            //Preparing trailers` recyclerView
            mTrailerAdapter = new TrailerAdapter(this, this, null);
            mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            mTrailerRecyclerView.setAdapter(mTrailerAdapter);

            //Preparing reviews` recyclerView
            mReviewAdapter = new ReviewAdapter(this, this, null);
            mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            mReviewRecyclerView.setAdapter(mReviewAdapter);
        }

    }

    public static ContentValues makeMovieValues(Movie movie) {

        ContentValues cv = new ContentValues();

        cv.put(MovieContract.MOVIE_ENTRY.COLUMN_TITLE, movie.getTitle());
        cv.put(MovieContract.MOVIE_ENTRY.COLUMN_YEAR, movie.getReleaseDate());
        cv.put(MovieContract.MOVIE_ENTRY.COLUMN_RATE, movie.getRate());
        //cv.put(MovieContract.MOVIE_ENTRY.COLUMN_DURATION, "");
        cv.put(MovieContract.MOVIE_ENTRY.COLUMN_OVERVIEW, movie.getOverView());
        cv.put(MovieContract.MOVIE_ENTRY.COLUMN_MOVIE_ID, movie.getTmdbId());
        cv.put(MovieContract.MOVIE_ENTRY.COLUMN_POSTER_PATH, movie.getPosterUrl());
        cv.put(MovieContract.MOVIE_ENTRY.COLUMN_BACK_POSTER, movie.getBackPoster());
        cv.put(MovieContract.MOVIE_ENTRY.COLUMN_VOTE_COUNT, movie.getVoteCount());

        String genreString = movie.getGenreString();
        if (genreString == null) {
            genreString = Utils.getGenresString(movie.getGenresIds());
        }
        cv.put(MovieContract.MOVIE_ENTRY.COLUMN_GENRES, genreString);

        return cv;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setChooserTitle(R.string.trailer_share_chooser_title)
                    .setText(getString(R.string.trailer_share_text, mCurrentMovie.getTitle(), getLatestTrailerUrl()))
                    .startChooser();
            return true;
        }
        return false;
    }

    @Override
    public void onTrailerItemClick(int position) {
        //Getting the current trailer object
        Movie.Trailer currentTrailer = mTrailerList.get(position);
        String site = currentTrailer.getSite();
        String trailerKey = currentTrailer.getKey();

        if (site.equals(getString(R.string.youtube_site_name))) {
            Uri trailerUri = Uri.parse(Utils.getYoutubeUrl(trailerKey));

            Intent trailerIntent = new Intent(Intent.ACTION_VIEW, trailerUri);
            if (getPackageManager().resolveActivity(trailerIntent, 0) != null) {
                startActivity(trailerIntent);
            }
        }
    }

    @Override
    public void onReviewClick(int position) {
        //Getting the clicked review from the list
        Review clickedReview = mReviewList.get(position);
        //Putting the clicked review in an intent and sending it to the review activity

        Intent reviewIntent = new Intent(this, ReviewActivity.class);
        reviewIntent.putExtra(REVIEW_EXTRA_KEY, clickedReview);
        startActivity(reviewIntent);
    }

    //  A helper method to query the database for a certain movie with its ID and return true if it`s in the base
    public boolean isFavourite() {
        Cursor movieCursor = getContentResolver().query(mMovieUri,
                null,
                null,
                null,
                null);

        if (movieCursor == null) return false;
        return movieCursor.moveToFirst();
    }

    //A helper method to get the latest trailer from videos{Including trailers and other related videos} list
    private String getLatestTrailerUrl() {
        if (mTrailerList == null && mTrailerList.size() <= 0) {
            return null;
        }

        //Iterating through the list to find a trailer
        String url = null;
        for (Movie.Trailer t : mTrailerList) {
            String type = t.getType();
            if (type.equals(getString(R.string.video_type_trailer))) {
                url = Utils.getYoutubeUrl(t.getKey());
                break;
            }
        }
        if (mTrailerList != null && mTrailerList.size() > 0 && url == null) { // If there is no trailers but there are other video type we get the first video`s url to share
            url = Utils.getYoutubeUrl(mTrailerList.get(0).getKey());
        }
        return url;
    }

}