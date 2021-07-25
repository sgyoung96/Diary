package com.example.diary1.datasave.query

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.diary1.constants.PostDiaryInfo
import com.example.diary1.constants.SQLiteDBInfo
import com.example.diary1.constants.UserInfo
import com.example.diary1.datasave.SQLiteDBHelper

object MyDiaryQuery {
    fun setMyFlag(context: Context, date: String) {
        val dbHelper = SQLiteDBHelper(context, SQLiteDBInfo.DB_NAME, null, 1)
        var database: SQLiteDatabase = dbHelper.readableDatabase
        var query = "SELECT ${PostDiaryInfo.DB_COL_MY}" + " " +
                    "FROM ${PostDiaryInfo.DB_TABLE_NAME}" + " " +
                    "WHERE ${PostDiaryInfo.DB_COL_USERID} = '${UserInfo.userID}'" + " " +
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
                    "WHERE ${PostDiaryInfo.DB_COL_USERID} = '${UserInfo.userID}'" + " " +
                    "AND ${PostDiaryInfo.DB_COL_DATE} = '$date'" + ";"
        } else {
            query = "UPDATE ${PostDiaryInfo.DB_TABLE_NAME}" + " " +
                    "SET " +
                    "${PostDiaryInfo.DB_COL_MY} = '0'" + " " +
                    "WHERE ${PostDiaryInfo.DB_COL_USERID} = '${UserInfo.userID}'" + " " +
                    "AND ${PostDiaryInfo.DB_COL_DATE} = '$date'" + ";"
        }

        Log.d("myFlagWhenSelect", ">>>>>>>>>>$myFlag")

        database.execSQL(query)
        database.close()
    }

    fun myDiaryQuery(): String {
        return "SELECT *" + " " +
               "FROM ${PostDiaryInfo.DB_TABLE_NAME}" + " " +
               "WHERE ${PostDiaryInfo.DB_COL_USERID} = '${UserInfo.userID}'" + " " +
               "AND ${PostDiaryInfo.DB_COL_MY} = '1'" + " " +
               "ORDER BY ${PostDiaryInfo.DB_COL_DATE} DESC" + ";"
    }
}