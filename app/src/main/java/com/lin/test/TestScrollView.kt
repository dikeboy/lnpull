package com.lin.test

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.text.Layout
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.lin.lnpull.PullListView
import com.lin.lnpull.PullScrollView
import com.lin.lnpull.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.test_listview.*
import org.w3c.dom.Text

/**
 * author : leo
 * date   : 2018/10/2822:28
 */
class TestScrollView : AppCompatActivity() {

     private lateinit var pullScrollView: PullScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pullScrollView = PullScrollView(this);
        var view = LayoutInflater.from(this).inflate(R.layout.test_scrollview,null) as LinearLayout
        pullScrollView.addChildView(view)
        setContentView(pullScrollView)
        for(i in 1..50){
            var tv = TextView(this)
            tv.setText("nihaoya"+i)
            tv.setTextColor(Color.BLACK)
            tv.height = 50
            view.addView(tv)
        }

        var handler = Handler()
        pullScrollView.setOnPullClickListener(object: PullScrollView.PullClickListener{
            override fun onRefrensh() {
                handler.postDelayed({
                    pullScrollView.scrollView.onRefreshComplete()
                },3000)

            }

            override fun onRetry() {
            }
        })
        handler.postDelayed({
            pullScrollView.finishLoading()
        },1500)
    }
}
