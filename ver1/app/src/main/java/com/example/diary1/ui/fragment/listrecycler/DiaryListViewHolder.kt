package com.example.diary1.ui.fragment.listrecycler

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diary1.R

class DiaryListViewHolder(item: View): RecyclerView.ViewHolder(item) {

    val itemDate = item.findViewById<TextView>(R.id.tv_diary_list_date)
    val itemTitle = item.findViewById<TextView>(R.id.tv_diary_list_title)
    val itemContent = item.findViewById<TextView>(R.id.tv_diary_list_content)
    val item = item

    // 관심목록 하트
    val itemMy = item.findViewById<ImageView>(R.id.iv_diary_list_my)

    fun bind(data: PostedDiaryInfo, listener: ItemClickListener) {
        itemDate.text = data.postDate
        itemTitle.text = data.postTitle
        itemContent.text = data.postContent
        if (data.postMy == "0") {
            itemMy.setImageResource(R.drawable.item_my_off)
        } else {
            itemMy.setImageResource(R.drawable.item_my_on)
        }

        Log.d("ViewHolderDate", ">>>>>>>>>>${data.postDate}")
        Log.d("ViewHolderTitle", ">>>>>>>>>>${data.postTitle}")
        Log.d("ViewHolderContent", ">>>>>>>>>>${data.postContent}")
        Log.d("ViewHolderMy", ">>>>>>>>>>${data.postMy}")

        item.setOnClickListener {
            listener.onItemClick(data)
        }

        itemMy.setOnClickListener {
            listener.onMyClick(data, adapterPosition)
        }
    }
}