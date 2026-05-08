package com.e.myfavoritemovies.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavoriteMovieDao {

    @Query("SELECT * From FavoriteMovie")
    LiveData<List<FavoriteMovieEntry>> loadAllFavoriteMovies();

    @Insert
    void insertFavoriteMovie(FavoriteMovieEntry favoriteMovie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavoriteMovie(FavoriteMovieEntry favoriteMovie);

    @Delete
    void deleteFavoriteMovie(FavoriteMovieEntry favoriteMovie);

    @Query("delete from FavoriteMovie where movieId = :movieId")
    void deleteFavoriteMovieById(String movieId);

}
