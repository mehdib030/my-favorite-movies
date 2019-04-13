package com.e.myfavoritemovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.e.myfavoritemovies.model.Movie;
import com.e.myfavoritemovies.utils.JsonUtils;
import com.e.myfavoritemovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String POPULAR_MOVIES="popular";
    private static final String TOP_RATED_MOVIES="top_rated";

    private ImageView mMoviePosterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // mMoviePosterImageView = findViewById(R.id.movie_poster_iv);
        //String[] movies =  getResources().getStringArray(R.array.movie_names);

        loadMoviesData();

    }

    private void launchDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION,position);
        startActivity(intent);
    }

    public void loadMoviesData(){
        new FetchMoviesTask().execute();
    }


    public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected Movie[] doInBackground(String... params) {

            URL url = NetworkUtils.buildUrl(TOP_RATED_MOVIES);

            Movie[] movies = null;

            try {
                String moviesJsonString = NetworkUtils.getResponseFromHttpUrl(url);
                movies =  JsonUtils.getMovieTitlesFromJson(MainActivity.this,moviesJsonString);

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }


            return movies;
        }
        @Override
        public void onPostExecute(Movie[] moviesData){
            if (moviesData != null){

                MoviesAdapter adapter = new MoviesAdapter(MainActivity.this,moviesData);

                GridView gridView = findViewById(R.id.movies_gridview);
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener( new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            launchDetailActivity(position);
                    }
                });

                //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(mMoviePosterImageView);
               /* ArrayAdapter<String> adapter = new ArrayAdapter(MainActivity.this,
                        android.R.layout.simple_list_item_1, moviesData);*/

                //TODO: replace below with recycler view
               /* ListView listView = findViewById(R.id.movies_listview);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener( new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        launchDetailActivity(position);
                    }
                });*/
            }
        }
    }
}
