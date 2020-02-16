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

    public FavoriteMovieEntry(int id, String title, String movieId){
        this.id=id;
        this.title=title;
        this.movieId=movieId;
    }

    @Ignore
    public FavoriteMovieEntry(String title,String movieId){
        this.title=title;
        this.movieId=movieId;
    }

    public String getTitle() {
        return title;
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

}
