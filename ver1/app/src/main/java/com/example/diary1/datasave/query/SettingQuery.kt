package com.example.diary1.datasave.query

import com.example.diary1.constants.RegisterInfo
import com.example.diary1.constants.UserInfo
import com.example.diary1.datasave.PostDiaryInfo

object SettingQuery {
    /**
     * 저장할 것 : 이름, 암호화된 비밀번호, 프로필사진 (USERINFO)
     */
    fun saveSettingQuery(name: String, pw: String): String {
        return "UPDATE ${RegisterInfo.DB_TABLE_NAME}" + " " +
               "SET" + " " +
               "${RegisterInfo.DB_COL_NAME} = '$name'" + ", " +
               "${RegisterInfo.DB_COL_PW} = '$pw'" + " " +
               "WHERE ${RegisterInfo.DB_COL_ID} = '${UserInfo.userID}'" + ";"
    }

    /**
     * 1. USERINFO 에서 삭제
     * 2. POSTINFO 에서 삭제
     */
    fun deleteAllQuery(): String {
        return "DELETE FROM ${RegisterInfo.DB_TABLE_NAME} WHERE ${RegisterInfo.DB_COL_ID} = '${UserInfo.userID}'" + ";" + " " +
               "DELETE FROM ${PostDiaryInfo.DB_TABLE_NAME} WHERE ${PostDiaryInfo.DB_COL_USERID} = '${UserInfo.userID}'" + ";"
    }
}