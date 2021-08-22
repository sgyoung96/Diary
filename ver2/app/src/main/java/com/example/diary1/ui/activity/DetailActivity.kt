package com.example.diary1.ui.activity

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.diary1.R
import com.example.diary1.constants.Constants
import com.example.diary1.constants.util.Utils
import com.example.diary1.datasave.database.MyDirayDB
import com.example.diary1.datasave.entity.PostInfo
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.fragment_post_diary.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity() : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    val db = MyDirayDB.getInstance(applicationContext)

    // DiaruListFragment - recyclerview - item 으로부터 데이터 넘겨받을 변수
    var itemData: PostInfo? = null

    // 수정 모드 플래그
    var isEditMode = false

    // 캘린더 관련 변수들
    var year: Int = 0           // 년
    var month: String = ""      // 월
    var date: String = ""       // 일
    var day: String = ""        // 요일

    var originalDate: String? = null

    var mCurrentPhotoPath: String? = null

    // MainPageActivity 로 전환시 화면 및 버튼 색상 초기화 할 플래그
    var btnClicked: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        init()

        /**
         * 날짜 텍스트뷰 클릭시 캘린더뷰 visible, 선택 날짜 textview 에 할당하기
         */
        tv_detail_date.setOnClickListener {
            cv_detail_calendar.visibility = View.VISIBLE

            // open 되어 있을 시 다른 뷰 클릭 이벤트 x
            et_detail_title.isEnabled = false
            iv_detail_image.isEnabled = false
            et_detail_content.isEnabled = false
            btn_detail_complete.isEnabled = false
            ib_detail_menu.isEnabled = false

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
                tv_detail_date.text = "${this.year}-${this.month!!}-${this.date} ${this.day}"

                // 클릭 이벤트 살리기
                et_detail_title.isEnabled = true
                iv_detail_image.isEnabled = true
                et_detail_content.isEnabled = true
                btn_detail_complete.isEnabled = true
                ib_detail_menu.isEnabled = true

                cv_detail_calendar.visibility = View.GONE
            }
        }

        /**
         * 이미지뷰 클릭시 앨범과 카메라에서 이미지 가져와 할당하기
         */
        iv_detail_image.setOnClickListener {
            /**
             * 1. 권한 체크
             * 2. 다이어로그 띄워서 앨범 혹은 카메라 진입
             * 3. 이미지뷰에 이미지 세팅
             */
            getImage()
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
                    var checkDate: List<PostInfo>? = null
                    CoroutineScope(Dispatchers.IO).launch {
                        checkDate = db!!.PostDao().checkDiary(Constants.userID, tv_detail_date.text.toString())
                    }
                    var date = ""
                    for (getDate in checkDate!!) {
                        date = getDate.post_date
                    }
                    if (date.isEmpty()) {
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
                val image = Utils.resizeImage(iv_detail_image.drawable, iv_detail_image.width, iv_detail_image.height)
                CoroutineScope(Dispatchers.IO).launch {
                    db!!.PostDao().saveDiary(Constants.userID, et_detail_title.text.toString(), tv_detail_date.text.toString(), et_detail_content.text.toString(), itemData?.post_date!!, image)
                }
            } catch (e: Exception) {
                Log.d("update diary exception", ">>>>>>>>>>$e")
                Toast.makeText(this, "일기를 저장 중에 오류가 발생했어요", Toast.LENGTH_SHORT).show()
            }

            // 수정 off 모드
            setCloseEditMode()

            isEditMode = false
        }
    }

    fun init() {
        // 데이터 바인딩
        itemData = intent.getSerializableExtra("DATA") as PostInfo
        btnClicked = intent.getSerializableExtra("btnClicked") as Int
        et_detail_title.setText(itemData?.post_title)
        tv_detail_date.text = itemData?.post_date
        et_detail_content.setText(itemData?.post_content)
        // 원래 날짜 값
        originalDate = itemData?.post_date

        // 수정 off 모드
        setCloseEditMode()

        // CalendarView GONE 처리
        cv_detail_calendar.visibility = View.GONE

        // 이미지뷰에 이미지 세팅
        var postSetting: List<PostInfo>? = null
        CoroutineScope(Dispatchers.IO).launch {
            postSetting = db!!.PostDao().getDiaryImage(Constants.userID, itemData?.post_date!!)
        }
        var imageFromDB: ByteArray
        var bitmapImage: Bitmap? = null
        for (postInfo in postSetting!!) {
            imageFromDB = postInfo.post_image
            bitmapImage = BitmapFactory.decodeByteArray(imageFromDB, 0, imageFromDB.size)
        }

        iv_detail_image.setImageBitmap(bitmapImage)

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
                    CoroutineScope(Dispatchers.IO).launch {
                        db!!.PostDao().deleteDiary(Constants.userID, itemData?.post_title!!, itemData?.post_date!!)
                    }
                    // 삭제가 완료 되었습니다 다이어로그
                    builder.setTitle("삭제 완료")
                    builder.setMessage("삭제가 완료되었습니다")

                    val listener = DialogInterface.OnClickListener { _, a ->
                        when (a) {
                            DialogInterface.BUTTON_NEUTRAL -> {
                                val intent = Intent(this, MainPageActivity::class.java)
                                intent.putExtra("btnClicked", btnClicked)
                                startActivity(intent)
                                overridePendingTransition(R.anim.act_up, 0)
                                finish()
                            }
                            DialogInterface.BUTTON_POSITIVE -> {
                                val intent = Intent(this, MainPageActivity::class.java)
                                intent.putExtra("btnClicked", btnClicked)
                                startActivity(intent)
                                overridePendingTransition(R.anim.act_up, 0)
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
     * 권한을 승인했는지 확인하는 함수
     */
    private fun checkPermission(permissions: Array<out String>, flag: Int): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, flag)
                return false
            }
        }
        return true
    }

    /**
     * 이미지뷰 클릭시 앨범과 카메라에서 이미지 삽입
     * 1. dialog 팝업
     * 2. 각각의 버튼을 클릭했을 때, 앨범 혹은 카메라로 이동
     * 3. 선택 혹은 촬영한 이미지 이미지뷰에 박음
     */
    private fun getImage() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("사진 등록")
        builder.setMessage("사연과 함께 사진을 등록하세요")

        var listener = DialogInterface.OnClickListener { _, a ->
            when (a) {
                DialogInterface.BUTTON_NEUTRAL -> {
                    openGallery()
                }
                DialogInterface.BUTTON_POSITIVE -> {
                    openCamera()
                }
            }
        }
        builder.setNeutralButton("앨범", listener)
        builder.setPositiveButton("카메라", listener)

        builder.show()
    }

    /**
     * 앨범 열기 : openGallery
     * 카메라 열기 : openCamera
     */
    private fun openGallery() {
        if (checkPermission(Utils.STORAGE_PERMISSION, Utils.PERMISSION_CAMERA)) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, Utils.REQUEST_STORAGE)
        }
    }

    private fun openCamera() {
        if (checkPermission(Utils.CAMERA_PERMISSION, Utils.PERMISSION_CAMERA)) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(packageManager) != null) {
                var photoFile: File? = null
                photoFile = createImageFile()
                if (photoFile != null) {
                    val providerURI = FileProvider.getUriForFile(this, packageName, photoFile)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI)
                }
            }
            startActivityForResult(intent, Utils.REQUEST_CAMERA)
        }
    }

    @Throws(IOException::class)
    fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_$timeStamp.jpg"
        var imageFile: File? = null
        val storageDir =
            File(Environment.getExternalStorageDirectory().toString() + "/Pictures", "${Constants.appName}")
        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString())
            storageDir.mkdirs()
        }
        imageFile = File(storageDir, imageFileName)
        mCurrentPhotoPath = imageFile.absolutePath
        return imageFile
    }

    /**
     * 이미지뷰에 이미지 셋팅
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("gall", ">>>>>>>>>>result code $resultCode")
        if(data == null ) Log.d("gall", ">>>>>>>>>>data null")
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Utils.REQUEST_STORAGE -> {
                    val uri = data?.data
                    Log.d("gall", ">>>>>>>>>>uri ${uri?.path}")
                    iv_detail_image.setImageURI(uri)
                }
                Utils.REQUEST_CAMERA -> {
                    // val bitmap = data?.extras?.get("data") as Bitmap
                    // iv_detail_image.setImageBitmap(bitmap)
                    galleryAddPic()
                }
            }
        }
    }

    fun galleryAddPic() {
        Log.i("galleryAddPic", "Call");
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        val file = File(mCurrentPhotoPath)
        val contentUri: Uri = Uri.fromFile(file)
        intent.setData(contentUri)
        sendBroadcast(intent)
        Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show()

        // 로컬에 저장한 이미지 이미지뷰에 세팅
        iv_detail_image.setImageURI(contentUri)
    }

    /**
     * 뒤로가기 버튼 클릭 시,
     * 1. 만약 수정 모드일 때, 수정 모드 종료, 초기 상태로 데이터 돌려놓기
     * 2. 수정 모드가 아닐 때, 화면 종료
     */
    override fun onBackPressed() {
        if (isEditMode) {
            init()

            isEditMode = false
        } else {
            val intent = Intent(this, MainPageActivity::class.java)
            intent.putExtra("btnClicked", btnClicked)
            startActivity(intent)
            overridePendingTransition(R.anim.act_up, 0)
            finish()
        }
    }
}