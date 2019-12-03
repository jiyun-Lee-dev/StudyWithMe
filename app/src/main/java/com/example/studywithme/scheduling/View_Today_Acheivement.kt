package com.example.studywithme.scheduling

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import com.example.studywithme.R
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import kotlinx.android.synthetic.main.activity_view_today_acheivement.*
import kotlinx.android.synthetic.main.activity_view_todo_acheivement.*

class View_Today_Acheivement : AppCompatActivity()  {

    fun timeGenerator() :String {

        //오늘 날짜 받아오려고 Calendar 클래스의 객체 생성
        val instance = Calendar.getInstance()
        //Calendar 객체의 일자 상수 얻어서 변수에 넣기
        var date = instance.get(Calendar.DATE).toString()
        return date
    }

    var today_date=timeGenerator()
    var yesterday_date_cal : Int = today_date.toInt()-1
    var yesterday_date : String = yesterday_date_cal.toString()
    var tomorrow_date_cal : Int = today_date.toInt()+1
    var tomorrow_date : String = tomorrow_date_cal.toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_view_today_acheivement)


        date_view1.setText(yesterday_date)
        date_view2.setText(today_date)
        date_view3.setText(tomorrow_date)

        //할일 달성률 보여주기 엑티비티로 이동
        button_view_todo_acheivement2.setOnClickListener {
            val intent = Intent(this, View_Todo_Acheivement::class.java)
            startActivity(intent)
        }

        //월별 달성률 보여주기 액티비티로 이동
        button_view_month_acheivement2.setOnClickListener {
            val intent1 = Intent(this, View_Month_Acheivement::class.java)
            startActivity(intent1)
        }

    }





}

