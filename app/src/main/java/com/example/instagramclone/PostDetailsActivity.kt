package com.example.instagramclone

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.instagramclone.models.Post
import java.io.File

class PostDetailsActivity : AppCompatActivity() {
    lateinit var ivProfileImage : ImageView
    lateinit var tvUsername : TextView
    lateinit var ivPostImage : ImageView
    lateinit var tvPostCaption : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)
        ivProfileImage = findViewById(R.id.ivProfileImage)
        tvUsername = findViewById(R.id.tvUsername)
        ivPostImage = findViewById(R.id.ivPostImage)
        tvPostCaption = findViewById(R.id.tvPostCaption)

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
        }

    }
}