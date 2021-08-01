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
import com.example.diary1.datasave.execsql.ExecQuery
import com.example.diary1.ui.activity.MainPageActivity
import com.example.diary1.ui.fragment.listrecycler.*
import kotlinx.android.synthetic.main.fragment_diary_list.*

/**
 * 1. 로컬에 저장한 글쓴 목록 데이터 객체에 담기
 * 2. recyclerView 이용해 목록 뿌리기
 */
// TODO : item 중, 하트 클릭시 이벤트 발생 (DB 변경, 이미지 변경, 관심목록 리스트 갱신)
// ColorPrimary 색상을 바꿔줘야 recyclerView 드래그했을 때 나타나는 색상이 반영된다.
class DiaryListFragment : Fragment(), DiaryListContract.View {

    var itemData: MutableList<PostedDiaryInfo>? = null
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

        val diaryListPresenter = DiaryListPresenter()
        diaryListPresenter.snedView(this)
        diaryListPresenter.sendData(requireContext())

        diaryListAdapter!!.setOnItemClickListener(object : ItemClickListener {
            override fun onItemClick(data: PostedDiaryInfo) {
                mainPageActivity?.goDetailActivity(data)
            }

            override fun onMyClick(data: PostedDiaryInfo, position: Int) {
                val item: PostedDiaryInfo = itemData!![position]
                Log.d("item", ">>>>>>>>>>$item")
                ExecQuery.setMyFlag(requireContext(), item.postDate)
                mainPageActivity?.changeFragment(1)
            }
        })
    }

    // Presenter 로부터 데이터 넘겨받음
    override fun sendData(data: MutableList<PostedDiaryInfo>) {
        itemData = data
        diaryListAdapter?.setData(data)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainPageActivity = activity as MainPageActivity
    }
}

/*
1. View실행될 때 빈 값인 데이터를 adapter 에 붙인다.
2. Adapter 와 ViewHolder 를 구성한다.
3. 데이터를 가져온다.
4. 데이터를 presenter 로 넘긴다.
5. 데이터를 view 로 넘긴다.
6. 데이터를 adapter 로 넘기고, setNotifyChanged() 함수를 호출한다.
*/