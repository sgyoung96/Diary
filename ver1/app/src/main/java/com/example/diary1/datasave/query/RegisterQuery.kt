package com.example.diary1.datasave.query

import com.example.diary1.constants.RegisterInfo

object RegisterQuery {
    fun register(name: String, id: String, pw: String): String {
        return "INSERT INTO ${RegisterInfo.DB_TABLE_NAME}" +
                "(" +
                RegisterInfo.DB_COL_NAME + "," +
                RegisterInfo.DB_COL_ID + "," +
                RegisterInfo.DB_COL_PW +
                ")" +
                "VALUES" +
                "(" +
                "'" + name + "'" + "," +
                "'" + id + "'" + "," +
                "'" + pw + "'" +
                ")" + ";"
    }

    fun checkOneRegister(id: String): String {
        return "SELECT * FROM ${RegisterInfo.DB_TABLE_NAME}" + " " +
                "WHERE ${RegisterInfo.DB_COL_ID} = '$id'" + ";"
    }

    fun checkAllRegister(): String {
        return "SELECT * FROM ${RegisterInfo.DB_TABLE_NAME}" + ";"
    }
}