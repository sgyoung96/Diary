package com.example.diary1.ui.fragment.listrecycler

import android.view.View

interface ItemClickListener {
    fun onItemClick(data: PostedDiaryInfo)
    fun onMyClick()
}