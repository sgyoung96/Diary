package com.example.diary1.datasave.queries

import com.example.diary1.constants.Constants
import com.example.diary1.datasave.constants.PostDiaryInfo
import com.example.diary1.datasave.constants.RegisterInfo

object Query {
    fun register(name: String, id: String, pw: String, image: String): String {
        return "INSERT INTO ${RegisterInfo.DB_TABLE_NAME}" +
                "(" +
                RegisterInfo.DB_COL_NAME + "," +
                RegisterInfo.DB_COL_ID + "," +
                RegisterInfo.DB_COL_PW + "," +
                RegisterInfo.DB_COLE_IMAGE +
                ")" +
                "VALUES" +
                "(" +
                "'" + name + "'" + "," +
                "'" + id + "'" + "," +
                "'" + pw + "'" + "," +
                "$image" +
                ")" + ";"
    }

    fun checkOneRegister(id: String): String {
        return "SELECT * FROM ${RegisterInfo.DB_TABLE_NAME}" + " " +
                "WHERE ${RegisterInfo.DB_COL_ID} = '$id'" + ";"
    }

    fun checkAllRegister(): String {
        return "SELECT * FROM ${RegisterInfo.DB_TABLE_NAME}" + ";"
    }

    /**
     * post 화면 진입 시, 등록한 이름 뿌려줄 쿼리
     */
    fun getDefaultQuery(id: String): String {
        return "SELECT ${RegisterInfo.DB_COL_NAME}, ${RegisterInfo.DB_COLE_IMAGE} FROM ${RegisterInfo.DB_TABLE_NAME}" + " " +
                "WHERE ${RegisterInfo.DB_COL_ID} = " + "'" + id + "'" + ";"
    }

    fun insertDiary(id: String, date: String, title: String, content: String, image: String): String {
        return "INSERT INTO ${PostDiaryInfo.DB_TABLE_NAME}" +
                "(" +
                PostDiaryInfo.DB_COL_USERID + "," +
                PostDiaryInfo.DB_COL_DATE + "," +
                PostDiaryInfo.DB_COL_TITLE + "," +
                PostDiaryInfo.DB_COL_CONTENT + "," +
                PostDiaryInfo.DB_COL_IMAGE +
                ")" +
                "VALUES" +
                "(" +
                "'" + id + "'" + "," +
                "'" + date + "'" + "," +
                "'" + title + "'" + "," +
                "'" + content + "'" + "," +
                image +
                ")" + ";"
    }

    fun checkDiary(id: String, date: String): String {
        return "SELECT * FROM ${PostDiaryInfo.DB_TABLE_NAME}" + " " +
                "WHERE ${PostDiaryInfo.DB_COL_USERID} = " + "'" + id + "'" +
                "AND ${PostDiaryInfo.DB_COL_DATE} = " + "'" + date + "'" + ";"
    }

    fun getListFromId(id: String): String {
        return "SELECT B.*" + " " +
                "FROM ${RegisterInfo.DB_TABLE_NAME} A, ${PostDiaryInfo.DB_TABLE_NAME} B" + " " +
                "WHERE A.${RegisterInfo.DB_COL_ID} = B.${PostDiaryInfo.DB_COL_USERID}" + " " +
                "AND A.${RegisterInfo.DB_COL_ID} = " + "'" + id + "'" + " " +
                "ORDER BY B.${PostDiaryInfo.DB_COL_DATE} DESC" + ";"
    }

    fun myDiaryQuery(): String {
        return "SELECT *" + " " +
                "FROM ${PostDiaryInfo.DB_TABLE_NAME}" + " " +
                "WHERE ${PostDiaryInfo.DB_COL_USERID} = '${Constants.userID}'" + " " +
                "AND ${PostDiaryInfo.DB_COL_MY} = '1'" + " " +
                "ORDER BY ${PostDiaryInfo.DB_COL_DATE} DESC" + ";"
    }

    fun saveDiary (id: String, title: String, date: String, content: String, originalDate: String, image: String): String {
        return "UPDATE ${PostDiaryInfo.DB_TABLE_NAME}" + " " +
                "SET" + " " +
                PostDiaryInfo.DB_COL_TITLE + " = " + "'" + title + "'" + "," + " " +
                PostDiaryInfo.DB_COL_DATE + " = " + "'" + date + "'" + "," + " " +
                PostDiaryInfo.DB_COL_CONTENT + " = " + "'" + content + "'" + " " +
                PostDiaryInfo.DB_COL_IMAGE + " = " + image +
                "WHERE ${PostDiaryInfo.DB_COL_USERID} = " + "'" + id + "'" +
                "  AND ${PostDiaryInfo.DB_COL_DATE} = " + "'" + originalDate + "'" + ";"
    }

    fun deleteQuery(id: String, title: String, date: String): String {
        return "DELETE FROM ${PostDiaryInfo.DB_TABLE_NAME}" + " " +
                "WHERE ${PostDiaryInfo.DB_COL_USERID} = '$id'" + " " +
                "  AND ${PostDiaryInfo.DB_COL_TITLE} = '$title'" + " " +
                "  AND ${PostDiaryInfo.DB_COL_DATE} = '$date'" + ";"
    }

    /**
     * 저장할 것 : 이름, 암호화된 비밀번호, 프로필사진 (USERINFO)
     */
    fun saveSettingQuery(name: String, pw: String, image: String): String {
        return "UPDATE ${RegisterInfo.DB_TABLE_NAME}" + " " +
                "SET" + " " +
                "${RegisterInfo.DB_COL_NAME} = '$name'" + ", " +
                "${RegisterInfo.DB_COL_PW} = '$pw'" + ", " +
                "${RegisterInfo.DB_COLE_IMAGE} = $image" + " " +
                "WHERE ${RegisterInfo.DB_COL_ID} = '${Constants.userID}'" + ";"
    }

    /**
     * 1. USERINFO 에서 삭제
     * 2. POSTINFO 에서 삭제
     */
    fun deleteAllQuery(): String {
        return "DELETE FROM ${RegisterInfo.DB_TABLE_NAME} WHERE ${RegisterInfo.DB_COL_ID} = '${Constants.userID}'" + ";" + " " +
                "DELETE FROM ${PostDiaryInfo.DB_TABLE_NAME} WHERE ${PostDiaryInfo.DB_COL_USERID} = '${Constants.userID}'" + ";"
    }
}