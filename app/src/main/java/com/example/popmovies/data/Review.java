package com.example.popmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Review implements Parcelable {

        /* Review constants */

    private static final String JSON_REVIEW_AUTHOR = "author";
    private static final String JSON_REVIEW_CONTENT = "content";


    @SerializedName(JSON_REVIEW_AUTHOR)
    @Expose
    private String author;

    @SerializedName(JSON_REVIEW_CONTENT)
    @Expose
    private String content;

    protected Review(Parcel in) {
        author = in.readString();
        content = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(author);
        dest.writeString(content);
    }

}

