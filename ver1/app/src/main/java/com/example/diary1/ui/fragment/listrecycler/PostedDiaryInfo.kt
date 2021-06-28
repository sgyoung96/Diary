package com.example.diary1.ui.fragment.listrecycler

import java.io.Serializable

data class PostedDiaryInfo (
    var postDate: String,
    var postTitle: String,
    var postContent: String
): Serializable
