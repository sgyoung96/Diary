package com.example.diary1.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.fragment.app.Fragment
import com.example.diary1.R
import com.example.diary1.ui.fragment.*
import com.example.diary1.ui.fragment.listrecycler.DiaryListViewHolder
import com.example.diary1.ui.fragment.listrecycler.PostedDiaryInfo
import com.example.diary1.util.BottomBtns
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_post_diary.*
import java.lang.Exception

// TODO : Activity 띄울 때 overridePendingTransition(R.anim.act_up, 0) 함수로 애니메이션 추가해주기
// TODO : 앱 설치시 나타나는 제목 수정
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
    val REQUEST_CAMERA = 3
    val REQUEST_STORAGE = 4

    /**
     * 뒤로가기 버튼 두 번 클릭 시 앱 종료때 사용할 변수
     */
    var waitTime: Long = 0

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        /**
         * 하단 버튼 색상 초기화
         * 일기 목록이 메인 화면이므로 색상을 바꿔줌
         */
        setBottomInit()
        selectBottomBtn(BottomBtns.DIARY_LIST)

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
                supportFragmentManager.beginTransaction().replace(R.id.vg_fragment_container, DiaryListFragment()).commitAllowingStateLoss()
            }
            BottomBtns.DIARY_POST -> {
                btnClicked = BottomBtns.DIARY_POST
                iv_bottom_post.setImageDrawable(getDrawable(R.drawable.bottom_button_post_on))
                tv_bottom_post.setTextColor(getColor(R.color.main_text_color))

                tv_title.text = getString(R.string.title_post_diary)
                supportFragmentManager.beginTransaction().replace(R.id.vg_fragment_container, PostDiaryFragment()).commitAllowingStateLoss()
            }
            BottomBtns.DIARY_MY -> {
                btnClicked = BottomBtns.DIARY_MY
                iv_bottom_my.setImageDrawable(getDrawable(R.drawable.bottom_button_my_on))
                tv_bottom_my.setTextColor(getColor(R.color.main_text_color))

                tv_title.text = getString(R.string.title_my_diary)
                supportFragmentManager.beginTransaction().replace(R.id.vg_fragment_container, TestFragment()).commitAllowingStateLoss()
            }
            BottomBtns.DIARY_SET -> {
                // SettingActivity 로 화면 전환
                val intent = Intent(this, SettingActivity::class.java)
                // 버튼 두 번 클릭시, 화면이 두 번 스택에 쌓이지 않도록 플래그 설정
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.putExtra("btnClicked", btnClicked)
                startActivity(intent)
            }
        }
    }

    fun changeFragment(index: Int) {
        if (index == 1) {
            try {
                supportFragmentManager.beginTransaction().replace(R.id.vg_fragment_container, DiaryListFragment()).commit()
            } catch (e: Exception) {
                Log.d("changeFragmentError", ">>>>>>>>>>$e")
                // FragmentManager has not been attached to a host.
                // -> fragment 에서 onAttach 함수에서 이 액티비티를 가져와야 한다.
            }
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
            startActivityForResult(intent, REQUEST_CAMERA)
        }
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
                    val bitmap = data?.extras?.get("data") as Bitmap
                    iv_post_image.setImageBitmap(bitmap)
                }
            }
        }
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
    fun goDeatilActivity(data: PostedDiaryInfo) {
       val intent = Intent(this, DetailActivity::class.java)
        // 버튼 두 번 클릭시, 화면이 두 번 스택에 쌓이지 않도록 플래그 설정
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtra("DATA", data)
        startActivity(intent)
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
     * SettingActivity 갔다가 BackPress 로 되돌아왔을 때
     */
    override fun onResume() {
        super.onResume()
        val flag: Int? = intent.getSerializableExtra("btnClicked") as Int?
        flag?.let {
            setBottomInit()
            selectBottomBtn(flag)
        }
    }
}