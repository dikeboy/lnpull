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
import com.lin.lnpull.*
import kotlinx.android.synthetic.main.test_listview.*
import kotlinx.android.synthetic.main.test_recycleview_linearlayout.*

/**
 * author : leo
 * date   : 2018/10/2822:29
 */
class TestRecycleViewLinearlayout : AppCompatActivity(){
    lateinit var  list: ArrayList<String>
    lateinit var  id_recyclerview: PullToRecycleView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_recycleview_linearlayout)
        list = ArrayList<String>()
        for(i in 1..50){
            list.add("nihaoya"+i)
        }
         id_recyclerview =pullRecycleView.listView
        id_recyclerview.setLayoutManager( LinearLayoutManager(this));
        id_recyclerview.adapter = HomeAdapter();
    testListView()

    }
    fun testListView(){

        var handler = Handler()
        pullRecycleView.setOnPullClickListener(object: PullRecycleView.PullClickListener{
            override fun onRefrensh() {

                handler.postDelayed({
                    pullRecycleView.listView?.onRefreshComplete()
                },3000)

            }

            override fun onRetry() {
            }

            override fun onMoreClick(): Boolean {
                return false
            }

            override fun onRefrenshPause() {
            }
        })
        handler.postDelayed({
            pullRecycleView.finishLoading()
        },1500)
    }

    inner class HomeAdapter : RecyclerView.Adapter<HomeAdapter.MyViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            var  holder:MyViewHolder
            if(viewType==0){
                holder = MyViewHolder(id_recyclerview.header);
                holder.nameTv.setTextColor(Color.BLACK)
                holder.nameTv.height=100
            }
            else{
                holder = MyViewHolder(LinearLayout(baseContext));
                holder.nameTv.setTextColor(Color.BLACK)
                holder.nameTv.height = 50
            }
            return holder;
        }

        override fun getItemViewType(position: Int): Int {
            return  if(position==0)  0 else 1
        }
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            Log.e("lin","laile="+list.get(position))
            if(position>0){
                holder.nameTv.setText(list.get(position))
            }

        }

        override fun getItemCount(): Int {
            return list.size
        }
        inner class MyViewHolder(var textView : View) : RecyclerView.ViewHolder(textView){
            lateinit  var nameTv: TextView
            init{
                nameTv = TextView(baseContext)
                if(textView is LinearLayout) {
                    var group = textView as LinearLayout
                    textView.layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT
                    )
                    group.addView(nameTv)
                }
            }

        }
    }
}
