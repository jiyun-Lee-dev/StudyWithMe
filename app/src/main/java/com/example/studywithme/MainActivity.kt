package com.example.studywithme

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import com.example.studywithme.Board.Write_Board
import com.example.studywithme.fragment.*
import com.example.studywithme.recommend.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bottomNavi
import kotlinx.android.synthetic.main.activity_second.*

class MainActivity : AppCompatActivity() {

    var calendarFrag = Calendar()
    var recommendFrag = Recommend()
    var homeFrag = Home()
    var boardFrag = Board()
    var bookmarkFrag = Bookmark()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.action_calendar -> {
                replaceFragment(calendarFrag)
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_recommend -> {
                replaceFragment(recommendFrag)
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_home -> {
                replaceFragment(homeFrag)
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_board -> {
                replaceFragment(boardFrag)
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_bookmark -> {
                replaceFragment(bookmarkFrag)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content, fragment, fragment.javaClass.getSimpleName())
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavi.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavi.setSelectedItemId(R.id.action_home)


    }



    fun onFragmentChange(index: Int) {
        if(index==1){
            supportFragmentManager.beginTransaction().replace(R.id.content, boardFrag).commit()
            bottomNavi.setSelectedItemId(R.id.action_board)
        }
        if(index==2){
            supportFragmentManager.beginTransaction().replace(R.id.content, recommendFrag).commit()
            bottomNavi.setSelectedItemId(R.id.action_recommend)
        }
        if(index==3){
            supportFragmentManager.beginTransaction().replace(R.id.content, calendarFrag).commit()
        }
        if(index==4){
            supportFragmentManager.beginTransaction().replace(R.id.content, bookmarkFrag).commit()
            bottomNavi.setSelectedItemId(R.id.action_bookmark)
        }
    }


}