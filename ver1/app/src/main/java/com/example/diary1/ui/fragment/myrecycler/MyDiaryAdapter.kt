package com.example.diary1.ui.fragment.myrecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.diary1.R
import com.example.diary1.ui.fragment.listrecycler.PostedDiaryInfo

class MyDiaryAdapter(val context: Context): RecyclerView.Adapter<MyDiaryViewHolder>() {
    var data: MutableList<PostedDiaryInfo>? = null
    var listener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyDiaryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_my_diary, parent, false)
        return MyDiaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyDiaryViewHolder, position: Int) {
        holder.bind(data!![position], listener!!, position)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setItemListener(listener: ItemClickListener) {
        this.listener = listener
    }
}