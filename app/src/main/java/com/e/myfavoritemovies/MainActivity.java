package com.e.myfavoritemovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.e.myfavoritemovies.Database.AppDatabase;
import com.e.myfavoritemovies.Database.FavoriteMovieEntry;
import com.e.myfavoritemovies.model.Movie;
import com.e.myfavoritemovies.utils.JsonUtils;
import com.e.myfavoritemovies.utils.NetworkUtils;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is the main entry point for the app, it fetches and displays a list of movies.
 * It also displays a menu for selecting popular and top rated movies.
 */
public class MainActivity extends AppCompatActivity implements MoviesRecyclerViewAdapter.ItemClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String POPULAR_MOVIES="Popular";
    private static final String TOP_RATED_MOVIES="top_rated";
    private static final String FAVORITES = "Favorites";

    private static final String LIFECYCLE_CALLBACKS_MOVIES = "moviesCallback";
    private static final String LIFECYCLE_CALLBACKS_MOVIE_TYPE = "movieTypeCallback";
    private static final String LIFECYCLE_CALLBACKS_MENU = "menuCallBack";
    private static final String BUNDLE_RECYCLER_LAYOUT = "recyclerLayout";

    private MoviesRecyclerViewAdapter adapter;

    private List<Movie> movieList;

    private String[] sort = {"Popular","Top Rated","Favorites"};

    private String movieType;

    private AppDatabase fmdb;

    private boolean inflateMenu=true;

    private RecyclerView recyclerView;

    Parcelable savedRecyclerLayoutState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_MOVIES)) {
                movieList = savedInstanceState
                        .getParcelableArrayList(LIFECYCLE_CALLBACKS_MOVIES);

                Log.i(TAG,"ON CREATE SAVED INSTANCE");
            }

            if (savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_MOVIE_TYPE)) {

                movieType = savedInstanceState.getString(LIFECYCLE_CALLBACKS_MOVIE_TYPE);
            }

            if(savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_MENU)){
                inflateMenu=false;
            }

        }

        fmdb = AppDatabase.getInstance(getApplicationContext());

    }

    private void launchDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION,position);
        intent.putExtra("movies", (ArrayList<Movie>)movieList);
        startActivity(intent);
    }

    public void loadMoviesData(){


        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getFavoriteMovies().observe(this, new Observer<List<FavoriteMovieEntry>>(){

            @Override
            public void onChanged(@Nullable List<FavoriteMovieEntry> favoriteMovieEntries) {
                new FetchMoviesTask(favoriteMovieEntries).execute();

            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i(TAG,"You clicked on "+adapter.getItem(position)+ " at position "+position);
        launchDetailActivity(position);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        if(savedInstanceState != null){
            recyclerView = findViewById(R.id.movies_recylerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
            getMenuInflater().inflate(R.menu.movies_sort_menu, menu);
            MenuItem item = menu.findItem(R.id.spinner_item);
            Spinner spinner = (Spinner) item.getActionView();
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sort);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {
                    Object item = adapterView.getItemAtPosition(position);

                    if (item != null) {
                        Toast.makeText(MainActivity.this, item.toString(),
                                Toast.LENGTH_SHORT).show();

                        String movieSort= item.toString();

                        if(!inflateMenu){
                            movieSort=movieType;
                        }

                        switch (movieSort) {
                            case "Popular":
                                movieType = POPULAR_MOVIES;
                                adapterView.setSelection(0);
                                loadMoviesData();
                                break;
                            case "Top Rated":
                            case "top_rated":
                                movieType = TOP_RATED_MOVIES;
                                adapterView.setSelection(1);
                                loadMoviesData();
                                break;
                            case "Favorites":
                                movieType = FAVORITES;
                                adapterView.setSelection(2);
                                loadFavoriteMovies();
                                break;
                        }

                        inflateMenu=true;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // TODO Auto-generated method stub
                }
            });

        return true;
    }

    /**
     * This async task retrieves the movie list
     */
    public class FetchMoviesTask extends AsyncTask<String, Integer, List<Movie>> {
        //List<Movie> favoriteList= new ArrayList();
        List<FavoriteMovieEntry> favoriteMovieEntries =  new ArrayList();

        public FetchMoviesTask(List<FavoriteMovieEntry> favoriteMovieEntries){
            this.favoriteMovieEntries=favoriteMovieEntries;
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            movieList= new ArrayList();

            loadPopularAndTopRatedMovies(this.favoriteMovieEntries);

            Log.i(TAG, "MOVIES LENGTH : "+movieList.size());
            return movieList;
        }
        @Override
        public void onProgressUpdate(Integer ... values){
            super.onProgressUpdate(values);
            Log.i(TAG,"ON UPDATE PROGRESS ... ");
        }
        @Override
        public void onPostExecute(List<Movie> moviesData){

            if (moviesData != null){
                recyclerView = findViewById(R.id.movies_recylerview);
                final GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,4);
                recyclerView.setLayoutManager(gridLayoutManager);
                adapter = new MoviesRecyclerViewAdapter(MainActivity.this,moviesData,moviesData.size());
                adapter.setClickListener(MainActivity.this);
                recyclerView.setAdapter(adapter);
            }
        }
    }

    /**
     * Loads popular and top rated movies.
     */
    public void loadPopularAndTopRatedMovies(List<FavoriteMovieEntry> favoriteMovieEntries){

           URL url = NetworkUtils.buildUrl(MainActivity.this,movieType.toLowerCase(),1,false,null);

        try {
            try {
                String moviesJsonString = NetworkUtils.getResponseFromHttpUrl(url);
                Movie[] movies = JsonUtils.getMovieTitlesFromJson(MainActivity.this, moviesJsonString);

                populateFavoriteState(movies,favoriteMovieEntries);
                movieList.addAll(Arrays.asList(movies));
            } catch(FileNotFoundException fnfe){
                Log.i(TAG, "Empty Results.");
            }
        } catch (IOException ioe) {
            Log.i(TAG, ioe.getMessage());
        } catch (JSONException jse) {
            Log.i(TAG, jse.getMessage());
        }
    }

    public void populateFavoriteState(Movie[] movies, List<FavoriteMovieEntry> favoriteMovieEntries){

        if(favoriteMovieEntries.size() > 0) {
            for(FavoriteMovieEntry fme: favoriteMovieEntries) {
                for (Movie movie : movies) {

                    if(movie.getId().equals(fme.getMovieId())){
                        movie.setFavorite(true);
                        break;
                    }
                }
            }
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    /**
     * Loads the favorite movies. The movies are retrieved one at a time by id.
     */
    public List<Movie>  loadFavoriteMovies() {

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        final List<Movie> favoriteList =  new ArrayList();

        viewModel.getFavoriteMovies().observe(this, new Observer<List<FavoriteMovieEntry>>(){

            @Override
            public void onChanged(@Nullable List<FavoriteMovieEntry> favoriteMovieEntries) {

                new FetchFavoriteMoviesTask(favoriteMovieEntries).execute();

            }
        });

        return favoriteList;
    }

    /**
     * This class fetches the user's favorite movies
     */
    public class FetchFavoriteMoviesTask extends AsyncTask<String, Integer, List<Movie>> {
        List<Movie> favoriteList= new ArrayList();
        List<FavoriteMovieEntry> favoriteMovieEntries =  new ArrayList();

        public FetchFavoriteMoviesTask(List<FavoriteMovieEntry> favoriteMovieEntries){
            this.favoriteMovieEntries=favoriteMovieEntries;
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            for(FavoriteMovieEntry fme:this.favoriteMovieEntries) {

                URL url = NetworkUtils.buildUrl(MainActivity.this, movieType, 1, true, fme.getMovieId());

                try {
                    try {
                        String moviesJsonString = NetworkUtils.getResponseFromHttpUrl(url);
                        favoriteList.add(JsonUtils.getFavoriteMovieTitlesFromJson(MainActivity.this, moviesJsonString));
                    } catch (FileNotFoundException fnfe) {
                        Log.i(TAG, "Empty Results.");
                    }
                } catch (IOException ioe) {
                    Log.i(TAG, ioe.getMessage());
                } catch (JSONException jse) {
                    Log.i(TAG, jse.getMessage());
                }
            }
            movieList = favoriteList;

            return favoriteList;
        }
        @Override
        public void onProgressUpdate(Integer ... values){
            super.onProgressUpdate(values);
            Log.i(TAG,"ON UPDATE PROGRESS ");
        }
        @Override
        public void onPostExecute(List<Movie> favoriteList){
            if (favoriteList.size() > 0){

                recyclerView = findViewById(R.id.movies_recylerview);
                final GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,4);
                recyclerView.setLayoutManager(gridLayoutManager);
                adapter = new MoviesRecyclerViewAdapter(MainActivity.this,favoriteList,favoriteList.size());
                adapter.setClickListener(MainActivity.this);
                recyclerView.setAdapter(adapter);
            }
        }
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG,"ON SAVE INSTANCE STATE MAIN ACTIVITY");

        outState.putString(LIFECYCLE_CALLBACKS_MOVIE_TYPE,movieType);

        outState.putParcelableArrayList(LIFECYCLE_CALLBACKS_MOVIES, (ArrayList<? extends Parcelable>) movieList);

        outState.putBoolean(LIFECYCLE_CALLBACKS_MENU, false);

        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());

    }



}
