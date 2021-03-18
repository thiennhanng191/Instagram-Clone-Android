package com.example.instagramclone.models

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser

@ParseClassName("Post")
class Post () : ParseObject() {
    companion object {
        const val KEY_DESCRIPTION ="description"
        const val KEY_IMAGE ="image"
        const val KEY_USER ="user"
    }

    var description : String?
        get() {
            return getString(KEY_DESCRIPTION)
        }
        set(description) {
            description?.let { put(KEY_DESCRIPTION, it) }
        }
    var image : ParseFile?
        get(){
            return getParseFile(KEY_IMAGE)
        }
        set(image) {
            image?.let { put(KEY_IMAGE, it) }
        }

    var user : ParseUser?
        get(){
            return getParseUser(KEY_USER)
        }
        set(user) {
            user?.let{ put(KEY_USER, it) }
        }

}