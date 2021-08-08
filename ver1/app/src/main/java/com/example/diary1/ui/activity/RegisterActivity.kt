package com.example.diary1.ui.activity

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteStatement
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.example.diary1.R
import com.example.diary1.constants.Constants
import com.example.diary1.datasave.constants.RegisterInfo
import com.example.diary1.datasave.constants.SQLiteDBInfo
import com.example.diary1.datasave.SQLiteDBHelper
import com.example.diary1.datasave.queries.Query
import com.example.diary1.constants.util.Utils
import kotlinx.android.synthetic.main.activity_register.*
import org.mindrot.jbcrypt.BCrypt
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * [1]
 * 이미지뷰를 눌렀을 때,
 * setOnClickListener 함수 실행
 * - Dialog 띄우기 : 사진을 선택하세요 // 앨범 / 카메라
 * >> 앨범 클릭시, 핸드폰 갤러리에 접근
 * -> 선택시, 이미지뷰에 사진 넣어지고, 변수에 사진 정보 할당
 * >> 카메라 클릭시, 사진 촬영 기능
 * -> 촬영시, 갤러리에 저장 + 이미지뷰에 사진 넣어짐 + 변수에 사진 정보 할당
 *
 * [2]
 * cf. 문자열 유효성 검사(정규식) (http://blog.naver.com/PostView.nhn?blogId=suda552&logNo=220813122485)
 * 회원가입 버튼을 눌렀을 때,
 * 1. EditText 빈칸일 때 예외
 * 2. 이름란 5글자 넘을 수 없음 예외 + 한글 예외
 * 3. 아이디 6에서 12글자 예외
 * 4. 아이디 영소문자 + 숫자 조합 체크
 * 5. 비밀번호 12에서 20글자 예외
 * 6. 비밀번호 영소문자 + 숫자 조합 체크
 * 7. 비밀번호 확인란 일치/불일치 체크
 * 8. 프로필 사진 등록(앨범에서 선택 및 사진 촬영) 예외
 * 9. 다 통과하면,
 * 9-1. 스택에서 이 화면 없애기
 * 9-2. 기기 내부에 회원가입 정보 저장하기 (이름, 아이디, 비밀번호, 프로필사진) // 저장할 때, 공백 없애기!
 * 9-3. 메인화면으로 화면 전환하기
 */

class RegisterActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // 이미지뷰 클릭시 앨범과 카메라에서 이미지 삽입
        iv_register_image.setOnClickListener {
            getImage()
        }

        btn_register_submit.setOnClickListener {
            // 기본 이미지 bitmap 변환
            // val defaultImage = (resources.getDrawable(R.drawable.ic_launcher_foreground) as Drawable).toBitmap(iv_register_image.width, iv_register_image.height, Bitmap.Config.ARGB_8888)
            var defaultImage: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground)
            // if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //     defaultImage = (DrawableCompat.wrap(defaultImage!!)).mutate()
            // }
            // // val bitmapDefault: Bitmap = Bitmap.createBitmap(defaultImage!!.intrinsicWidth, defaultImage.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val bitmapDefault: Bitmap = Bitmap.createScaledBitmap((defaultImage as Drawable).toBitmap(iv_register_image.width, iv_register_image.height, Bitmap.Config.ARGB_8888), iv_register_image.width, iv_register_image.height, true)
            val defaultByteArray = ByteArrayOutputStream()
            bitmapDefault.compress(Bitmap.CompressFormat.PNG, 70, defaultByteArray)
            val defaultByte: ByteArray = defaultByteArray.toByteArray()
            val defaultString: String = Base64.encodeToString(defaultByte, Base64.DEFAULT)

            val compareImage = (iv_register_image.drawable as Drawable).toBitmap(iv_register_image.width, iv_register_image.height, Bitmap.Config.ARGB_8888)
            val compareByteArray = ByteArrayOutputStream()
            compareImage.compress(Bitmap.CompressFormat.PNG, 70, compareByteArray)
            val compareByte: ByteArray = compareByteArray.toByteArray()
            val compareString: String = Base64.encodeToString(compareByte, Base64.DEFAULT)

            /**
             * 예외처리 (입력 양식)
             */
            // EditText 공란일 때 예외처리 - Toast 띄우고 이후 액션 없어서 return 처리 안함
            if (et_register_name.text.isNullOrEmpty() ||
                et_register_id.text.isNullOrEmpty() ||
                et_register_pw.text.isNullOrEmpty() ||
                et_register_pw2.text.isNullOrEmpty()) {
                Toast.makeText(this, "양식을 다 채워주세요", Toast.LENGTH_SHORT).show()
            } else if (compareString == defaultString) {
                Toast.makeText(this, "프로필 사진을 등록해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                /**
                 * 1. 입력양식 예외처리
                 * 2. 데이터 저장
                 * 3. 화면 전환 및 종료
                 */
                // 공백이 포함되면 안내 메시지 띄우고 return
                if (et_register_name.text.toString().contains(" ") ||
                    et_register_id.text.toString().contains(" ") ||
                    et_register_pw.text.toString().contains(" ") ||
                    et_register_pw2.text.toString().contains(" ")) {
                    Toast.makeText(this, "공백은 포함될 수 없어요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (et_register_name.text.length > 5) {
                    Toast.makeText(this, "이름은 5글자를 넘을 수 없어요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (!Utils.checkName(et_register_name.text.toString())) {
                    Toast.makeText(this, "한글 이름을 정확히 써주세요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (et_register_id.text.length !in 6..10) {
                    Toast.makeText(this, "아이디는 6-10글자 사이로 써야 해요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // true || false
                if(Utils.checkLetter(et_register_id.text.toString()) || Utils.checkNumber(et_register_id.text.toString())) {
                    Toast.makeText(this, "아이디는 영소문자와 숫자의 조합으로 이루어져야 해요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (et_register_pw.text.length !in 12..20 || et_register_pw2.text.length !in 12..20) {
                    Toast.makeText(this, "비밀번호는 12-20글자 사이로 써야 해요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // true || false
                if(Utils.checkLetter(et_register_pw.text.toString()) || Utils.checkNumber(et_register_pw.text.toString())) {
                    Toast.makeText(this, "비밀번호는 영소문자와 숫자의 조합으로 이루어져야 해요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (et_register_pw.text.toString() != et_register_pw2.text.toString()) {
                    Toast.makeText(this, "비밀번호가 일치하지 않아요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                Log.d("PASS", ">>>>>>>>>>PASS")

                /**
                 * 입력 양식 예외처리 통과 후 데이터 로컬에 저장
                 * 이 함수 안에서 finishRegister() 함수 호출하여 마지막 단계 진행
                 */
                saveData()
            }
        }
    }

    private fun saveData() {
        // dbHelper 초기화 (내가 생성하고 override 한 메소드가 있는 클래스)
        val dbHelper = SQLiteDBHelper(this, SQLiteDBInfo.DB_NAME, null, 1)
        // Insert 모드로 데이터 저장소 가져옴
        var database: SQLiteDatabase = dbHelper.writableDatabase
        var checkRegisterQuery: String
        var result: Cursor

        /**
         * 1. key 값인 ID 로 먼저 DB 를 SELECT 한다.
         * 2. 만약 결과가 있으면, return 하고, toast 메시지 띄움
         * 3. 만약 결과가 없으면, 회원가입 진행
         */
        if (Utils.checkMember(this, et_register_id.text.toString())) {
            Toast.makeText(this, "이미 등록된 ID 입니다", Toast.LENGTH_SHORT).show()
            return
        }

        /**
         * [1]
         * 비밀번호를 암호화한 것을 DB 에 삽입
         * 변수 : pw
         * 60 byte 의 문자열이 됨
         * 생성된 해쉬를 원래 비밀번호로 검증하는 방법. 맞을 경우 true 반환. 주로 로그인 로직에서 사용
         * => val isValidPW = BCrypt.checkpw(et_register_pw.test.toString(), pw)
         * [2]
         * ImageView image -> blob
         */
        val pw: String = BCrypt.hashpw(et_register_pw.text.toString(), BCrypt.gensalt(10))
        val registerQuery = Query.register(et_register_name.text.toString(), et_register_id.text.toString(), pw, "?")
        val resizedImage: Bitmap = Bitmap.createScaledBitmap((iv_register_image.drawable as BitmapDrawable).bitmap, iv_register_image.width, iv_register_image.height, true)
        val stream = ByteArrayOutputStream()
        resizedImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val convertedImage: ByteArray = stream.toByteArray()

        val sqlStatement: SQLiteStatement = database.compileStatement(registerQuery)
        sqlStatement.bindBlob(1, convertedImage)
        sqlStatement.execute()
        // database.execSQL(registerQuery) // ************ 여기서 터진다

        // 제대로 삽입됐는지 확인 (Select)
        database = dbHelper.readableDatabase
        // 1. 직전에 회원가입한 정보 확인
        try{
            checkRegisterQuery = Query.checkOneRegister(et_register_id.text.toString())
            result = database.rawQuery(checkRegisterQuery, null)
            while (result.moveToNext()) {
                Log.d("SHOW ONE REGISTER INFO", ">>>>>>>>>>${result.getString(result.getColumnIndex(
                    RegisterInfo.DB_COL_ID))}, pw: ${result.getString(result.getColumnIndex(
                    RegisterInfo.DB_COL_PW))}")
                // akakak2
            }
        } catch (e: Exception) {
            Log.d("SHOW ONE INFO ERROR", ">>>>>>>>>>$e")
            // android.database.sqlite.SQLiteException: near "=": syntax error (code 1 SQLITE_ERROR[1]): , while compiling: SELECT * FROM USERINFOWHERE USERID = sksksk1;
        }

        // 2. 지금까지 회원가입한 모든 정보 확인
        try {
            checkRegisterQuery = Query.checkAllRegister()
            result = database.rawQuery(checkRegisterQuery, null)
            while (result.moveToNext()) {
                Log.d("SHOW ALL REGISTER INFO", ">>>>>>>>>>${result.getString(result.getColumnIndex(
                    RegisterInfo.DB_COL_ID
                ))}")
                // rkrkrk1 sksksk1 ekekek1 fkfkfk1 akakak2 가 한줄씩 나옴
            }
        } catch (e: Exception) {
            Log.d("SHOW ALL INFO ERROR", ">>>>>>>>>>$e")
        }

        // DB 닫기
        database.close()
        finishRegister()
    }

    /**
     * 1. 회원가입이 완료됐다는 다이얼로그 표시 후,
     * 2. 메인 화면으로 화면 전환하고
     * 3. 스택에서 이 화면 없앰
     */
    private fun finishRegister() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("가입 완료")
        builder.setMessage("회원가입이 완료되었습니다.")

        var listener = DialogInterface.OnClickListener { _, a ->
            when (a) {
                DialogInterface.BUTTON_NEGATIVE -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    Log.d("화면전환", ">>>>>>>>>Success")
                    finish()
                    Log.d("액티비티 종료", ">>>>>>>>>Success")
                }
            }
        }
        builder.setNegativeButton("확인", listener)

        builder.show()
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
                val photoFile: File? = createImageFile()

                if (photoFile != null) { // getUriForFile의 두 번째 인자는 Manifest provier의 authorites와 일치해야 함
                    val providerURI = FileProvider.getUriForFile(this, packageName, photoFile)
                    // 인텐트에 전달할 때는 FileProvier의 Return값인 content://로만!!, providerURI의 값에 카메라 데이터를 넣어 보냄
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI)
                }
            }
            startActivityForResult(intent, REQUEST_CAMERA)
        }
    }

    // 사진을 찍기 전, 사진이 저장되는 임시 파일 생성. 파일의 형식 지정
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

    // 이미지를 로컬 폴더에 저장
    fun galleryAddPic() {
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        val file = File(mCurrentPhotoPath)
        Log.d("mcurrentPhotoPath", ">>>>>>>>>>$mCurrentPhotoPath")
        val contentUri: Uri = Uri.fromFile(file)
        intent.setData(contentUri)
        sendBroadcast(intent)
        Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show()

        // 로컬에 저장한 이미지 이미지뷰에 세팅
        iv_register_image.setImageURI(contentUri)
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
                    iv_register_image.setImageURI(uri)
                }
                REQUEST_CAMERA -> {
                    // val bitmap = data?.extras?.get("data") as Bitmap
                    // iv_register_image.setImageBitmap(bitmap)
                    galleryAddPic()
                }
            }
        }
    }

    /**
     * 뒤로가기 버튼 누르면 이 화면 종료 (LoginActivity 로 이동)
     */
    override fun onBackPressed() {
        finish()
    }
}
