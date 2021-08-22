package com.example.diary1.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.diary1.R
import com.example.diary1.constants.Constants
import com.example.diary1.ui.fragment.*
import com.example.diary1.constants.util.BottomBtns
import com.example.diary1.datasave.entity.PostInfo
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.android.synthetic.main.fragment_post_diary.*
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

// BottomNavigationView 에서 캘린더 아이콘 삭제 X -> Joda Time 라이브러리 사용하여 일기 쓴 날에 해당하여 표시 주기
// Calendar 리사이클러뷰 그리드로 그리기 + 뷰페이저
class MainPageActivity : AppCompatActivity() {

    // 버튼 색상 플래그
    var btnClicked: Int? = null

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
     * 뒤로가기 버튼 두 번 클릭 시 앱 종료때 사용할 변수
     */
    var waitTime: Long = 0

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        hideKeyboard()

        /**
         * 하단 버튼 색상 초기화
         * 일기 목록이 메인 화면이므로 색상을 바꿔줌
         */
        setBottomInit()
        selectBottomBtn(BottomBtns.DIARY_LIST)
        btnClicked = BottomBtns.DIARY_LIST

        /**
         * 하단 메뉴 버튼 이벤트
         * 1. 일기목록 2. 요일별 일기 3. 일기쓰기 4. 캘린더
         * 클릭시 아이콘, 텍스트 색상과 타이틀 바뀌고 프레그먼트 화면 전환
         */
        bottom_btn_list.setOnClickListener {
            setBottomInit()
            selectBottomBtn(BottomBtns.DIARY_LIST)
        }
        bottom_btn_post.setOnClickListener {
            setBottomInit()
            selectBottomBtn(BottomBtns.DIARY_POST)
        }
        bottom_btn_my.setOnClickListener {
            setBottomInit()
            selectBottomBtn(BottomBtns.DIARY_MY)
        }
        bottom_btn_setting.setOnClickListener {
            setBottomInit()
            selectBottomBtn(BottomBtns.DIARY_SET)
        }
    }

    fun setBottomInit() {
        iv_bottom_list.setImageDrawable(getDrawable(R.drawable.bottom_button_list_off))
        tv_bottom_list.setTextColor(getColor(R.color.main_sub_text_color))

        iv_bottom_post.setImageDrawable(getDrawable(R.drawable.bottom_button_post_off))
        tv_bottom_post.setTextColor(getColor(R.color.main_sub_text_color))

        iv_bottom_my.setImageDrawable(getDrawable(R.drawable.bottom_button_my_off))
        tv_bottom_my.setTextColor(getColor(R.color.main_sub_text_color))

        iv_bottom_setting.setImageDrawable(getDrawable(R.drawable.bottom_button_setting_off))
        tv_bottom_setting.setTextColor(getColor(R.color.main_sub_text_color))
    }

    fun selectBottomBtn(index: Int) {
        when(index) {
            BottomBtns.DIARY_LIST -> {
                btnClicked = BottomBtns.DIARY_LIST
                iv_bottom_list.setImageDrawable(getDrawable(R.drawable.bottom_button_list_on))
                tv_bottom_list.setTextColor(getColor(R.color.main_text_color))

                tv_title.text = getString(R.string.title_daily_diary_list)
                supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_right, 0).replace(R.id.vg_fragment_container, DiaryListFragment(applicationContext)).commitAllowingStateLoss()
            }
            BottomBtns.DIARY_POST -> {
                btnClicked = BottomBtns.DIARY_POST
                iv_bottom_post.setImageDrawable(getDrawable(R.drawable.bottom_button_post_on))
                tv_bottom_post.setTextColor(getColor(R.color.main_text_color))

                tv_title.text = getString(R.string.title_post_diary)
                supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_right, 0).replace(R.id.vg_fragment_container, PostDiaryFragment(applicationContext)).commitAllowingStateLoss()
            }
            BottomBtns.DIARY_MY -> {
                btnClicked = BottomBtns.DIARY_MY
                iv_bottom_my.setImageDrawable(getDrawable(R.drawable.bottom_button_my_on))
                tv_bottom_my.setTextColor(getColor(R.color.main_text_color))

                tv_title.text = getString(R.string.title_my_diary)
                supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_right, 0).replace(R.id.vg_fragment_container, MyDiaryFragment(applicationContext)).commitAllowingStateLoss()
            }
            BottomBtns.DIARY_SET -> {
                // SettingActivity 로 화면 전환
                val intent = Intent(this, SettingActivity::class.java)
                // 버튼 두 번 클릭시, 화면이 두 번 스택에 쌓이지 않도록 플래그 설정
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.putExtra("btnClicked", btnClicked)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, 0)
                finish()
            }
        }
    }
    
