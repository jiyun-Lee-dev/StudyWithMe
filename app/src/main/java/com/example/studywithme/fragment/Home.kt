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
        toolbarTitle.text = "홈"

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
    }

    // menu에 있는 메뉴 클릭 시 이벤트 처리
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            //뒤로가기 버튼 클릭 시
            android.R.id.home -> {
                activity!!.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}