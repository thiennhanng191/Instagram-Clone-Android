package com.example.instagramclone

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instagramclone.models.Post
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery


class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG ="MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        queryPosts();
    }

    private fun queryPosts() {
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER) // get user alongside with the post
        // get all posts
        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                e?.let{
                    Log.e(TAG, "Issue with Login", e);
                    return
                }
                if (posts != null) {
                    for (post in posts) {
                        Log.i(TAG, "Post: " + post.description + ", username: " + post.user!!.username)
                    }
                }
            }
        })
    }
}