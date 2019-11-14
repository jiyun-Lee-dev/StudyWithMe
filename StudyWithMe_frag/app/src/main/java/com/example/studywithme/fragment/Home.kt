package com.example.studywithme.fragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.studywithme.MainActivity
import com.example.studywithme.R
import android.R.attr.button
import android.content.Intent
import android.R.attr.fragment
import android.support.annotation.IdRes
import android.support.v4.app.FragmentTransaction
import android.text.TextUtils.replace
import android.util.Log
import kotlinx.android.synthetic.main.activity_second.*


class Home : Fragment() {

    var activity: MainActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as MainActivity
    }

    override fun onDetach() {
        super.onDetach()

        activity = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        val yejin = view.findViewById<Button>(R.id.yejin)
        val jinju = view.findViewById<Button>(R.id.jinju)
        val hyewon = view.findViewById<Button>(R.id.hyewon)
        val jiyun = view.findViewById<Button>(R.id.jiyun)

        yejin.setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                activity?.onFragmentChange(1)
            }
        })
        jinju.setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                activity?.onFragmentChange(2)
            }
        })
        hyewon.setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                activity?.onFragmentChange(3)
            }
        })
        jiyun.setOnClickListener(object :View.OnClickListener {
            override fun onClick(v: View?) {
                activity?.onFragmentChange(4)
            }
        })

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}