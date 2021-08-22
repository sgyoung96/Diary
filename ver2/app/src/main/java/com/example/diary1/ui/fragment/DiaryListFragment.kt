package com.example.diary1.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diary1.R
import com.example.diary1.constants.Constants
import com.example.diary1.datasave.constants.PostDiaryInfo
import com.example.diary1.datasave.database.MyDirayDB
import com.example.diary1.datasave.entity.PostInfo
import com.example.diary1.ui.activity.MainPageActivity
import com.example.diary1.ui.fragment.listrecycler.*
import kotlinx.android.synthetic.main.fragment_diary_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 1. 로컬에 저장한 글쓴 목록 데이터 객체에 담기
 * 2. recyclerView 이용해 목록 뿌리기
 */
// ColorPrimary 색상을 바꿔줘야 recyclerView 드래그했을 때 나타나는 색상이 반영된다.
class DiaryListFragment(context: Context) : Fragment() {

    var applicationContext = context

    var getPost: MutableList<PostInfo> = mutableListOf()
    var itemData: MutableList<PostInfo>? = null
    var diaryListAdapter: DiaryListAdapter? = null
    /**
     * 화면전환할 때 사용할 변수 (onAttach 함수에서 초기화해준다.)
     */
    var mainPageActivity: MainPageActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diary_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_diary_list.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        diaryListAdapter = DiaryListAdapter(requireContext())
        rv_diary_list.adapter = diaryListAdapter

        val db = MyDirayDB.getInstance(applicationContext)
        val posting = db!!.PostDao().getListFromId(Constants.userID)
        for (data in posting) {
            getPost.add(PostInfo(Constants.userID, data.post_date, data.post_title, data.post_content, data.post_my, byteArrayOf()))
        }
        diaryListAdapter?.diaryData = getPost
        diaryListAdapter?.notifyDataSetChanged()
        itemData = getPost

        diaryListAdapter!!.setOnItemClickListener(object : ItemClickListener {
            override fun onItemClick(data: PostInfo) {
                mainPageActivity?.goDetailActivity(data)
            }

            override fun onMyClick(data: PostInfo, position: Int) {
                val item: PostInfo = itemData!![position]
                Log.d("item", ">>>>>>>>>>$item")

                // 조회한 flag 가 0이면 1로 업데이트, 1이면 0으로 업데이트
                val myFlag = db.PostDao().selectMyFlag(Constants.userID, item.post_date)
                var getMyFlag = ""
                for (flag in myFlag) {
                    getMyFlag = flag.post_my
                }
                if (getMyFlag == PostDiaryInfo.POST_MY_DEFAULT) {
                    CoroutineScope(Dispatchers.IO).launch {
                        db.PostDao().updateMyFlag1(Constants.userID, item.post_date)
                    }
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        db.PostDao().updateMyFlag0(Constants.userID, item.post_date)
                    }
                }

                mainPageActivity?.changeFragment(1)
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainPageActivity = activity as MainPageActivity
    }
}