package com.e.myfavoritemovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class MainActivity extends AppCompatActivity implements MoviesRecyclerViewAdapter.ItemClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String POPULAR_MOVIES="popular";
    private static final String TOP_RATED_MOVIES="top_rated";

    private MoviesRecyclerViewAdapter adapter;

    private List<Movie> movieList;

    private String[] sort = {"Popular","Top Rated"};

    private String movieSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void launchDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION,position);
        intent.putExtra("movies", (ArrayList<Movie>)movieList);
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
                            movieSort = POPULAR_MOVIES;
                            break;
                        case "Top Rated":
                            movieSort = TOP_RATED_MOVIES;
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



    public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected Movie[] doInBackground(String... params) {


            movieList= new ArrayList();

            try {
                    URL url = NetworkUtils.buildUrl(movieSort,1);
                    try {
                        String moviesJsonString = NetworkUtils.getResponseFromHttpUrl(url);
                        movieList.addAll(
                                Arrays.asList(JsonUtils.getMovieTitlesFromJson(MainActivity.this, moviesJsonString)));
                    } catch(FileNotFoundException fnfe){
                        Log.i(TAG, "Empty Results.");
                    }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            Movie[] movies = new Movie[movieList.size()];
            return movieList.toArray(movies);
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
}
