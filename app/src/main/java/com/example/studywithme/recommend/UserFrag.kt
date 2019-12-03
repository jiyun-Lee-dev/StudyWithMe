package com.example.studywithme.recommend

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studywithme.R
import kotlinx.android.synthetic.main.fragment_user.*

class UserFrag : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_user, container, false)

/*
        rv_user_list.adapter = UserAdapter()
        rv_user_list.layoutManager = LinearLayoutManager(view.context)

        val myLayoutManager = GridLayoutManager(view.context, 2)
        rv_user_list.layoutManager = myLayoutManager
*/
        return view
    }
}