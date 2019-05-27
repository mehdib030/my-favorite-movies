package com.e.myfavoritemovies.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FavoriteMovieDao {

    @Query("SELECT * From FavoriteMovie")
    List<FavoriteMovieEntry> loadAllFavoriteMovies();

    @Insert
    void insertFavoriteMovie(FavoriteMovieEntry favoriteMovie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavoriteMovie(FavoriteMovieEntry favoriteMovie);

    @Delete
    void deleteFavoriteMovie(FavoriteMovieEntry favoriteMovie);

}
