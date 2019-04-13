package com.e.myfavoritemovies.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/"; //TODO: move to properties file

    private static final String API_KEY_VALUE = "2830d7b62b683f28c1c092afbea0196e"; //TODO:move to properties file

    private static final String API_KEY_NAME="api_key";


    /**
     * Returns the url of either the popular or top rated movies
     * @param movieType either popular or top rated
     * @return a url for the popular or top rated movies
     */
    public static URL buildUrl(String movieType){

        Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendPath(movieType)
                .appendQueryParameter(API_KEY_NAME,API_KEY_VALUE).build();

        URL url=null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace(); //TODO:remove
        }
        Log.v(TAG, "Built Url "+url);

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
