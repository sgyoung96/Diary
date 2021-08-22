package com.example.diary1.datasave.dao

import androidx.annotation.Nullable
import androidx.room.*
import com.example.diary1.datasave.constants.RegisterInfo
import com.example.diary1.datasave.entity.UserInfo

@Dao
interface UserDao {
    @Insert
    fun register(user: UserInfo)

    @Query("SELECT * FROM ${RegisterInfo.DB_TABLE_NAME} WHERE ${RegisterInfo.DB_COL_ID} = :id")
    fun checkOneRegister(id: String): List<UserInfo>

    @Query("SELECT * FROM ${RegisterInfo.DB_TABLE_NAME} WHERE ${RegisterInfo.DB_COL_ID} = :id")
    fun checkPW(id: String): List<UserInfo>

    // @Query("SELECT ${RegisterInfo.DB_COL_NAME}, ${RegisterInfo.DB_COLE_IMAGE} FROM ${RegisterInfo.DB_TABLE_NAME} WHERE ${RegisterInfo.DB_COL_ID} = :id")
    // @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    // fun getDefault(id: String): List<UserInfo>

    @Query("UPDATE ${RegisterInfo.DB_TABLE_NAME} SET ${RegisterInfo.DB_COL_NAME} = :name, ${RegisterInfo.DB_COL_PW} = :pw, ${RegisterInfo.DB_COLE_IMAGE} = :image WHERE ${RegisterInfo.DB_COL_ID} = :id")
    fun saveSetting(id: String, name: String, pw: String, image: ByteArray)

    @Query("DELETE FROM ${RegisterInfo.DB_TABLE_NAME} WHERE ${RegisterInfo.DB_COL_ID} = :id")
    fun deleteUser(id: String)
}