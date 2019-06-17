package com.e.myfavoritemovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.e.myfavoritemovies.Database.AppDatabase;
import com.e.myfavoritemovies.Database.FavoriteMovieEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<FavoriteMovieEntry>> favoriteMovies;

    public MainViewModel(@NonNull Application application) {
        super(application);

        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG,"Actively retrieving the favorite movies from the database");
        favoriteMovies = database.favoriteMovieDao().loadAllFavoriteMovies();
    }

    public LiveData<List<FavoriteMovieEntry>> getFavoriteMovies() {
        return favoriteMovies;
    }
}
