package com.example.instagramclone

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream
import java.io.File


class PreviewPhotoActivity : AppCompatActivity() {
    internal lateinit var ivPreviewPhoto : ImageView
    internal lateinit var btnNext : Button
    internal lateinit var ibGoBack : ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_photo)
        ibGoBack = findViewById(R.id.ibGoBack)

        ibGoBack.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                finish()
            }

        })

        ivPreviewPhoto = findViewById(R.id.ivPreviewPhoto)

        var photoFile = getIntent().extras!!["photoFile"] as File?

        // get and decompress bitmap received from MainActivity
        var bytes = getIntent().getByteArrayExtra("imageBitmapBytes")
        var takenPhotoBitmap = bytes?.size?.let { BitmapFactory.decodeByteArray(bytes, 0, it) }
        ivPreviewPhoto.setImageBitmap(takenPhotoBitmap)

        btnNext = findViewById(R.id.btnNext)
        btnNext.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val stream = ByteArrayOutputStream()
                takenPhotoBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                val bytes = stream.toByteArray()
                var intent = Intent(this@PreviewPhotoActivity, AddNewPostActivity::class.java)
                intent.putExtra( "imageBitmapBytes", bytes)
                intent.putExtra("photoFile", photoFile)
                startActivity(intent)
            }
        })
    }
}