package com.example.diary1.ui.fragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.diary1.R
import com.example.diary1.datasave.constants.PostDiaryInfo
import com.example.diary1.constants.Constants
import com.example.diary1.constants.util.Utils
import com.example.diary1.datasave.database.MyDirayDB
import com.example.diary1.datasave.entity.PostInfo
import com.example.diary1.ui.activity.MainPageActivity
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_post_diary.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * 1. 카메라와 앨범으로부터 이미지 넘겨 받아와 이미지뷰에 세팅
 * 2. 날짜 선택 버튼 클릭시, 캘린더를 통해서 날짜 정보 받아와 텍스트뷰에 세팅 (연월일, 요일)
 * 3. 제목, 날짜, 내용 문자열 로컬DB에 저장 (SQLite) - 저장할 키값은 아이디
 */

class PostDiaryFragment(context: Context) : Fragment() {

    var applicationContext = context

    /**
     * 화면전환할 때 사용할 변수 (onAttach 함수에서 초기화해준다.)
     */
    var mainPageActivity: MainPageActivity? = null

    /**
     * CalendarView 의 날짜 속성 가져올 때 사용할 변수들
     * day : 한글 요일로 떨어짐
     */
    var year: Int? = null
    var month: String? = null
    var date: String? = null
    var day: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_diary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * DB 에서 id 값으로 이름 가져오기
         */
        val db = MyDirayDB.getInstance(applicationContext)
        val userSetting = db!!.userDao().checkOneRegister(Constants.userID)
        var name = ""
        for (userInfo in userSetting!!) {
            name = userInfo.userName
        }

        cv_post_calendar.visibility = View.GONE
        tv_post_main_text.text = name + "님, " + getString(R.string.post_main_text)

        /**
         * 이미지뷰 클릭시 앨범 혹은 카메라로부터 이미지 가져와서 이미지 세팅
         */
        iv_post_image.setOnClickListener {
            // MainPageActivity 에 카메라 오픈 관련 함수 만들어서, 그쪽에서 이미지 처리될 수 있도록 하기
            mainPageActivity?.setImageOnPostDiaryFragment()
        }

        /**
         * 날짜 선택 버튼 클릭 시, 캘린더가 띄워지면서, 날짜 선택 시, 연월일/요일 날짜정보가 텍스트뷰에 박힘
         * 1. 캘린더뷰를 띄운다.
         * 2. 빈 공간 터치시 캘린더 뷰 GONE 처리 한다.
         * 3. 달력에서 날짜 선택 시 캘린더 뷰 GONE 처리 -> 텍스트뷰에 날짜 박힌다.
         */
        tv_select_date_text.setOnClickListener {
            cv_post_calendar.visibility = View.VISIBLE
            Log.d("calendar", ">>>>>>>>>>클릭했어요")

            // 오픈되어 있을 때는 다른 뷰 클릭 이벤트 x
            iv_post_image.isEnabled = false
            et_input_content.isEnabled = false
            btn_post_submit.isEnabled = false

            /**
             * setOnChangeListener 에 인자로 사용할 변수들
             * calendarView: CalendarView, year: Int, month: Int, dayOfMonth: Int
             */
            cv_post_calendar.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
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

                // 일이 한자리 수면 앞에 0 붙임
                if (dayOfMonth.toString().length == 1) {
                    this.date = "0$dayOfMonth"
                } else {
                    this.date = dayOfMonth.toString()
                }

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
                tv_select_date_text.text = "${this.year}-${this.month!!}-${this.date} ${this.day}"

                // 다른 뷰 클릭 이벤트 다시 가져오기
                iv_post_image.isEnabled = true
                et_input_content.isEnabled = true
                btn_post_submit.isEnabled = true

                cv_post_calendar.visibility = View.GONE
            }
        }

        /**
         * 기록하기 버튼 클릭 시, 날짜/제목/내용이 SQLite 통해서 로컬에 저장됨
         */
        btn_post_submit.setOnClickListener {
            /**
             * 예외처리 :
             * 1. 날짜 지정 안 한 것 체크
             * 2. 날짜 중복 체크 - 해당 날짜에 이미 정보가 있으면 return, Toast 띄우기
             * 3. 제목 공란 체크 (글자 수 제한은 xml 에서 처리함)
             * 4. 내용 공란 체크 (글자 수 제한은 xml 에서 처리함)
             * 5. 이미지 지정 안 한 것 체크
             */
            // 날짜 지정 안 한 것 체크
            if (tv_select_date_text.text.toString() == "${getString(R.string.post_tv_date_time)}") {
                Toast.makeText(context, "날짜를 지정해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 날짜 중복 체크
            val checkDate = db.PostDao().checkDiary(Constants.userID, tv_select_date_text.text.toString())
            var date = ""
            for (getDate in checkDate) {
                date = getDate.post_date
            }
            if (!date.isEmpty()) {
                Toast.makeText(context, "해당 날짜에 이미 일기가 있어요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 제목 공란 체크
            if (et_input_title.text.isNullOrEmpty()) {
                Toast.makeText(context, "제목을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 내용 공란 체크
            if (et_input_content.text.isNullOrEmpty()) {
                Toast.makeText(context, "내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (Utils.checkDefaultPosting(requireContext(), iv_post_image.drawable, iv_post_image.width, iv_post_image.height)) {
                Toast.makeText(requireContext(), "사진을 등록해 주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
1
            val image = Utils.resizeImage(iv_post_image.drawable, iv_post_image.width, iv_post_image.height)
            CoroutineScope(Dispatchers.IO).launch {
                db!!.PostDao().insertDiary(PostInfo(Constants.userID,
                                                    tv_select_date_text.text.toString(),
                                                    et_input_title.text.toString(),
                                                    et_input_content.text.toString(),
                                                    PostDiaryInfo.POST_MY_DEFAULT,
                                                    image))
            }

            // 마무리 : 다이어로그 띄우고 화면 전환
            finishPosting()
        }

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
                    mainPageActivity?.changeColorAfterPosting()
                }
            }
        }
        builder.setPositiveButton("확인", listener)

        builder.show()
    }
}