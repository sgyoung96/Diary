package com.example.diary1.datasave.entity

import androidx.room.*
import com.example.diary1.datasave.constants.PostDiaryInfo
import java.io.Serializable

@Entity(primaryKeys = [PostDiaryInfo.DB_COL_USERID, PostDiaryInfo.DB_COL_DATE])
data class PostInfo(
    var userId: String = "",
    var post_date: String,
    var post_title: String,
    var post_content: String,
    var post_my: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var post_image: ByteArray
): Serializable