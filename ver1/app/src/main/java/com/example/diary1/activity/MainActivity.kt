package com.example.diary1.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.diary1.R
import com.example.diary1.util.RegisterUtils
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

        /**
         * 1. DB 에서 ID 값 찾아서 SELECT
         * 2. 정보가 존재한다면, 성공 로그 찍고 화면 전환
         * 3. 정보가 존재하지 않는다면, 실패 로그 찍고, Toast.show()
         */
        btn_login.setOnClickListener {
            if (!RegisterUtils.checkMember(this, et_id.text.toString())) {
                Log.d("checkMember()", ">>>>>>>>>>회원정보 없음")
                Toast.makeText(this, "회원가입을 먼저 해주세요", Toast.LENGTH_SHORT).show()
            } else {
                /**
                 * 1. DB에서 일치하는 정보를 찾으면,
                 * 2. UserInfo 에 name, id, pw 정보 각각 담기
                 * 3. 화면 전환
                 * 4. 스택에서 이 화면 없애기
                 */
                Log.d("로그인 여부", ">>>>>>>>>>로그인 성공")
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
/*
[물어볼 것]
if 문에 return 안 해도 되는지
RegisterActivity > 변수 선언 많아도 되는지 (같은 이름으로 지역 변수들 선언함)
else 문 안에서 예외처리 뿐만 아니라 모든 작업들 다 해도 되는지 (if 문 사용법)
 */