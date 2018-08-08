package com.example.popmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;

import com.example.popmovies.R;
import com.example.popmovies.data.Movie;

import java.util.ArrayList;
import java.util.List;

/* Utils contains helper methods to make the internet call and get the result as a string*/

public class Utils {

    public static final String BASE_URL = "http://api.themoviedb.org/3/movie/";


    //checking network connectivity
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnectedOrConnecting());
    }

    /**
     * Helper  method to convert the poster image path to a full loadable url
     * to be used with Picasso
     * <p>
     * {\/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg} ==to==> {http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg}
     *
     * @param backPoster Determines whether it is a backDrop image or not to get it in slightly higher resolution}
     */
    public static String getPosterUrl(String imagePath, boolean backPoster) {
        int width = 185;
        if (backPoster) {
            width = 780;
        }
        final String BASE_URL = "http://image.tmdb.org/t/p/w" + width + "/";
        String path = imagePath.replace("\\", "/");

        return BASE_URL + path;
    }


    // Example == > http://img.youtube.com/vi/2LqzF5WauAw/0.jpg
    public static String getYoutubeThumbnail(String path) {
        Uri.Builder builder = Uri.parse("http://img.youtube.com/vi/")
                .buildUpon().appendPath(path)
                .appendPath("0.jpg");

        return builder.build().toString();

    }

    public static String getYoutubeUrl(String videoKey) {
        return "https://www.youtube.com/watch?v=" + videoKey;
    }

    private static String getGenre(int id) {
        switch (id) {
            case 28:
                return "Action";

            case 12:
                return "Adventure";

            case 16:
                return "Animation";

            case 35:
                return "Comedy";

            case 80:
                return "Crime";

            case 99:
                return "Documentary";

            case 18:
                return "Drama";

            case 10751:
                return "Family";

            case 14:
                return "Fantasy";

            case 36:
                return "History";

            case 27:
                return "Horror";

            case 10402:
                return "Music";

            case 9648:
                return "Mystery";

            case 10749:
                return "Romance";

            case 878:
                return "Science Fiction";

            case 10770:
                return "TV Movie";

            case 53:
                return "Thriller";

            case 10752:
                return "War";

            case 37:
                return "Western";
            default:
                return null;
        }
    }

    public static List<Integer> getGenresIdList(List<Movie.Genre> genres) {
        List<Integer> genreIds = new ArrayList<>(genres.size());
        for (int i = 0; i < genres.size(); i++) {
            int id = genres.get(i).getId();
            genreIds.add(id);
        }
        return genreIds;
    }

    public static String getGenresString(List<Integer> genresList) {

        if (genresList == null || genresList.size() == 0) return "";

        StringBuilder builder = new StringBuilder();
        builder.append(" ");

        for (int i = 0; i < genresList.size(); i++) {
            if (i > 0) builder.append(", ");
            builder.append(Utils.getGenre(genresList.get(i)));
        }

        return builder.toString();
    }

    public static String getDurationString(Context context, String minutes) {
        if (minutes == null || TextUtils.isEmpty(minutes)) {
            return "N/A";
        } else {
            int minutesInt = Integer.parseInt(minutes);
            String duration;
            if (minutesInt < 60) {
                duration = context.getString(R.string.duration_string, String.valueOf(0), String.valueOf(minutes));
            } else {
                int hrs = minutesInt / 60;
                int mins = minutesInt % 60;
                duration = context.getString(R.string.duration_string, String.valueOf(hrs), String.valueOf(mins));
            }
            return duration;
        }
    }

    public static String getMoneyAmountString(Context context,int moneyAmount) {
        String moneyString;

        int billions = moneyAmount / 1000000000;
        if (billions > 0) {
            String amount = String.valueOf(moneyAmount / 1000000000);
            moneyString = context.getString(R.string.money_amount_billion, amount);
        }else {
            String amount = String.valueOf(moneyAmount / 1000000);
            moneyString = context.getString(R.string.money_amount_million, amount);
        }
        return moneyString;
    }
}
