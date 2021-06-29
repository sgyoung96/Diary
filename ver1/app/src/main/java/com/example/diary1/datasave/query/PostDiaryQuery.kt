package com.example.diary1.datasave.query

import com.example.diary1.constants.RegisterInfo
import com.example.diary1.datasave.PostDiaryInfo

object PostDiaryQuery {
    /**
     * post 화면 진입 시, 등록한 이름 뿌려줄 쿼리
     */
    fun getNameQuery(id: String): String {
        return "SELECT USERNAME FROM ${RegisterInfo.DB_TABLE_NAME}" + " " +
               "WHERE ${RegisterInfo.DB_COL_ID} = " + "'" + id + "'" + ";"
    }

    fun insertDiary(id: String, date: String, title: String, content: String): String {
        return "INSERT INTO ${PostDiaryInfo.DB_TABLE_NAME}" +
                "(" +
                PostDiaryInfo.DB_COL_USERID + "," +
                PostDiaryInfo.DB_COL_DATE + "," +
                PostDiaryInfo.DB_COL_TITLE + "," +
                PostDiaryInfo.DB_COL_CONTENT +
                ")" +
                "VALUES" +
                "(" +
                "'" + id + "'" + "," +
                "'" + date + "'" + "," +
                "'" + title + "'" + "," +
                "'" + content + "'" +
                ")" + ";"
    }

    fun checkDiary(id: String, date: String): String {
        return "SELECT * FROM ${PostDiaryInfo.DB_TABLE_NAME}" + " " +
               "WHERE ${PostDiaryInfo.DB_COL_USERID} = " + "'" + id + "'" +
               "AND ${PostDiaryInfo.DB_COL_DATE} = " + "'" + date + "'" + ";"
    }
}