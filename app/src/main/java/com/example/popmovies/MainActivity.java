package com.example.popmovies;

/* The app design is mostly inspired from this design
* https://www.uplabs.com/posts/conceptual-movie-app-ui
*
* The icons are downloaded from flaticon.com*/

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.popmovies.adapters.MovieAdapter;
import com.example.popmovies.adapters.MovieCursorAdapter;
import com.example.popmovies.data.MovieService;
import com.example.popmovies.data.Movie;
import com.example.popmovies.data.MoviesData;
import com.example.popmovies.sqldata.MovieContract;
import com.example.popmovies.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, LoaderManager.LoaderCallbacks<Cursor>,
        MovieAdapter.onListItemClickListener {

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.recycler_view_main)
    RecyclerView mRecyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    //The root layout for the SnackBar
    @BindView(R.id.frame_layout)
    View mFrameLayout;

    @BindView(R.id.tv_error)
    TextView errorTextView;

    MovieAdapter mAdapter;

    MovieCursorAdapter mCursorAdapter;

    public static final int LOADER_FAVOURITES_ID = 222;

    //The shared preferences value for the list order
    String mMoviesQuery;

    /*
    The movies list is saved to get the clicked object and pass it in the intent, and also to be save dto the save instance state
     */
    List<Movie> mMovieList;

    //Movie object extra name
    public final static String MOVIE_EXTRA_NAME = "movie";

    //Span count for the GridView
    private static final int GRID_LAYOUT_SPAN = 2;

    SharedPreferences mSharedPreferences;

    //Error code for handling no connection and no Api
    final static int CODE_NO_CONNECTION = 22;
    final static int CODE_NO_API = 24;
    final static int CODE_NO_FAVOURITES = 26;

    //Used to store and retrieve movies list from the saved state bundle
    private final static String MOVIES_BUNDLE_KEY = "moviesData";
    private final static String QUERY_BUNDLE_KEY = "queryType";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }


        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();
                SharedPreferences.Editor editor = mSharedPreferences.edit();

                switch (item.getItemId()) {
                    case R.id.menu_item_popular:
                        editor.putString(getString(R.string.pref_list_order_key), getString(R.string.pref_list_popular_value));
                        editor.apply();
                        setTitle(getString(R.string.pref_list_popular_label));
                        return true;

                    case R.id.menu_item_top:
                        editor.putString(getString(R.string.pref_list_order_key), getString(R.string.pref_list_top_rated_value));
                        editor.apply();
                        setTitle(getString(R.string.pref_list_top_rated_label));
                        return true;

                    case R.id.menu_item_favourites:
                        editor.putString(getString(R.string.pref_list_order_key), getString(R.string.pref_list_favourites_value));
                        editor.apply();
                        setTitle(getString(R.string.pref_list_favourites_label));
                        return true;

                }
                return false;
            }
        });

        //SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mMoviesQuery = sharedPreferences.getString(getString(R.string.pref_list_order_key), getString(R.string.pref_list_order_default_value));

        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIES_BUNDLE_KEY)) {
            mMovieList = savedInstanceState.getParcelableArrayList(MOVIES_BUNDLE_KEY);
            String savedQueryType = savedInstanceState.getString(QUERY_BUNDLE_KEY);
            if (mMovieList != null && savedQueryType != null && savedQueryType.equals(mMoviesQuery)) {

                mAdapter = new MovieAdapter(this, mMovieList, this);

                mRecyclerView.setAdapter(mAdapter);

                mRecyclerView.setLayoutManager(new GridLayoutManager(this, GRID_LAYOUT_SPAN));
                return;
            }
        }

        //Updating data with a retrofit call
        updateMoviesList();


    }

    // A helper method to update the movies list
    private void updateMoviesList() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mMoviesQuery = sharedPreferences.getString(getString(R.string.pref_list_order_key), getString(R.string.pref_list_order_default_value));


        if (!Utils.isConnected(this) && !(mMoviesQuery.equals(getString(R.string.pref_list_favourites_value)))) {
            showErrorMessage(CODE_NO_CONNECTION);
            return;
        }

        String apiKey = getString(R.string.API_KEY);
        if (apiKey.equals("")) {
            showErrorMessage(CODE_NO_API);
            return;
        }

        //Clearing the recyclerView
        mRecyclerView.setAdapter(null);

        //Showing favourites if it`s the case
        if (mMoviesQuery.equals(getString(R.string.pref_list_favourites_value))) {

            mRecyclerView.setLayoutManager(new GridLayoutManager(this, GRID_LAYOUT_SPAN));

            //Clearing the adapter
            if (mAdapter != null) mAdapter.setMovieList(null);

            showLoadingBar();

            LoaderManager manager = getLoaderManager();
            Loader loader = manager.getLoader(LOADER_FAVOURITES_ID);
            if (loader != null) {
                manager.restartLoader(LOADER_FAVOURITES_ID, null, this);
            } else {
                manager.initLoader(LOADER_FAVOURITES_ID, null, this);
            }

        } else {
            //Clearing the cursor adapter
            /*After querying The database for Favourite movies a cursor is returned with a notification Uri and if i returned to the popular or top rated movies
            and added a new movie to the favourites the cursor gets notified and triggers the adapter to show its data again while it should show Popular or top rated
             that is why we are clearing the adapter`s reference*/
            mCursorAdapter = null;


            mAdapter = new MovieAdapter(this, null, this);

            mRecyclerView.setAdapter(mAdapter);

            mRecyclerView.setLayoutManager(new GridLayoutManager(this, GRID_LAYOUT_SPAN));


            //Retrofit object
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Utils.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            //Service class
            MovieService service = retrofit.create(MovieService.class);


            Call<MoviesData.MoviesListWrapper> moviesCall = service.getMovieList(mMoviesQuery, apiKey);


            //Showing the Loading indicator
            showLoadingBar();

            //Enqueuing the call
            moviesCall.enqueue(new Callback<MoviesData.MoviesListWrapper>() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful() && response.body() != null) {

                        MoviesData.MoviesListWrapper data = (MoviesData.MoviesListWrapper) response.body();
                        if (data != null) {
                            mMovieList = data.getMoviesList();

                            //Hiding the Loading indicator
                            hideLoadingBarAndError();
                            //Updating the adapter with new data
                            mAdapter.setMovieList(mMovieList);
                            //Scrolling to the top
                            mRecyclerView.scrollToPosition(0);
                        }
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!(mMoviesQuery.equals(getString(R.string.pref_list_favourites_value))) && mMovieList != null) {//Favourite movies
            // Top Rated || Popular
            outState.putParcelableArrayList(MOVIES_BUNDLE_KEY, (ArrayList<? extends Parcelable>) mMovieList);
            outState.putString(QUERY_BUNDLE_KEY, mMoviesQuery);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawers();
                return true;
            }
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_list_order_key))) {
            updateMoviesList();
        }
    }


    //Showing the loading progress bar and hiding the recyclerView
    private void showLoadingBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    //Hiding the loading  bar and showing data
    private void hideLoadingBarAndError() {
        mProgressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

    }

    private void showErrorMessage(int errorCode) {

        mProgressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);

        if (errorCode == CODE_NO_API) {
            errorTextView.setText(R.string.error_no_api_msg);
        } else if (errorCode == CODE_NO_CONNECTION) {
            errorTextView.setText(R.string.error_no_connection_msg);
            Snackbar.make(mFrameLayout, R.string.error_no_connection_msg, Snackbar.LENGTH_LONG)
                    .show();
        } else if (errorCode == CODE_NO_FAVOURITES) {
            errorTextView.setText(getString(R.string.error_no_favourites));
        }
    }

    @Override
    public void onItemClick(View view, int pos) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        Movie clickedMovie = mMovieList.get(pos);
        detailIntent.putExtra(MOVIE_EXTRA_NAME, clickedMovie);

        //Transition options
        //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view.findViewById(R.id.main_movie_poster), getString(R.string.poster_transition));
        startActivity(detailIntent);//, options.toBundle());
    }


    //Loader Callbacks to query the database for favourite movies
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                MovieContract.MOVIE_ENTRY.BASE_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data == null || !data.moveToFirst()){
            showErrorMessage(CODE_NO_FAVOURITES);
            return;
        }
        mCursorAdapter = new MovieCursorAdapter(this, data);


        //Hiding the Loading indicator
        hideLoadingBarAndError();

        if (mMoviesQuery.equals(getString(R.string.pref_list_favourites_value))) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(mCursorAdapter);

            mRecyclerView.scrollToPosition(0);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}