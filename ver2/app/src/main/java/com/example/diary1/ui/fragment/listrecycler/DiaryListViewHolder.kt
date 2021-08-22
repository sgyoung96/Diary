package com.example.diary1.ui.fragment.listrecycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diary1.R
import com.example.diary1.datasave.entity.PostInfo

class DiaryListViewHolder(item: View): RecyclerView.ViewHolder(item) {

    val itemDate = item.findViewById<TextView>(R.id.tv_diary_list_date)
    val itemTitle = item.findViewById<TextView>(R.id.tv_diary_list_title)
    val itemContent = item.findViewById<TextView>(R.id.tv_diary_list_content)
    val item = item

    // 관심목록 하트
    val itemMy = item.findViewById<ImageView>(R.id.iv_diary_list_my)

    fun bind(data: PostInfo, listener: ItemClickListener) {
        itemDate.text = data.post_date
        itemTitle.text = data.post_title
        itemContent.text = data.post_content

        if (data.post_my == "0") {
            itemMy.setImageResource(R.drawable.item_my_off)
        } else {
            itemMy.setImageResource(R.drawable.item_my_on)
        }

        item.setOnClickListener {
            listener.onItemClick(data)
        }

        itemMy.setOnClickListener {
            listener.onMyClick(data, adapterPosition)
        }
    }
}