package com.example.diary1.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.transition.Visibility
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.solver.widgets.ConstraintWidget.VISIBLE
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.example.diary1.R
import com.example.diary1.constants.PostDiaryInfo
import com.example.diary1.constants.RegisterInfo
import com.example.diary1.constants.SQLiteDBInfo
import com.example.diary1.constants.UserInfo
import com.example.diary1.datasave.SQLiteDBHelper
import com.example.diary1.datasave.query.PostDiaryQuery
import com.example.diary1.ui.activity.LoginActivity
import com.example.diary1.ui.activity.MainPageActivity
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_post_diary.*
import kotlinx.coroutines.MainScope
import org.w3c.dom.Text
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * 1. 카메라와 앨범으로부터 이미지 넘겨 받아와 이미지뷰에 세팅
 * 2. 날짜 선택 버튼 클릭시, 캘린더를 통해서 날짜 정보 받아와 텍스트뷰에 세팅 (연월일, 요일)
 * 3. 제목, 날짜, 내용 문자열 로컬DB에 저장 (SQLite) - 저장할 키값은 아이디
 */

// TODO : 로컬에 이미지 저장
class PostDiaryFragment : Fragment() {

    /**
     * 화면전환할 때 사용할 변수 (onAttach 함수에서 초기화해준다.)
     */
    var mainPageActivity: MainPageActivity? = null

