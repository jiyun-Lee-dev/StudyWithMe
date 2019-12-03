package com.example.studywithme

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.studywithme.fragment.*
import com.example.studywithme.recommend.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bottomNavi
import kotlinx.android.synthetic.main.activity_second.*

class MainActivity : AppCompatActivity() {

    // 프래그먼트를 변수로 저장해서 mainactivity에서 사용.
    var calendarFrag = Calendar()
    var recommendFrag = Recommend()
    var homeFrag = Home()
    var boardFrag = Board()
    var bookmarkFrag = Bookmark()

    // 하단바에서 메뉴 선택하면 해당 프래그먼트로 replace해주는 함수를 호출하는 함수
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

    // 프래그먼트 교체하는 함수
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content, fragment, fragment.javaClass.getSimpleName())
            .commit()
    }

    // 메인 액티비티 실행될 때 바로 실행되는 것들. 기본으로 선택되어있는 하단바 메뉴는 action_home
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavi.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavi.setSelectedItemId(R.id.action_home)
    }

    // supportFragmentManager는 액티비티 내에서 fagmentmanager에 접근하기 위해 사용.
    // 프래그먼트는 트랜잭션을 통해 add, remove, replace 등을 할 수 있기 때문에 beginTransaction 호출
    // commit()을 호출해야만 트랜잭션 작업을 정상적으로 수행할 수 있음.
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
            bottomNavi.setSelectedItemId(R.id.action_calendar)
        }
        if(index==4){
            supportFragmentManager.beginTransaction().replace(R.id.content, bookmarkFrag).commit()
            bottomNavi.setSelectedItemId(R.id.action_bookmark)
        }
    }


}
