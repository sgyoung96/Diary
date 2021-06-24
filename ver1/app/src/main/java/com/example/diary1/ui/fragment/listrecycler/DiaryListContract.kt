package com.example.diary1.ui.fragment.listrecycler

interface DiaryListContract {
    interface View {
        fun sendData(data: MutableList<PostedDiaryInfo>)
    }
    interface Presenter {
        fun snedView(view: View)
//        fun sendData()
    }
}