package com.e.myfavoritemovies;

import android.arch.persistence.room.util.StringUtil;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
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
import com.e.myfavoritemovies.model.Review;
import com.e.myfavoritemovies.utils.JsonUtils;
import com.e.myfavoritemovies.utils.NetworkUtils;
import org.json.JSONException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MoviesRecyclerViewAdapter.ItemClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String POPULAR_MOVIES="popular";
    private static final String TOP_RATED_MOVIES="top_rated";
    private static final String FAVORITES = "favorites";

    private MoviesRecyclerViewAdapter adapter;

    private MoviesRecyclerViewAdapter mAdapter;

    private List<Movie> movieList;

    private String[] sort = {"Popular","Top Rated","Favorites"};

    private String movieType;

    private AppDatabase fmdb;

    private List<FavoriteMovieEntry> favoriteMovies =new ArrayList();

    private List<Review> reviewList;

    private String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fmdb = AppDatabase.getInstance(getApplicationContext());
    }

    private void launchDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION,position);
        intent.putExtra("movies", (ArrayList<Movie>)movieList);
       // Movie movie = movieList.get(position);
        //intent.putExtra("movie", movieList.get(position));
        startActivity(intent);
    }

    public void loadMoviesData(){
        new FetchMoviesTask().execute();
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i(TAG,"You clicked on "+adapter.getItem(position)+ " at position "+position);
        launchDetailActivity(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.movies_sort_menu,menu);

        MenuItem item = menu.findItem(R.id.spinner_item);
        Spinner spinner = (Spinner) item.getActionView();

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,sort);
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

                    switch(item.toString()) {
                        case "Popular":
                            movieType = POPULAR_MOVIES;
                            break;
                        case "Top Rated":
                            movieType = TOP_RATED_MOVIES;
                            break;
                        case "Favorites":
                            movieType =FAVORITES;
                            break;
                    }
                    loadMoviesData();
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
    public class FetchMoviesTask extends AsyncTask<String, Integer, Movie[]> {

        @Override
        protected Movie[] doInBackground(String... params) {

            movieList= new ArrayList();

            if(movieType == FAVORITES){
                loadFavoriteMovies();
            } else {
                loadPopularAndTopRatedMovies();
            }

            Movie[] movies = new Movie[movieList.size()];
            Log.i(TAG, "MOVIES LENGTH : "+movies.length);
            return movieList.toArray(movies);
        }
        @Override
        public void onProgressUpdate(Integer ... values){
            super.onProgressUpdate(values);
            Log.i(TAG,"ON UPDATE PROGRESS ");
        }
        @Override
        public void onPostExecute(Movie[] moviesData){
            if (moviesData != null){

                RecyclerView recyclerView = findViewById(R.id.movies_recylerview);

                final GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,4);

                recyclerView.setLayoutManager(gridLayoutManager);

                adapter = new MoviesRecyclerViewAdapter(MainActivity.this,moviesData,moviesData.length);

                adapter.setClickListener(MainActivity.this);

                recyclerView.setAdapter(adapter);
            }
        }

    }

    /**
     * Loads popular and top rated movies.
     */
    public void loadPopularAndTopRatedMovies(){

           URL url = NetworkUtils.buildUrl(MainActivity.this,movieType,1,false,null);

        try {
            try {
                String moviesJsonString = NetworkUtils.getResponseFromHttpUrl(url);
                movieList.addAll(
                        Arrays.asList(JsonUtils.getMovieTitlesFromJson(MainActivity.this, moviesJsonString)));
            } catch(FileNotFoundException fnfe){
                Log.i(TAG, "Empty Results.");
            }
        } catch (IOException ioe) {
            Log.i(TAG, ioe.getMessage());
        } catch (JSONException jse) {
            Log.i(TAG, jse.getMessage());
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
       // if(movieType == FAVORITES) {
            //loadFavoriteMovies();

           /* runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "SETTING ADAPTER "+movieList.size());
                    adapter.setMovies(movieList);
                }
            });*/

       // }

    }


    public void fectchMoviesFromDatatabase(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {

            @Override
            public void run() {
                List<FavoriteMovieEntry> favoriteMoviesEntries  = fmdb.favoriteMovieDao().loadAllFavoriteMovies();
                favoriteMovies.addAll(favoriteMoviesEntries);

            }
        });

    }
    /**
     * Loads the favorite movies. The movies are retrieved one at a time by id.
     */
    public void loadFavoriteMovies() {

        List<FavoriteMovieEntry> favoriteMoviesEntries  = fmdb.favoriteMovieDao().loadAllFavoriteMovies();
        favoriteMovies.addAll(favoriteMoviesEntries);

            if (!favoriteMovies.isEmpty()){

                Iterator<FavoriteMovieEntry> it = favoriteMovies.iterator();
                while(it.hasNext()){
                    FavoriteMovieEntry fme = it.next();
                    Log.i(TAG, "Id : " + fme.getMovieId() + ", Title : " + fme.getTitle());

                    String id = fme.getMovieId();

                    if(id != null) {

                        URL url = NetworkUtils.buildUrl(MainActivity.this, movieType, 1, true, id);

                        try {
                            try {
                                String moviesJsonString = NetworkUtils.getResponseFromHttpUrl(url);
                                movieList.add(JsonUtils.getFavoriteMovieTitlesFromJson(MainActivity.this, moviesJsonString));
                            } catch (FileNotFoundException fnfe) {
                                Log.i(TAG, "Empty Results.");
                            }
                        } catch (IOException ioe) {
                            Log.i(TAG, ioe.getMessage());
                        } catch (JSONException jse) {
                            Log.i(TAG, jse.getMessage());
                        }

                    }
                }

                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "SETTING ADAPTER "+movieList.size());
                        mAdapter.setMovies  (movieList);
                    }
                });*/
            }
    }


}
