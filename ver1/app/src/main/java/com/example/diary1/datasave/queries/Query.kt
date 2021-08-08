package com.example.diary1.datasave.queries

import com.example.diary1.constants.Constants
import com.example.diary1.datasave.constants.PostDiaryInfo
import com.example.diary1.datasave.constants.RegisterInfo

object Query {
    /**
     * 회원가입 쿼리
     */
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

    /**
     * 회원가입 이력이 있는지 확인
     */
    fun checkOneRegister(id: String): String {
        return "SELECT * FROM ${RegisterInfo.DB_TABLE_NAME}" + " " +
                "WHERE ${RegisterInfo.DB_COL_ID} = '$id'" + ";"
    }

    /**
     * 가입한 모든 회원 목록 조회 - 코드에서 가입한 목록 확인할 때만 사용하는 쿼리
     */
    fun checkAllRegister(): String {
        return "SELECT * FROM ${RegisterInfo.DB_TABLE_NAME}" + ";"
    }

    /**
     * post 화면 진입 시, 등록한 이름과 프로필 사진 뿌려줄 쿼리
     */
    fun getDefaultQuery(): String {
        return "SELECT ${RegisterInfo.DB_COL_NAME}, ${RegisterInfo.DB_COLE_IMAGE} FROM ${RegisterInfo.DB_TABLE_NAME}" + " " +
                "WHERE ${RegisterInfo.DB_COL_ID} = " + "'" + Constants.userID + "'" + ";"
    }

    /**
     * 다이어리 추가
     */
    fun insertDiary(date: String, title: String, content: String, image: String): String {
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
                "'" + Constants.userID + "'" + "," +
                "'" + date + "'" + "," +
                "'" + title + "'" + "," +
                "'" + content + "'" + "," +
                image +
                ")" + ";"
    }

    /**
     * 해당 날짜에 등록된 목록이 있는지 체크
     */
    fun checkDiary(date: String): String {
        return "SELECT * FROM ${PostDiaryInfo.DB_TABLE_NAME}" + " " +
                "WHERE ${PostDiaryInfo.DB_COL_USERID} = " + "'" + Constants.userID + "'" +
                "AND ${PostDiaryInfo.DB_COL_DATE} = " + "'" + date + "'" + ";"
    }

    /**
     * 다이어리 목록 뿌려주기 위한 쿼리
     */
    fun getListFromId(): String {
        return "SELECT B.*" + " " +
                "FROM ${RegisterInfo.DB_TABLE_NAME} A, ${PostDiaryInfo.DB_TABLE_NAME} B" + " " +
                "WHERE A.${RegisterInfo.DB_COL_ID} = B.${PostDiaryInfo.DB_COL_USERID}" + " " +
                "AND A.${RegisterInfo.DB_COL_ID} = " + "'" + Constants.userID + "'" + " " +
                "ORDER BY B.${PostDiaryInfo.DB_COL_DATE} DESC" + ";"
    }

    /**
     * 관심목록 추가한 목록 뿌려주기
     */
    fun myDiaryQuery(): String {
        return "SELECT *" + " " +
                "FROM ${PostDiaryInfo.DB_TABLE_NAME}" + " " +
                "WHERE ${PostDiaryInfo.DB_COL_USERID} = '${Constants.userID}'" + " " +
                "AND ${PostDiaryInfo.DB_COL_MY} = '1'" + " " +
                "ORDER BY ${PostDiaryInfo.DB_COL_DATE} DESC" + ";"
    }

    /**
     * DtailActivity - 수정
     */
    fun saveDiary (title: String, date: String, content: String, originalDate: String, image: String): String {
        return "UPDATE ${PostDiaryInfo.DB_TABLE_NAME}" + " " +
                "SET" + " " +
                PostDiaryInfo.DB_COL_TITLE + " = " + "'" + title + "'" + "," + " " +
                PostDiaryInfo.DB_COL_DATE + " = " + "'" + date + "'" + "," + " " +
                PostDiaryInfo.DB_COL_CONTENT + " = " + "'" + content + "'" + ", " + " " +
                PostDiaryInfo.DB_COL_IMAGE + " = " + image + " " +
                "WHERE ${PostDiaryInfo.DB_COL_USERID} = " + "'" + Constants.userID + "'" +
                "  AND ${PostDiaryInfo.DB_COL_DATE} = " + "'" + originalDate + "'" + ";"
    }

    /**
     * DetailActivity - 삭제
     */
    fun deleteQuery(title: String, date: String): String {
        return "DELETE FROM ${PostDiaryInfo.DB_TABLE_NAME}" + " " +
                "WHERE ${PostDiaryInfo.DB_COL_USERID} = '${Constants.userID}'" + " " +
                "  AND ${PostDiaryInfo.DB_COL_TITLE} = '$title'" + " " +
                "  AND ${PostDiaryInfo.DB_COL_DATE} = '$date'" + ";"
    }

    /**
     * DetailActivity - 해당 id 의, 해당 일자의 일기 목록에 저장된 이미지 뿌리기 (기타 정보는 recyclerview item 으로부터 가져옴. 이미지는 넘겨주는 게 안 되서 따로 조회함)
     */
    fun getDiaryImage(date: String): String{
        return "SELECT ${PostDiaryInfo.DB_COL_IMAGE} FROM ${PostDiaryInfo.DB_TABLE_NAME} " +
               "WHERE ${PostDiaryInfo.DB_COL_USERID} = '${Constants.userID}' " +
               "AND ${PostDiaryInfo.DB_COL_DATE} = '$date'" + ";"
    }

    /**
     * [설정]
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
     * [설정]
     * 1. USERINFO 에서 삭제
     * 2. POSTINFO 에서 삭제
     */
    fun deleteAllQuery(): String {
        return "DELETE FROM ${RegisterInfo.DB_TABLE_NAME} WHERE ${RegisterInfo.DB_COL_ID} = '${Constants.userID}'" + ";" + " " +
                "DELETE FROM ${PostDiaryInfo.DB_TABLE_NAME} WHERE ${PostDiaryInfo.DB_COL_USERID} = '${Constants.userID}'" + ";"
    }
}