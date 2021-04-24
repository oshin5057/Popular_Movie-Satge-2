package com.example.android.popularmoviefinalstage.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String API_KEY = "3c62d00f4bf06037163f7adc86429a1f";

    private static final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/movie";

    private static final String language = "en-US";

    private static final boolean includeAdults = false;

    private static final boolean includeVideo = false;

    private static final int page = 1;

    static final String API_KEY_PARAM = "api_key";
    static final String LANGUAGE_PARAM = "language";
    static final String INCLUDE_ADULT_PARAM = "include_adult";
    static final String INCLUDE_VIDEO_PARAM = "include_video";
    static final String PAGES_PARAM = "page";
    static final String VIDEO_PATH = "videos";
    static final String REVIEWS_PATH = "reviews";

    public static URL buildUrl(String sortBy){
        Uri buildUri = Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendPath(sortBy)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .appendQueryParameter(INCLUDE_ADULT_PARAM, String.valueOf(includeAdults))
                .appendQueryParameter(INCLUDE_VIDEO_PARAM, String.valueOf(includeVideo))
                .appendQueryParameter(PAGES_PARAM, String.valueOf(page))
                .build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildTrailerRequestUrl(String movieId){
        Uri buildUri = Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(VIDEO_PATH)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildReviewsRequestUrl(String movieId){
        Uri buildUri = Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(REVIEWS_PATH)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl (URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput){
                return scanner.next();
            }
            else {
                return null;
            }
        }
        finally {
            urlConnection.disconnect();
        }
    }
}
