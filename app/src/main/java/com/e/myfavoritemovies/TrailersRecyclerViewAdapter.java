package com.e.myfavoritemovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.myfavoritemovies.model.Trailer;

import java.util.List;

public class TrailersRecyclerViewAdapter extends RecyclerView.Adapter<TrailersRecyclerViewAdapter.ViewHolder> {


    private int mNumberOfItems;
    private List<Trailer> trailers;
    private LayoutInflater mLayoutInflater;
    private Context context;
    private BtnClickListener mClickListener = null;
    
    public TrailersRecyclerViewAdapter(Context context,List<Trailer> trailers, int numberOfItems,BtnClickListener listener){
        this.context=context;
        this.trailers=trailers;
        this.mNumberOfItems=numberOfItems;
        this.mLayoutInflater =  LayoutInflater.from(context);
        this.mClickListener = listener;
    }

    @NonNull
    @Override
    public TrailersRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  mLayoutInflater.inflate(R.layout.linearlayout_trailers,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersRecyclerViewAdapter.ViewHolder holder, int position) {
        final Trailer trailer = trailers.get(position);

        holder.mPlayButton.setText("Play Trailer "+ ++position);

        holder.mPlayButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(mClickListener != null) {
                    System.out.println("TRAILER BUTTON PRESSED TESTING : http://www.youtube.com/watch?v="+trailer.getKey());

                    context.startActivity(
                            new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://www.youtube.com/watch?v="+trailer.getKey())));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mNumberOfItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatButton mPlayButton;

        public ViewHolder(View itemView) {
            super(itemView);

            mPlayButton = itemView.findViewById(R.id.movie_trailer_button);
        }

        @Override
        public void onClick(View view) {
            mPlayButton = view.findViewById(R.id.movie_trailer_button);

        }
    }

    public interface BtnClickListener {
        public abstract void onBtnClick(int position);
    }
}
