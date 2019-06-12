package com.e.myfavoritemovies.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName="FavoriteMovie")
public class FavoriteMovieEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String movieId;
    private String title;
    private boolean favorite;

    public FavoriteMovieEntry(int id, String title, String movieId,boolean favorite){
        this.id=id;
        this.title=title;
        this.movieId=movieId;
        this.favorite=favorite;
    }

    @Ignore
    public FavoriteMovieEntry(String title,String movieId,boolean favorite){
        this.title=title;
        this.movieId=movieId;
        this.favorite=favorite;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }


    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

}
