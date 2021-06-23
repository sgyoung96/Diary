package com.example.diary1.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diary1.R
import com.example.diary1.ui.fragment.listrecycler.DiaryListAdapter
import com.example.diary1.ui.fragment.listrecycler.GetPostingData
import com.example.diary1.ui.fragment.listrecycler.PostedDiaryInfo

/**
 * 1. 로컬에 저장한 글쓴 목록 데이터 객체에 담기
 * 2. recyclerView 이용해 목록 뿌리기
 */
// TODO : RecyclerView 속성 카드뷰 형태로.. 둥글둥글하게
class DiaryListFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View =  inflater.inflate(R.layout.fragment_diary_list, container, false)

        // DB 에서 데이터 가져와서 객체에 담음
        val getPostingData = GetPostingData(requireContext())
        getPostingData.getData()

        val data = mutableListOf<PostedDiaryInfo>()

        Log.d("data", ">>>>>>>>>>${data.size}") // 0

        val adapter = DiaryListAdapter(requireContext())
        adapter.setData(data)
        view.findViewById<RecyclerView>(R.id.rv_diary_list).addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        view.findViewById<RecyclerView>(R.id.rv_diary_list).adapter = adapter

        return view
    }
}