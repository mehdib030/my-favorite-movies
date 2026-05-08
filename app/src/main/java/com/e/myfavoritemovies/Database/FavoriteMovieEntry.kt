package com.e.myfavoritemovies.Database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "FavoriteMovie")
data class FavoriteMovieEntry(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val title: String,
    val movieId: String
) {
    @Ignore
    constructor(title: String, movieId: String) : this(0, title, movieId)
}
