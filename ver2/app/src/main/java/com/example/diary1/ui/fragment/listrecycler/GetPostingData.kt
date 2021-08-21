package com.example.diary1.ui.fragment.listrecycler

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.diary1.datasave.constants.SQLiteDBInfo
import com.example.diary1.constants.Constants
import com.example.diary1.datasave.constants.PostDiaryInfo
import com.example.diary1.datasave.SQLiteDBHelper
import com.example.diary1.datasave.queries.Query

/**
 * 1. DB open
 * 2. SELECT
 * 3. result 값 꺼내서 변수에 할당
 * 4. DTO 에 값 할당
 */
class GetPostingData(context: Context) {

    var data: MutableList<PostedDiaryInfo> = mutableListOf()
    val diaryListPresenter = DiaryListPresenter()

    val dbHelper = SQLiteDBHelper(context, SQLiteDBInfo.DB_NAME, null, 1)
    val database: SQLiteDatabase = dbHelper.readableDatabase
    val sqlQuery: String = Query.getListFromId()
    val result = database.rawQuery(sqlQuery, null)

    fun getData(view: DiaryListContract.View) {
        while (result.moveToNext()) {
            data.add(
                PostedDiaryInfo(result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_DATE)),
                                result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_TITLE)),
                                result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_CONTENT)),
                                result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_MY))))

            Log.d("checkData1", ">>>>>>>>>>$data")
            Log.d("title", ">>>>>>>>>>${result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_TITLE))}")
            Log.d("date", ">>>>>>>>>>${result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_DATE))}")
            Log.d("content", ">>>>>>>>>>${result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_CONTENT))}")
            Log.d("my", ">>>>>>>>>>${result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_MY))}")
        }
        Log.d("checkData2", ">>>>>>>>>>$data")

        diaryListPresenter.getData(data, view)
    }
}