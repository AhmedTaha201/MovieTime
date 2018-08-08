package com.example.popmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popmovies.R;
import com.example.popmovies.data.Movie;
import com.example.popmovies.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> mMovieList;
    private final Context mContext;
    private final onListItemClickListener mClickHandler;

    public MovieAdapter(Context context, List<Movie> mMovieList, onListItemClickListener mClickHandler) {
        this.mContext = context;
        this.mMovieList = mMovieList;
        this.mClickHandler = mClickHandler;
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Inflating item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movies_recycler_view_item, parent, false);
        //Item viewHolder
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        if (mMovieList != null) {
            Movie movie = mMovieList.get(position);

            //Movie title
            String movieTitle = movie.getTitle();
            holder.mainTitle.setText(movieTitle);

            //Movie Poster
            String movieImage = Utils.getPosterUrl(movie.getPosterUrl(), false);
            Picasso.with(mContext)
                    .load(movieImage)
                    .error(R.drawable.placeholder1)
                    .placeholder(R.drawable.placeholder1)
                    .into(holder.mainPoster);
            //Content description for the poster
            holder.mainPoster.setContentDescription(mContext.getString(R.string.a11y_main_poster, movieTitle));
        }

    }

    @Override
    public int getItemCount() {
        if (mMovieList != null) return mMovieList.size();
        return 0;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.main_movie_poster)
        ImageView mainPoster;
        @BindView(R.id.main_movie_title)
        TextView mainTitle;

        private MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickHandler.onItemClick(v, position);

        }
    }

    public void setMovieList(List<Movie> movieList) {
        this.mMovieList = movieList;
        if(movieList != null) notifyDataSetChanged();
    }

    public interface onListItemClickListener {
        //Passing the view to use transitions
        void onItemClick(View view, int pos);
    }

}
