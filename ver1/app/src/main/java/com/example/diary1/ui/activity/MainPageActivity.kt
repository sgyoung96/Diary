package com.example.diary1.ui.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.example.diary1.R
import com.example.diary1.ui.fragment.CalendarFragment
import com.example.diary1.ui.fragment.DiaryListFragment
import com.example.diary1.ui.fragment.PostDiaryFragment
import com.example.diary1.ui.fragment.TestFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main_page.*

/**
 * 해야할 일
 * Toolbar kotlin 예제 책 보고 한 대로 만들어서 타이틀 바꾸기
 * BottomNavigationView 아이콘과 텍스트 선택하면 색상 바뀌도록
 */
class MainPageActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        // 왜 안바껴....
        try {
            tb_title.title = "${R.string.title_daily_diary_list}"
        } catch (e: Exception) {
            Log.d("TITLE SETTING FAIL", ">>>>>>>>>>$e")
        }

        bn_bottom_icons.setOnNavigationItemSelectedListener(this)

        bn_bottom_icons.itemIconTintList = getColorStateList(R.color.main_highlight)
        bn_bottom_icons.itemTextColor = getColorStateList(R.color.main_highlight)


        supportFragmentManager.beginTransaction().add(R.id.vg_fragment_container, DiaryListFragment()).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bottom_item_list -> {
                if (bn_bottom_icons.isSelected) {
                    bn_bottom_icons.itemIconTintList = getColorStateList(R.color.input_highlight)
                    bn_bottom_icons.itemTextColor = getColorStateList(R.color.input_highlight)
                } else {
                    bn_bottom_icons.itemIconTintList = getColorStateList(R.color.main_highlight)
                    bn_bottom_icons.itemTextColor = getColorStateList(R.color.main_highlight)
                }
                tb_title.setTitle(R.string.title_daily_diary_list)
                supportFragmentManager.beginTransaction().replace(R.id.vg_fragment_container, DiaryListFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.bottom_item_post -> {
                tb_title.setTitle(R.string.title_post_diary)
                supportFragmentManager.beginTransaction().replace(R.id.vg_fragment_container, PostDiaryFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.bottom_item_calendar -> {
                tb_title.setTitle(R.string.title_calendar)
                supportFragmentManager.beginTransaction().replace(R.id.vg_fragment_container, CalendarFragment()).commitAllowingStateLoss()
                return true
            }
        }
        return false
    }
}