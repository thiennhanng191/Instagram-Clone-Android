package com.example.instagramclone.fragments

import EndlessRecyclerViewScrollListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.instagramclone.MainActivity
import com.example.instagramclone.PostDetailsActivity
import com.example.instagramclone.PostsAdapter
import com.example.instagramclone.R
import com.example.instagramclone.models.Post
import com.parse.FindCallback
import com.parse.Parse.getApplicationContext
import com.parse.ParseException
import com.parse.ParseQuery


open class PostsFragment : Fragment(), PostsAdapter.OnPostListener {
    companion object {
        const val TAG = "PostsFragment"
    }
    lateinit var rvPosts: RecyclerView
    lateinit var adapter : PostsAdapter
    lateinit var allPosts : MutableList<Post>
    lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener
    lateinit var swipeContainer : SwipeRefreshLayout

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
        swipeContainer = view.findViewById(R.id.swipeContainer) as SwipeRefreshLayout;

        // create layout for one row in the list
        rvPosts = view.findViewById(R.id.rvPosts)
        // create the data source
        allPosts = mutableListOf<Post>()
        // create the adapter
        adapter = context?.let { PostsAdapter(it, allPosts, this) }!!
        // set the adapter on the recycler view
        rvPosts.adapter = adapter
        // set the layout manager on the recycler view
        val linearLayoutManager = LinearLayoutManager(context)
        rvPosts.layoutManager = linearLayoutManager

        endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadMorePosts(page)
            }
        }
        rvPosts.addOnScrollListener(endlessRecyclerViewScrollListener)
        swipeContainer.setOnRefreshListener { // Your code to refresh the list here.
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            queryPosts()
        }
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);

        queryPosts()
    }

    private fun loadMorePosts(page: Int) {
        var page = page

        var displayLimit = 20
        // Get the count on a collection
        Log.d("PostsFragment", "Load more data " + page)

        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER) // get user alongside with the post
        // get latest 20 posts
        query.limit = 5
        // query.skip = page * displayLimit
        query.setSkip(page*5)
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

                posts?.let { adapter.addAll(it) }
                // adapter.notifyDataSetChanged()
            }
        })


    }

    protected fun queryPosts() {
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER) // get user alongside with the post
        // get latest 20 posts
        query.limit = 5
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
                adapter.clear()
                posts?.let { adapter.addAll(it) }
                // adapter.notifyDataSetChanged()
                swipeContainer?.let{swipeContainer.isRefreshing = false}
            }
        })
    }

    override fun onPostClick(position: Int) {
        Log.d(TAG, "check post clicked")
        val post = allPosts.get(position)
        var intent = Intent(getApplicationContext(), PostDetailsActivity::class.java)
        intent.putExtra("post", post)
        startActivity(intent)
    }
}