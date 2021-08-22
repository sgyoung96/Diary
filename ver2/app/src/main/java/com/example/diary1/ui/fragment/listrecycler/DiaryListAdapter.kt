package com.example.diary1.ui.fragment.listrecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.diary1.R
import com.example.diary1.datasave.entity.PostInfo

class DiaryListAdapter(val context: Context): RecyclerView.Adapter<DiaryListViewHolder>() {

    lateinit var diaryData: MutableList<PostInfo>
    var listener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_diary_list, parent, false)

        return DiaryListViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiaryListViewHolder, position: Int) {
        listener?.let { holder.bind(diaryData[position], it) }
    }

    override fun getItemCount(): Int {
        return diaryData.size
    }

    fun setOnItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }
}