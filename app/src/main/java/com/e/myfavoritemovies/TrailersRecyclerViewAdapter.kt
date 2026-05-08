package com.e.myfavoritemovies

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.e.myfavoritemovies.model.Trailer

class TrailersRecyclerViewAdapter(
    private val context: Context,
    private val trailers: List<Trailer>,
    private val mNumberOfItems: Int,
    private val mClickListener: BtnClickListener?
) : RecyclerView.Adapter<TrailersRecyclerViewAdapter.ViewHolder>() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mLayoutInflater.inflate(R.layout.linearlayout_trailers, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trailer = trailers[position]
        holder.mPlayButton.text = "Play Trailer ${position + 1}"

        holder.mPlayButton.setOnClickListener {
            if (mClickListener != null) {
                println("TRAILER BUTTON PRESSED TESTING : http://www.youtube.com/watch?v=${trailer.key}")
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=${trailer.key}"))
                )
            }
        }
    }

    override fun getItemCount(): Int = mNumberOfItems

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val mPlayButton: AppCompatButton = itemView.findViewById(R.id.movie_trailer_button)

        override fun onClick(view: View) {
            // No-op: click handling is done in onBindViewHolder
        }
    }

    interface BtnClickListener {
        fun onBtnClick(position: Int)
    }
}
