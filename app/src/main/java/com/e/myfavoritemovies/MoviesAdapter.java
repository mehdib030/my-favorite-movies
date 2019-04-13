package com.e.myfavoritemovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.e.myfavoritemovies.model.Movie;
import com.squareup.picasso.Picasso;

public class MoviesAdapter extends BaseAdapter {

    private Context context;
    private Movie[] movies;
    int c=0;

    public MoviesAdapter(Context context,Movie[] movies){
        this.context=context;
        this.movies=movies;
    }

    @Override
    public int getCount() {
        return movies.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        Movie movie = movies[position];


        System.out.println("Total movies : "+c++);

        if(view == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.linearlayout_movie, null);
        }

        ImageView mMovieImageView = view.findViewById(R.id.poster_iv);
        TextView mOriginalTitleTextView = view.findViewById(R.id.movie_title_tv);
       /* mPlotSynopsisTextView = findViewById(R.id.plot_synopsis_tv);
        mRatingTextView = findViewById(R.id.rating_tv);
        mReleaseDateTextView = findViewById(R.id.release_date_tv);*/

       if (movie != null) {
           System.out.println("IMAGE URL : " + movie.getImage());

           Picasso.get().load("http://image.tmdb.org/t/p/w185//" + movie.getImage()).into(mMovieImageView);

           mOriginalTitleTextView.setText(movie.getOriginalTitle());
       }

        return view;
    }
}
