package com.example.diary1.ui.fragment

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diary1.R
import com.example.diary1.datasave.constants.PostDiaryInfo
import com.example.diary1.datasave.constants.SQLiteDBInfo
import com.example.diary1.datasave.SQLiteDBHelper
import com.example.diary1.datasave.execsql.ExecQuery
import com.example.diary1.datasave.queries.Query
import com.example.diary1.ui.activity.MainPageActivity
import com.example.diary1.ui.fragment.listrecycler.PostedDiaryInfo
import com.example.diary1.ui.fragment.myrecycler.ItemClickListener
import com.example.diary1.ui.fragment.myrecycler.MyDiaryAdapter
import kotlinx.android.synthetic.main.fragment_diary_list.*
import kotlinx.android.synthetic.main.fragment_my_diary.*

class MyDiaryFragment : Fragment() {
    var data: MutableList<PostedDiaryInfo> = mutableListOf()
    var myDiaryAdapter: MyDiaryAdapter? = null
    var mainPageActivity: MainPageActivity? = null
    var itemData: MutableList<PostedDiaryInfo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_diary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myDiaryAdapter = MyDiaryAdapter(requireContext())

        val dbHelper = SQLiteDBHelper(context, SQLiteDBInfo.DB_NAME, null, 1)
        val database: SQLiteDatabase = dbHelper.readableDatabase
        val query = Query.myDiaryQuery()
        val result: Cursor = database.rawQuery(query, null)

        while (result.moveToNext()) {
            data.add(
                PostedDiaryInfo(
                    result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_DATE)),
                    result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_TITLE)),
                    result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_CONTENT)),
                    result.getString(result.getColumnIndex(PostDiaryInfo.DB_COL_MY)))
            )
        }
        database.close()

        myDiaryAdapter?.data = data
        itemData = data
        rv_my_diary.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        rv_my_diary.adapter = myDiaryAdapter

        myDiaryAdapter?.setItemListener(object : ItemClickListener {
            override fun onItemClick(data: PostedDiaryInfo) {
                mainPageActivity?.goDetailActivity(data)
            }

            override fun onMyClick(data: PostedDiaryInfo, position: Int) {
                val item: PostedDiaryInfo = itemData!![position]
                ExecQuery.setMyFlag(requireContext(), item.postDate)
                mainPageActivity?.changeFragment(3)
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainPageActivity = activity as MainPageActivity
    }
}