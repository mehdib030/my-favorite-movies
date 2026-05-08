package com.e.myfavoritemovies.utils

import android.content.Context
import android.util.Log
import com.e.myfavoritemovies.model.Movie
import com.e.myfavoritemovies.model.Review
import com.e.myfavoritemovies.model.Trailer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.HttpURLConnection

object JsonUtils {

    private val TAG: String = JsonUtils::class.java.simpleName

    private const val MOVIE_ID = "id"
    private const val MOVIE_POSTER = "poster_path"
    private const val MOVIE_RATING = "vote_average"
    private const val MOVIE_RELEASE_DATE = "release_date"
    private const val MOVIE_TITLE = "title"
    private const val MOVIE_ORIGINAL_TITLE = "original_title"
    private const val MOVIE_OVERVIEW = "overview"

    private const val REVIEW_ID = "id"
    private const val REVIEW_AUTHOR = "author"
    private const val REVIEW_CONTENT = "content"
    private const val REVIEW_URL = "url"

    private const val TRAILER_ID = "id"
    private const val TRAILER_KEY = "key"

    @JvmStatic
    @Throws(JSONException::class)
    fun getMovieTitlesFromJson(context: Context, jsonResponse: String): Array<Movie>? {
        val movieJson = JSONObject(jsonResponse)
        val MOVIE_RESULTS = "results"
        val MOVIE_MESSAGE_CODE = "cod"

        if (movieJson.has(MOVIE_MESSAGE_CODE)) {
            val errorCode = movieJson.getInt(MOVIE_MESSAGE_CODE)
            when (errorCode) {
                HttpURLConnection.HTTP_OK -> { }
                HttpURLConnection.HTTP_NOT_FOUND -> return null
                else -> return null
            }
        }

        val movieArray = movieJson.getJSONArray(MOVIE_RESULTS)
        val parsedMovieTitles = Array(movieArray.length()) { i ->
            mapJsonObjectToMovie(movieArray.getJSONObject(i))
        }

        return parsedMovieTitles
    }

    @JvmStatic
    @Throws(JSONException::class)
    fun getFavoriteMovieTitlesFromJson(context: Context, jsonResponse: String): Movie? {
        val movieJson = JSONObject(jsonResponse)
        val MOVIE_MESSAGE_CODE = "cod"

        if (movieJson.has(MOVIE_MESSAGE_CODE)) {
            val errorCode = movieJson.getInt(MOVIE_MESSAGE_CODE)
            when (errorCode) {
                HttpURLConnection.HTTP_OK -> { }
                HttpURLConnection.HTTP_NOT_FOUND -> return null
                else -> return null
            }
        }

        val movie = mapJsonObjectToMovie(movieJson)
        movie.isFavorite = true
        return movie
    }

    private fun mapJsonObjectToMovie(jsonMovie: JSONObject): Movie {
        val movie = Movie()
        try {
            movie.id = jsonMovie.getString(MOVIE_ID)
            movie.image = jsonMovie.getString(MOVIE_POSTER)
            movie.originalTitle = jsonMovie.getString(MOVIE_ORIGINAL_TITLE)
            movie.plotSynopsis = jsonMovie.getString(MOVIE_OVERVIEW)
            movie.rating = jsonMovie.getString(MOVIE_RATING)
            movie.releaseDate = DateUtils.formatDate(jsonMovie.getString(MOVIE_RELEASE_DATE))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return movie
    }

    private fun convertJSONArrayToList(jsonArray: JSONArray?): List<String> {
        val listdata = ArrayList<String>()
        if (jsonArray != null) {
            for (i in 0 until jsonArray.length()) {
                try {
                    listdata.add(jsonArray.getString(i))
                } catch (e: JSONException) {
                    Log.v(TAG, "Convert JSON to List Exception " + e.message)
                }
            }
        }
        return listdata
    }

    @JvmStatic
    @Throws(JSONException::class)
    fun getReviewsFromJson(context: Context, jsonResponse: String): Array<Review>? {
        val reviewJson = JSONObject(jsonResponse)
        val REVIEW_RESULTS = "results"
        val MOVIE_MESSAGE_CODE = "cod"

        if (reviewJson.has(MOVIE_MESSAGE_CODE)) {
            val errorCode = reviewJson.getInt(MOVIE_MESSAGE_CODE)
            when (errorCode) {
                HttpURLConnection.HTTP_OK -> { }
                HttpURLConnection.HTTP_NOT_FOUND -> return null
                else -> return null
            }
        }

        val reviewArray = reviewJson.getJSONArray(REVIEW_RESULTS)
        val parsedReviews = Array(reviewArray.length()) { i ->
            mapJsonObjectToReview(reviewArray.getJSONObject(i))
        }

        return parsedReviews
    }

    @JvmStatic
    @Throws(JSONException::class)
    fun getTrailersFromJson(context: Context, jsonResponse: String): Array<Trailer>? {
        val trailerJson = JSONObject(jsonResponse)
        val TRAILER_RESULTS = "results"
        val MOVIE_MESSAGE_CODE = "cod"

        if (trailerJson.has(MOVIE_MESSAGE_CODE)) {
            val errorCode = trailerJson.getInt(MOVIE_MESSAGE_CODE)
            when (errorCode) {
                HttpURLConnection.HTTP_OK -> { }
                HttpURLConnection.HTTP_NOT_FOUND -> return null
                else -> return null
            }
        }

        val trailerArray = trailerJson.getJSONArray(TRAILER_RESULTS)
        val parsedTrailers = Array(trailerArray.length()) { i ->
            mapJsonObjectToTrailer(trailerArray.getJSONObject(i))
        }

        return parsedTrailers
    }

    private fun mapJsonObjectToReview(jsonReview: JSONObject): Review {
        val review = Review()
        try {
            review.id = jsonReview.getString(REVIEW_ID)
            review.author = jsonReview.getString(REVIEW_AUTHOR)
            review.content = jsonReview.getString(REVIEW_CONTENT)
            review.url = jsonReview.getString(REVIEW_URL)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return review
    }

    private fun mapJsonObjectToTrailer(jsonTrailer: JSONObject): Trailer {
        val trailer = Trailer()
        try {
            trailer.id = jsonTrailer.getString(TRAILER_ID)
            trailer.key = jsonTrailer.getString(TRAILER_KEY)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return trailer
    }
}
