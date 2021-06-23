package com.example.diary1.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.diary1.R

/**
 * 1. 로컬에 저장한 글쓴 목록 데이터 객체에 담기
 * 2. recyclerView 이용해 목록 뿌리기
 */
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


        return view
    }

}

/**
 * [화면에 데이터 뿌려주는 함수를 만들어서 재사용하기]
 * 1. DB 를 열어서 로그인한 아이디 값으로 정보를 SELECT 해 온다.
 * 2. result 에서 컬럼별로 mutableList 로 객체에 담는다.
 * 3. 객체를 ViewHolder 에 넘겨준다.
 * 4. ViewHolder 는 Adapter 에 데이터를 알려준다.
 * 5. Adapter 는 View 에 데이터를 알려준다.
 * 6. 화면의 recyclerView.adapter = Adapter 해서 서로 붙여준다.
 */