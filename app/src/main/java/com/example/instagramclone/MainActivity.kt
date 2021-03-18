package com.example.instagramclone

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import com.example.instagramclone.models.Post
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import java.io.ByteArrayOutputStream
import java.io.File


class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG ="MainActivity"
        const val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 19
    }

    internal lateinit var btnAddPost : ImageButton
    internal lateinit var photoFile : File
    internal var photoFileName : String = "photo.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        btnAddPost = findViewById(R.id.ibAddPost)

        btnAddPost.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                launchCamera()
            }

        })

        /*
        btnAddPost.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val i = Intent(this@MainActivity, AddNewPostActivity::class.java)
                startActivity(i)
            }

        })
        */

        queryPosts();
    }

    private fun launchCamera() {
        // create Intent to take a picture and return control to the calling application
        var intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // create a File reference for future access
        photoFile = getPhotofileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        val fileProvider = FileProvider.getUriForFile(this@MainActivity, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        // call startActivityForResult, as long as the result is not null
        // (i.e there's a camera app that can handle our file) -> safe to use the intent
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath) // takenImage is a bitmap
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview

                // compress bitmap before passing it as a Parcelable to the Preview Photoactivity
                // or else would get into Failed Binder Transaction error because the size is too large
                val stream = ByteArrayOutputStream()
                takenImage.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                val bytes = stream.toByteArray()
                var intent = Intent(this@MainActivity, PreviewPhotoActivity::class.java)
                intent.putExtra( "imageBitmapBytes", bytes)
                startActivity(intent)
                // val ivPreview: ImageView =
                //findViewById<View>(android.R.id.ivPreview) as ImageView
                // ivPreview.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*
     Returns the File for a photo stored on disk given the fileName
     */
    private fun getPhotofileUri(photoFileName: String) : File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories
        // This way, don't need to request external read/write runtime permission
        var mediaStorageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // create the storage directory if it doesn't exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on file name
        val file = File(mediaStorageDir.getPath() + File.separator + photoFileName)
        return file
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