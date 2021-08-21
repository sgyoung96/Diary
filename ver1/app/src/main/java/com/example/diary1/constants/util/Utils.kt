package com.example.diary1.constants.util

import android.Manifest
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.diary1.R
import com.example.diary1.datasave.SQLiteDBHelper
import com.example.diary1.datasave.constants.SQLiteDBInfo
import com.example.diary1.datasave.queries.Query
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_post_diary.*
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern

object Utils {
    /**
     * 카메라와 앨범으로부터 이미지 가져오는 기능에 필요한 변수들
     */
    val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    val STORAGE_PERMISSION = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    const val PERMISSION_CAMERA = 1
    const val PERMISSION_STORAGE = 2
    // const val REQUEST_CAMERA = 3
    const val REQUEST_STORAGE = 4
    const val REQUEST_CAMERA = 1

    // true : 회원가입 되어 있음 false : 회원정보 없음
    fun checkMember(context: Context, id: String): Boolean {
        val dbHelper = SQLiteDBHelper(context, SQLiteDBInfo.DB_NAME, null, 1)
        val readDatabase: SQLiteDatabase = dbHelper.readableDatabase
        val checkRegisterQuery = Query.checkOneRegister(id)
        val result = readDatabase.rawQuery(checkRegisterQuery, null)
        return result.moveToNext()
    }

    /**
     * 정규식 체크
     */
    fun checkName(text: String): Boolean {
        return Pattern.matches("^[가-힣]*$", text)
    }

    // 아이디와 비밀번호가 영소문자 + 숫자로 이루어져 있는지 체크
    // checkLetter, checkNumber
    fun checkLetter(text: String): Boolean {
        return Pattern.matches("^[a-z]*$", text)
    }

    fun checkNumber(text: String): Boolean {
        return Pattern.matches("^[0-9]*$", text)
    }

    /**
     * 이미지 처리
     * 1. 이미지를 세팅했는지 체크, true: 세팅 안 함, false: 세팅함
     * 2. image resize
     */
    fun checkDefaultProfile(context: Context, ivDrawable: Drawable, width: Int, height: Int): Boolean {
        // 기본 이미지 bitmap 변환
        // val defaultImage = (resources.getDrawable(R.drawable.ic_launcher_foreground) as Drawable).toBitmap(iv_register_image.width, iv_register_image.height, Bitmap.Config.ARGB_8888)
        var defaultImage: Drawable? = ContextCompat.getDrawable(context, R.drawable.img_blank_profile)
        // if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        //     defaultImage = (DrawableCompat.wrap(defaultImage!!)).mutate()
        // }
        // // val bitmapDefault: Bitmap = Bitmap.createBitmap(defaultImage!!.intrinsicWidth, defaultImage.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val bitmapDefault: Bitmap = Bitmap.createScaledBitmap((defaultImage as Drawable).toBitmap(width, height, Bitmap.Config.ARGB_8888), width, height, true)
        val defaultByteArray = ByteArrayOutputStream()
        bitmapDefault.compress(Bitmap.CompressFormat.PNG, 70, defaultByteArray)
        val defaultByte: ByteArray = defaultByteArray.toByteArray()
        val defaultString: String = Base64.encodeToString(defaultByte, Base64.DEFAULT)

        val compareImage = (ivDrawable as Drawable).toBitmap(width, height, Bitmap.Config.ARGB_8888)
        val compareByteArray = ByteArrayOutputStream()
        compareImage.compress(Bitmap.CompressFormat.PNG, 70, compareByteArray)
        val compareByte: ByteArray = compareByteArray.toByteArray()
        val compareString: String = Base64.encodeToString(compareByte, Base64.DEFAULT)

        return compareString == defaultString
    }

    fun checkDefaultPosting(context: Context, ivDrawable: Drawable, width: Int, height: Int): Boolean {
        // 기본 이미지 bitmap 변환
        // val defaultImage = (resources.getDrawable(R.drawable.ic_launcher_foreground) as Drawable).toBitmap(iv_register_image.width, iv_register_image.height, Bitmap.Config.ARGB_8888)
        var defaultImage: Drawable? = ContextCompat.getDrawable(context, R.drawable.img_blank_post)
        // if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        //     defaultImage = (DrawableCompat.wrap(defaultImage!!)).mutate()
        // }
        // // val bitmapDefault: Bitmap = Bitmap.createBitmap(defaultImage!!.intrinsicWidth, defaultImage.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val bitmapDefault: Bitmap = Bitmap.createScaledBitmap((defaultImage as Drawable).toBitmap(width, height, Bitmap.Config.ARGB_8888), width, height, true)
        val defaultByteArray = ByteArrayOutputStream()
        bitmapDefault.compress(Bitmap.CompressFormat.PNG, 70, defaultByteArray)
        val defaultByte: ByteArray = defaultByteArray.toByteArray()
        val defaultString: String = Base64.encodeToString(defaultByte, Base64.DEFAULT)

        val compareImage = (ivDrawable as Drawable).toBitmap(width, height, Bitmap.Config.ARGB_8888)
        val compareByteArray = ByteArrayOutputStream()
        compareImage.compress(Bitmap.CompressFormat.PNG, 70, compareByteArray)
        val compareByte: ByteArray = compareByteArray.toByteArray()
        val compareString: String = Base64.encodeToString(compareByte, Base64.DEFAULT)

        return compareString == defaultString
    }

    fun resizeImage(ivDrawable: Drawable, width: Int, height: Int): ByteArray {
        val bitmapImage: Bitmap = (ivDrawable as BitmapDrawable).bitmap
        val resizedImage: Bitmap = Bitmap.createScaledBitmap(bitmapImage, width, height, true)
        val stream = ByteArrayOutputStream()
        resizedImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}