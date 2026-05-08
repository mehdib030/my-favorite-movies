package com.e.myfavoritemovies.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Movie @JvmOverloads constructor(
    var id: String? = null,
    var originalTitle: String? = null,
    var image: String? = null,
    var plotSynopsis: String? = null,
    var rating: String? = null,
    var releaseDate: String? = null,
    var isFavorite: Boolean = false
) : Parcelable, Serializable {
    @IgnoredOnParcel
    var reviews: MutableList<Review> = mutableListOf()
    @IgnoredOnParcel
    var trailers: MutableList<Trailer> = mutableListOf()
}
