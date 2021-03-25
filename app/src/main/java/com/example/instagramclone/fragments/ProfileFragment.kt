package com.example.instagramclone.fragments

import android.util.Log
import com.example.instagramclone.MainActivity
import com.example.instagramclone.models.Post
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment : PostsFragment() {
    @Override
    protected fun queryPost() {

        super.queryPosts()
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER) // get user alongside with the post
        // Get posts made by the current logged in
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
        // get latest 20 posts
        query.limit = 20
        query.addDescendingOrder(Post.KEY_CREATED_AT)
        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: List<Post>?, e: ParseException?) {
                e?.let{
                    Log.e(MainActivity.TAG, "Issue with Login", e);
                    return
                }
                if (posts != null) {
                    for (post in posts) {
                        Log.i(MainActivity.TAG, "Post: " + post.description + ", username: " + post.user!!.username)
                    }
                }

                posts?.let { allPosts.addAll(it) }
                adapter.notifyDataSetChanged()
            }
        })
    }
}