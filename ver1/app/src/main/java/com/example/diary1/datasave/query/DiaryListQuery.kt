package com.example.diary1.datasave.query

import com.example.diary1.constants.PostDiaryInfo
import com.example.diary1.constants.RegisterInfo

object DiaryListQuery {
    fun getListFromId(id: String): String {
        return "SELECT B.*" + " " +
                "FROM ${RegisterInfo.DB_TABLE_NAME} A, ${PostDiaryInfo.DB_TABLE_NAME} B" + " " +
                "WHERE A.${RegisterInfo.DB_COL_ID} = B.${PostDiaryInfo.DB_COL_USERID}" + " " +
                "AND A.${RegisterInfo.DB_COL_ID} = " + "'" + id + "'" + " " +
                "ORDER BY B.${PostDiaryInfo.DB_COL_DATE} DESC" + ";"
    }
}