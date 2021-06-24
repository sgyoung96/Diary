package com.example.diary1.ui.fragment.listrecycler

import android.content.Context
import android.util.Log

class DiaryListPresenter(): DiaryListContract.Presenter {
    lateinit var view: DiaryListContract.View
    lateinit var data: MutableList<PostedDiaryInfo>

    override fun snedView(view: DiaryListContract.View) {
        this.view = view
    }

    // GetPostingData 로부터 데이터 가져옴
    fun getData(data: MutableList<PostedDiaryInfo>, view: DiaryListContract.View) {
        this.data = data
        Log.d("presenter's data", ">>>>>>>>>>${this.data}")

        view.sendData(this.data)
    }

    // View 로 데이터 넘겨줌
    fun sendData(context: Context) {
        // DB 에서 데이터 가져와서 객체에 담음
        val getPostingData = GetPostingData(context)
        getPostingData.getData(view)
    }
}