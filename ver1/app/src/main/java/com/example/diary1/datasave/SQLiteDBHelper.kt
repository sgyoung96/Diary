package com.example.diary1.datasave

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.diary1.constants.RegisterInfo

class SQLiteDBHelper (context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

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
        val sql: String = "CREATE TABLE if not exists ${RegisterInfo.DB_TABLE_NAME}" +
                        "(" +
                        "${RegisterInfo.DB_COL_NAME} VARCHAR(10)" + "," +
                        "${RegisterInfo.DB_COL_ID} VARCHAR(10) PRIMARY KEY" + "," +
                        "${RegisterInfo.DB_COL_PW} VARCHAR(60)" +
                        ")" + ";"

        db.execSQL(sql)
    }

    fun createDiaryContent(db: SQLiteDatabase) {
        val sql: String = "CREATE TABLE if not exists ${PostDiaryInfo.DB_TABLE_NAME}" +
                "(" +
                "${PostDiaryInfo.DB_COL_USERID} VARCHAR(10)" + "," +
                "${PostDiaryInfo.DB_COL_DATE} VARCHAR(17)" + "," +
                "${PostDiaryInfo.DB_COL_TITLE} VARCHAR(30)" + "," +
                "${PostDiaryInfo.DB_COL_CONTENT} VARCHAR(1000)" +
                ")" + ";"

        db.execSQL(sql)
    }
}

/* [사용 예시]

1. INSERT
쓰기 모드로 데이터 저장소를 가져옴
val db = dbHelper.writableDatabase

열 이름이 키인 새 값 맵을 만들기
val values = ContentValues().apply {
    put(FeedEntry.COLUMN_NAME_TITLE, title)
    put(FeedEntry.COLUMN_NAME_SUBTITLE, subtitle)
}

새 행을 삽입하고 새 행의 기본 키 값을 반환
val newRowId = db?.insert(FeedEntry.TABLE_NAME, null, values)
Cf. insert() 메서드는 오류가 발생하면 -1을 반환

2. DELETE
쿼리의 'where'부분을 정
val selection = "${FeedEntry.COLUMN_NAME_TITLE} LIKE ?"
자리 표시 자 순서로 인수를 지정
val selectionArgs = arrayOf("MyTitle")
SQL 문을 실행
val deletedRows = db.delete(FeedEntry.TABLE_NAME, selection, selectionArgs)

3. UPDATE
val db = dbHelper.writableDatabase

하나의 열에 들어갈 새 값
val title = "NewTitle"
val values = ContentValues().apply {
    put(FeedEntry.COLUMN_NAME_TITLE, title)
}

제목에 따라 업데이트 할 행
val selection = "${FeedEntry.COLUMN_NAME_TITLE} LIKE ?"
val selectionArgs = arrayOf("OldTitle")
val count = db.update(
        FeedEntry.TABLE_NAME,
        values,
        selection,
        selectionArgs)
Cf. 테이블을 업데이트하면 insert()의 ContentValues 구문과 delete()의 WHERE 구문이 결합됨

4. SELECT
val db = dbHelper.readableDatabase

val projection = arrayOf(BaseColumns._ID, FeedEntry.COLUMN_NAME_TITLE, FeedEntry.COLUMN_NAME_SUBTITLE)

title이 My Title인 것 선택
val selection = "${FeedEntry.COLUMN_NAME_TITLE} = ?"
val selectionArgs = arrayOf("My Title")

결과를 정렬할 방법
val sortOrder = "${FeedEntry.COLUMN_NAME_SUBTITLE} DESC"

val cursor = db.query(
        FeedEntry.TABLE_NAME,
        projection,
        selection,
        selectionArgs,
        null,
        null,
        sortOrder
)

*/