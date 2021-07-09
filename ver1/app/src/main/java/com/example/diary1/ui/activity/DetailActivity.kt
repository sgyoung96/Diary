package com.example.diary1.ui.activity

import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Visibility
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.diary1.R
import com.example.diary1.constants.SQLiteDBInfo
import com.example.diary1.constants.UserInfo
import com.example.diary1.datasave.SQLiteDBHelper
import com.example.diary1.datasave.query.DetailDiaryQuery
import com.example.diary1.datasave.query.PostDiaryQuery
import com.example.diary1.ui.fragment.listrecycler.PostedDiaryInfo
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_post_diary.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

// TODO : 날짜 제어 -> '일'도 한자리면 앞에 0 추가하기
// TODO : 수정 모드 아닐 때, 텍스트뷰 클릭 되는 문제 잡기 (플래그로...?)
// TODO : 이미지뷰 클릭시, 카메라와 앨범으로부터 이미지 가져오기
class DetailActivity() : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    var itemData: PostedDiaryInfo? = null

    // 수정 모드 플래그
    var isEditMode = false

    var dbHelper: SQLiteDBHelper? = null
    var database: SQLiteDatabase? = null
    var sqlQuery: String? = null
    var result: Cursor? = null

    var year: Int = 0           // 년
    var month: String = ""      // 월
    var date: Int = 0           // 일
    var day: String = ""        // 요일

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        dbHelper = SQLiteDBHelper(this, SQLiteDBInfo.DB_NAME, null, 1)
        database = dbHelper?.readableDatabase

        // 데이터 바인딩
        itemData = intent.getSerializableExtra("DATA") as PostedDiaryInfo
        et_detail_title.setText(itemData?.postTitle)
        tv_detail_date.text = itemData?.postDate
        et_detail_content.setText(itemData?.postContent)
        // 원래 날짜 값
        val originalDate = itemData?.postDate

        // 수정 off 모드
        setCloseEditMode()

        // CalendarView GONE 처리
        cv_detail_calendar.visibility = View.GONE

        /**
         * 날짜 텍스트뷰 클릭시 캘린더뷰 visible, 선택 날짜 textview 에 할당하기
         */
        tv_detail_date.setOnClickListener {
            cv_detail_calendar.visibility = View.VISIBLE

            // 다른 곳 클릭 시 달력 사라짐
            view.setOnClickListener {
                cv_detail_calendar.visibility = View.GONE
            }

            /**
             * setOnChangeListener 에 인자로 사용할 변수들
             * calendarView: CalendarView, year: Int, month: Int, dayOfMonth: Int
             */
            cv_detail_calendar.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
                Log.d("선택했어요", ">>>>>>>>>>날짜 선택했어요")
                // 연월일 세팅
                this.year = year
                // this.month 는 1달 이전꺼로 출력됨
                // 월이 한자리 수이면 앞에 0 붙임
                val monthPlusOne = month + 1
                if (monthPlusOne.toString().length == 1) {
                    this.month = "0$monthPlusOne"
                } else {
                    this.month = "$monthPlusOne"
                }

                this.date = dayOfMonth
                Log.d("year, month, date", ">>>>>>>>>>${this.year}, ${this.month}, ${this.date}")

                // 요일 구하기
                val todayCalendar = Calendar.getInstance()
                todayCalendar.set(year, month, dayOfMonth)
                val todayDate = todayCalendar.time // time의 월은 정확한 월로 출력됨
                Log.d("todayDate", ">>>>>>>>>>$todayDate")
                val dateformat = SimpleDateFormat("EEEE", Locale.getDefault())
                this.day = dateformat.format(todayDate)
                Log.d("day", ">>>>>>>>>>${this.day}")

                // 년, 월, 일, 요일 텍스트뷰에 세팅
                tv_detail_date.text = "${this.year}-${this.month!!}-${this.date} ${this.day}"

                cv_detail_calendar.visibility = View.GONE
            }
        }

        /**
         * 수정 버튼 클릭 시,
         * 1. 데이터 저장
         * 2. 수정 off 모드
         */
        btn_detail_complete.setOnClickListener {
            /**
             * db 데이터 처리 (try ~ catch, execption 시, toast 띄우기)
             * 1. 공백 예외처리
             * 2. 날짜 예외처리
             * 3. 데이터 삽입
             */
            if (et_detail_title.text.toString().replace(" ", "") == "") {
                return@setOnClickListener
            }

            if (et_detail_content.text.toString().replace(" ", "") == "") {
                return@setOnClickListener
            }

            /**
             * [날짜 중복 체크]
             * 날짜를 변경하지 않았을 시 -> 중복 예외 걸지 않고 넘어감
             * 날짜를 변경했을 시 -> 중복 체크함!
             * 원래 날짜 : originalDate
             */
            if (tv_detail_date.text.toString() != originalDate) { // 날짜를 변경했을 시
                try {
                    sqlQuery = PostDiaryQuery.checkDiary(UserInfo.userID, tv_detail_date.text.toString())
                    result = database?.rawQuery(sqlQuery, null)
                    if (result!!.moveToNext()) {
                        Toast.makeText(this, "해당 날짜에 이미 일기가 있어요", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                } catch (e: Exception) {
                    Log.d("check diary exception", ">>>>>>>>>>$e")
                    Toast.makeText(this, "일기를 확인 중에 오류가 발생했어요", Toast.LENGTH_SHORT).show()
                }
            }

            // 데이터 삽입
            try {
                database = dbHelper?.writableDatabase
                sqlQuery = DetailDiaryQuery.saveDiary(UserInfo.userID, et_detail_title.text.toString(), tv_detail_date.text.toString(), et_detail_content.text.toString(), originalDate!!)
                database?.execSQL(sqlQuery)
            } catch (e: Exception) {
                Log.d("update diary exception", ">>>>>>>>>>$e")
                Toast.makeText(this, "일기를 저장 중에 오류가 발생했어요", Toast.LENGTH_SHORT).show()
            }

            // 수정 off 모드
            setCloseEditMode()

            // db 연결 닫기
            database?.close()

            isEditMode = false
        }
    }

    fun showPopup(view: View) {
        PopupMenu(this, view).apply {
            setOnMenuItemClickListener(this@DetailActivity)
            inflate(R.menu.item_detail)
            show()
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_detail_edit -> {
                isEditMode = true
                setEditMode()
                true
            }
            R.id.menu_detail_delete -> {
                deleteData()
                true
            }
            else -> false
        }
    }

    /**
     * 메뉴에서 수정 메뉴 클릭했을 시, 수정 모드
     * 1. EditText(title, content) 포커스 넣기, 수정 가능하도록
     * 2. TextView(date), ImageView 클릭 이벤트 발생 하도록
     * 3. 수정 버튼 보이기
     */
    fun setEditMode() {
        // 수정 모드 기본 세팅
        et_detail_title.isEnabled = true
        tv_detail_date.isEnabled = true
        iv_detail_image.isEnabled = true
        et_detail_content.isEnabled = true

        btn_detail_complete.visibility = View.VISIBLE
    }

    /**
     * 1. 정말 삭제하겠느냐는 다이어로그 띄우고, 예 누르면 삭제.
     * 2. 삭제 처리가 끝나면 삭제가 완료되었다는 다이어로그 띄우고, 예 누르면 화면 전환
     */
    fun deleteData() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("일기장 삭제")
        builder.setMessage("정말로 일기를 삭제하시겠습니까?")

        val listener = DialogInterface.OnClickListener { _, a ->
            when (a) {
                DialogInterface.BUTTON_NEUTRAL -> {
                    database = dbHelper?.writableDatabase
                    sqlQuery = DetailDiaryQuery.deleteQuery(UserInfo.userID, et_detail_title.text.toString(), tv_detail_date.text.toString())
                    database?.execSQL(sqlQuery)
                    database?.close()

                    // 삭제가 완료 되었습니다 다이어로그
                    builder.setTitle("삭제 완료")
                    builder.setMessage("삭제가 완료되었습니다")

                    val listener = DialogInterface.OnClickListener { _, a ->
                        when (a) {
                            DialogInterface.BUTTON_NEUTRAL -> {
                                startActivity(Intent(this, MainPageActivity::class.java))
                                finish()
                            }
                            DialogInterface.BUTTON_POSITIVE -> {
                                startActivity(Intent(this, MainPageActivity::class.java))
                                finish()
                            }
                        }
                    }

                    builder.setNeutralButton("    ", listener)
                    builder.setPositiveButton("확인", listener)
                    builder.show()
                }
                DialogInterface.BUTTON_POSITIVE -> {
                    return@OnClickListener
                }
            }
        }
        builder.setNeutralButton("예", listener)
        builder.setPositiveButton("아니오", listener)

        builder.show()


    }

    /**
     * 수정 off 모드
     * 1. EditText(title, content) 포커스 없애기, 수정 못하도록
     * 2. TextView(date), ImageView 클릭 이벤트 발생 안 하도록
     * 3. 수정 버튼 가리기
     * 4. 초기 상태로 돌아가기
     */
    fun setCloseEditMode() {
        et_detail_title.isEnabled = false
        tv_detail_date.isEnabled = false
        iv_detail_image.isEnabled = false
        et_detail_content.isEnabled = false

        cv_detail_calendar.visibility = View.GONE
        btn_detail_complete.visibility = View.GONE

        isEditMode = false
    }

    /**
     * 뒤로가기 버튼 클릭 시,
     * 1. 만약 수정 모드일 때, 수정 모드 종료, 초기 상태로 데이터 돌려놓기
     * 2. 수정 모드가 아닐 때, 화면 종료
     */
    override fun onBackPressed() {
        if (isEditMode) {
            setCloseEditMode()

            et_detail_title.setText(itemData?.postTitle)
            tv_detail_date.text = itemData?.postDate
            et_detail_content.setText(itemData?.postContent)

            isEditMode = false
        } else {
            finish()
        }
    }
}