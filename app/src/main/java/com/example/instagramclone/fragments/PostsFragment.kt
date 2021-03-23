package com.example.instagramclone.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramclone.MainActivity
import com.example.instagramclone.PostsAdapter
import com.example.instagramclone.R
import com.example.instagramclone.models.Post
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery

class PostsFragment : Fragment() {
    companion object {
        const val TAG = "PostsFragment"
    }
    lateinit var rvPosts: RecyclerView
    lateinit var adapter : PostsAdapter
    lateinit var allPosts : MutableList<Post>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // create layout for one row in the list
        rvPosts = view.findViewById(R.id.rvPosts)
        // create the data source
        allPosts = mutableListOf<Post>()
        // create the adapter
        adapter = context?.let { PostsAdapter(it, allPosts) }!!
        // set the adapter on the recycler view
        rvPosts.adapter = adapter
        // set the layout manager on the recycler view
        rvPosts.layoutManager = LinearLayoutManager(context)

        queryPosts()
    }

    private fun queryPosts() {
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER) // get user alongside with the post
        // get all posts
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