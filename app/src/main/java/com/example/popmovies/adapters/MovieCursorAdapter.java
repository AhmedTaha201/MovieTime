package com.example.popmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popmovies.DetailActivity;
import com.example.popmovies.R;
import com.example.popmovies.data.Movie;
import com.example.popmovies.sqldata.MovieContract;
import com.example.popmovies.utils.Utils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.popmovies.MainActivity.MOVIE_EXTRA_NAME;


public class MovieCursorAdapter extends RecyclerView.Adapter<MovieCursorAdapter.MovieViewHolder> {

    private final Context mContext;
    private Cursor mCursor;

    //Columns indices to access cursor data
    private int INDEX_COLUMN_TITLE;
    private int INDEX_COLUMN_YEAR;
    private int INDEX_COLUMN_RATE;
    //private int INDEX_COLUMN_DURATION;
    private int INDEX_COLUMN_OVERVIEW;
    private int INDEX_COLUMN_MOVIE_ID;
    private int INDEX_COLUMN_POSTER;
    private int INDEX_COLUMN_BACK_POSTER;
    private int INDEX_COLUMN_VOTE_COUNT;
    private int INDEX_COLUMN_GENRES;


    public MovieCursorAdapter(Context context, Cursor mCursor) {
        this.mContext = context;
        this.mCursor = mCursor;

        //Initializing constants
        INDEX_COLUMN_TITLE = mCursor.getColumnIndex(MovieContract.MOVIE_ENTRY.COLUMN_TITLE);
        INDEX_COLUMN_YEAR = mCursor.getColumnIndex(MovieContract.MOVIE_ENTRY.COLUMN_YEAR);
        INDEX_COLUMN_RATE = mCursor.getColumnIndex(MovieContract.MOVIE_ENTRY.COLUMN_RATE);
        //INDEX_COLUMN_DURATION = mCursor.getColumnIndex(MovieContract.MOVIE_ENTRY.COLUMN_DURATION);
        INDEX_COLUMN_OVERVIEW = mCursor.getColumnIndex(MovieContract.MOVIE_ENTRY.COLUMN_OVERVIEW);
        INDEX_COLUMN_MOVIE_ID = mCursor.getColumnIndex(MovieContract.MOVIE_ENTRY.COLUMN_MOVIE_ID);
        INDEX_COLUMN_POSTER = mCursor.getColumnIndex(MovieContract.MOVIE_ENTRY.COLUMN_POSTER_PATH);
        INDEX_COLUMN_BACK_POSTER = mCursor.getColumnIndex(MovieContract.MOVIE_ENTRY.COLUMN_BACK_POSTER);
        INDEX_COLUMN_VOTE_COUNT = mCursor.getColumnIndex(MovieContract.MOVIE_ENTRY.COLUMN_VOTE_COUNT);
        INDEX_COLUMN_GENRES = mCursor.getColumnIndex(MovieContract.MOVIE_ENTRY.COLUMN_GENRES);


    }


    @Override
    public MovieCursorAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.favourites_item, parent, false);
        //Item viewHolder
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieCursorAdapter.MovieViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String movieTitle = mCursor.getString(INDEX_COLUMN_TITLE);
        String moviePoster = mCursor.getString(INDEX_COLUMN_BACK_POSTER);
        String movieRate = mCursor.getString(INDEX_COLUMN_RATE);

        //Binding data
        //Poster
        Picasso.with(mContext)
                .load(Utils.getPosterUrl(moviePoster, true))
                .placeholder(R.drawable.placeholder1)
                .error(R.drawable.placeholder1)
                .into(holder.mainPoster);
        //Title
        holder.mainTitle.setText(movieTitle);
        holder.mainRate.setText(movieRate);


    }


    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.back_poster_card)
        ImageView mainPoster;

        @BindView(R.id.movieTitle_card)
        TextView mainTitle;

        @BindView(R.id.movieRate_card)
        TextView mainRate;

        private MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            mCursor.moveToPosition(getAdapterPosition());


        /* Constructing a movie from the data of the cursor to be passed to the detail activity
        * and use the same logic of the recyclerView */


            Movie CurrentMovie = new Movie();

            String movieTitle = mCursor.getString(INDEX_COLUMN_TITLE);
            String movieYear = mCursor.getString(INDEX_COLUMN_YEAR);
            String movieRate = mCursor.getString(INDEX_COLUMN_RATE);
            String movieOverView = mCursor.getString(INDEX_COLUMN_OVERVIEW);
            String moviePoster = mCursor.getString(INDEX_COLUMN_POSTER);
            String movieBackPoster = mCursor.getString(INDEX_COLUMN_BACK_POSTER);
            String movieId = mCursor.getString(INDEX_COLUMN_MOVIE_ID);
            String movieVoteCount = mCursor.getString(INDEX_COLUMN_VOTE_COUNT);
            String movieGenres = mCursor.getString(INDEX_COLUMN_GENRES);

            CurrentMovie.setTitle(movieTitle);
            CurrentMovie.setReleaseDate(movieYear);
            CurrentMovie.setRate(movieRate);
            CurrentMovie.setOverView(movieOverView);
            CurrentMovie.setPosterUrl(moviePoster);
            CurrentMovie.setBackPoster(movieBackPoster);
            CurrentMovie.setTmdbId(movieId);
            CurrentMovie.setVoteCount(movieVoteCount);
            CurrentMovie.setGenreString(movieGenres);

            Intent detailIntent = new Intent(mContext, DetailActivity.class);
            detailIntent.putExtra(MOVIE_EXTRA_NAME, CurrentMovie);

            //Transition options
            //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, v.findViewById(R.id.main_movie_poster), mContext.getString(R.string.poster_transition));

            mContext.startActivity(detailIntent);//, options.toBundle());
        }
    }

    public void swapCursor(Cursor mCursor) {
        this.mCursor = mCursor;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) return mCursor.getCount();
        return 0;
    }
}