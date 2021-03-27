package com.example.instagramclone.fragments

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.instagramclone.*
import com.example.instagramclone.R
import com.example.instagramclone.models.Post
import com.parse.*
import com.parse.Parse.getApplicationContext
import jp.wasabeef.glide.transformations.RoundedCornersTransformation


class ProfileFragment : PostsFragment() {
    lateinit var postsImagesAdapter: PostsImagesAdapter
    lateinit var ivPostImage : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    @Override
    protected override fun queryPosts() {

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
                postsImagesAdapter.notifyDataSetChanged()
            }
        })
    }

    @Override
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // create layout for one row in the list
        rvPosts = view.findViewById(R.id.rvPosts)
        // create the data source
        allPosts = mutableListOf<Post>()
        // create the adapter
        postsImagesAdapter = context?.let {
            Log.d("ProfileFragment", "checkpoint profile fragment")
            PostsImagesAdapter(it, allPosts) }!!
        adapter = context?.let {
            PostsAdapter(it, allPosts)
        }!!

        // set the adapter on the recycler view
        rvPosts.adapter = postsImagesAdapter
        // set the layout manager on the recycler view
        val gridLayoutManger = GridLayoutManager(context, 3)
        val divider: ItemDecoration = object : ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.setEmpty()
            }
        }
        val mStaggerGridLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        rvPosts.layoutManager = gridLayoutManger
        rvPosts.addItemDecoration(RecyclerViewItemDecorator(0))


        queryPosts()
        ivPostImage = view.findViewById(R.id.ivProfileImageCurrentUser)

        var profileImage = ""

        ParseUser.getCurrentUser().fetchInBackground(object : GetCallback<ParseUser> {
            // fetch user from server
            override fun done(user: ParseUser?, e: ParseException?) {
                if (user != null) {
                    Log.d("ProfileFragment", "check user: " + (user.getParseFile("profileImage")?.url
                            ?: "none"))

                     profileImage = user.getParseFile("profileImage")?.url
                             ?: "none"

                    Glide.with(getApplicationContext()).load(profileImage).apply(RequestOptions.circleCropTransform()).into(ivPostImage)
                }
            }

        })



    }
}