package com.example.instagramclone

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.instagramclone.models.Post
import com.parse.*

class UserProfileActivity : AppCompatActivity(), PostsImagesAdapter.OnPostThumbnailListener {
    companion object {
        const val TAG ="UserProfileActivity"

    }
    lateinit var rvPostsProfile : RecyclerView
    lateinit var allPosts : MutableList<Post>
    lateinit var tvUsername : TextView
    lateinit var ivProfileImage : ImageView
    lateinit var postsImagesAdapter: PostsImagesAdapter
    lateinit var post : Post

    @Override
    protected fun queryPosts() {
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER) // get user alongside with the post
        // Get posts made by the current logged in
        query.whereEqualTo(Post.KEY_USER, post.user)
        // get latest 20 posts
        query.limit = 20
        query.addDescendingOrder(Post.KEY_CREATED_AT)
        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: List<Post>?, e: ParseException?) {
                e?.let{
                    Log.e(TAG, "Issue get posts from user", e);
                    return
                }
                if (posts != null) {
                    for (post in posts) {
                        Log.i(TAG, "Post: " + post.description + ", username: " + post.user!!.username)
                    }
                }

                posts?.let { allPosts.addAll(it) }
                postsImagesAdapter.notifyDataSetChanged()
            }
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        tvUsername = findViewById(R.id.tvUsername)
        ivProfileImage = findViewById(R.id.ivProfileImage)

        post = getIntent().extras!!["post"] as Post

        if (post != null) {
            // create layout for one row in the list
            rvPostsProfile = findViewById(R.id.rvPostsProfile)
            // create the data source
            allPosts = mutableListOf<Post>()
            // create the adapter
            postsImagesAdapter = PostsImagesAdapter(this, allPosts, this)

            // set the adapter on the recycler view
            rvPostsProfile.adapter = postsImagesAdapter
            // set the layout manager on the recycler view
            val gridLayoutManger = GridLayoutManager(this, 3)
            val divider: RecyclerView.ItemDecoration = object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    outRect.setEmpty()
                }
            }
            val mStaggerGridLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            rvPostsProfile.layoutManager = gridLayoutManger
            rvPostsProfile.addItemDecoration(RecyclerViewItemDecorator(0))

            var profileImage = post.user?.getParseFile("profileImage")?.url
            Glide.with(Parse.getApplicationContext()).load(profileImage).apply(RequestOptions.circleCropTransform()).into(ivProfileImage)
            tvUsername.setText(post.user?.username)

            queryPosts()
        }
    }

    override fun onPostThumbnailClick(position: Int) {
        val post = allPosts.get(position)
        var intent = Intent(Parse.getApplicationContext(), PostDetailsActivity::class.java)
        intent.putExtra("post", post)
        startActivity(intent)
    }
}