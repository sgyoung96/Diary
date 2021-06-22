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
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.fragment.app.Fragment
import com.example.diary1.R
import com.example.diary1.ui.fragment.CalendarFragment
import com.example.diary1.ui.fragment.DiaryListFragment
import com.example.diary1.ui.fragment.PostDiaryFragment
import com.example.diary1.ui.fragment.TestFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_post_diary.*
import java.lang.Exception

// TODO : BottomNavigationView 아이콘과 텍스트 선택하면 색상 바뀌도록
// TODO : BottomNavigationView 에서 캘린더 아이콘 삭제 X -> Joda Time 라이브러리 사용하여 일기 쓴 날에 해당하여 표시 주기
// TODO : BottomNavigationView 에서 맨 오른쪽에 프로필 수정 기능 추가하기
// TODO : Calendar 리사이클러뷰 그리드로 그리기 + 뷰페이저
// TODO : BottomNavigationView 를 일반 View 들로 그려서 Selected = true 이런거 설정 주는 거로 수정
class MainPageActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        tv_title.text = getString(R.string.title_daily_diary_list)
        bn_bottom_icons.itemIconTintList = null
        bn_bottom_icons.setOnNavigationItemSelectedListener(this)

        //bn_bottom_icons.itemIconTintList = getColorStateList(R.color.main_highlight)
        //bn_bottom_icons.itemTextColor = getColorStateList(R.color.main_highlight)

        bn_bottom_icons.itemBackground = getDrawable(R.drawable.bottom_navigation_menu_selector_color)

        supportFragmentManager.beginTransaction().add(R.id.vg_fragment_container, DiaryListFragment()).commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bottom_item_list -> {
                tv_title.text = getString(R.string.title_daily_diary_list)
                supportFragmentManager.beginTransaction().replace(R.id.vg_fragment_container, DiaryListFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.bottom_item_post -> {
                tv_title.text = getString(R.string.title_post_diary)
                supportFragmentManager.beginTransaction().replace(R.id.vg_fragment_container, PostDiaryFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.bottom_item_daily -> {
                tv_title.text = getString(R.string.title_diary_list_per_day)
                supportFragmentManager.beginTransaction().replace(R.id.vg_fragment_container, TestFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.bottom_item_calendar -> {
                tv_title.text = getString(R.string.title_calendar)
                supportFragmentManager.beginTransaction().replace(R.id.vg_fragment_container, CalendarFragment()).commitAllowingStateLoss()
                return true
            }
        }
        return false // true 로 부장님이 바꿔주셨음
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
}