package com.example.diary1.ui.activity

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteStatement
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import com.example.diary1.R
import com.example.diary1.datasave.constants.RegisterInfo
import com.example.diary1.datasave.constants.SQLiteDBInfo
import com.example.diary1.constants.Constants
import com.example.diary1.datasave.SQLiteDBHelper
import com.example.diary1.datasave.queries.Query
import com.example.diary1.util.Utils
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_setting.*
import org.mindrot.jbcrypt.BCrypt
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class SettingActivity : AppCompatActivity() {

    // MainPageActivity 에서 마지막에 선택한 버튼 플래그
    var btnClicked: Int? = null

    /**
     * SQLite 관련 변수들
     */
    var dbHelper: SQLiteDBHelper? = null
    var database: SQLiteDatabase? = null
    var sqlQuery: String? = null
    var result: Cursor? = null

    /**
     * 카메라와 앨범으로부터 이미지 가져오는 기능에 필요한 변수들
     * CAMERA_PERMISSION, CAMERA_PERMISSION, PERMISSION_CAMERA, PERMISSION_STORAGE, REQUEST_CAMERA, REQUEST_STORAGE
     */
    val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    val STORAGE_PERMISSION = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val PERMISSION_CAMERA = 1
    val PERMISSION_STORAGE = 2
    // val REQUEST_CAMERA = 3
    val REQUEST_STORAGE = 4
    val REQUEST_CAMERA = 1

    var mCurrentPhotoPath: String = ""

    /**
     * 초기화 : 아이디, 이름, 프로필 사진
     * 변경 가능 : 이름, 비밀번호, 비밀번호 확인, 프로필 사진
     * [기능]
     * 1. 수정 - saveSettings
     * 2. 탈퇴 - deleteUserInfo
     * [예외처리]
     * 1. 이름 - 5글자 제한 - xml, 한글만 가능
     * 2. 비밀번호 - 암호화하여 저장할 것! 비밀번호 일치 불일치 검사 할것! 비밀번호 유효성 검사(정규식) 할 것!
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        btnClicked = intent.getSerializableExtra("btnClicked") as Int

        dbHelper = SQLiteDBHelper(this, SQLiteDBInfo.DB_NAME, null, 1)
        // 초기화
        init()

        // 이미지뷰에 이미지 추가
        iv_setting_profile.setOnClickListener {
            getImage()
        }

        /**
         * 비밀번호 일치 여부 실시간 체크
         */
        et_setting_check_pw.doOnTextChanged { text, start, before, count ->
            if (et_setting_check_pw.text.toString() == et_setting_pw.text.toString()) {
                tv_setting_pw_check_guide.setTextColor(getColor(R.color.setting_checked_pw))
                tv_setting_pw_check_guide.setText(getString(R.string.setting_checked_pw))
            }
        }

        /**
         * 1. 예외처리
         * 2. DB처리
         */
        btn_setting_edit.setOnClickListener {
            if (et_setting_name.text.contains(" ") || et_setting_pw.text.contains(" ") || et_setting_check_pw.text.contains(" ")) {
                Toast.makeText(this, "공백은 포함될 수 없어요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Utils.checkName(et_setting_name.text.toString())) {
                Toast.makeText(this, "한글 이름을 정확히 써주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (et_setting_pw.text.length !in 12..20 || et_setting_check_pw.text.length !in 12..20) {
                Toast.makeText(this, "비밀번호는 12-20글자 사이로 써야 해요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // true || false
            if(Utils.checkLetter(et_setting_pw.text.toString()) || Utils.checkNumber(et_setting_pw.text.toString())) {
                Toast.makeText(this, "비밀번호는 영소문자와 숫자의 조합으로 이루어져야 해요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (et_setting_pw.text.toString() == et_setting_check_pw.text.toString()) {
                saveSettings()
            }
        }

        btn_setting_delete.setOnClickListener {
            deleteUserInfo()
        }

    }

    /**
     * 1. Constants 로 선언한 UserId 를 EditTet ID 란에 넣기 (isEnabled = false)
     * 2. id 를 where 절로 주어서 받아온 이름 값 EditText 에 넣기 (isEnabled = true)
     * 3. 이름 란 힌트 - 받아온 이름 값으로 설정
     * 4. DB open, id 를 where 절로 조회하여 해당 유저 정보 중 프로필사진 정보 가져와 이미지뷰에 넣기
     *    단, 프로필 사진을 지정하지 않았다면 초기화하지 않음 (xml 에 설정한 src 그대로 나타냄)
     */
    // TODO: 2. DB open, id 를 where 절로 조회하여 해당 유저 정보 중 프로필사진 정보 가져와 이미지뷰에 넣기
    fun init() {
        et_setting_id.isEnabled = false
        et_setting_id.setText(Constants.userID)

        /**
         * 이름과 이미지 세팅하기
         */
        var name = ""
        var bitmapImage: Bitmap? = null
        database = dbHelper?.readableDatabase
        sqlQuery = Query.getDefaultQuery()
        result = database?.rawQuery(sqlQuery, null)
        if (result!!.moveToNext()) {
            name = result!!.getString(result!!.getColumnIndex(RegisterInfo.DB_COL_NAME))
            val imageFromDB = result!!.getBlob(result!!.getColumnIndex(RegisterInfo.DB_COLE_IMAGE))
            bitmapImage = BitmapFactory.decodeByteArray(imageFromDB, 0, imageFromDB.size)
        }
        val centerCropImage: Bitmap
        if (bitmapImage!!.width >= bitmapImage.height) {
            centerCropImage = Bitmap.createBitmap(bitmapImage, bitmapImage.width/2 - bitmapImage.height/2, 0, bitmapImage.height, bitmapImage.height)
        } else {
            centerCropImage = Bitmap.createBitmap(bitmapImage, 0, bitmapImage.height/2 - bitmapImage.width/2, bitmapImage.width, bitmapImage.width)
        }


        et_setting_name.setText(name)
        et_setting_name.hint = name
        iv_setting_profile.setImageBitmap(centerCropImage)

        et_setting_pw.setText("")
        et_setting_check_pw.setText("")

        tv_setting_pw_check_guide.text = getString(R.string.setting_check_pw_guide)
        tv_setting_pw_check_guide.setTextColor(getColor(R.color.main_sub_text_color))
    }

    /**
     * 권한을 승인했는지 확인하는 함수
     */
    private fun checkPermission(permissions: Array<out String>, flag: Int): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, flag)
                return false
            }
        }
        return true
    }

    /**
     * 이미지뷰 클릭시 앨범과 카메라에서 이미지 삽입
     * 1. dialog 팝업
     * 2. 각각의 버튼을 클릭했을 때, 앨범 혹은 카메라로 이동
     * 3. 선택 혹은 촬영한 이미지 이미지뷰에 박음
     */
    private fun getImage() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("프로필 사진 등록")
        builder.setMessage("프로필 사진을 등록하세요")

        val listener = DialogInterface.OnClickListener { _, a ->
            when (a) {
                DialogInterface.BUTTON_NEUTRAL -> {
                    openGallery()
                }
                DialogInterface.BUTTON_POSITIVE -> {
                    openCamera()
                }
            }
        }
        builder.setNeutralButton("앨범", listener)
        builder.setPositiveButton("카메라", listener)

        builder.show()
    }

    /**
     * 앨범 열기 : openGallery
     * 카메라 열기 : openCamera
     */
    private fun openGallery() {
        if (checkPermission(STORAGE_PERMISSION, PERMISSION_CAMERA)) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, REQUEST_STORAGE)
        }
    }

    private fun openCamera() {
        if (checkPermission(CAMERA_PERMISSION, PERMISSION_CAMERA)) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(packageManager) != null) {
                val photoFile = createImageFile()
                if (photoFile != null) {
                    val providerURI = FileProvider.getUriForFile(this, packageName, photoFile)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI)
                }
            }
            startActivityForResult(intent, REQUEST_CAMERA)
        }
    }

    @Throws(IOException::class)
    fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_$timeStamp.jpg"
        var imageFile: File? = null
        val storageDir = File(Environment.getExternalStorageDirectory().toString() + "/Pictures", "${Constants.appName}")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        imageFile = File(storageDir, imageFileName)
        mCurrentPhotoPath = imageFile.absolutePath
        return imageFile
    }

    /**
     * 이미지뷰에 이미지 셋팅
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("gall", ">>>>>>>>>>result code $resultCode")
        if(data == null ) Log.d("gall", ">>>>>>>>>>data null")
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_STORAGE -> {
                    val uri = data?.data
                    Log.d("gall", ">>>>>>>>>>uri ${uri?.path}")
                    iv_setting_profile.setImageURI(uri)
                }
                REQUEST_CAMERA -> {
                    // val bitmap = data?.extras?.get("data") as Bitmap
                    // iv_setting_profile.setImageBitmap(bitmap)
                    galleryAddPic()
                }
            }
        }
    }

    fun galleryAddPic() {
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        val file = File(mCurrentPhotoPath)
        val contentUri: Uri = Uri.fromFile(file)
        intent.setData(contentUri)
        sendBroadcast(intent)
        Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show()

        // 로컬에 저장한 이미지 이미지뷰에 세팅
        iv_setting_profile.setImageURI(contentUri)
    }

    /**
     * 변경 정보 저장
     * 1. pw 암호화
     * 2. writable 모드로 db open
     * 3. insert sql 할당
     * 4. 실행
     */
    fun saveSettings() {
        // 입력된 비밀번호값 암호화
        val pw: String = BCrypt.hashpw(et_setting_pw.text.toString(), BCrypt.gensalt(10))
        val bitmapImage: Bitmap = (iv_setting_profile.drawable as BitmapDrawable).bitmap
        val resizedImage: Bitmap = Bitmap.createScaledBitmap(bitmapImage, iv_setting_profile.width, iv_setting_profile.height, true)
        val stream = ByteArrayOutputStream()
        resizedImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val convertedImage: ByteArray = stream.toByteArray()

        database = dbHelper?.writableDatabase
        sqlQuery = Query.saveSettingQuery(et_setting_name.text.toString(), pw, "?")
        val sqliteStatement: SQLiteStatement = database!!.compileStatement(sqlQuery)
        sqliteStatement.bindBlob(1, convertedImage)
        sqliteStatement.execute()
        // database?.execSQL(sqlQuery)

        // 변경사항이 모두 저장되었습니다 다이어로그
        val builder = AlertDialog.Builder(this)
        builder.setTitle("저장 완료")
        builder.setMessage("변경사항이 모두 저장되었습니다")
        val listener = DialogInterface.OnClickListener { _, a ->
            when (a) {
                DialogInterface.BUTTON_NEUTRAL -> {
                    init()
                    return@OnClickListener
                }
                DialogInterface.BUTTON_POSITIVE -> {
                    init()
                    return@OnClickListener
                }
            }
        }
        builder.setNeutralButton("    ", listener)
        builder.setPositiveButton("확인", listener)
        builder.show()
    }

    /**
     * 계정 삭제
     */
    fun deleteUserInfo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("회원 탈퇴")
        builder.setMessage("정말로 탈퇴하시겠습니까? 관련 정보는 모두 사라집니다")

        val listener = DialogInterface.OnClickListener { _, a ->
            when (a) {
                DialogInterface.BUTTON_NEUTRAL -> {
                    database = dbHelper?.writableDatabase
                    sqlQuery = Query.deleteAllQuery()
                    database?.execSQL(sqlQuery)

                    // 삭제가 완료 되었습니다 다이어로그
                    builder.setTitle("계정 삭제")
                    builder.setMessage("삭제가 완료되었습니다")

                    val listener = DialogInterface.OnClickListener { _, a ->
                        when (a) {
                            DialogInterface.BUTTON_NEUTRAL -> {
                                database?.close()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                            DialogInterface.BUTTON_POSITIVE -> {
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                        }
                    }

                    builder.setNeutralButton("    ", listener)
                    builder.setPositiveButton("확인", listener)
                    builder.show()
                }
                DialogInterface.BUTTON_POSITIVE -> {
                    return@OnClickListener
                }
            }
        }
        builder.setNeutralButton("예", listener)
        builder.setPositiveButton("아니오", listener)

        builder.show()
    }

    /**
     * 뒤로가기 버튼 누르면 db 닫고, 이 화면 종료 (MainPageActivity - 직전 Fragment 로 이동)
     */
    override fun onBackPressed() {
        database?.close()
        val intent = Intent(this, MainPageActivity::class.java)
        intent.putExtra("btnClicked", btnClicked)
        startActivity(intent)
        finish()
    }
}