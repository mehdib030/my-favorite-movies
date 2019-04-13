package com.e.myfavoritemovies.utils;

import android.content.Context;

import com.e.myfavoritemovies.model.Movie;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String MOVIE_POSTER="poster_path";

    private static final String MOVIE_RATING="vote_average";

    private static final String MOVIE_RELEASE_DATE="release_date";

    private static final String MOVIE_TITLE="title";

    private static final String MOVIE_ORIGINAL_TITLE="original_title";

    private static final String MOVIE_OVERVIEW="overview";



    public static Movie[] getMovieTitlesFromJson(Context context, String jsonResponse) throws JSONException {

        Movie[] parsedMovieTitles=null;

        JSONObject movieJson =  new JSONObject(jsonResponse);

        final String MOVIE_RESULTS = "results";

        final String MOVIE_TOTAL_RESULTS="total_results";

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

        int totalResults = movieJson.getInt(MOVIE_TOTAL_RESULTS);

        parsedMovieTitles = new Movie[totalResults];

        JSONArray movieArray = movieJson.getJSONArray(MOVIE_RESULTS);

        if(movieArray != null){

            for(int i=0;i < movieArray.length();i++){

                JSONObject jsonMovie = movieArray.getJSONObject(i);

                Movie movie = mapJsonObjectToMovie(jsonMovie);

                parsedMovieTitles[i]=movie;


               /* String originalTitle = movie.getString(MOVIE_ORIGINAL_TITLE);
                if(originalTitle == null){
                    parsedMovieTitles[i]="";
                } else {
                    parsedMovieTitles[i]=originalTitle;
                }*/
            }
        }

        return parsedMovieTitles;
    }

    private static Movie mapJsonObjectToMovie(JSONObject jsonMovie){
        Movie movie = new Movie();

        try {
            movie.setImage(jsonMovie.getString(MOVIE_POSTER));
            movie.setOriginalTitle(jsonMovie.getString(MOVIE_ORIGINAL_TITLE));
            movie.setPlotSynopsis(jsonMovie.getString(MOVIE_OVERVIEW));
            movie.setRating(jsonMovie.getString(MOVIE_RATING));
            movie.setReleaseDate(jsonMovie.getString(MOVIE_RELEASE_DATE));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movie;
    }

    /**
     * Parses the json movie string
     * @param json
     * @return a Movie object
     */
    public static Movie parseMovieJson(String json) {
        Movie movie = new Movie();

       /* try {
            JSONObject sandwichJson = new JSONObject(json);
            JSONObject name = sandwichJson.getJSONObject("name");
            sandwich.setMainName(name.getString("mainName"));
            JSONArray alsoKnownAsArray = name.getJSONArray("alsoKnownAs");
            sandwich.setAlsoKnownAs(convertJSONArrayToList(alsoKnownAsArray));
            sandwich.setDescription(sandwichJson.getString("description"));
            sandwich.setImage(sandwichJson.getString("image"));
            sandwich.setIngredients(convertJSONArrayToList(sandwichJson.getJSONArray("ingredients")));
            sandwich.setPlaceOfOrigin(sandwichJson.getString("placeOfOrigin"));

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
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
                    e.printStackTrace();
                }
            }
        }
        return listdata;
    }
//
//    {\"name\":{\"mainName\":\"Ham and cheese
//        sandwich\",\"alsoKnownAs\":[]},\"placeOfOrigin\":\"\",\"description\":\"A ham and cheese
//        sandwich is a common type of sandwich. It is made by putting cheese and sliced ham
//        between two slices of bread. The bread is sometimes buttered and/or toasted. Vegetables
//        like lettuce, tomato, onion or pickle slices can also be included. Various kinds of
//        mustard and mayonnaise are also
//        common.\",\"image\":\"https://upload.wikimedia.org/wikipedia/commons/thumb/5/50/Grilled_ham_and_cheese_014.JPG/800px-Grilled_ham_and_cheese_014.JPG\",\"ingredients\":[\"Sliced
//        bread\",\"Cheese\",\"Ham\"]}
}
