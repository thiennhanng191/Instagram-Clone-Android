package com.example.instagramclone

import android.app.Application
import com.example.instagramclone.models.Post
import com.parse.Parse
import com.parse.ParseObject


class ParseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Register parse models
        ParseObject.registerSubclass(Post::class.java)

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(BuildConfig.APPLICATION_ID)
                .clientKey(BuildConfig.CLIENT_KEY)
                .server("https://parseapi.back4app.com")
                .build()
        )
    }
}