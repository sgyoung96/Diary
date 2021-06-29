package com.example.diary1.datasave.query

import com.example.diary1.datasave.PostDiaryInfo

object DetailDiaryQuery {
    fun saveDiary (id: String, title: String, date: String, content: String, originalDate: String): String {
        return "UPDATE ${PostDiaryInfo.DB_TABLE_NAME}" + " " +
               "SET" + " " +
               PostDiaryInfo.DB_COL_TITLE + " = " + "'" + title + "'" + "," + " " +
               PostDiaryInfo.DB_COL_DATE + " = " + "'" + date + "'" + "," + " " +
               PostDiaryInfo.DB_COL_CONTENT + " = " + "'" + content + "'" + " " +
               "WHERE ${PostDiaryInfo.DB_COL_USERID} = " + "'" + id + "'" +
               "  AND ${PostDiaryInfo.DB_COL_DATE} = " + "'" + originalDate + "'" + ";"
    }

    fun deleteQuery(id: String, title: String, date: String): String {
        return "DELETE FROM ${PostDiaryInfo.DB_TABLE_NAME}" + " " +
                "WHERE ${PostDiaryInfo.DB_COL_USERID} = '$id'" + " " +
                "  AND ${PostDiaryInfo.DB_COL_TITLE} = '$title'" + " " +
                "  AND ${PostDiaryInfo.DB_COL_DATE} = '$date'" + ";"
    }
}