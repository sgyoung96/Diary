package com.example.diary1.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.diary1.datasave.SQLiteDBHelper
import com.example.diary1.datasave.constants.SQLiteDBInfo
import com.example.diary1.datasave.queries.Query
import java.util.regex.Pattern

object Utils {
    // true : 회원가입 되어 있음 false : 회원정보 없음
    fun checkMember(context: Context, id: String): Boolean {
        val dbHelper = SQLiteDBHelper(context, SQLiteDBInfo.DB_NAME, null, 1)
        val readDatabase: SQLiteDatabase = dbHelper.readableDatabase
        val checkRegisterQuery = Query.checkOneRegister(id)
        val result = readDatabase.rawQuery(checkRegisterQuery, null)
        return result.moveToNext()
    }

    /**
     * 정규식 체크
     */
    fun checkName(text: String): Boolean {
        return Pattern.matches("^[가-힣]*$", text)
    }

    // 아이디와 비밀번호가 영소문자 + 숫자로 이루어져 있는지 체크
    // checkLetter, checkNumber
    fun checkLetter(text: String): Boolean {
        return Pattern.matches("^[a-z]*$", text)
    }

    fun checkNumber(text: String): Boolean {
        return Pattern.matches("^[0-9]*$", text)
    }
}