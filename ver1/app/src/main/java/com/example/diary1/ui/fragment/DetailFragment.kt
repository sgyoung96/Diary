package com.example.diary1.ui.fragment

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.diary1.R
import com.example.diary1.ui.fragment.listrecycler.DiaryListViewHolder
import com.example.diary1.ui.fragment.listrecycler.PostedDiaryInfo
import kotlinx.android.synthetic.main.fragment_detail.*

// TODO : 뒤로가기 버튼 눌렀을 때 종료되지 않고, DiaryListFragment 로 화면 전환되도록
class DetailFragment(data: PostedDiaryInfo) : Fragment() {

    val itemdata = data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_detail_title.text = itemdata.postTitle
        tv_detail_date.text = itemdata.postDate
        tv_detail_content.text = itemdata.postContent

    }
}