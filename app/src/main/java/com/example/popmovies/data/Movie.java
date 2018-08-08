package com.example.popmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.popmovies.utils.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Movie implements Parcelable {


    /* Json parsing constants*/
    private static final String JSON_MOVIE_TITLE = "original_title";
    private static final String JSON_MOVIE_POSTER = "poster_path";
    private static final String JSON_MOVIE_BACK_POSTER = "backdrop_path";
    private static final String JSON_MOVIE_OVERVIEW = "overview";
    private static final String JSON_MOVIE_RATE = "vote_average";
    private static final String JSON_MOVIE_RELEASE_DATE = "release_date";
    private static final String JSON_MOVIE_ID = "id";
    private static final String JSON_MOVIE_GENRE_IDS = "genre_ids";
    private static final String JSON_MOVIE_RUNTIME = "runtime";
    private static final String JSON_MOVIE_TAG_LINE = "tagline";
    private static final String JSON_MOVIE_VOTE_COUNT = "vote_count";
    private static final String JSON_MOVIE_BUDGET = "budget";
    private static final String JSON_MOVIE_REVENUE = "revenue";
    private static final String JSON_MOVIE_TRAILERS = "videos";
    private static final String JSON_MOVIE_REVIEWS = "reviews";
    private static final String JSON_MOVIE_GENRES = "genres";


    @SerializedName(JSON_MOVIE_TITLE)
    @Expose
    private String title;

    @SerializedName(JSON_MOVIE_OVERVIEW)
    @Expose
    private String overView;

    @SerializedName(JSON_MOVIE_RATE)
    @Expose
    private String rate;

    @SerializedName(JSON_MOVIE_RELEASE_DATE)
    @Expose
    private String releaseDate;

    @SerializedName(JSON_MOVIE_ID)
    @Expose
    private String tmdbId;

    @SerializedName(JSON_MOVIE_POSTER)
    @Expose
    //This is just the last portion of the url
    //WE can get the full Url using Utils.getPosterUrl()
    private String posterUrl;

    @SerializedName(JSON_MOVIE_BACK_POSTER)
    @Expose
    private String backPoster;

    @SerializedName(JSON_MOVIE_GENRE_IDS)
    @Expose
    private List<Integer> genresIds;

    private String genreString;

    @SerializedName(JSON_MOVIE_RUNTIME)
    @Expose
    private String runtime;

    @SerializedName(JSON_MOVIE_TAG_LINE)
    @Expose
    private String tagLine;

    @SerializedName(JSON_MOVIE_VOTE_COUNT)
    @Expose
    private String voteCount;

    @SerializedName(JSON_MOVIE_BUDGET)
    @Expose
    private String budget;

    @SerializedName(JSON_MOVIE_REVENUE)
    @Expose
    private String revenue;

    @SerializedName(JSON_MOVIE_TRAILERS)
    @Expose
    private MoviesData.TrailerWrapper mTrailerWrapper;

    @SerializedName(JSON_MOVIE_REVIEWS)
    @Expose
    private MoviesData.ReviewWrapper mReviewsWrapper;

    @SerializedName(JSON_MOVIE_GENRES)
    @Expose
    private List<Genre> genreList;

    public Movie(){

    }


    protected Movie(Parcel in) {
        //Read in the same order as it was written
        title = in.readString();
        overView = in.readString();
        rate = in.readString();
        releaseDate = in.readString();
        posterUrl = in.readString();
        backPoster = in.readString();
        tmdbId = in.readString();
        voteCount = in.readString();
        genreString = in.readString();
        genresIds = in.readArrayList(List.class.getClassLoader());

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(overView);
        dest.writeString(rate);
        dest.writeString(releaseDate);
        dest.writeString(posterUrl);
        dest.writeString(backPoster);
        dest.writeString(tmdbId);
        dest.writeString(voteCount);
        dest.writeString(genreString);
        dest.writeList(genresIds);

    }

    @Override
    public int describeContents() {
        return 0;
    }


    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return title;
    }


    public String getOverView() {
        return overView;
    }

    public String getRate() {
        return rate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getBackPoster() {
        return backPoster;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTmdbId() {
        return tmdbId;
    }

    public List<Integer> getGenresIds() {
        return genresIds;
    }

    public void setGenresIds(List<Integer> genresIds) {
        this.genresIds = genresIds;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getTagLine() {
        return tagLine;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public String getBudget() {
        return budget;
    }

    public String getRevenue() {
        return revenue;
    }

    public List<Trailer> getTrailerList() {
        return mTrailerWrapper.getTrailerList();
    }

    public List<Review> getReviewList() {
        return mReviewsWrapper.getReviewList();
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setTmdbId(String tmdbId) {
        this.tmdbId = tmdbId;
    }

    public List<Genre> getGenreList() {
        return genreList;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setBackPoster(String backPoster) {
        this.backPoster = backPoster;
    }

    public String getGenreString() {
        return genreString;
    }

    public void setGenreString(String genreString) {
        this.genreString = genreString;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public class Trailer implements Parcelable{

        /* Trailer constants */

        private static final String JSON_TRAILER_NAME = "name";
        private static final String JSON_TRAILER_KEY = "key";
        private static final String JSON_TRAILER_TYPE = "type";
        private static final String JSON_TRAILER_SITE = "site";

        @SerializedName(JSON_TRAILER_NAME)
        @Expose
        private String name;

        @SerializedName(JSON_TRAILER_KEY)
        @Expose
        private String key;

        @SerializedName(JSON_TRAILER_SITE)
        @Expose
        private String site;

        @SerializedName(JSON_TRAILER_TYPE)
        @Expose
        private String type;

        protected Trailer(Parcel in) {
            name = in.readString();
            key = in.readString();
            site = in.readString();
            type = in.readString();
        }

        public  final Creator<Trailer> CREATOR = new Creator<Trailer>() {
            @Override
            public Trailer createFromParcel(Parcel in) {
                return new Trailer(in);
            }

            @Override
            public Trailer[] newArray(int size) {
                return new Trailer[size];
            }
        };

        public String getName() {
            return name;
        }

        public String getKey() {
            return key;
        }

        public String getSite() {
            return site;
        }

        public String getType() {
            return type;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeString(key);
            dest.writeString(site);
            dest.writeString(type);
        }
    }



    public class Genre implements Parcelable{
        /* Genre constants */

        private static final String JSON_GENRE_ID = "id";
        private static final String JSON_GENRE_NAME = "name";

        @SerializedName(JSON_GENRE_ID)
        @Expose
        private int id;

        @SerializedName(JSON_GENRE_NAME)
        @Expose
        private String name;

        protected Genre(Parcel in) {
            id = in.readInt();
            name = in.readString();
        }

        public  final Creator<Genre> CREATOR = new Creator<Genre>() {
            @Override
            public Genre createFromParcel(Parcel in) {
                return new Genre(in);
            }

            @Override
            public Genre[] newArray(int size) {
                return new Genre[size];
            }
        };

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
        }
    }
}
