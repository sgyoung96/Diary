package com.example.diary1.datasave.entity

import androidx.room.*

@Entity
data class UserInfo(
    @PrimaryKey var userId: String = "",
    var userName: String,
    var userPw: String = "",
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    var userImage: ByteArray
)