package com.lin.test

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.lin.lnpull.PullListView
import com.lin.lnpull.R
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.widget.RecyclerView.ViewHolder
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.ScrollView
import com.lin.lnpull.PullToRefreshListView
import kotlinx.android.synthetic.main.activity_main.view.*
import org.w3c.dom.Text


class MainActivity : AppCompatActivity(),View.OnClickListener{
     lateinit var  list: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testListView.setOnClickListener(this)
        testScrollView.setOnClickListener(this)
        testRecycleViewGrid.setOnClickListener(this)
        testRecycleViewLinearLayout.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.testListView -> jumpActivity(TestListView().javaClass)
            R.id.testScrollView -> jumpActivity(TestScrollView().javaClass)
            R.id.testRecycleViewLinearLayout -> jumpActivity(TestRecycleViewLinearlayout().javaClass)
            R.id.testRecycleViewGrid -> jumpActivity(TestRecycleViewGrid().javaClass)
        }
    }

    open fun  jumpActivity(clazz:Class<Activity> ){
        var intent = Intent()
        intent.setClass(this, clazz)
        startActivity(intent)

    }
}
