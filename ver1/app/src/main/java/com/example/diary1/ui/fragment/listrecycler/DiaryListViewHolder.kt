package com.example.diary1.ui.fragment.listrecycler

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diary1.R

class DiaryListViewHolder(item: View): RecyclerView.ViewHolder(item) {

    var itemDate = item.findViewById<TextView>(R.id.tv_diary_list_date)
    var itemTitle = item.findViewById<TextView>(R.id.tv_diary_list_title)
    var itemContent = item.findViewById<TextView>(R.id.tv_diary_list_content)
    val item = item

    fun bind(data: PostedDiaryInfo, listener: ItemClickListener) {
        itemDate.text = data.postDate
        itemTitle.text = data.postTitle
        itemContent.text = data.postContent

        Log.d("ViewHolderDate", ">>>>>>>>>>${data.postDate}")
        Log.d("ViewHolderTitle", ">>>>>>>>>>${data.postTitle}")
        Log.d("ViewHolderContent", ">>>>>>>>>>${data.postContent}")

        item.setOnClickListener {
            listener.onItemClick(data)
        }
    }
}

/*
item.setOnClickListener(View.OnClickListener() {
            override fun onClick(v: View) {
                val position = adapterPosition
                listener.onItemClick(this, item, data, position)
            }
        })
*/