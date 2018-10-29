package com.lin.test

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.lin.lnpull.PullRecycleAdapter
import com.lin.lnpull.PullRecycleView
import com.lin.lnpull.PullToRecycleView
import com.lin.lnpull.R
import kotlinx.android.synthetic.main.test_recycleview_linearlayout.*

/**
 * author : leo
 * date   : 2018/10/2822:29
 */
class TestRecycleViewGrid : AppCompatActivity(){
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
        id_recyclerview.setLayoutManager( GridLayoutManager(this,2));

        id_recyclerview.adapter = HomeAdapter(id_recyclerview);
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


    inner  class HomeAdapter: PullRecycleAdapter<HomeAdapter.MyViewHolder>{


        constructor(pullToRecycleView : PullToRecycleView): super(pullToRecycleView){

        }

        override fun onBindMViewHolder(pullViewHolder: MyViewHolder, i: Int) {
            pullViewHolder.nameTv.setText(list.get(i))
        }

        override fun onCreateMViewHolder(viewGroup: ViewGroup?, i: Int): PullViewHolder {
            var  holder:MyViewHolder;
            holder = MyViewHolder(LinearLayout(baseContext));
            holder.nameTv.setTextColor(Color.BLACK)
            holder.nameTv.height = 50
            return holder
        }

        override fun getMItemCount(): Int {
            return list.size
        }
        inner class MyViewHolder(var textView : View) : PullRecycleAdapter.PullViewHolder(textView){
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
