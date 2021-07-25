package com.example.diary1.ui.fragment.myrecycler

import com.example.diary1.ui.fragment.listrecycler.PostedDiaryInfo

interface ItemClickListener {
    fun onItemClick(data: PostedDiaryInfo)
    fun onMyClick(data: PostedDiaryInfo, position: Int)
}