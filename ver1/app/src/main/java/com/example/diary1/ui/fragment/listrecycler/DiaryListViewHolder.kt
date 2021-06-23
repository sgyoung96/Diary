package com.example.diary1.ui.fragment.listrecycler

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diary1.R

class DiaryListViewHolder(item: View): RecyclerView.ViewHolder(item) {

    var itemTitle = item.findViewById<TextView>(R.id.tv_diary_list_title)
    var itemDate = item.findViewById<TextView>(R.id.tv_diary_list_date)
    var itemContent = item.findViewById<TextView>(R.id.tv_diary_list_content)

    fun bind(data: PostedDiaryInfo) {
        itemTitle.text = data.postTitle
        itemDate.text = data.postDate
        itemContent.text = data.postContent
    }

}