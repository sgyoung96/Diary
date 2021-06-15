package com.example.diary1.datasave.query

import com.example.diary1.constants.RegisterInfo
import kotlinx.android.synthetic.main.activity_register.*

class RegisterQuery {
    companion object {
        fun register(name: String, id: String, pw: String): String {
            var query: String = "INSERT INTO ${RegisterInfo.DB_TABLE_NAME}" +
                    "(" +
                    "${RegisterInfo.DB_COL_NAME}" + "," +
                    "${RegisterInfo.DB_COL_ID}" + "," +
                    "${RegisterInfo.DB_COL_PW}" +
                    ")" +
                    "VALUES" +
                    "(" +
                    "'" + "$name" + "'" + "," +
                    "'" + "$id" + "'" + "," +
                    "'" + "$pw" + "'" +
                    ")" + ";"
            return query
        }

        fun checkOneRegister(id: String): String {
            var query: String = "SELECT * FROM ${RegisterInfo.DB_TABLE_NAME}" + " " +
                    "WHERE ${RegisterInfo.DB_COL_ID} = '$id'" + ";"
            return query
        }

        fun checkAllRegister(): String {
            var query: String = "SELECT * FROM ${RegisterInfo.DB_TABLE_NAME}" + ";"
            return query
        }
    }
}