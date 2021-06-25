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
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.fragment.app.Fragment
import com.example.diary1.R
import com.example.diary1.ui.fragment.*
import com.example.diary1.ui.fragment.listrecycler.DiaryListViewHolder
import com.example.diary1.ui.fragment.listrecycler.PostedDiaryInfo
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_post_diary.*
import java.lang.Exception

// TODO : 뒤로가기 버튼 두 번 눌러야 앱이 종료되도록. (한 번 누르면 토스트 띄우기)
// TODO : 앱 설치시 나타나는 제목 수정
// TODO : 다이어리 수정 기능 추가 (상세페이지에서)
// TODO : BottomNavigationView 에서 캘린더 아이콘 삭제 X -> Joda Time 라이브러리 사용하여 일기 쓴 날에 해당하여 표시 주기
// TODO : Calendar 리사이클러뷰 그리드로 그리기 + 뷰페이저
class MainPageActivity : AppCompatActivity() {

    /**
     * 하단 버튼 선택에 관한 플래그
     */
    var ischecked_list: Boolean = true
    var ischecked_per_day: Boolean = false
    var ischecked_post: Boolean = false
    var ischecked_calendar: Boolean = false

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

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        /**
         * 하단 버튼 색상 초기화
         * 일기 목록이 메인 화면이므로 색상을 바꿔줌
         */
        iv_bottom_list.setImageDrawable(getDrawable(R.drawable.bottom_button_list_on))
        tv_bottom_list.setTextColor(getColor(R.color.main_text_color))

        iv_bottom_per_day.setImageDrawable(getDrawable(R.drawable.bottom_button_per_day_off))
        tv_bottom_per_day.setTextColor(getColor(R.color.main_sub_text_color))

        iv_bottom_post.setImageDrawable(getDrawable(R.drawable.bottom_button_post_off))
        tv_bottom_post.setTextColor(getColor(R.color.main_sub_text_color))

        iv_bottom_calendar.setImageDrawable(getDrawable(R.drawable.bottom_button_calendar_off))
        tv_bottom_calendar.setTextColor(getColor(R.color.main_sub_text_color))

        tv_title.text = getString(R.string.title_daily_diary_list)

        supportFragmentManager.beginTransaction().add(R.id.vg_fragment_container, DiaryListFragment()).commit()

