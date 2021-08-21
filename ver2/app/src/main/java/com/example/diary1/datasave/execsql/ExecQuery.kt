package com.example.diary1.datasave.execsql

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.diary1.constants.Constants
import com.example.diary1.datasave.SQLiteDBHelper
import com.example.diary1.datasave.constants.PostDiaryInfo
import com.example.diary1.datasave.constants.RegisterInfo
import com.example.diary1.datasave.constants.SQLiteDBInfo
import org.mindrot.jbcrypt.BCrypt

object ExecQuery {
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
            // 여기서 터지는 이유를 찾아보자!!! -> ture 일 경우는 괜찮음. 근데 비밀번호 불일치시 터짐
            // -> 처음부터 해시로 저장했으면 ㄱㅊ. 그렇지 않았던 경우에 문제가 발생함
            // manifest 에서 android:allowBackup="false" 설정 바꿔주고, 핸드폰에서 설정 > 앱 > 데이터 삭제(데이터베이스까지 삭제된다는 알림 뜸) 로 해결
            // -> 앱 삭제 후 재설치시 기존 정보 다 날아감
            val comparePW = BCrypt.checkpw(pw, result.getString(result.getColumnIndex(RegisterInfo.DB_COL_PW)))
            if (comparePW) {
                Constants.userID = result.getString(result.getColumnIndex(RegisterInfo.DB_COL_ID))
                database.close()
                true
            } else {
                database.close()
                false
            }
        } else {
            database.close()
            false
        }
    }

    fun setMyFlag(context: Context, date: String) {
        val dbHelper = SQLiteDBHelper(context, SQLiteDBInfo.DB_NAME, null, 1)
        var database: SQLiteDatabase = dbHelper.readableDatabase
        var query = "SELECT ${PostDiaryInfo.DB_COL_MY}" + " " +
                "FROM ${PostDiaryInfo.DB_TABLE_NAME}" + " " +
                "WHERE ${PostDiaryInfo.DB_COL_USERID} = '${Constants.userID}'" + " " +
                "AND ${PostDiaryInfo.DB_COL_DATE} = '$date'" + ";"
        val result: Cursor = database.rawQuery(query, null)
        var myFlag: String = ""
        if (result.moveToNext()) {
            myFlag = result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_MY))
        }

        database = dbHelper.writableDatabase
        if (myFlag == "0") {
            query = "UPDATE ${PostDiaryInfo.DB_TABLE_NAME}" + " " +
                    "SET " +
                    "${PostDiaryInfo.DB_COL_MY} = '1'" + " " +
                    "WHERE ${PostDiaryInfo.DB_COL_USERID} = '${Constants.userID}'" + " " +
                    "AND ${PostDiaryInfo.DB_COL_DATE} = '$date'" + ";"
        } else {
            query = "UPDATE ${PostDiaryInfo.DB_TABLE_NAME}" + " " +
                    "SET " +
                    "${PostDiaryInfo.DB_COL_MY} = '0'" + " " +
                    "WHERE ${PostDiaryInfo.DB_COL_USERID} = '${Constants.userID}'" + " " +
                    "AND ${PostDiaryInfo.DB_COL_DATE} = '$date'" + ";"
        }

        Log.d("myFlagWhenSelect", ">>>>>>>>>>$myFlag")

        database.execSQL(query)
        database.close()
    }
}