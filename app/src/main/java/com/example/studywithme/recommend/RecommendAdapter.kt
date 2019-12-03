package com.example.studywithme.recommend

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter


class RecommendAdapter(private val fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return InfoFrag()
            else -> return UserFrag()
        }
    }

    override fun getCount(): Int {
        return 2
    }

}