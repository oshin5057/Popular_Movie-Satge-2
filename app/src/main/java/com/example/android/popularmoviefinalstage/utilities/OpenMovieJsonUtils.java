package com.example.android.popularmoviefinalstage.utilities;

import com.example.android.popularmoviefinalstage.Movie;
import com.example.android.popularmoviefinalstage.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class OpenMovieJsonUtils {
    public static List<Movie> getMovieListFromJson (String movieJsonString) throws JSONException {
        List<Movie> movies = new ArrayList<>();

        final String RESULTS_ARRAY = "results";
        final String MOVIE_ID = "id";
        final String ORIGINAL_TITLE = "original_title";
        final String VOTE_AVERAGE = "vote_average";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String DATE = "release_date";
        final String MESSAGE_CODE = "cod";

        JSONObject movieJson = new JSONObject(movieJsonString);

        if (movieJson.has(MESSAGE_CODE)) {

            int errorCode = movieJson.getInt(MESSAGE_CODE);
            switch (errorCode){
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray resultsArray = movieJson.getJSONArray(RESULTS_ARRAY);
        for (int i =0; i<resultsArray.length(); i++){
            JSONObject movieSingeItem = resultsArray.getJSONObject(i);

            int id = movieSingeItem.getInt(MOVIE_ID);
            Double rate = movieSingeItem.getDouble(VOTE_AVERAGE);
            String poster = movieSingeItem.getString(POSTER_PATH);
            String originalTitle = movieSingeItem.getString(ORIGINAL_TITLE);
            String overview = movieSingeItem.getString(OVERVIEW);
            String date = movieSingeItem.getString(DATE);

            Movie singleMovie = new Movie(id, rate, poster, originalTitle, overview, date);
            movies.add(singleMovie);
        }
        return movies;
    }

    public static List<String> getMovieYoutubeKeyFromJson (String trailerJsonString) throws JSONException{
        final String RESULTS_ARRAY = "results";
        final String KEY_ITEM = "key";
        final List<String> keys = new ArrayList<>();

        JSONObject root = new JSONObject(trailerJsonString);
        JSONArray resultArray = root.getJSONArray(RESULTS_ARRAY);
        for (int i = 0; i<resultArray.length(); i++){
            JSONObject trailerInfo = resultArray.getJSONObject(i);
            String key = trailerInfo.getString(KEY_ITEM);
            keys.add(key);
        }
        return keys;
    }

    public static List<Review> getMovieReviewFromJson (String reviewJsonString) throws JSONException{
        final String RESULTS_ARRAY = "results";
        final String AUTHOR_ITEM = "author";
        final String CONTENT_ITEM = "content";
        List<Review> reviews = new ArrayList<>();

        JSONObject root = new JSONObject(reviewJsonString);
        JSONArray resultsArray = root.getJSONArray(RESULTS_ARRAY);
        for (int i = 0; i<resultsArray.length(); i++){
            JSONObject reviewItem = resultsArray.getJSONObject(i);
            String author = reviewItem.getString(AUTHOR_ITEM);
            String review = reviewItem.getString(CONTENT_ITEM);
            reviews.add(new Review(review, author));
        }
        return reviews;
    }
}
