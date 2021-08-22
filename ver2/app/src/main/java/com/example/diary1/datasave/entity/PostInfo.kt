package com.example.diary1.datasave.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class PostInfo(
    @PrimaryKey var userId: String = "",
    var post_date: String,
    var post_title: String,
    var post_content: String,
    var post_my: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var post_image: ByteArray
): Serializable