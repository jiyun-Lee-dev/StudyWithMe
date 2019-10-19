package com.example.studywithme

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import com.example.studywithme.fragment.*
import kotlinx.android.synthetic.main.activity_second.*

class Jinju : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.action_calendar -> {
                val fragment = Calendar()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_home -> {
                var fragment = Home()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_recommend -> {
                val fragment = Recommend()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_board -> {
                val fragment = Board()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_bookmark -> {
                val fragment = Bookmark()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content, fragment, fragment.javaClass.getSimpleName())
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavi.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavi.setSelectedItemId(R.id.action_home);

    }
}