package com.example.diary1.ui.fragment.myrecycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diary1.R
import com.example.diary1.datasave.entity.PostInfo

class MyDiaryViewHolder(item: View): RecyclerView.ViewHolder(item) {

    val itemDate = item.findViewById<TextView>(R.id.tv_my_diary_date)
    val itemTitle = item.findViewById<TextView>(R.id.tv_my_diary_title)
    val itemContent = item.findViewById<TextView>(R.id.tv_my_diary_content)
    val itemMy = item.findViewById<ImageView>(R.id.iv_my_diary_my)

    val item = item

    fun bind(data: PostInfo, listener: ItemClickListener, position: Int) {
        itemDate.text = data.post_date
        itemTitle.text = data.post_title
        itemContent.text = data.post_content
        itemMy.setImageResource(R.drawable.item_my_on)

        item.setOnClickListener {
            listener.onItemClick(data)
        }

        itemMy.setOnClickListener {
            listener.onMyClick(data, position)
        }
    }
}