package com.example.instagramclone

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.instagramclone.models.Post
import com.parse.ParseException
import com.parse.ParseFile
import com.parse.ParseUser
import com.parse.SaveCallback
import java.io.File

class AddNewPostActivity : AppCompatActivity() {
    internal lateinit var ibGoBack: ImageButton
    internal lateinit var btnShare: Button
    internal lateinit var etPostCaption: EditText
    internal lateinit var ivPhotoPost: ImageView

    companion object {
        const val TAG ="AddNewPostActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_post)

        ibGoBack = findViewById(R.id.ibGoBack)

        ibGoBack.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                finish()
            }

        })
        ivPhotoPost = findViewById(R.id.ivPhotoPost)

        // get and decompress bitmap received from MainActivity
        var photoFile = getIntent().extras!!["photoFile"] as File?

        var bytes = getIntent().getByteArrayExtra("imageBitmapBytes")
        var takenPhotoBitmap = bytes?.size?.let { BitmapFactory.decodeByteArray(bytes, 0, it) }
        // Load the taken image into a preview
        ivPhotoPost.setImageBitmap(takenPhotoBitmap)

        etPostCaption = findViewById(R.id.etPostCaption)
        btnShare = findViewById(R.id.btnShare)
        btnShare.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val description = etPostCaption.getText().toString() // description (i.e caption)
                description?.let{
                    if (description.isEmpty()) {
                        Toast.makeText(
                            this@AddNewPostActivity,
                            "Description cannot be empty",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                }
                val currentUser = ParseUser.getCurrentUser()

                photoFile?.let { savePost(description, currentUser, it) }
            }
        })
    }
    private fun savePost(description: String, currentUser: ParseUser, photoFile : File) {
        var post = Post()
        post.description = description
        post.image = ParseFile(photoFile)
        post.user = currentUser
        post.saveInBackground(object : SaveCallback {
            override fun done(e: ParseException?) {
                e?.let{
                    Log.e(TAG, "Error while saving", e)
                    Toast.makeText(
                        this@AddNewPostActivity,
                        "Error while saving",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Log.i(TAG, "Post saved successfully")
                // reset EditText
                etPostCaption.setText("")

                // reset ImageView
                ivPhotoPost.setImageResource(0)
                // finish()
                var intent = Intent(this@AddNewPostActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                Toast.makeText(
                    getApplicationContext(),
                    "Post added successfully",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

        })
    }
}