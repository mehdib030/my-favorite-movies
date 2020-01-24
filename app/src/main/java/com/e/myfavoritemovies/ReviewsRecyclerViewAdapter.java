package com.e.myfavoritemovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.e.myfavoritemovies.model.Review;

import java.util.List;

public class ReviewsRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ViewHolder> {

    private int mNumberOfItems;
    private List<Review> reviews;
    private LayoutInflater mLayoutInflater;
    private ItemClickListener mItemClickListener;
    private Context context;

    public ReviewsRecyclerViewAdapter(Context context, List<Review> reviews,int numberOfItems){
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
        Review review = reviews.get(position);

        holder.mAuthorTextView.setText(review.getAuthor());

        holder.mContentTextView.setText(review.getContent());

        holder.mUrlTextView.setText(review.getUrl());
    }

    @Override
    public int getItemCount() {
        return this.mNumberOfItems;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mIdTextView;
        TextView mAuthorTextView;
        TextView mContentTextView;
        TextView mUrlTextView;

        ViewHolder(View itemView){
            super(itemView);

            mAuthorTextView = itemView.findViewById(R.id.review_author);

            mContentTextView = itemView.findViewById(R.id.review_content);

            mUrlTextView = itemView.findViewById(R.id.review_url);



            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mItemClickListener != null) mItemClickListener.onItemClick(view,getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
