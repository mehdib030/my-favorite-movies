package com.e.myfavoritemovies

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.e.myfavoritemovies.model.Review

class ReviewsRecyclerViewAdapter(
    private val context: Context,
    private val reviews: List<Review>,
    private val mNumberOfItems: Int
) : RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ViewHolder>() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var mItemClickListener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mLayoutInflater.inflate(R.layout.linearlayout_reviews, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviews[position]
        holder.mAuthorTextView.text = review.author
        holder.mContentTextView.text = review.content
        holder.mUrlTextView.text = review.url
    }

    override fun getItemCount(): Int = mNumberOfItems

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val mAuthorTextView: TextView = itemView.findViewById(R.id.review_author)
        val mContentTextView: TextView = itemView.findViewById(R.id.review_content)
        val mUrlTextView: TextView = itemView.findViewById(R.id.review_url)

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
