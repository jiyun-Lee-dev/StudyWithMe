package com.example.studywithme.Board

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.studywithme.R


class WholeboardFrag : Fragment() {
    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wholeboard, container, false)
    }
}