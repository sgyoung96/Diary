package com.example.diary1.ui.fragment.listrecycler

import android.graphics.Bitmap
import java.io.Serializable

data class PostedDiaryInfo (
    var postDate: String,
    var postTitle: String,
    var postContent: String,
    var postMy: String
): Serializable
