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


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private List<Movie.Trailer> mTrailerList;
    private Context mContext;
    private onTrailerClickListener mOnCLickListener;

    public TrailerAdapter(Context context, onTrailerClickListener mOnClickListener, List<Movie.Trailer> trailerList) {
        this.mContext = context;
        this.mOnCLickListener = mOnClickListener;
        this.mTrailerList = trailerList;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.trailer_item_view, parent, false);

        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Movie.Trailer trailer = mTrailerList.get(position);
        //Thumbnail
        Picasso.with(mContext)
                .load(Utils.getYoutubeThumbnail(trailer.getKey()))
                .into(holder.thumbnail);

        //Title
        holder.title.setText(trailer.getName());

    }

    @Override
    public int getItemCount() {
        if (mTrailerList != null && mTrailerList.size() != 0) {
            return mTrailerList.size();
        }
        return 0;
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.trailer_thumbnail)
        ImageView thumbnail;

        @BindView(R.id.trailer_title)
        TextView title;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnCLickListener.onTrailerItemClick(getAdapterPosition());
        }
    }

    //Getting the list of trailers to show
    public void setTrailerList(List<Movie.Trailer> trailerList) {
        if (trailerList != null && trailerList.size() != 0) {
            this.mTrailerList = trailerList;
            notifyDataSetChanged();
        }
    }

    public interface onTrailerClickListener {
        void onTrailerItemClick(int position);
    }
}