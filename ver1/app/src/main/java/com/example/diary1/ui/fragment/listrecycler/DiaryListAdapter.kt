package com.example.diary1.ui.fragment.listrecycler

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.diary1.R

class DiaryListAdapter(val context: Context): RecyclerView.Adapter<DiaryListViewHolder>() {

    lateinit var diaryData: MutableList<PostedDiaryInfo>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_diary_list, parent, false)

        return DiaryListViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiaryListViewHolder, position: Int) {
        holder.bind(diaryData[position])
    }

    override fun getItemCount(): Int {
        return diaryData.size
    }

    fun setData(data: MutableList<PostedDiaryInfo>) {
        this.diaryData = data
        notifyDataSetChanged()
    }
}