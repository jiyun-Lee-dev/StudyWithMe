package com.example.studywithme.fragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
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
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.example.studywithme.scheduling.AddGoalDialog
import kotlinx.android.synthetic.main.activity_second.*


class Home : Fragment() {
    var activity: MainActivity? = null
    // 마지막으로 뒤로가기 버튼이 터치된 시간
    private var lastTimeBackPressed:Long = 0L

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

        // 상단바 메뉴 쓰는 것으로 설정
        setHasOptionsMenu(true)

        // 상단바 이름 바꾸기
        var toolbarTitle: TextView = activity!!.findViewById(R.id.toolbar_title)
        toolbarTitle.text = "스터디위드미"


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

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // menu에 있는 메뉴 클릭 시 이벤트 처리
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.home_profile -> {
                val fragment = Profile() // Fragment 생성
                val fm = fragmentManager
                val fmt = fm?.beginTransaction()
                fmt?.replace(R.id.content, fragment)?.addToBackStack(null)?.commit()
            }

            //뒤로가기 버튼 클릭 시
            android.R.id.home -> {
                activity!!.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}