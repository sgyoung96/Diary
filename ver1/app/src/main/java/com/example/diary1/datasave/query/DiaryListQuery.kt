package com.example.diary1.datasave.query

import com.example.diary1.constants.PostDiaryInfo
import com.example.diary1.constants.RegisterInfo

class DiaryListQuery {
    companion object {
        fun getListFromId(id: String): String {
            return "SELECT B.${PostDiaryInfo.DB_COL_DATE}, B.${PostDiaryInfo.DB_COL_TITLE}, B.${PostDiaryInfo.DB_COL_CONTENT}" + " " +
                    "FROM ${RegisterInfo.DB_TABLE_NAME} A, ${PostDiaryInfo.DB_TABLE_NAME} B" + " " +
                    "WHERE A.${RegisterInfo.DB_COL_ID} = B.${PostDiaryInfo.DB_COL_USERID}" + " " +
                    "AND A.${RegisterInfo.DB_COL_ID} = " + "'" + id + "'" + ";"
        }
    }
}