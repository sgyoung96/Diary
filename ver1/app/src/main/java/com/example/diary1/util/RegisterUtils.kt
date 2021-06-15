package com.example.diary1.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.diary1.constants.SQLiteDBInfo
import com.example.diary1.datasave.SQLiteDBHelper
import com.example.diary1.datasave.query.RegisterQuery
import kotlinx.android.synthetic.main.activity_register.*

class RegisterUtils {
    companion object {
        // true : 회원가입 되어 있음 false : 회원정보 없음
        fun checkMember(context: Context, id: String): Boolean {
            val dbHelper = SQLiteDBHelper(context, SQLiteDBInfo.DB_NAME, null, 1)
            val readDatabase: SQLiteDatabase = dbHelper.readableDatabase
            val checkRegisterQuery = RegisterQuery.checkOneRegister(id)
            val result = readDatabase.rawQuery(checkRegisterQuery, null)
            while (result.moveToNext()) {
                return true
                break
            }
            return false
        }
    }
}