        /**
         * 하단 메뉴 버튼 이벤트
         * 1. 일기목록 2. 요일별 일기 3. 일기쓰기 4. 캘린더
         * 클릭시 아이콘, 텍스트 색상과 타이틀 바뀌고 프레그먼트 화면 전환
         */
        bottom_btn_list.setOnClickListener {
            ischecked_list = true
            ischecked_per_day = false
            ischecked_post = false
            ischecked_calendar = false
            if (ischecked_list) {
                iv_bottom_list.setImageDrawable(getDrawable(R.drawable.bottom_button_list_on))
                tv_bottom_list.setTextColor(getColor(R.color.main_text_color))

                iv_bottom_per_day.setImageDrawable(getDrawable(R.drawable.bottom_button_per_day_off))
                tv_bottom_per_day.setTextColor(getColor(R.color.main_sub_text_color))

                iv_bottom_post.setImageDrawable(getDrawable(R.drawable.bottom_button_post_off))
                tv_bottom_post.setTextColor(getColor(R.color.main_sub_text_color))

                iv_bottom_calendar.setImageDrawable(getDrawable(R.drawable.bottom_button_calendar_off))
                tv_bottom_calendar.setTextColor(getColor(R.color.main_sub_text_color))
            }

            tv_title.text = getString(R.string.title_daily_diary_list)
            supportFragmentManager.beginTransaction().replace(R.id.vg_fragment_container, DiaryListFragment()).commitAllowingStateLoss()
        }
        bottom_btn_per_day.setOnClickListener {
            ischecked_list = false
            ischecked_per_day = true
            ischecked_post = false
            ischecked_calendar = false
            if (ischecked_per_day) {
                iv_bottom_list.setImageDrawable(getDrawable(R.drawable.bottom_button_list_off))
                tv_bottom_list.setTextColor(getColor(R.color.main_sub_text_color))

                iv_bottom_per_day.setImageDrawable(getDrawable(R.drawable.bottom_button_per_day_on))
                tv_bottom_per_day.setTextColor(getColor(R.color.main_text_color))

                iv_bottom_post.setImageDrawable(getDrawable(R.drawable.bottom_button_post_off))
                tv_bottom_post.setTextColor(getColor(R.color.main_sub_text_color))

                iv_bottom_calendar.setImageDrawable(getDrawable(R.drawable.bottom_button_calendar_off))
                tv_bottom_calendar.setTextColor(getColor(R.color.main_sub_text_color))
            }

            tv_title.text = getString(R.string.title_diary_list_per_day)
            supportFragmentManager.beginTransaction().replace(R.id.vg_fragment_container, TestFragment()).commitAllowingStateLoss()
        }
        bottom_btn_post.setOnClickListener {
            ischecked_list = false
            ischecked_per_day = false
            ischecked_post = true
            ischecked_calendar = false
            if (ischecked_post) {
                iv_bottom_list.setImageDrawable(getDrawable(R.drawable.bottom_button_list_off))
                tv_bottom_list.setTextColor(getColor(R.color.main_sub_text_color))

                iv_bottom_per_day.setImageDrawable(getDrawable(R.drawable.bottom_button_per_day_off))
                tv_bottom_per_day.setTextColor(getColor(R.color.main_sub_text_color))

                iv_bottom_post.setImageDrawable(getDrawable(R.drawable.bottom_button_post_on))
                tv_bottom_post.setTextColor(getColor(R.color.main_text_color))

                iv_bottom_calendar.setImageDrawable(getDrawable(R.drawable.bottom_button_calendar_off))
                tv_bottom_calendar.setTextColor(getColor(R.color.main_sub_text_color))
            }

            tv_title.text = getString(R.string.title_post_diary)
            supportFragmentManager.beginTransaction().replace(R.id.vg_fragment_container, PostDiaryFragment()).commitAllowingStateLoss()
        }
        bottom_btn_calendar.setOnClickListener {
            ischecked_list = false
            ischecked_per_day = false
            ischecked_post = false
            ischecked_calendar = true
            if (ischecked_calendar) {
                iv_bottom_list.setImageDrawable(getDrawable(R.drawable.bottom_button_list_off))
                tv_bottom_list.setTextColor(getColor(R.color.main_sub_text_color))

                iv_bottom_per_day.setImageDrawable(getDrawable(R.drawable.bottom_button_per_day_off))
                tv_bottom_per_day.setTextColor(getColor(R.color.main_sub_text_color))

                iv_bottom_post.setImageDrawable(getDrawable(R.drawable.bottom_button_post_off))
                tv_bottom_post.setTextColor(getColor(R.color.main_sub_text_color))

                iv_bottom_calendar.setImageDrawable(getDrawable(R.drawable.bottom_button_calendar_on))
                tv_bottom_calendar.setTextColor(getColor(R.color.main_text_color))
            }

            tv_title.text = getString(R.string.title_calendar)
            supportFragmentManager.beginTransaction().replace(R.id.vg_fragment_container, CalendarFragment()).commitAllowingStateLoss()
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
        var builder = AlertDialog.Builder(this)
        builder.setTitle("사진 등록")
        builder.setMessage("사연과 함께 사진을 기록으로 남겨보아요")

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
        iv_bottom_list.setImageDrawable(getDrawable(R.drawable.bottom_button_list_on))
        tv_bottom_list.setTextColor(getColor(R.color.main_text_color))

        iv_bottom_per_day.setImageDrawable(getDrawable(R.drawable.bottom_button_per_day_off))
        tv_bottom_per_day.setTextColor(getColor(R.color.main_sub_text_color))

        iv_bottom_post.setImageDrawable(getDrawable(R.drawable.bottom_button_post_off))
        tv_bottom_post.setTextColor(getColor(R.color.main_sub_text_color))

        iv_bottom_calendar.setImageDrawable(getDrawable(R.drawable.bottom_button_calendar_off))
        tv_bottom_calendar.setTextColor(getColor(R.color.main_sub_text_color))
    }

    /**
     * DiaryList Fragment 에서 item 클릭시, 상세페이지로 이동
     * 하단 버튼 색상 바꾸기
     */
    fun goDetailFragment(data: PostedDiaryInfo) {
        supportFragmentManager.beginTransaction().replace(R.id.vg_fragment_container, DetailFragment(data)).commit()

        iv_bottom_list.setImageDrawable(getDrawable(R.drawable.bottom_button_list_off))
        tv_bottom_list.setTextColor(getColor(R.color.main_sub_text_color))

        iv_bottom_per_day.setImageDrawable(getDrawable(R.drawable.bottom_button_per_day_off))
        tv_bottom_per_day.setTextColor(getColor(R.color.main_sub_text_color))

        iv_bottom_post.setImageDrawable(getDrawable(R.drawable.bottom_button_post_off))
        tv_bottom_post.setTextColor(getColor(R.color.main_sub_text_color))

        iv_bottom_calendar.setImageDrawable(getDrawable(R.drawable.bottom_button_calendar_off))
        tv_bottom_calendar.setTextColor(getColor(R.color.main_sub_text_color))
    }
}