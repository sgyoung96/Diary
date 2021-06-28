package com.example.diary1.datasave.query

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.diary1.constants.RegisterInfo
import com.example.diary1.constants.SQLiteDBInfo
import com.example.diary1.constants.UserInfo
import com.example.diary1.datasave.SQLiteDBHelper
import kotlinx.android.synthetic.main.activity_login.*
import org.mindrot.jbcrypt.BCrypt

object LoginQuery {

    /**
     * 비밀번호가 일치하면 true
     * 비밀번호가 불일치면 false
     */
    fun checkPW(context: Context, id: String, pw: String): Boolean {
        val dbHelper = SQLiteDBHelper(context, SQLiteDBInfo.DB_NAME, null, 1)
        val database: SQLiteDatabase = dbHelper.readableDatabase
        val checkPWQuery: String = "SELECT * FROM ${RegisterInfo.DB_TABLE_NAME}" + " " +
                                   "WHERE ${RegisterInfo.DB_COL_ID} = '$id'" + ";"
        val result: Cursor = database.rawQuery(checkPWQuery, null)

        return if (result.moveToNext()) {
            // 생성된 해쉬를 원래 비밀번호로 검증하는 방법. 맞을 경우 true 반환. 주로 로그인 로직에서 사용
            val comparePW = BCrypt.checkpw(pw, result.getString(result.getColumnIndex(RegisterInfo.DB_COL_PW)))
            if (comparePW) {
                UserInfo.userID = result.getString(result.getColumnIndex(RegisterInfo.DB_COL_ID))
                true
            } else {
                false
            }
        } else {
            false
        }
    }
}