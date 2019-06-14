package com.e.myfavoritemovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.e.myfavoritemovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Recycler adapter to hold images in view holder
 */
public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.ViewHolder> {

    private int mNumberOfItems;
    private List<Movie> movies;
    private LayoutInflater mLayoutInflater;
    private ItemClickListener mItemClickListener;
    private Context context;


    public MoviesRecyclerViewAdapter(Context context, List<Movie> movies,int numberOfItems){
          this.mLayoutInflater =  LayoutInflater.from(context);
          this.movies=movies;
          this.mNumberOfItems=numberOfItems;
          this.context=context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.linearlayout_movie,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Movie movie = movies.get(position);

            if (movie != null) {
                Picasso.get().load(this.context.getString(R.string.image_base_url)+ movie.getImage()).into(holder.mMovieImageView);
            }
    }

    @Override
    public int getItemCount() {
        return this.mNumberOfItems;
    }

    public String getItem(int id){
        return movies.get(id).getOriginalTitle();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView mMovieImageView;

        ViewHolder(View itemView){
            super(itemView);
            mMovieImageView = itemView.findViewById(R.id.poster_image_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mItemClickListener != null) mItemClickListener.onItemClick(view,getAdapterPosition());
        }
    }

    public void setClickListener(ItemClickListener itemClickListener){
        this.mItemClickListener=itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setMovies(List<Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }
}
