package com.example.popmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.popmovies.R;
import com.example.popmovies.data.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    Context mContext;
    List<Review> mReviewList;
    onReviewClickListener mClickListener;


    public ReviewAdapter(Context mContext,onReviewClickListener mClickListener, List<Review> mReviewList) {
        this.mContext = mContext;
        this.mReviewList = mReviewList;
        this.mClickListener = mClickListener;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.review_list_item, parent, false);


        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {

        Review currentReview = mReviewList.get(position);

        holder.mReviewerTV.setText(currentReview.getAuthor());
        holder.mReview.setText(currentReview.getContent());

    }

    @Override
    public int getItemCount() {
        if (mReviewList != null && mReviewList.size() != 0) return mReviewList.size();
        return 0;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_reviewer_name)
        TextView mReviewerTV;

        @BindView(R.id.tv_review)
        TextView mReview;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickListener.onReviewClick(getAdapterPosition());
        }
    }

    public void setReviewList(List<Review> reviewList) {
        if (reviewList != null && reviewList.size() != 0) {
            mReviewList = reviewList;
            notifyDataSetChanged();
        }
    }

    public interface onReviewClickListener{
        void onReviewClick(int position);
    }
}
