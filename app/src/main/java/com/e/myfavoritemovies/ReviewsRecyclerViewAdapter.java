package com.e.myfavoritemovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.e.myfavoritemovies.model.Review;

public class ReviewsRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ViewHolder> {

    private int mNumberOfItems;
    private Review[] reviews;
    private LayoutInflater mLayoutInflater;
    private MoviesRecyclerViewAdapter.ItemClickListener mItemClickListener;
    private Context context;

    public ReviewsRecyclerViewAdapter(Context context, Review[] reviews,int numberOfItems){
        this.mLayoutInflater =  LayoutInflater.from(context);
        this.reviews=reviews;
        this.mNumberOfItems=numberOfItems;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.linearlayout_reviews,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviews[position];
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ViewHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mItemClickListener != null) mItemClickListener.onItemClick(view,getAdapterPosition());
        }
    }

    public void setClickListener(MoviesRecyclerViewAdapter.ItemClickListener itemClickListener){
        this.mItemClickListener=itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
