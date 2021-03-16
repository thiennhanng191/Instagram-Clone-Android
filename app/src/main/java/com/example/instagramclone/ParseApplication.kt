package com.example.instagramclone

import android.app.Application
import com.parse.Parse

class ParseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(BuildConfig.APPLICATION_ID)
                .clientKey(BuildConfig.CLIENT_KEY)
                .server("https://parseapi.back4app.com")
                .build()
        )
    }
}