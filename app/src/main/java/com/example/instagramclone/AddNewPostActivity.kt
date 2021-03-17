package com.example.instagramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.instagramclone.models.Post
import com.parse.ParseException
import com.parse.ParseUser
import com.parse.SaveCallback

class AddNewPostActivity : AppCompatActivity() {
    internal lateinit var ibGoBack: ImageButton
    internal lateinit var btnShare: Button
    internal lateinit var etPostCaption: EditText

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
                savePost(description, currentUser)
            }
        })
    }
    private fun savePost(description: String, currentUser: ParseUser) {
        var post = Post()
        post.description = description
        // TODO post.image = image
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
                etPostCaption.setText("") // reset Edit Text
            }

        })
    }
}