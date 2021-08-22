package com.example.diary1.ui.fragment

import android.content.Context
import android.os.Bundle
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
import com.example.diary1.ui.fragment.myrecycler.ItemClickListener
import com.example.diary1.ui.fragment.myrecycler.MyDiaryAdapter
import kotlinx.android.synthetic.main.fragment_my_diary.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyDiaryFragment(context: Context) : Fragment() {

    var applicationContext = context

    var data: MutableList<PostInfo> = mutableListOf()
    var getPost: MutableList<PostInfo> = mutableListOf()
    var myDiaryAdapter: MyDiaryAdapter? = null
    var mainPageActivity: MainPageActivity? = null
    var itemData: MutableList<PostInfo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_diary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myDiaryAdapter = MyDiaryAdapter(requireContext())

        val db = MyDirayDB.getInstance(applicationContext)
        var posting: List<PostInfo>? = null
        CoroutineScope(Dispatchers.IO).launch {
            posting = db!!.PostDao().getListFromId(Constants.userID)
        }
        for (data in posting!!) {
            getPost.add(PostInfo(Constants.userID, data.post_date, data.post_title, data.post_content, data.post_my, byteArrayOf()))
        }

        myDiaryAdapter?.data = data
        itemData = data
        rv_my_diary.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        rv_my_diary.adapter = myDiaryAdapter

        myDiaryAdapter?.setItemListener(object : ItemClickListener {
            override fun onItemClick(data: PostInfo) {
                mainPageActivity?.goDetailActivity(data)
            }

            override fun onMyClick(data: PostInfo, position: Int) {
                val item: PostInfo = itemData!![position]
                // 조회한 flag 가 0이면 1로 업데이트, 1이면 0으로 업데이트
                var myFlag: List<PostInfo>? = null
                CoroutineScope(Dispatchers.IO).launch {
                    myFlag = db!!.PostDao().selectMyFlag(Constants.userID, item.post_date)
                }
                var getMyFlag = ""
                for (flag in myFlag!!) {
                    getMyFlag = flag.post_my
                }
                if (getMyFlag == PostDiaryInfo.POST_MY_DEFAULT) {
                    CoroutineScope(Dispatchers.IO).launch {
                        db!!.PostDao().updateMyFlag1(Constants.userID, item.post_date)
                    }
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        db!!.PostDao().updateMyFlag0(Constants.userID, item.post_date)
                    }
                }
                mainPageActivity?.changeFragment(3)
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainPageActivity = activity as MainPageActivity
    }
}