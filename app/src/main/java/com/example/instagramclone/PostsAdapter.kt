package com.example.instagramclone

import android.content.Context
import android.content.Intent
import android.util.Log
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
import com.example.instagramclone.utils.TimeFormatter
import com.parse.GetCallback
import com.parse.Parse
import com.parse.Parse.getApplicationContext
import com.parse.ParseException
import com.parse.ParseUser

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
        private var tvCreatedAt : TextView = itemView.findViewById(R.id.tvCreatedAt)
        private var tvLikeCount : TextView = itemView.findViewById(R.id.tvLikeCount)
        private var ibLike : ImageButton = itemView.findViewById(R.id.ibLike)
        private var viewContainerUserProfile : View = itemView.findViewById(R.id.viewContainerUserProfile)

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

            ParseUser.getCurrentUser().fetchInBackground(object : GetCallback<ParseUser> {
                // fetch user from server
                override fun done(user: ParseUser?, e: ParseException?) {
                    if (user != null) {
                        var array = user.getList<String>("postsLiked")
                        // add the post to the list of posts that current user has liked
                        if (array != null) {
                            if (array.contains(post.objectId)) {
                                ibLike.setImageResource(R.drawable.ic_instagram_heart_filled)
                                ibLike.isEnabled = false
                            }
                        }
                    }
                }
            })

            ibLike.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View?) {
                    ibLike.setImageResource(R.drawable.ic_instagram_heart_filled)
                    post.likeCount = post.likeCount!! + 1
                    post.save() // update post's like count
                    post.likeCount?.let { tvLikeCount.setText(if (it > 1) ("" + it + " likes") else ("" + it + " like")) }

                    // fetch the current logged in user to update the lists of liked posts
                    ParseUser.getCurrentUser().fetchInBackground(object : GetCallback<ParseUser> {
                        // fetch user from server
                        override fun done(user: ParseUser?, e: ParseException?) {
                            if (user != null) {
                                var array = user.getList<String>("postsLiked")
                                // add the post to the list of posts that current user has liked
                                if (array == null) {
                                    Log.d("PostsAdapter", "Check array null")
                                    array = mutableListOf<String>()
                                }
                                array.add(post.objectId)
                                array?.let { user.put("postsLiked", it) }
                                user.saveInBackground()
                            }
                        }
                    })
                }
            })

            post.likeCount?.let { tvLikeCount.setText(if (it > 1) ("" + it + " likes") else ("" + it + " like")) }

            tvCreatedAt.setText(TimeFormatter.getTimeStamp(post.createdAt))

            viewContainerUserProfile.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {
                    var intent = Intent(context, UserProfileActivity::class.java)
                    intent.putExtra("post", post)
                    context.startActivity(intent)
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