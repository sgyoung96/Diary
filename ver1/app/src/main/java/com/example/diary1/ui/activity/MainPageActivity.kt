package com.example.diary1.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.diary1.R
import com.example.diary1.ui.fragment.CalendarFragment
import com.example.diary1.ui.fragment.DiaryListFragment
import com.example.diary1.ui.fragment.PostDiaryFragment
import com.example.diary1.ui.fragment.TestFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main_page.*

class MainPageActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        bn_bottom_icons.setOnNavigationItemSelectedListener(this)

        tb_title.setTitle(R.string.title_daily_diary_list)
        supportFragmentManager.beginTransaction().add(R.id.vg_fragment_container, DiaryListFragment()).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bottom_item_list -> {
                supportFragmentManager.beginTransaction().replace(R.id.vg_fragment_container, DiaryListFragment()).commitAllowingStateLoss()
                tb_title.setTitle(R.string.title_daily_diary_list)
                return true
            }
            R.id.bottom_item_post -> {
                supportFragmentManager.beginTransaction().replace(R.id.vg_fragment_container, PostDiaryFragment()).commitAllowingStateLoss()
                tb_title.setTitle(R.string.title_post_diary)
                return true
            }
            R.id.bottom_item_calendar -> {
                supportFragmentManager.beginTransaction().replace(R.id.vg_fragment_container, CalendarFragment()).commitAllowingStateLoss()
                tb_title.setTitle(R.string.title_calendar)
                return true
            }
        }
        return false
    }
}