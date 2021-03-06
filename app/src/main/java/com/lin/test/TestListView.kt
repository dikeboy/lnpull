package com.lin.test

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.lin.lnpull.PullListView
import com.lin.lnpull.PullToRefreshListView
import com.lin.lnpull.R
import com.lin.lnpull.R.id.pullLinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.test_listview.*

/**
 * author : leo
 * date   : 2018/10/2822:28
 */
class TestListView : BaseActivity(){
    lateinit var  list: ArrayList<String>
    lateinit var  adapter: MAdapter;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_listview)
        list = ArrayList<String>()

        var handler = Handler()
        adapter  = MAdapter()
        pullLinearLayout.setAdapter(adapter)
        pullLinearLayout.setOnPullClickListener(object: PullListView.PullClickListener{
            override fun onRefrensh() {

                handler.postDelayed({
                    pullLinearLayout.listView?.onRefreshComplete()
                },3000)

            }

            override fun onRetry() {
            }

            override fun onMoreClick(): Boolean {
                if(list.size>10){
                    handler.postDelayed({
                            testListView();
                    },1500)
                    return false
                }
                return true
            }

            override fun onRefrenshPause() {
            }
        })
        handler.postDelayed({
            pullLinearLayout.finishLoading()
            pullLinearLayout.showLoadingMore();
        },1500)
        testListView()
    }

    fun testListView(){
        for(i in 1..50){
            list.add("nihaoya"+i)
        }
        adapter.notifyDataSetChanged()
    }

    inner  class MAdapter: BaseAdapter(){
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var holder: Holder
            var view: View
            if(convertView ==null){
                view = TextView(baseContext)
                holder = Holder()
                holder.nameTv = view
                view.tag = holder
            }
            else{
                holder=convertView.tag as Holder;
                view = convertView
            }
            holder.nameTv.setTextColor(Color.BLACK)
            holder.nameTv.text = getItem(position)
            holder.nameTv.setOnClickListener{ view -> println("nihao")}
            return view
        }

        override fun getItem(position: Int): String {
            return  list.get(position)
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return  list.size
        }

        inner class Holder{
            lateinit var nameTv: TextView
        }
    }


}
