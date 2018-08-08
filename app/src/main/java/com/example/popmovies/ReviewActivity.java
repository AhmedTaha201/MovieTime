package com.example.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.popmovies.data.Review;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewActivity extends AppCompatActivity {

    @BindView(R.id.tv_reviewer_name)
    TextView mReviewerName;

    @BindView(R.id.tv_review)
    TextView mReviewContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(DetailActivity.REVIEW_EXTRA_KEY)){
            Review review = intent.getParcelableExtra(DetailActivity.REVIEW_EXTRA_KEY);

            mReviewerName.setText(review.getAuthor());
            mReviewContent.setText(review.getContent());
        }else {
            Log.e(ReviewActivity.class.getSimpleName(), "No extra Review");
        }
    }
}
