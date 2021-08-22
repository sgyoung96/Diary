package com.example.diary1.datasave.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.diary1.datasave.dao.PostDao
import com.example.diary1.datasave.dao.UserDao
import com.example.diary1.datasave.entity.PostInfo
import com.example.diary1.datasave.entity.UserInfo

@Database(entities = arrayOf(UserInfo::class, PostInfo::class), version = 1)
abstract class MyDirayDB: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun PostDao(): PostDao

    companion object {
        private var instance: MyDirayDB? = null

        @Synchronized
        fun getInstance(context: Context): MyDirayDB? {
            if (instance == null) {
                synchronized(MyDirayDB::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MyDirayDB::class.java,
                        "my-diary-db"
                    ).allowMainThreadQueries()
                        .build()
                }
            }
            return instance
        }
    }
}