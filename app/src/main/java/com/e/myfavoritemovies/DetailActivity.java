package com.e.myfavoritemovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.e.myfavoritemovies.Database.AppDatabase;
import com.e.myfavoritemovies.Database.FavoriteMovieEntry;
import com.e.myfavoritemovies.model.Movie;
import com.e.myfavoritemovies.model.Trailer;
import com.e.myfavoritemovies.utils.JsonUtils;
import com.e.myfavoritemovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private static final String MARK_AS_FAVORITE = "MARK AS FAVORITE";
    private static final String REMOVE_AS_FAVORITE = "REMOVE AS FAVORITE";

    private static final String BASE_URL="http://image.tmdb.org/t/p/w185//";

    private ImageView mMovieImageView;
    private TextView mOriginalTitleTextView;
    private TextView mPlotSynopsisTextView;
    private TextView mRatingTextView;
    private TextView mReleaseDateTextView;

    private AppDatabase fmdb;
    private List<Trailer> trailerList;
    private String movieId;
    private Movie movie=null;
    private TrailersRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        fmdb =  AppDatabase.getInstance(getApplicationContext());

        mMovieImageView = findViewById(R.id.image_iv);
        mOriginalTitleTextView = findViewById(R.id.original_title_tv);
        mPlotSynopsisTextView = findViewById(R.id.plot_synopsis_tv);
        mRatingTextView = findViewById(R.id.rating_tv);
        mReleaseDateTextView = findViewById(R.id.release_date_tv);

        Intent intent = getIntent();

        if(intent == null){
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION,DEFAULT_POSITION);

        if(position == DEFAULT_POSITION){
            closeOnError();
            return;
        }

        ArrayList<Movie> movies = (ArrayList<Movie>) intent.getSerializableExtra("movies");
        this.movie = movies.get(position);

        this.movieId = movie.getId();

        populateUI(movie);

        Picasso.get().load(BASE_URL +movie.getImage()).into(mMovieImageView);

        AppCompatButton favoriteMoviesButton = findViewById(R.id.favorite_movie_button);

        if(movie.isFavorite()) {
            favoriteMoviesButton.setText(R.string.remove_favorite_movie_value);
        } else {
            favoriteMoviesButton.setText(R.string.favorite_movie_value);
        }

        favoriteMoviesButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                String buttonText =  ((AppCompatButton)view).getText().toString();

                switch(buttonText){
                    case MARK_AS_FAVORITE:
                        onSaveButtonClicked(movie,view);
                        break;
                    case REMOVE_AS_FAVORITE:
                        onRemoveButtonClicked(movie,view);
                        break;
                }
            }
        });

        populateTrailers();

        AppCompatButton reviewsButton = findViewById(R.id.movie_reviews_button);

        reviewsButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String buttonText =  ((AppCompatButton)view).getText().toString();

                launchReviewsActivity();
            }
        });
    }

    /**
     * This method populates the ui
     * @param movie
     */
    private void populateUI(Movie movie){
        mOriginalTitleTextView.setText(movie.getOriginalTitle());
        mPlotSynopsisTextView.setText(movie.getPlotSynopsis());
        mRatingTextView.setText(movie.getRating());
        mReleaseDateTextView.setText(movie.getReleaseDate());
    }

    public void onSaveButtonClicked(Movie movie,View view){

        String title=movie.getOriginalTitle();
        String id = movie.getId();

        final FavoriteMovieEntry favoriteMovieEntry = new FavoriteMovieEntry(title,id);

        Log.i(TAG,"Saving favorite movie : "+title+", id : "+id);

        AppExecutors.getInstance().diskIO().execute(new Runnable(){

            @Override
            public void run() {
                fmdb.favoriteMovieDao().insertFavoriteMovie(favoriteMovieEntry);
            }
        });

        ((AppCompatButton)view).setText(R.string.remove_favorite_movie_value);

    }

    public void onRemoveButtonClicked(Movie movie,View view){ //TODO: merge with onSaveButtonClicked()
        String title=movie.getOriginalTitle();
        final String id = movie.getId();

        Log.i(TAG,"Removing favorite movie : "+title+", id : "+id);

        AppExecutors.getInstance().diskIO().execute(new Runnable(){

            @Override
            public void run() {
                fmdb.favoriteMovieDao().deleteFavoriteMovieById(id);
            }
        });

        ((AppCompatButton)view).setText(R.string.favorite_movie_value);
    }


    private void closeOnError(){
        finish();
        Toast.makeText(this,R.string.movie_details_not_available,Toast.LENGTH_SHORT);
    }

    private void launchReviewsActivity() {
        Intent intent = new Intent(this, ReviewsActivity.class);
        intent.putExtra("movie", this.movie);
        startActivity(intent);
    }

    public void populateTrailers(){
        new fetchTrailersTask().execute();
    }

    public class fetchTrailersTask extends AsyncTask<String,Integer,List<Trailer>>{

        @Override
        protected List<Trailer> doInBackground(String... strings) {

            trailerList =  new ArrayList(0);

            trailerList = loadTrailers();

            return trailerList;
        }

        @Override
        public void onPostExecute(List<Trailer> trailers){
            if(trailers != null ){
                RecyclerView recyclerView =  findViewById(R.id.trailers_recylerview);

                final LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(DetailActivity.this);

                recyclerView.setLayoutManager(linearLayoutManager);

                adapter =  new TrailersRecyclerViewAdapter(DetailActivity.this,trailers,trailers.size(),new TrailersRecyclerViewAdapter.BtnClickListener() {

                    @Override
                    public void onBtnClick(int position) {
                        // TODO Auto-generated method stub
                    }

                });



                recyclerView.setAdapter(adapter);
            }
        }
    }

    public List<Trailer> loadTrailers(){

        URL url = NetworkUtils.buildTrailersUrl(DetailActivity.this,movieId,1);

        try {
            try {
                String trailersJsonString = NetworkUtils.getResponseFromHttpUrl(url);
                trailerList.addAll(
                        Arrays.asList(JsonUtils.getTrailersFromJson(DetailActivity.this, trailersJsonString)));

                this.movie.setTrailers(trailerList);
            } catch(FileNotFoundException fnfe){
                Log.i(TAG, "Empty Results.");
            }
        } catch (IOException ioe) {
            Log.i(TAG, ioe.getMessage());
        } catch (JSONException jse) {
            Log.i(TAG, jse.getMessage());
        }
        return trailerList;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.i(TAG,"ON SAVE INSTANCE STATE DETAILS");
    }
}
