package com.example.studywithme.scheduling

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.example.studywithme.R
import kotlinx.android.synthetic.main.activity_view_todo_acheivement.*

class View_Todo_Acheivement : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_view_todo_acheivement)


        //오늘 달성률 보여주기 엑티비티로 이동
        button_view_today_acheivement1.setOnClickListener {
            val intent = Intent(this, View_Today_Acheivement::class.java)
            startActivity(intent)
        }

        //월별 달성률 보여주기 액티비티로 이동
        button_view_month_acheivement1.setOnClickListener {
            val intent1 = Intent(this, View_Month_Acheivement::class.java)
            startActivity(intent1)
        }





    }


}