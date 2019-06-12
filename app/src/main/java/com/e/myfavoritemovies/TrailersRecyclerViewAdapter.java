package com.e.myfavoritemovies;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.e.myfavoritemovies.model.Review;
import com.e.myfavoritemovies.model.Trailer;

import java.io.IOException;
import java.util.List;

public class TrailersRecyclerViewAdapter extends RecyclerView.Adapter<TrailersRecyclerViewAdapter.ViewHolder> {


    private int mNumberOfItems;
    private List<Trailer> trailers;
    private LayoutInflater mLayoutInflater;
    private ReviewsRecyclerViewAdapter.ItemClickListener mItemClickListener;
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

       /* holder.mIdTextView.setText(trailer.getId());
        holder.mKeyTextView.setText(trailer.getKey());*/
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

                    //YouTubeIntents.createPlayVideoIntent(context, videoId));
                   //

                    /*MediaPlayer mp = new MediaPlayer();
                    try {
                        mp.setDataSource("http://www.youtube.com/watch?v="+trailer.getKey());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        mp.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mp.start();*/

                    // mClickListener.onBtnClick((Integer) v.getId());
                }
            }
        });
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

            /*mIdTextView = itemView.findViewById(R.id.trailer_id);
            mKeyTextView =  itemView.findViewById(R.id.trailer_key);*/
            mPlayButton = itemView.findViewById(R.id.movie_trailer_button);


        }

        @Override
        public void onClick(View view) {
            mPlayButton = view.findViewById(R.id.movie_trailer_button);

            //System.out.println("TRAILER BUTTON PRESSED");

        }



      /* mPlayButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v=Hxy8BZGQ5Jo")));
                Log.i("Video", "Video Playing....");

            }
        });*/
    }

    public interface BtnClickListener {
        public abstract void onBtnClick(int position);
    }
}
