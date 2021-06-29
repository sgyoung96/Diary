package com.example.diary1.datasave.query

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.diary1.constants.RegisterInfo
import com.example.diary1.constants.SQLiteDBInfo
import com.example.diary1.constants.UserInfo
import com.example.diary1.datasave.SQLiteDBHelper
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
            // 여기서 터지는 이유를 찾아보자!!! -> ture 일 경우는 괜찮음. 근데 비밀번호 불일치시 터짐
            // -> 처음부터 해시로 저장했으면 ㄱㅊ. 그렇지 않았던 경우에 문제가 발생함
            // manifest 에서 android:allowBackup="false" 설정 바꿔주고, 핸드폰에서 설정 > 앱 > 데이터 삭제(데이터베이스까지 삭제된다는 알림 뜸) 로 해결
            // -> 앱 삭제 후 재설치시 기존 정보 다 날아감
            val comparePW = BCrypt.checkpw(pw, result.getString(result.getColumnIndex(RegisterInfo.DB_COL_PW)))
            if (comparePW) {
                UserInfo.userID = result.getString(result.getColumnIndex(RegisterInfo.DB_COL_ID))
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
}