package com.example.diary1.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.diary1.R
import com.example.diary1.ui.fragment.listrecycler.PostedDiaryInfo
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity() : AppCompatActivity() {

     var itemData: PostedDiaryInfo ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        itemData = intent.getSerializableExtra("DATA") as PostedDiaryInfo
        tv_detail_title.text = itemData?.postTitle
        tv_detail_date.text = itemData?.postDate
        tv_detail_content.text = itemData?.postContent
    }
}