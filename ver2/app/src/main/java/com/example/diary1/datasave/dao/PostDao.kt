package com.example.diary1.datasave.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.diary1.datasave.constants.PostDiaryInfo
import com.example.diary1.datasave.constants.RegisterInfo
import com.example.diary1.datasave.entity.PostInfo

@Dao
interface PostDao {
    @Insert
    fun insertDiary(post: PostInfo)

    @Query("SELECT * FROM ${PostDiaryInfo.DB_TABLE_NAME} WHERE ${PostDiaryInfo.DB_COL_USERID} = :id AND ${PostDiaryInfo.DB_COL_DATE} = :date")
    fun checkDiary(id: String, date: String): List<PostInfo>

    @Query("SELECT B.* FROM ${RegisterInfo.DB_TABLE_NAME} A, ${PostDiaryInfo.DB_TABLE_NAME} B WHERE A.${RegisterInfo.DB_COL_ID} = B.${PostDiaryInfo.DB_COL_USERID} AND A.${RegisterInfo.DB_COL_ID} = :id ORDER BY B.${PostDiaryInfo.DB_COL_DATE} DESC")
    fun getListFromId(id: String): List<PostInfo>

    @Query("SELECT * FROM ${PostDiaryInfo.DB_TABLE_NAME} WHERE ${PostDiaryInfo.DB_COL_USERID} = :id AND ${PostDiaryInfo.DB_COL_MY} = '1' ORDER BY ${PostDiaryInfo.DB_COL_DATE} DESC")
    fun getMyDiary(id: String): List<PostInfo>

    @Query("UPDATE ${PostDiaryInfo.DB_TABLE_NAME} SET ${PostDiaryInfo.DB_COL_TITLE} = :title, ${PostDiaryInfo.DB_COL_DATE} = :date, ${PostDiaryInfo.DB_COL_CONTENT} = :content, ${PostDiaryInfo.DB_COL_IMAGE} = :image WHERE ${PostDiaryInfo.DB_COL_USERID} = :id AND ${PostDiaryInfo.DB_COL_DATE} = :originalDate")
    fun saveDiary(id: String, title: String, date: String, content: String, originalDate: String, image: ByteArray)

    @Query("DELETE FROM ${PostDiaryInfo.DB_TABLE_NAME} WHERE ${PostDiaryInfo.DB_COL_USERID} = :id AND ${PostDiaryInfo.DB_COL_TITLE} = :title AND ${PostDiaryInfo.DB_COL_DATE} = :date")
    fun deleteDiary(id: String, title: String, date: String)

    @Query("SELECT ${PostDiaryInfo.DB_TABLE_NAME}.* FROM ${PostDiaryInfo.DB_TABLE_NAME} WHERE ${PostDiaryInfo.DB_COL_USERID} = :id AND ${PostDiaryInfo.DB_COL_DATE} = :date")
    fun getDiaryImage(id: String, date: String): List<PostInfo>

    @Query("DELETE FROM ${PostDiaryInfo.DB_TABLE_NAME} WHERE ${PostDiaryInfo.DB_COL_USERID} = :id")
    fun deletePost(id: String)

    @Query("SELECT ${PostDiaryInfo.DB_TABLE_NAME}.* FROM ${PostDiaryInfo.DB_TABLE_NAME} WHERE ${PostDiaryInfo.DB_COL_USERID} = :id AND ${PostDiaryInfo.DB_COL_DATE} = :date")
    fun selectMyFlag(id: String, date: String): List<PostInfo>

    @Query("UPDATE ${PostDiaryInfo.DB_TABLE_NAME} SET ${PostDiaryInfo.DB_COL_MY} = '0' WHERE ${PostDiaryInfo.DB_COL_USERID} = :id AND ${PostDiaryInfo.DB_COL_DATE} = :date")
    fun updateMyFlag0(id: String, date: String)

    @Query("UPDATE ${PostDiaryInfo.DB_TABLE_NAME} SET ${PostDiaryInfo.DB_COL_MY} = '1' WHERE ${PostDiaryInfo.DB_COL_USERID} = :id AND ${PostDiaryInfo.DB_COL_DATE} = :date")
    fun updateMyFlag1(id: String, date: String)
}