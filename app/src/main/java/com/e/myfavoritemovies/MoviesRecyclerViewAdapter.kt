package com.e.myfavoritemovies

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.e.myfavoritemovies.model.Movie
import com.squareup.picasso.Picasso

class MoviesRecyclerViewAdapter(
    private val context: Context,
    private val movies: List<Movie>,
    private val mNumberOfItems: Int
) : RecyclerView.Adapter<MoviesRecyclerViewAdapter.ViewHolder>() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var mItemClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mLayoutInflater.inflate(R.layout.linearlayout_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        Picasso.get()
            .load(context.getString(R.string.image_base_url) + movie.image)
            .into(holder.mMovieImageView)
    }

    override fun getItemCount(): Int = mNumberOfItems

    fun getItem(id: Int): String? = movies[id].originalTitle

    fun setClickListener(itemClickListener: ItemClickListener) {
        mItemClickListener = itemClickListener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val mMovieImageView: ImageView = itemView.findViewById(R.id.poster_image_iv)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            mItemClickListener?.onItemClick(view, adapterPosition)
        }
    }

    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}
