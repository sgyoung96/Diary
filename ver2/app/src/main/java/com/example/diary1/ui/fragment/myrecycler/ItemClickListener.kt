package com.example.diary1.ui.fragment.myrecycler

import com.example.diary1.datasave.entity.PostInfo

interface ItemClickListener {
    fun onItemClick(data: PostInfo)
    fun onMyClick(data: PostInfo, position: Int)
}