package com.e.myfavoritemovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.e.myfavoritemovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private static final String BASE_URL="http://image.tmdb.org/t/p/w185//";

    private ImageView mMovieImageView;
    private TextView mOriginalTitleTextView;
    private TextView mPlotSynopsisTextView;
    private TextView mRatingTextView;
    private TextView mReleaseDateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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

        Movie movie = movies.get(position);

        populateUI(movie);
        Picasso.get().load(BASE_URL +movie.getImage()).into(mMovieImageView);
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

    private void closeOnError(){
        finish();
        Toast.makeText(this,"Movie data not available.",Toast.LENGTH_SHORT);
    }
}
