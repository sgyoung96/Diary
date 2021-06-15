package com.example.diary1.datasave

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager {

    companion object {
        const val PREFERENCES_NAME = "rebuild_preference"
        // 이건 왜 const 가 못 붙을까 (Const 'val' has type 'Set<String>'. Only primitives and String are allowed)
        val DEFAULT_VALUE_STRINGSET: Set<String> = setOf<String>()
        const val DEFAULT_VALUE_STRING = ""
        const val DEFAULT_VALUE_BOOLEAN = false
        const val DEFAULT_VALUE_INT = -1
        const val DEFAULT_VALUE_LONG = -1L
        const val DEFAULT_VALUE_FLOAT = -1F

        fun getPreference(context: Context): SharedPreferences {
            return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        }

        // 타입별 값 저장 : setStringSet, setString, setBoolean, setInt, setLong, setFloat

        /**
         * Set<String> 값 저장
         * @param context
         * @param key
         * @param value
         */
        fun setStringSet(context: Context, key: String, value: Set<String>) {
            var sharedPreferences: SharedPreferences = getPreference(context)
            var editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putStringSet(key, value)
            editor.commit()
        }

        /**
         * String 값 저장
         * @param context
         * @param key
         * @param value
         */
        fun setString(context: Context, key: String, value: String) {
            var sharedPreferences: SharedPreferences = getPreference(context)
            var editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString(key, value)
            editor.commit()
        }

        /**
         *  Boolean 값 저장
         *  @param context
         *  @param key
         *  @param value
         */
        fun setBoolean(context: Context, key: String, value: Boolean) {
            var sharedPreferences: SharedPreferences = getPreference(context)
            var editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean(key, value)
            editor.commit()
        }

        /**
         *  Int 값 저장
         *  @param context
         *  @param key
         *  @param value
         */
        fun setInt(context: Context, key: String, value: Int) {
            var sharedPreferences: SharedPreferences = getPreference(context)
            var editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putInt(key, value)
            editor.commit()
        }

        /**
         *  Long 값 저장
         *  @param context
         *  @param key
         *  @param value
         */
        fun setLong(context: Context, key: String, value: Long) {
            var sharedPreferences: SharedPreferences = getPreference(context)
            var editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putLong(key, value)
            editor.commit()
        }

        /**
         *  Float 값 저장
         *  @param context
         *  @param key
         *  @param value
         */
        fun setFloat(context: Context, key: String, value: Float) {
            var sharedPreferences: SharedPreferences = getPreference(context)
            var editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putFloat(key, value)
            editor.commit()
        }

        // 타입별 값 로드 : getStringSet, getString, getBoolean, getInt, getLong, getFloat

        /**
         * Set<String> 값 로드
         * @param context
         * @param key
         * @return
         */
        fun getStringSet(context: Context, key: String): Set<String> {
            var sharedPreferences: SharedPreferences = getPreference(context)
            var value: Set<String>? = sharedPreferences.getStringSet(key, DEFAULT_VALUE_STRINGSET)
            return value!!
        }

        /**
         * String 값 로드
         * @param context
         * @param key
         * @return
         */
        fun getString(context: Context, key: String): String {
            var sharedPreferences: SharedPreferences = getPreference(context)
            var value: String? = sharedPreferences.getString(key, DEFAULT_VALUE_STRING)
            return value!!
        }

        /**
         * Boolean 값 로드
         * @param context
         * @param key
         * @return
         */
        fun getBoolean(context: Context, key: String): Boolean {
            var sharedPreferences: SharedPreferences = getPreference(context)
            var value: Boolean = sharedPreferences.getBoolean(key, DEFAULT_VALUE_BOOLEAN)
            return value
        }

        /**
         * Int 값 로드
         * @param context
         * @param key
         * @return
         */
        fun getInt(context: Context, key: String): Int {
            var sharedPreferences: SharedPreferences = getPreference(context)
            var value: Int = sharedPreferences.getInt(key, DEFAULT_VALUE_INT)
            return value
        }

        /**
         * Long 값 로드
         * @param context
         * @param key
         * @return
         */
        fun getLong(context: Context, key: String): Long {
            var sharedPreferences: SharedPreferences = getPreference(context)
            var value: Long = sharedPreferences.getLong(key, DEFAULT_VALUE_LONG)
            return value
        }

        /**
         * Float 값 로드
         * @param context
         * @param key
         * @return
         */
        fun getFloat(context: Context, key: String): Float {
            var sharedPreferences: SharedPreferences = getPreference(context)
            var value: Float = sharedPreferences.getFloat(key, DEFAULT_VALUE_FLOAT)
            return value
        }

        // 삭제 기능 : removeKey, clear

        /**
         * 키 값 삭제
         * @param context
         * @param key
         */
        fun removeKey(context: Context, key: String) {
            var sharedPreferences: SharedPreferences = getPreference(context)
            var edit: SharedPreferences.Editor = sharedPreferences.edit()
            edit.remove(key)
            edit.commit()
        }

        /**
         * 모든 저장 데이터 삭제
         * @param context
         */
        fun clear(context: Context) {
            var sharedPreferences: SharedPreferences = getPreference(context)
            var edit: SharedPreferences.Editor = sharedPreferences.edit()
            edit.clear()
            edit.commit()
        }
    }
}