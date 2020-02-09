package com.e.myfavoritemovies.utils;

import android.content.Context;

import com.e.myfavoritemovies.model.Movie;
import com.e.myfavoritemovies.model.Review;
import com.e.myfavoritemovies.model.Trailer;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String MOVIE_ID="id";
    private static final String MOVIE_POSTER="poster_path";
    private static final String MOVIE_RATING="vote_average";
    private static final String MOVIE_RELEASE_DATE="release_date";
    private static final String MOVIE_TITLE="title";
    private static final String MOVIE_ORIGINAL_TITLE="original_title";
    private static final String MOVIE_OVERVIEW="overview";

    private static final String REVIEW_ID="id";
    private static final String REVIEW_AUTHOR = "author";
    private static final String REVIEW_CONTENT = "content";
    private static final String REVIEW_URL = "url";

    private static final String TRAILER_ID="id";
    private static final String TRAILER_KEY = "key";




    public static Movie[] getMovieTitlesFromJson(Context context, String jsonResponse) throws JSONException {

        Movie[] parsedMovieTitles=null;
        JSONObject movieJson =  new JSONObject(jsonResponse);
        final String MOVIE_RESULTS = "results";
        final String MOVIE_MESSAGE_CODE = "cod";

        if(movieJson.has(MOVIE_MESSAGE_CODE)){

            int errorCode = movieJson.getInt(MOVIE_MESSAGE_CODE);

            switch(errorCode){

                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }


        }

        JSONArray movieArray = movieJson.getJSONArray(MOVIE_RESULTS);
        parsedMovieTitles = new Movie[movieArray.length()];

        if(movieArray != null){

            for(int i=0;i < movieArray.length();i++){

                JSONObject jsonMovie = movieArray.getJSONObject(i);

                Movie movie = mapJsonObjectToMovie(jsonMovie);

                parsedMovieTitles[i]=movie;

            }
        }

        return parsedMovieTitles;
    }

    public static Movie getFavoriteMovieTitlesFromJson(Context context, String jsonResponse) throws JSONException {
        JSONObject movieJson =  new JSONObject(jsonResponse);

        final String MOVIE_MESSAGE_CODE = "cod";


        if(movieJson.has(MOVIE_MESSAGE_CODE)){

            int errorCode = movieJson.getInt(MOVIE_MESSAGE_CODE);

            switch(errorCode){

                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        Movie movie = mapJsonObjectToMovie(movieJson);
        movie.setFavorite(true);

        return movie;
    }

    private static Movie mapJsonObjectToMovie(JSONObject jsonMovie){
        Movie movie = new Movie();

        try {
            movie.setId(jsonMovie.getString(MOVIE_ID));
            movie.setImage(jsonMovie.getString(MOVIE_POSTER));
            movie.setOriginalTitle(jsonMovie.getString(MOVIE_ORIGINAL_TITLE));
            movie.setPlotSynopsis(jsonMovie.getString(MOVIE_OVERVIEW));
            movie.setRating(jsonMovie.getString(MOVIE_RATING));
            movie.setReleaseDate(DateUtils.formatDate(jsonMovie.getString(MOVIE_RELEASE_DATE)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movie;
    }

    /**
     * Converts a json array to a Java list
     * @param jsonArray
     * @return a list
     */
    private static List<String> convertJSONArrayToList(JSONArray jsonArray){

        ArrayList<String> listdata = new ArrayList();

        if (jsonArray != null) {
            for (int i=0;i<jsonArray.length();i++){
                try {
                    listdata.add(jsonArray.getString(i));
                } catch (JSONException e) {
                    //e.printStackTrace();
                }
            }
        }
        return listdata;
    }

    /**
     * Gets the reviews from the json string response
     * https://api.themoviedb.org/3/movie/4
     * @param context
     * @param jsonResponse
     * @return
     * @throws JSONException
     */
    public static Review[] getReviewsFromJson(Context context, String jsonResponse) throws JSONException{

        Review[] parsedReviews=null;

        JSONObject reviewJson =  new JSONObject(jsonResponse);

        final String REVIEW_RESULTS = "results";

        final String MOVIE_MESSAGE_CODE = "cod";


        if(reviewJson.has(MOVIE_MESSAGE_CODE)){

            int errorCode = reviewJson.getInt(MOVIE_MESSAGE_CODE);

            switch(errorCode){

                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }


        }

        JSONArray reviewArray = reviewJson.getJSONArray(REVIEW_RESULTS);

        parsedReviews = new Review[reviewArray.length()];

        if(reviewArray != null){

            for(int i=0;i < reviewArray.length();i++){

                JSONObject jsonReview = reviewArray.getJSONObject(i);

                Review review = mapJsonObjectToReview(jsonReview);

                parsedReviews[i]=review;

            }
        }
        return parsedReviews;
    }

    /**
     * Gets the reviews from the json string response
     * https://api.themoviedb.org/3/movie/4
     * @param context
     * @param jsonResponse
     * @return
     * @throws JSONException
     */
    public static Trailer[] getTrailersFromJson(Context context, String jsonResponse) throws JSONException{

        Trailer[] parsedTrailers=null;

        JSONObject trailerJson =  new JSONObject(jsonResponse);

        final String TRAILER_RESULTS = "results";

        final String MOVIE_MESSAGE_CODE = "cod";


        if(trailerJson.has(MOVIE_MESSAGE_CODE)){

            int errorCode = trailerJson.getInt(MOVIE_MESSAGE_CODE);

            switch(errorCode){

                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray trailerArray = trailerJson.getJSONArray(TRAILER_RESULTS);

        parsedTrailers = new Trailer[trailerArray.length()];

        if(trailerArray != null){

            for(int i=0;i < trailerArray.length();i++){

                JSONObject jsonReview = trailerArray.getJSONObject(i);

                Trailer trailer = mapJsonObjectToTrailer(jsonReview);

                parsedTrailers[i]=trailer;

            }
        }
        return parsedTrailers;
    }


    private static Review mapJsonObjectToReview(JSONObject jsonReview){
        Review review = new Review();

        try {
            review.setId(jsonReview.getString(REVIEW_ID));
            review.setAuthor(jsonReview.getString(REVIEW_AUTHOR));
            review.setContent(jsonReview.getString(REVIEW_CONTENT));
            review.setUrl(jsonReview.getString(REVIEW_URL));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return review;
    }

    private static Trailer mapJsonObjectToTrailer(JSONObject jsonReview){
        Trailer trailer = new Trailer();

        try {
            trailer.setId(jsonReview.getString(TRAILER_ID));
            trailer.setKey(jsonReview.getString(TRAILER_KEY));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return trailer;
    }
}