// TODO: 하트 클릭시, notifyChange 가 안 먹고 바로 갱신이 안돼서 fragment 자기 자신으로 이동시킨건데, 이동 없이 하는 방법 없을까?
    fun changeFragment(index: Int) {
        if (index == 1) {
            try {
                supportFragmentManager.beginTransaction().replace(R.id.vg_fragment_container, DiaryListFragment(applicationContext)).commit()
            } catch (e: Exception) {
                Log.d("changeFragmentError", ">>>>>>>>>>$e")
                // FragmentManager has not been attached to a host.
                // -> fragment 에서 onAttach 함수에서 이 액티비티를 가져와야 한다.
            }
        }
        if (index == 3) {
            supportFragmentManager.beginTransaction().replace(R.id.vg_fragment_container, MyDiaryFragment(applicationContext)).commit()
            setBottomInit()
            selectBottomBtn(BottomBtns.DIARY_MY)
        }
    }

    /**
     * PostDiaryFragment 에서 이미지뷰 클릭했을 때 앨범 혹은 카메라에서 이미지 가져와 이미지뷰에 세팅하는 함수
     */
    fun setImageOnPostDiaryFragment() {
        getImage() // RegisterActivity 에서 활용한 함수 그대로 가져온다.
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
        builder.setTitle("사진 등록")
        builder.setMessage("사연과 함께 사진을 기록으로 남겨보아요")

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
                var photoFile: File? = null
                photoFile = createImageFile()
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
        val storageDir =
            File(Environment.getExternalStorageDirectory().toString() + "/Pictures", "${Constants.appName}")
        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString())
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
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_STORAGE -> {
                    val uri = data?.data
                    try {
                        iv_post_image.setImageURI(uri)
                    } catch (e: Exception) {
                        Log.d("gallery Error", ">>>>>>>>>>$e")
                    }
                }
                REQUEST_CAMERA -> {
                    // val bitmap = data?.extras?.get("data") as Bitmap
                    // iv_post_image.setImageBitmap(bitmap)
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
        iv_post_image.setImageURI(contentUri)
    }

    /**
     * 포스팅 완료하고 화면 전환할 때 버튼 색상 바꾸기
     */
    fun changeColorAfterPosting() {
        setBottomInit()
        selectBottomBtn(BottomBtns.DIARY_LIST)
    }

    /**
     * DiaryList Fragment 에서 item 클릭시, 상세페이지로 이동
     */
    fun goDetailActivity(data: PostInfo) {
       val intent = Intent(this, DetailActivity::class.java)
        // 버튼 두 번 클릭시, 화면이 두 번 스택에 쌓이지 않도록 플래그 설정
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtra("DATA", data)
        intent.putExtra("btnClicked", btnClicked)
        startActivity(intent)
        overridePendingTransition(R.anim.act_up, 0)
        finish()
    }

    /**
     * 뒤로가기 버튼을 두 번 누르면 앱 종료
     */
    override fun onBackPressed() {
        if (System.currentTimeMillis() - waitTime >= 1500) {
            waitTime = System.currentTimeMillis()
            Toast.makeText(this, "뒤로가기 버튼을 한 번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
        } else {
            finish()
        }
    }

    /**
     * Activity 갔다가 BackPress 로 되돌아왔을 때
     */
    override fun onResume() {
        super.onResume()
        val flag: Int? = intent.getSerializableExtra("btnClicked") as Int?
        flag?.let {
            setBottomInit()
            selectBottomBtn(flag)
        }
        hideKeyboard()
    }

    fun hideKeyboard() {
        val manager: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.RESULT_HIDDEN)
    }
}