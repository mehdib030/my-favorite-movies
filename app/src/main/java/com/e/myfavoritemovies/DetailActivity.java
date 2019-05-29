package com.e.myfavoritemovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.e.myfavoritemovies.Database.AppDatabase;
import com.e.myfavoritemovies.Database.FavoriteMovieEntry;
import com.e.myfavoritemovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

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
        final Movie movie = movies.get(position);

        populateUI(movie);
        Picasso.get().load(BASE_URL +movie.getImage()).into(mMovieImageView);

        AppCompatButton favoriteMoviesButton = findViewById(R.id.favorite_movie_button);

        favoriteMoviesButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                System.out.println("Button clicked id : "+view.getId());



                String buttonText =  ((AppCompatButton)view).getText().toString();

                switch(buttonText){
                    case MARK_AS_FAVORITE:
                        onSaveButtonClicked(movie,view);
                        break;
                    case REMOVE_AS_FAVORITE:
                        onRemoveButtonClicked(movie,view);
                        break;
                }

                //System.out.println("~~~~ BUTTON TEXT : "+buttonText);
                //onSaveButtonClicked(movie,view);
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

        System.out.println("Saving favorite movie : "+title+", id : "+id);

        AppExecutors.getInstance().diskIO().execute(new Runnable(){

            @Override
            public void run() {
                fmdb.favoriteMovieDao().insertFavoriteMovie(favoriteMovieEntry);

                List<FavoriteMovieEntry> favs = fmdb.favoriteMovieDao().loadAllFavoriteMovies();

                Iterator it = favs.iterator();

                while(it.hasNext()){
                    FavoriteMovieEntry fav = (FavoriteMovieEntry) it.next();
                    System.out.println(" FAV : "+fav.getTitle()+", id : "+fav.getMovieId());
                }
                //finish();
            }
        });

        ((AppCompatButton)view).setText("REMOVE AS FAVORITE"); //TODO: put is strings.xml

    }

    public void onRemoveButtonClicked(Movie movie,View view){ //TODO: merge with onSaveButtonClicked()
        String title=movie.getOriginalTitle();
        final String id = movie.getId();

        final FavoriteMovieEntry favoriteMovieEntry = new FavoriteMovieEntry(title,id);

        System.out.println("Removing favorite movie : "+title+", id : "+id);

        AppExecutors.getInstance().diskIO().execute(new Runnable(){

            @Override
            public void run() {
                fmdb.favoriteMovieDao().deleteFavoriteMovieById(id);

                List<FavoriteMovieEntry> favs = fmdb.favoriteMovieDao().loadAllFavoriteMovies();

                Iterator it = favs.iterator();

                while(it.hasNext()){
                    FavoriteMovieEntry fav = (FavoriteMovieEntry) it.next();
                    System.out.println(" FAV : "+fav.getTitle()+", id : "+fav.getMovieId());
                }
            }
        });

        ((AppCompatButton)view).setText("MARK AS FAVORITE"); //TODO: put is strings.xml
    }


    private void closeOnError(){
        finish();
        Toast.makeText(this,"Movie data not available.",Toast.LENGTH_SHORT);
    }
}
