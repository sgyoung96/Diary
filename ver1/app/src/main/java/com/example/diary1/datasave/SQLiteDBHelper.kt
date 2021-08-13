package com.example.diary1.datasave

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.diary1.datasave.constants.PostDiaryInfo
import com.example.diary1.datasave.constants.RegisterInfo

class SQLiteDBHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    // DB 가 생성될 시점에 최초 1회 실행
    override fun onCreate(db: SQLiteDatabase) {
        createUserInfo(db)
        createDiaryContent(db)
    }

    // ??????????
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // 앱 재시작시, 안 탄다.
    }

    // 앱 재시작시 탄다!!
    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)

        createUserInfo(db!!)
        createDiaryContent(db)
    }

    // 테이블이 존재하지 않는 경우 생성
    fun createUserInfo(db: SQLiteDatabase) {
        val sql = "CREATE TABLE if not exists ${RegisterInfo.DB_TABLE_NAME}" +
                  "(" +
                  "${RegisterInfo.DB_COL_NAME} VARCHAR(10)" + "," +
                  "${RegisterInfo.DB_COL_ID} VARCHAR(10) PRIMARY KEY" + "," +
                  "${RegisterInfo.DB_COL_PW} VARCHAR(60)" + "," +
                  "${RegisterInfo.DB_COLE_IMAGE} BLOB" +
                  ")"

        db.execSQL(sql)
    }

    fun createDiaryContent(db: SQLiteDatabase) {
        val sql = "CREATE TABLE if not exists ${PostDiaryInfo.DB_TABLE_NAME}" +
                  "(" +
                  "${PostDiaryInfo.DB_COL_USERID} VARCHAR(10)" + "," +
                  "${PostDiaryInfo.DB_COL_DATE} VARCHAR(17)" + "," +
                  "${PostDiaryInfo.DB_COL_TITLE} VARCHAR(30)" + "," +
                  "${PostDiaryInfo.DB_COL_CONTENT} VARCHAR(1000)" + "," +
                  "${PostDiaryInfo.DB_COL_MY} VARCHAR(1) DEFAULT '${PostDiaryInfo.POST_MY_DEFAULT}'" + "," +
                  "${PostDiaryInfo.DB_COL_IMAGE} BLOB" +
                  ")"

        db.execSQL(sql)
    }
}