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
import android.widget.ImageButton
import com.example.studywithme.scheduling.AddGoalDialog
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

        val toolbar_btn_profile= view.findViewById<ImageButton>(R.id.toolbar_btn_profile)

        toolbar_btn_profile.setOnClickListener{
            val fragment = Profile() // Fragment 생성
            val fm = fragmentManager
            val fmt = fm?.beginTransaction()
            fmt?.replace(R.id.content, fragment)?.addToBackStack(null)?.commit()
        }


        val home_add_todo = view.findViewById<Button>(R.id.home_add_todo)
        val home_add_goal = view.findViewById<Button>(R.id.home_add_goal)


        home_add_goal.setOnClickListener {
            val myDialogFragment = AddGoalDialog()
            myDialogFragment.setTargetFragment(this, 0)
            myDialogFragment.show(fragmentManager!!, "Search Filter")
        }


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}