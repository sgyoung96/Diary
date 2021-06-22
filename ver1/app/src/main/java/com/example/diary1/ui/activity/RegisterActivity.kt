package com.example.diary1.ui.activity

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.diary1.R
import com.example.diary1.constants.RegisterInfo
import com.example.diary1.constants.SQLiteDBInfo
import com.example.diary1.datasave.SQLiteDBHelper
import com.example.diary1.datasave.query.RegisterQuery
import com.example.diary1.util.RegUtils
import com.example.diary1.util.RegisterUtils
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.Exception
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
    val REQUEST_CAMERA = 3
    val REQUEST_STORAGE = 4

/*    // SharedPrefernce 사용을 위해 선언한 변수
    var context: Context? = null
*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

/*
        context = this
*/

        // 이미지뷰 클릭시 앨범과 카메라에서 이미지 삽입
        // TODO: 이미지 로컬에 저장.
        iv_register_image.setOnClickListener {
            getImage()
        }

        btn_register_submit.setOnClickListener {
            /**
             * 예외처리 (입력 양식)
             */
            // EditText 공란일 때 예외처리 - Toast 띄우고 이후 액션 없어서 return 처리 안함
            if (et_register_name.text.isNullOrEmpty() ||
                et_register_id.text.isNullOrEmpty() ||
                et_register_pw.text.isNullOrEmpty() ||
                et_register_pw2.text.isNullOrEmpty()) {
                Toast.makeText(this, "양식을 다 채워주세요", Toast.LENGTH_SHORT).show()
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

                if (!RegUtils.checkName(et_register_name.text.toString())) {
                    Toast.makeText(this, "한글 이름을 정확히 써주세요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (et_register_id.text.length !in 6..10) {
                    Toast.makeText(this, "아이디는 6-10글자 사이로 써야 해요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // true || false
                if(RegUtils.checkLetter(et_register_id.text.toString()) || RegUtils.checkNumber(et_register_id.text.toString())) {
                    Toast.makeText(this, "아이디는 영소문자와 숫자의 조합으로 이루어져야 해요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (et_register_pw.text.length !in 12..20 || et_register_pw2.text.length !in 12..20) {
                    Toast.makeText(this, "비밀번호는 12-20글자 사이로 써야 해요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // true || false
                if(RegUtils.checkLetter(et_register_pw.text.toString()) || RegUtils.checkNumber(et_register_pw.text.toString())) {
                    Toast.makeText(this, "비밀번호는 영소문자와 숫자의 조합으로 이루어져야 해요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (et_register_pw.text.toString() != et_register_pw2.text.toString()) {
                    Toast.makeText(this, "비밀번호가 일치하지 않아요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
/*
//                // 이미지뷰에 있는 이미지 비트맵 변환
//                var uploadImage = drawableToBitmap(iv_register_image.drawable)
//                // drawable 에 있는 기본 이미지 비트맵 변환 (.xml 파일을 drawable로 변환 먼저!)
////                var compareImage: Drawable? = null
////                var compareImage = drawableToBitmap(R.drawable.ic_launcher_foreground as Drawable)
////                var compareImage = drawableToBitmap(R.drawable.ic_launcher_foreground.toDrawable().mutate())
////                var compareImage: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_foreground)
////                compareImage = drawableToBitmap(R.drawable.ic_launcher_foreground.toDrawable()) // found : Bitmap, required: Drawable
//                var compareImage: Bitmap? = null
//
//                try {
//                    compareImage = drawableToBitmap(R.drawable.ic_launcher_foreground.toDrawable()) // compareImage = null, e = java.lang.IllegalArgumentException: width and height must be > 0
//                } catch (e: Exception) {
//                    Toast.makeText(this, "$e", Toast.LENGTH_LONG).show()
//                }
//
////                var drawableUri: String? = null
//
////                try {
////                    drawableUri = R.drawable.ic_launcher_foreground.getResourceUri(this)
////                } catch (e: Exception) {
////                    Toast.makeText(this, "$e", Toast.LENGTH_LONG).show()
////                }
//
////                try {
////                    compareImage = drawableToDrawble(drawableUri!!) // compareImage = null, drawableUri = "android.resource://com.example.diary1/drawable/ic_launcher_foreground"
////                } catch (e: Exception) {
////                    Toast.makeText(this, "$e", Toast.LENGTH_LONG).show()
////                }
//
//
//
//                // 비트맵 변환한 이미지를 비교
//                if (!sameAs(uploadImage, compareImage!!)) {
//                    Toast.makeText(this, "프로필사진을 추가해 주세요", Toast.LENGTH_SHORT).show()
//                }

//                var temp = iv_register_image.resources.toString() // android.content.res.Resources@f780ff5
//                var temp2 = R.drawable.ic_launcher_foreground.toString() // 2131165289

//                var tempDrawable = iv_register_image.drawable.current // {VectorDrawable@11449}

//                var tempDrawable2 = R.drawable.ic_launcher_foreground.toDrawable() // {ColorDrawable@11451}
//
//                if ( /* 변환 없이 imageView 에 쓴 리소스 파일과 drawable 에 있는 파일을 비교해 볼까? */ ) {
//                    Toast.makeText(this, "프로필사진을 추가해 주세요", Toast.LENGTH_SHORT).show()
//                } // -----> 안된다.
*/

                Log.d("PASS", ">>>>>>>>>>PASS")

                /**
                 * 입력 양식 예외처리 통과 후 데이터 로컬에 저장
                 * 이 함수 안에서 finishRegister() 함수 호출하여 마지막 단계 진행
                 */
                saveData()
            }
        }
    }

/*
//    // 이미지 비트맵 변환
//    companion object fun drawableToBitmap(drawable: Drawable): Bitmap {
//        if (drawable is BitmapDrawable) {
//            return (drawable as BitmapDrawable).bitmap
//        }
//
//        var bitmap: Bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
//        var canvas: Canvas = Canvas(bitmap)
//        drawable.setBounds(0, 0, canvas.width, canvas.height)
//        drawable.draw(canvas)
//
//        return bitmap
//    }

//    // 이미지 비교
//    fun sameAs(bitmap1: Bitmap, bitmap2: Bitmap): Boolean {
//        var buffer1: ByteBuffer = ByteBuffer.allocate(bitmap1.height * bitmap1.rowBytes)
//        bitmap1.copyPixelsToBuffer(buffer1)
//
//        var buffer2: ByteBuffer = ByteBuffer.allocate(bitmap2.height * bitmap2.rowBytes)
//        bitmap2.copyPixelsToBuffer(buffer2)
//
//        return Arrays.equals(buffer1.array(), buffer2.array())
//    }
//
//    // drawable 폴더의 파일 url 경로 얻기
//    fun Int.getResourceUri(context: Context): String {
//        return context.resources.let {
//            Uri.Builder()
//                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
//                .authority(it.getResourcePackageName(this))
//                .appendPath(it.getResourceTypeName(this))
//                .appendPath(it.getResourceEntryName(this))
//                .build()
//                .toString()
//        }
//    }
//
    // drawable 폴더의 파일 drawable 타입으로 변환
//    fun drawableToDrawble(url: String): Drawable {
//        var x: Bitmap? = null
//        var connection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
//
//        var input: InputStream = connection.inputStream
//        x = BitmapFactory.decodeStream(input)
//
//        return BitmapDrawable(resources, x)
//    }

    // URi 를 Drawable 파일로 변환
*/

    /**
     * 입력한 텍스트 값을 묶어서 로컬에 저장
     * 1. SharedPreference 사용
     * 2. SQLite 사용
     * 3. Room 사용 (SQLite)
     * 4. Realm 사용
     */

/*    // [1] : SharedPreference
    fun saveUserInfo() {
        // Test
        SharedPreferenceManager.setStringSet(context!!, "1", setOf("name", "id", "pw"))
        var getFirstSet: Set<String>? = SharedPreferenceManager.getStringSet(context!!, "1")
        if (getFirstSet.isNullOrEmpty()) {
            SharedPreferenceManager.setStringSet(
                context!!,
                RegisterInfo.REGISTER_SEQ.toString(),
                setOf(et_register_name.text.toString(),
                    et_register_id.text.toString(),
                    et_register_pw.text.toString())
            )
        }
    }
*/

    // [2] : SQLite
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
        if (RegisterUtils.checkMember(this, et_register_id.text.toString())) {
            Toast.makeText(this, "이미 등록된 ID 입니다", Toast.LENGTH_SHORT).show()
            return
        }

        val registerQuery = RegisterQuery.register(et_register_name.text.toString(), et_register_id.text.toString(), et_register_pw.text.toString())
        try {
            database.execSQL(registerQuery) // ************ 여기서 터진다
        } catch (e: Exception) {
            Log.d("INSERT EXCEPTION", ">>>>>>>>>>$e")
            // android.database.sqlite.SQLiteException: no such table: USERINFO (code 1 SQLITE_ERROR[1]): , while compiling: INSERT INTO USERINFO(USERNAME,USERID,USERPW)VALUES('가','rkrkrk1','rkrkrkrkrkrk1');
        }

        // 제대로 삽입됐는지 확인 (Select)
        database = dbHelper.readableDatabase
        // 1. 직전에 회원가입한 정보 확인
        try{
            checkRegisterQuery = RegisterQuery.checkOneRegister(et_register_id.text.toString())
            result = database.rawQuery(checkRegisterQuery, null)
            while (result.moveToNext()) {
                Log.d("SHOW ONE REGISTER INFO", ">>>>>>>>>>${result.getString(result.getColumnIndex(
                    RegisterInfo.DB_COL_ID
                ))}")
                // akakak2
            }
        } catch (e: Exception) {
            Log.d("SHOW ONE INFO ERROR", ">>>>>>>>>>$e")
            // android.database.sqlite.SQLiteException: near "=": syntax error (code 1 SQLITE_ERROR[1]): , while compiling: SELECT * FROM USERINFOWHERE USERID = sksksk1;
        }

        // 2. 지금까지 회원가입한 모든 정보 확인
        try {
            checkRegisterQuery = RegisterQuery.checkAllRegister()
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
                DialogInterface.BUTTON_NEUTRAL -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    Log.d("화면전환", ">>>>>>>>>Success")
                    finish()
                    Log.d("액티비티 종료", ">>>>>>>>>Success")
                }
            }
        }
        builder.setNeutralButton("확인", listener)

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
        var builder = AlertDialog.Builder(this)
        builder.setTitle("프로필 사진 등록")
        builder.setMessage("프로필 사진을 등록하세요")

        var listener = DialogInterface.OnClickListener { _, a ->
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
            startActivityForResult(intent, REQUEST_CAMERA)
        }
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
                    val bitmap = data?.extras?.get("data") as Bitmap
                    iv_register_image.setImageBitmap(bitmap)
                }
            }
        }
    }
}
