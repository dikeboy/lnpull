package com.lin.test

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

/**
 *   author : leo
 *   date   : 2018/10/3116:24
 */
 open class BaseActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(getMyTitle() != null){
            supportActionBar?.title = getMyTitle();
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }


    open fun getMyTitle(): String? {
        return  javaClass.simpleName;
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item)
    }
}