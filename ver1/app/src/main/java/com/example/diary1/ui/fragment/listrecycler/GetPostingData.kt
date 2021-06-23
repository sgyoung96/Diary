package com.example.diary1.ui.fragment.listrecycler

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.diary1.constants.SQLiteDBInfo
import com.example.diary1.constants.UserInfo
import com.example.diary1.datasave.PostDiaryInfo
import com.example.diary1.datasave.SQLiteDBHelper
import com.example.diary1.datasave.query.DiaryListQuery

/**
 * 1. DB open
 * 2. SELECT
 * 3. result 값 꺼내서 변수에 할당
 * 4. DTO 에 값 할당
 */
class GetPostingData(context: Context) {

    var data: PostedDiaryInfo? = null

    val dbHelper = SQLiteDBHelper(context, SQLiteDBInfo.DB_NAME, null, 1)
    val database: SQLiteDatabase = dbHelper.readableDatabase
    val sqlQuery: String = DiaryListQuery.getListFromId(UserInfo.userID)
    val result = database.rawQuery(sqlQuery, null)

    fun getData() {
        while (result.moveToNext()) {
            data?.postTitle = result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_TITLE))
            data?.postDate = result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_DATE))
            data?.postContent = result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_CONTENT))

            Log.d("title", ">>>>>>>>>>${result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_TITLE))}")
            Log.d("date", ">>>>>>>>>>${result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_DATE))}")
            Log.d("content", ">>>>>>>>>>${result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_CONTENT))}")
        }

        // 왜지..? 당황스럽네
        Log.d("checkTitle", ">>>>>>>>>>${data?.postTitle}") // null
        Log.d("checkDate", ">>>>>>>>>>${data?.postDate}") // null
        Log.d("checkContent", ">>>>>>>>>>${data?.postContent}") // null
    }
}