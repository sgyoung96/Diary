package com.example.diary1.ui.activity

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.diary1.R
import com.example.diary1.constants.RegisterInfo
import com.example.diary1.constants.SQLiteDBInfo
import com.example.diary1.constants.UserInfo
import com.example.diary1.datasave.SQLiteDBHelper
import com.example.diary1.datasave.query.RegisterQuery
import com.example.diary1.util.RegisterUtils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

/**
 * 타이틀바 없애기 :
 * values > themes> themes.xml 에서 속성 추가해줌
 * 상태바 색상 변경 : values > themes> themes.xml 에서
 * <!-- Status bar color. -->
 * <item name="android:statusBarColor" tools:targetApi="l">?attr/colorOnSecondary</item> 속성 변경
 */
class LoginActivity : AppCompatActivity() {

    /**
     * 뒤로가기 버튼 두 번 클릭 시 앱 종료때 사용할 변수
     */
    var waitTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 회원가입 폼으로 이동
        btn_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
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
                 * 1. DB에서 해당 ID 값에 해당하는 PW 찾아서 비교, 예외처리
                 * 2. UserInfo 에 name, id, pw 정보 각각 담기
                 * 3. 화면 전환
                 * 4. 스택에서 이 화면 없애기
                 */
                Log.d("ID 존재 여부", ">>>>>>>>>>ID 있음")

                val dbHelper = SQLiteDBHelper(this, SQLiteDBInfo.DB_NAME, null, 1)
                val database: SQLiteDatabase = dbHelper.readableDatabase
//                val checkPWQuery: String = RegisterQuery.checkOneRegister(et_id.text.toString())
                val result: Cursor = database.rawQuery(RegisterQuery.checkOneRegister(et_id.text.toString()), null)
                var comparePW: String
                while (result.moveToNext()) {
                    Log.d("SHOW ONE REGISTER INFO", ">>>>>>>>>>${result.getString(result.getColumnIndex(RegisterInfo.DB_COL_PW))}")

                    comparePW = result.getString(result.getColumnIndex(RegisterInfo.DB_COL_PW))

                    if (comparePW != et_pw.text.toString()) {
                        Log.d("비밀번호 일치 여부", ">>>>>>>>>>불일치")
                        Toast.makeText(this, "비밀번호를 확인해 주세요", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    UserInfo.userName = result.getString(result.getColumnIndex(RegisterInfo.DB_COL_NAME))
                    UserInfo.userID = result.getString(result.getColumnIndex(RegisterInfo.DB_COL_ID))
                    UserInfo.userPW = result.getString(result.getColumnIndex(RegisterInfo.DB_COL_PW))
                }
                Log.d("변수에 값 할당 확인", ">>>>>>>>>>name : ${UserInfo.userName}, id : ${UserInfo.userID}, pw : ${UserInfo.userPW}")

                //Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()

                // 화면 전환
                val intent = Intent(this, MainPageActivity::class.java)
                // 버튼 두 번 클릭시, 화면이 두 번 스택에 쌓이지 않도록 플래그 설정
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

                try {
                    startActivity(intent) // *********** 여기서 터짐
                    Log.d("INTENT", ">>>>>>>>>>SUCCESS")
                } catch (e: Exception) {
                    Log.d("INTENT FAIL", ">>>>>>>>>>$e")
                    // android.content.ActivityNotFoundException: Unable to find explicit activity class {com.example.diary1/com.example.diary1.ui.activity.MainPageActivity}; have you declared this activity in your AndroidManifest.xml?
                    // -> Manifest 에 없어서 수동으로 추가해 주었다.
                }

                // 화면 종료
                finish()
                Log.d("FINISH", ">>>>>>>>>>SUCCESS")
            }
        }
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
}