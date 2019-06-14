package com.e.myfavoritemovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import java.util.List;





public class ReviewsActivity extends AppCompatActivity implements ReviewsRecyclerViewAdapter.ItemClickListener{

    private static final String TAG = ReviewsActivity.class.getSimpleName();

    private ReviewsRecyclerViewAdapter adapter;

    private List<Review> reviewList;

    private String movieId;

    private Movie movie=null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        Intent intent = getIntent();

        if(intent == null){
            closeOnError();
        }

        Movie movie = (Movie)intent.getParcelableExtra("movie");
        this.movie = movie;

        this.movieId = movie.getId();

        new ReviewsActivity.FetchReviewsTask().execute();

    }

    @Override
    public void onItemClick(View view, int position) {

    }


    public class FetchReviewsTask extends AsyncTask<String,Integer, List<Review>> {

        @Override
        protected List<Review> doInBackground(String... strings) {

            reviewList= new ArrayList();

            reviewList = loadReviews();

            Review[] reviews = new Review[reviewList.size()];
            Log.i(TAG, "Reviews LENGTH : "+reviews.length);

            return reviewList;

        }
        @Override
        public void onPostExecute(List<Review> reviews){
            populateUI();
        }
    }

    private List<Review> loadReviews(){

        URL url = NetworkUtils.buildReviewsUrl(ReviewsActivity.this,movieId,1,false,null);

        try {
            try {
                String reviewsJsonString = NetworkUtils.getResponseFromHttpUrl(url);
                reviewList.addAll(
                        Arrays.asList(JsonUtils.getReviewsFromJson(ReviewsActivity.this, reviewsJsonString)));

                this.movie.setReviews(reviewList);
            } catch(FileNotFoundException fnfe){
                Log.i(TAG, "Empty Results.");
            }
        } catch (IOException ioe) {
            Log.i(TAG, ioe.getMessage());
        } catch (JSONException jse) {
            Log.i(TAG, jse.getMessage());
        }

        return reviewList;
    }

    private void closeOnError(){
        finish();
        Toast.makeText(this,"Movie reviews not available.",Toast.LENGTH_SHORT);
    }


    public void populateUI(){
        if (reviewList != null){

            RecyclerView recyclerView = findViewById(R.id.review_recylerview);

            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReviewsActivity.this);

            recyclerView.setLayoutManager(linearLayoutManager);

            adapter = new ReviewsRecyclerViewAdapter(ReviewsActivity.this,reviewList,reviewList.size());

            //adapter.setClickListener(ReviewsActivity.this);

            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this,"Movie reviews not available.",Toast.LENGTH_SHORT);
        }
    }


}
