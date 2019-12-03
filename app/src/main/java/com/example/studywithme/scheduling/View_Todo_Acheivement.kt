package com.example.studywithme.scheduling

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.studywithme.R
import kotlinx.android.synthetic.main.fragment_calendar_view_todo_acheivement.*

class View_Todo_Acheivement : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_calendar_view_todo_acheivement, container, false)

        // 프래그먼트 버튼에서 오늘 달성률 프래그먼트로 연결하는 코드
        val view_today = view.findViewById<Button>(R.id.button_view_today_acheivement1)
        view_today.setOnClickListener {
            val fragment = View_Today_Acheivement() // Fragment 생성
            val fm = fragmentManager
            val fmt = fm?.beginTransaction()
            fmt?.replace(R.id.content, fragment)?.addToBackStack(null)?.commit()
        }

        // 프래그먼트 버튼에서 월별별 달성률 프래그먼트로 연결하는 코드
        val view_month = view.findViewById<Button>(R.id. button_view_month_acheivement1)
        view_month.setOnClickListener {
            val fragment = View_Month_Acheivement() // Fragment 생성
            val fm = fragmentManager
            val fmt = fm?.beginTransaction()
            fmt?.replace(R.id.content, fragment)?.addToBackStack(null)?.commit()
        }

        return view


    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


}