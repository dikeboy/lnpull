package com.lin.lnpull

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.BaseAdapter
import android.widget.TextView
import com.lin.pull.PullListLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
     lateinit var  list: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list = ArrayList<String>()
        for(i in 1..100){
            list.add("nihaoya"+i)
        }
        pullLinearLayout.listView?.adapter= MAdapter()
        pullLinearLayout.setOnPullClickListener(object: PullListLayout.PullClickListener{
            override fun onRefrensh() {
                 var handler =Handler()
                handler.postDelayed({
                    pullLinearLayout.listView?.onRefreshComplete()
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
        pullLinearLayout.finishLoading()
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
