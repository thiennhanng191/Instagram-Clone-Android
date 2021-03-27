package com.example.instagramclone

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.instagramclone.models.Post
import com.example.instagramclone.utils.TimeFormatter
import com.parse.GetCallback
import com.parse.ParseException
import com.parse.ParseUser
import java.io.File

class PostDetailsActivity : AppCompatActivity() {
    lateinit var ivProfileImage : ImageView
    lateinit var tvUsername : TextView
    lateinit var ivPostImage : ImageView
    lateinit var tvPostCaption : TextView
    lateinit var tvCreatedAt : TextView
    lateinit var ibLike : ImageButton
    lateinit var tvLikeCount : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)
        ivProfileImage = findViewById(R.id.ivProfileImage)
        tvUsername = findViewById(R.id.tvUsername)
        ivPostImage = findViewById(R.id.ivPostImage)
        tvPostCaption = findViewById(R.id.tvPostCaption)
        ibLike = findViewById(R.id.ibLike)
        tvLikeCount = findViewById(R.id.tvLikeCount)

        var post = getIntent().extras!!["post"] as Post?

        if (post != null) {
            tvUsername.setText(post.user?.username)
            tvPostCaption.setText(post.description)
            val image = post.image
            image?.let {
                Glide.with(this).load(post?.image?.url).into(ivPostImage)
            }
            val profileImage = post.user?.getParseFile("profileImage")?.url
            Glide.with(this).load(profileImage).apply(RequestOptions.circleCropTransform()).into(ivProfileImage)

            tvCreatedAt = findViewById(R.id.tvCreatedAt)
            tvCreatedAt.setText(TimeFormatter.getTimeStamp(post.createdAt))

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

        }
    }
}