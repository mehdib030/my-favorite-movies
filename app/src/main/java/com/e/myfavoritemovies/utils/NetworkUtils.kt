package com.e.myfavoritemovies.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.e.myfavoritemovies.R
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.Scanner

object NetworkUtils {

    private val TAG: String = NetworkUtils::class.java.simpleName
    private const val API_KEY_NAME = "api_key"

    @JvmStatic
    fun buildUrl(context: Context, movieType: String, page: Int, favoriteMovies: Boolean, id: String?): URL? {
        val builder = Uri.parse(context.getString(R.string.base_url)).buildUpon()

        val builtUri = if (!favoriteMovies) {
            builder.appendPath(movieType)
                .appendQueryParameter(API_KEY_NAME, context.getString(R.string.api_key_value))
                .appendQueryParameter("page", page.toString())
                .build()
        } else {
            builder.appendPath(id)
                .appendQueryParameter(API_KEY_NAME, context.getString(R.string.api_key_value))
                .build()
        }

        var url: URL? = null
        try {
            url = URL(builtUri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        Log.v(TAG, "Built Url $url")
        return url
    }

    @JvmStatic
    fun buildReviewsUrl(context: Context, movieId: String, page: Int, favoriteMovies: Boolean, id: String?): URL? {
        val builder = Uri.parse(context.getString(R.string.base_url)).buildUpon()

        val builtUri = builder.appendPath(movieId).appendPath("reviews")
            .appendQueryParameter(API_KEY_NAME, context.getString(R.string.api_key_value))
            .appendQueryParameter("page", page.toString())
            .build()

        var url: URL? = null
        try {
            url = URL(builtUri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        Log.v(TAG, "Built Url $url")
        return url
    }

    @JvmStatic
    fun buildTrailersUrl(context: Context, movieId: String, page: Int): URL? {
        val builder = Uri.parse("http://api.themoviedb.org/3/movie/").buildUpon()

        val builtUri = builder.appendPath(movieId).appendPath("videos")
            .appendQueryParameter(API_KEY_NAME, context.getString(R.string.api_key_value))
            .appendQueryParameter("page", page.toString())
            .build()

        var url: URL? = null
        try {
            url = URL(builtUri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        Log.v(TAG, "Built Url $url")
        return url
    }

    @JvmStatic
    @Throws(IOException::class)
    fun getResponseFromHttpUrl(url: URL): String? {
        val urlConnection = url.openConnection() as HttpURLConnection
        try {
            val scanner = Scanner(urlConnection.inputStream)
            scanner.useDelimiter("\\A")
            return if (scanner.hasNext()) {
                scanner.next()
            } else {
                null
            }
        } finally {
            urlConnection.disconnect()
        }
    }
}
