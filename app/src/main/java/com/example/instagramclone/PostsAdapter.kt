package com.example.instagramclone

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.models.Post

class PostsAdapter(val context : Context, var posts : List<Post>) : RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view, context)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts.get(position)
        holder.bind(post)
    }

    class ViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {
        private var tvUsername : TextView = itemView.findViewById(R.id.tvUsername)
        private var ivPostImage : ImageView = itemView.findViewById(R.id.ivPostImage)
        private var tvPostCaption : TextView = itemView.findViewById(R.id.tvPostCaption)

        fun bind(post: Post) {
            // Bind the post data to the view elements
            tvUsername.setText(post.user?.username)
            tvPostCaption.setText(post.description)
            val image = post.image
            image?.let {
                Glide.with(context).load(post.image?.url).into(ivPostImage)
            }
        }
    }

}