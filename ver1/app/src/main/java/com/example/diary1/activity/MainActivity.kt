package com.example.diary1.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.diary1.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 타이틀바 없애기 :
 * values > themes> themes.xml 에서 속성 추가해줌
 * 상태바 색상 변경 : values > themes> themes.xml 에서
 * <!-- Status bar color. -->
 * <item name="android:statusBarColor" tools:targetApi="l">?attr/colorOnSecondary</item> 속성 변경
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 회원가입 폼으로 이동
        btn_register.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            // 버튼 두 번 클릭시, 화면이 두 번 스택에 쌓이지 않도록 플래그 설정
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

        btn_login.setOnClickListener {

        }
    }
}