package com.example.studywithme.scheduling

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import com.example.studywithme.R
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.view.View
import kotlinx.android.synthetic.main.activity_view_month_acheivement.*
import kotlinx.android.synthetic.main.activity_view_today_acheivement.*
import kotlinx.android.synthetic.main.activity_view_todo_acheivement.*
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter


class View_Month_Acheivement : AppCompatActivity()  {


    //년도 계산 함수
    fun yearGenerator() :String {

        //오늘 년도 받아오려고 Calendar 클래스의 객체 생성
        val instance = Calendar.getInstance()
        //Calendar 객체의 년도 상수 얻어서 변수에 넣기
        var year = instance.get(Calendar.YEAR).toString()
        return year
    }

    //월 계산 함수
    fun monthGenerator() :String {

        //오늘 년도 받아오려고 Calendar 클래스의 객체 생성
        val instance = Calendar.getInstance()
        //Calendar 객체의 월 상수 얻어서 변수에 넣기
        var month = instance.get(Calendar.MONTH).toString()
        return month
    }


    var today_year=yearGenerator()
    var today_month_to_cal=monthGenerator()
    var today_month_cal : Int = today_month_to_cal.toInt()+1
    var today_month : String = today_month_cal.toString()
    var last_month_cal : Int = today_month.toInt()-1
    var last_month : String = last_month_cal.toString()
    var next_month_cal : Int = today_month.toInt()+1
    var next_month : String = next_month_cal.toString()


    //dday 계산 연습
    var practice_data ="2019-11-08"
    var change_data = LocalDate.parse(practice_data, DateTimeFormatter.ISO_DATE)
    var localdate = LocalDate.now()
    var period = change_data.toEpochDay() - localdate.toEpochDay()





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_view_month_acheivement)


        monthandyear_view_now.setText(today_year+"년 "+today_month+"월")
        month_view_last.setText("◀◀"+last_month+"월")
        month_view_next.setText(next_month+"월"+"▶▶")
        no_meaning.setText(period.toString())


        //할일 달성률 보여주기 엑티비티로 이동
        button_view_todo_acheivement3.setOnClickListener {
            val intent = Intent(this, View_Todo_Acheivement::class.java)
            startActivity(intent)
        }

        //오늘 달성률 보여주기 액티비티로 이동
        button_view_today_acheivement3.setOnClickListener {
            val intent1 = Intent(this, View_Today_Acheivement::class.java)
            startActivity(intent1)
        }



    }





}

