package com.e.myfavoritemovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.e.myfavoritemovies.model.Review;
import com.e.myfavoritemovies.model.Trailer;

import java.util.List;

public class TrailersRecyclerViewAdapter extends RecyclerView.Adapter<TrailersRecyclerViewAdapter.ViewHolder> {


    private int mNumberOfItems;
    private List<Trailer> trailers;
    private LayoutInflater mLayoutInflater;
    private ReviewsRecyclerViewAdapter.ItemClickListener mItemClickListener;
    private Context context;

    public TrailersRecyclerViewAdapter(Context context,List<Trailer> trailers, int numberOfItems){
        this.context=context;
        this.trailers=trailers;
        this.mNumberOfItems=numberOfItems;
        this.mLayoutInflater =  LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TrailersRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  mLayoutInflater.inflate(R.layout.linearlayout_trailers,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersRecyclerViewAdapter.ViewHolder holder, int position) {
        Trailer trailer = trailers.get(position);

        holder.mIdTextView.setText(trailer.getId());
        holder.mKeyTextView.setText(trailer.getKey());
        holder.mPlayButton.setText("Trailer "+ ++position);
    }

    @Override
    public int getItemCount() {
        return this.mNumberOfItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mIdTextView;
        TextView mKeyTextView;
        AppCompatButton mPlayButton;

        public ViewHolder(View itemView) {
            super(itemView);

            mIdTextView = itemView.findViewById(R.id.trailer_id);
            mKeyTextView =  itemView.findViewById(R.id.trailer_key);
            mPlayButton = itemView.findViewById(R.id.movie_trailer_button);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
