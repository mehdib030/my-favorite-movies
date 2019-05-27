package com.e.myfavoritemovies.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName="FavoriteMovie")
public class FavoriteMovieEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    public FavoriteMovieEntry(int id, String title){
        this.id=id;
        this.title=title;
    }

    @Ignore
    public FavoriteMovieEntry(String title){
        this.title=title;
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





}
