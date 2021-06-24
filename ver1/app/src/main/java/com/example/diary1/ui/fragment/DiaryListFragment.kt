package com.example.diary1.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diary1.R
import com.example.diary1.ui.fragment.listrecycler.*

/**
 * 1. 로컬에 저장한 글쓴 목록 데이터 객체에 담기
 * 2. recyclerView 이용해 목록 뿌리기
 */
class DiaryListFragment : Fragment(), DiaryListContract.View {

    var diaryListAdapter: DiaryListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View =  inflater.inflate(R.layout.fragment_diary_list, container, false)

        view.findViewById<RecyclerView>(R.id.rv_diary_list)?.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        diaryListAdapter = DiaryListAdapter(requireContext())
        view.findViewById<RecyclerView>(R.id.rv_diary_list)?.adapter = diaryListAdapter

        val diaryListPresenter = DiaryListPresenter()
        diaryListPresenter.snedView(this)
        diaryListPresenter.sendData(requireContext())

        return view
    }

    // Presenter 로부터 데이터 넘겨받음
    override fun sendData(data: MutableList<PostedDiaryInfo>) {
        diaryListAdapter?.setData(data)
    }
}

/*
1. View실행될 때 빈 값인 데이터를 adapter 에 붙인다.
2. Adapter 와 ViewHolder 를 구성한다.
3. 데이터를 가져온다.
4. 데이터를 presenter 로 넘긴다.
5. 데이터를 view 로 넘긴다.
6. 데이터를 dapter 로 넘기고, setNotifyChanged() 함수를 호출한다.
*/