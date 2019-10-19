package com.example.studywithme.Board

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.example.studywithme.R
import kotlinx.android.synthetic.main.myboard.*




class MyboardFrag : Fragment() {
    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_myboard, container, false)

        val write_button = view.findViewById<Button>(R.id.write_button)
        write_button.setOnClickListener{
            val intent= Intent(activity, Write_Board::class.java)
            startActivity(intent)
        }

        return view
    }
}
