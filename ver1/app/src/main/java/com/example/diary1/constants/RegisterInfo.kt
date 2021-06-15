package com.example.diary1.constants

import android.provider.BaseColumns

class RegisterInfo {
    companion object UserInfo: BaseColumns {
        const val DB_TABLE_NAME = "USERINFO"
        const val DB_COL_NAME = "USERNAME"
        const val DB_COL_ID = "USERID"
        const val DB_COL_PW = "USERPW"
    }
}