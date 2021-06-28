package com.example.diary1.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.diary1.R

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
    }

    /**
     * 뒤로가기 버튼 누르면 이 화면 종료 (MainPageActivity 로 이동)
     */
    override fun onBackPressed() {
        finish()
    }
}