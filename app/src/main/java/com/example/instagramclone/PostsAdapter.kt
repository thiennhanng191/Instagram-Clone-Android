package com.example.instagramclone

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.instagramclone.models.Post
import com.parse.Parse.getApplicationContext

class PostsAdapter(
    val context: Context,
    var posts: MutableList<Post>,
    private val mOnPostListener: OnPostListener
) : RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view, context, mOnPostListener)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts.get(position)
        holder.bind(post)
    }

    // Clear all elements of the recycler
    fun clear() {
        posts.clear() // modified the existing tweets list - should not make a new ArrayList() to keep the same reference so as not to mess up the recycler view
        notifyDataSetChanged()
    }

    // Add a list of items
    fun addAll(postsList: List<Post>) {
        posts.addAll(postsList)
    }

    class ViewHolder(itemView: View, val context: Context, onPostListener: OnPostListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var tvUsername : TextView = itemView.findViewById(R.id.tvUsername)
        private var ivPostImage : ImageView = itemView.findViewById(R.id.ivPostImage)
        private var tvPostCaption : TextView = itemView.findViewById(R.id.tvPostCaption)
        private var ivProfileImage : ImageView = itemView.findViewById(R.id.ivProfileImage)
        private var onPostListener : OnPostListener = onPostListener

        private var ibLike : ImageButton = itemView.findViewById(R.id.ibLike)
        fun bind(post: Post) {
            itemView.setOnClickListener(this)
            // Bind the post data to the view elements
            tvUsername.setText(post.user?.username)
            tvPostCaption.setText(post.description)
            val image = post.image
            image?.let {
                Glide.with(context).load(post.image?.url).into(ivPostImage)
            }
            val profileImage = post.user?.getParseFile("profileImage")?.url
            Glide.with(context).load(profileImage).apply(RequestOptions.circleCropTransform()).into(ivProfileImage)

            ibLike.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View?) {
                    ibLike.setImageResource(R.drawable.ic_instagram_heart_filled)
                }
            })
        }

        override fun onClick(v: View?) {
            onPostListener.onPostClick(adapterPosition)
        }
    }

     interface OnPostListener {
        fun onPostClick(position : Int)
    }

}