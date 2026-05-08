package com.e.myfavoritemovies.Database

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

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
