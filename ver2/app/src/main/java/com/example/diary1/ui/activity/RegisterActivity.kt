package com.example.diary1.ui.activity

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
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
import com.example.diary1.R
import com.example.diary1.constants.Constants
import com.example.diary1.constants.util.Utils
import com.example.diary1.datasave.database.MyDirayDB
import com.example.diary1.datasave.entity.UserInfo
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

// TODO : insert O, select X

// TODO : 현재 RegisterActivity, LoginActivity 에서 사용하는 Util.checkMember 사용 안 하고, RegisterActivity 에서 DB 바로 실행하는 중..
//  Thread 처리 해야 하는데, Thread 돌리면 그 안에 안 탐.
//  RegisterActivity 에서 DB 처리 성공하면, 전체적으로 Room 사용한 부분들 손봐야 함

// TODO : 위에꺼 처리 완료 후 전체적으로 db open 한 부분들 db.close 처리 해주기
class RegisterActivity : AppCompatActivity() {

    var mCurrentPhotoPath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // 이미지뷰 클릭시 앨범과 카메라에서 이미지 삽입
        iv_register_image.setOnClickListener {
            getImage()
        }

        btn_register_submit.setOnClickListener {
            /**
             * 예외처리 (입력 양식)
             */
            if (et_register_name.text.isNullOrEmpty() ||
                et_register_id.text.isNullOrEmpty() ||
                et_register_pw.text.isNullOrEmpty() ||
                et_register_pw2.text.isNullOrEmpty()) {
                Toast.makeText(this, "양식을 다 채워주세요", Toast.LENGTH_SHORT).show()
            } else if (Utils.checkDefaultProfile(this, iv_register_image.drawable, iv_register_image.width, iv_register_image.height)) {
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

                if (et_register_pw.text.length !in 8..20 || et_register_pw2.text.length !in 8..20) {
                    Toast.makeText(this, "비밀번호는 8-20글자 사이로 써야 해요", Toast.LENGTH_SHORT).show()
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
        val db = MyDirayDB.getInstance(applicationContext)
        var getMember: List<UserInfo>? = listOf()
        // CoroutineScope(Dispatchers.IO).launch {
        //     getMember = db!!.userDao().checkOneRegister(et_register_id.text.toString()) // 왜 안 타..
        // }
        // GlobalScope.launch {
        //     getMember = db!!.userDao().checkOneRegister(et_register_id.text.toString()) // 왜 안 타.. ㅠㅠㅠㅠㅠㅠ
        // }

        // java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.
        // getMember = db!!.userDao().checkOneRegister(et_register_id.text.toString())

        if (getMember?.size!! > 0) {
            Toast.makeText(this, "이미 등록된 ID 입니다", Toast.LENGTH_SHORT).show()
            return
        }

        val pw: String = BCrypt.hashpw(et_register_pw.text.toString(), BCrypt.gensalt(10))
        val image = Utils.resizeImage(iv_register_image.drawable, (iv_register_image.drawable as BitmapDrawable).bitmap.width/2, (iv_register_image.drawable as BitmapDrawable).bitmap.height/2)

        // val db = MyDirayDB.getInstance(applicationContext)
        CoroutineScope(Dispatchers.IO).launch {
            db!!.userDao().register(UserInfo(et_register_id.text.toString(), et_register_name.text.toString(), pw, image)) // 잘 탐!!
        }

        finishRegister()
    }

    private fun finishRegister() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("가입 완료")
        builder.setMessage("회원가입이 완료되었습니다.")

        var listener = DialogInterface.OnClickListener { _, a ->
            when (a) {
                DialogInterface.BUTTON_NEGATIVE -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
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
        if (checkPermission(Utils.STORAGE_PERMISSION, Utils.PERMISSION_CAMERA)) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, Utils.REQUEST_STORAGE)
        }
    }

    private fun openCamera() {
        if (checkPermission(Utils.CAMERA_PERMISSION, Utils.PERMISSION_CAMERA)) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(packageManager) != null) {
                val photoFile: File? = createImageFile()

                if (photoFile != null) { // getUriForFile의 두 번째 인자는 Manifest provier의 authorites와 일치해야 함
                    val providerURI = FileProvider.getUriForFile(this, packageName, photoFile)
                    // 인텐트에 전달할 때는 FileProvier의 Return값인 content://로만!!, providerURI의 값에 카메라 데이터를 넣어 보냄
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI)
                }
            }
            startActivityForResult(intent, Utils.REQUEST_CAMERA)
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
                Utils.REQUEST_STORAGE -> {
                    val uri = data?.data
                    Log.d("gall", ">>>>>>>>>>uri ${uri?.path}")
                    iv_register_image.setImageURI(uri)
                }
                Utils.REQUEST_CAMERA -> {
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
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.right_in, 0)
        finish()
    }
}