    /**
     * CalendarView 의 날짜 속성 가져올 때 사용할 변수들
     * day : 한글 요일로 떨어짐
     */
    var year: Int? = null
    var month: Int? = null
    var date: Int? = null
    var day: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_post_diary, container, false)
        val calendar = view.findViewById<CalendarView>(R.id.cv_post_calendar)
        calendar.visibility = View.GONE
        tv_post_main_text.text = UserInfo.userName + "님, ${getString(R.string.post_main_text)}"

        /**
         * 이미지뷰 클릭시 앨범 혹은 카메라로부터 이미지 가져와서 이미지 세팅
         */
        view.findViewById<ImageView>(R.id.iv_post_image).setOnClickListener {
            // MainPageActivity 에 카메라 오픈 관련 함수 만들어서, 그쪽에서 이미지 처리될 수 있도록 하기
            mainPageActivity?.setImageOnPostDiaryFragment()
        }

        /**
         * 날짜 선택 버튼 클릭 시, 캘린더가 띄워지면서, 날짜 선택 시, 연월일/요일 날짜정보가 텍스트뷰에 박힘
         * 1. 캘린더뷰를 띄운다.
         * 2. 빈 공간 터치시 캘린더 뷰 GONE 처리 한다.
         * 3. 달력에서 날짜 선택 시 캘린더 뷰 GONE 처리 -> 텍스트뷰에 날짜 박힌다.
         */
        view.findViewById<TextView>(R.id.tv_select_date_click).setOnClickListener {
            calendar.visibility = View.VISIBLE
            Log.d("calendar", ">>>>>>>>>>클릭했어요")

            view.setOnClickListener {
                calendar.visibility = View.GONE
            }

            /**
             * setInChangeListener 에 인자로 사용할 변수들
             * calendarView: CalendarView, year: Int, month: Int, dayOfMonth: Int
             */
            calendar.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
                Log.d("선택했어요", ">>>>>>>>>>날짜 선택했어요")
                // 연월일 세팅
                this.year = year
                this.month = month // this.month 는 1달 이전꺼로 출력됨
                this.date = dayOfMonth
                Log.d("year, month, date", ">>>>>>>>>>${this.year}, ${this.month}, ${this.date}")

                // 요일 구하기
                val todayCalendar = Calendar.getInstance()
                todayCalendar.set(this.year!!, this.month!!, this.date!!)
                val todayDate = todayCalendar.time // time의 월은 정확한 월로 출력됨
                Log.d("todayDate", ">>>>>>>>>>$todayDate")
                val dateformat = SimpleDateFormat("EEEE", Locale.getDefault())
                this.day = dateformat.format(todayDate)
                Log.d("day", ">>>>>>>>>>${this.day}")

                // 년, 월, 일, 요일 텍스트뷰에 세팅
                view.findViewById<TextView>(R.id.tv_select_date_text).text = "${this.year}-${this.month!! + 1}-${this.date} ${this.day}"

                calendar.visibility = View.GONE
            }
        }

        /**
         * 기록하기 버튼 클릭 시, 날짜/제목/내용이 SQLite 통해서 로컬에 저장됨
         */
        view.findViewById<Button>(R.id.btn_post_submit).setOnClickListener {
            /**
             * 예외처리 :
             * 1. 날짜 지정 안 한 것 체크
             * 2. 날짜 중복 체크 - 해당 날짜에 이미 정보가 있으면 return, Toast 띄우기
             * 3. 제목 공란 체크 (글자 수 제한은 xml 에서 처리함)
             * 4. 내용 공란 체크 (글자 수 제한은 xml 에서 처리함)
             */
            // 날짜 지정 안 한 것 체크
            if (view.findViewById<TextView>(R.id.tv_select_date_text).text.toString() == "${getString(R.string.post_tv_date_time)}") {
                Toast.makeText(context, "날짜를 지정해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 날짜 중복 체크 - SQLite 사용
            // dbHelper 초기화 (내가 생성하고 override 한 메소드가 있는 클래스)
            val dbHelper = SQLiteDBHelper(context, SQLiteDBInfo.DB_NAME, null, 1)
            // Insert 모드로 데이터 저장소 가져옴
            var database: SQLiteDatabase = dbHelper.readableDatabase
            var sqlQuery: String = PostDiaryQuery.checkDiary(UserInfo.userID, view.findViewById<TextView>(R.id.tv_select_date_text).text.toString())
            var result: Cursor
            result = database.rawQuery(sqlQuery, null)
            while (result.moveToNext()) {
                Toast.makeText(context, "해당 날짜에 이미 일기가 있어요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            // 제목 공란 체크
            if (view.findViewById<EditText>(R.id.et_input_title).text.isNullOrEmpty()) {
                Toast.makeText(context, "제목을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 내용 공란 체크
            if (view.findViewById<EditText>(R.id.et_input_content).text.isNullOrEmpty()) {
                Toast.makeText(context, "내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            /**
             * SQLite 저장 처리
             * 1. 데이터 삽입
             * 2. 제대로 삽입되었는지 확인
             * 3. 처리가 끝난 후에는 닫아주기
             */
            // Toast.makeText(context, "통과", Toast.LENGTH_SHORT).show()
            // 1. 데이터 삽입
            database = dbHelper.writableDatabase
            sqlQuery = PostDiaryQuery.insertDiary(UserInfo.userID,
            view.findViewById<TextView>(R.id.tv_select_date_text).text.toString(),
            view.findViewById<EditText>(R.id.et_input_title).text.toString(),
            view.findViewById<EditText>(R.id.et_input_content).text.toString())

            database.execSQL(sqlQuery)

            // 2. 제대로 삽입외었는지 확인
            database = dbHelper.readableDatabase
            sqlQuery = PostDiaryQuery.checkDiary(UserInfo.userID,
            view.findViewById<TextView>(R.id.tv_select_date_text).text.toString())

            result = database.rawQuery(sqlQuery, null)
            while (result.moveToNext()) {
                Log.d("저장 정보 확인, id", ">>>>>>>>>>${result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_USERID))}")
                Log.d("저장 정보 확인, date", ">>>>>>>>>>${result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_DATE))}")
                Log.d("저장 정보 확인, title", ">>>>>>>>>>${result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_TITLE))}")
                Log.d("저장 정보 확인, content", ">>>>>>>>>>${result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_CONTENT))}")
            }

            database.close()

            // 마무리 : 다이어로그 띄우고 화면 전환
            finishPosting()
        }

        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_post_diary, container, false)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainPageActivity = activity as MainPageActivity
    }

    /**
     * dialog 띄우고 DiaryListFragment 로 화면 바꾸기
     */
    private fun finishPosting() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("기록")
        builder.setMessage("오늘의 사연이 기록되었어요")

        val listener = DialogInterface.OnClickListener { _, a ->
            when (a) {
                DialogInterface.BUTTON_POSITIVE -> {
                    // DiaryListFragment 로 이동
                    mainPageActivity?.changeFragment(1)
                }
            }
        }
        builder.setPositiveButton("확인", listener)

        builder.show()
    }

}