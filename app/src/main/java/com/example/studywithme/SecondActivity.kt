package com.example.studywithme

import android.content.Intent
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.FrameLayout
import com.example.studywithme.Board.MyBoard
import com.example.studywithme.recommend.UserFrag
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        //본인이 넘어가고 싶은 화면이 있으면 저  secondactivity 부분에 넘어가려는 클래스 이름 적으면 됨
        yejin.setOnClickListener {
            val intent_yejin = Intent(this, MyBoard::class.java)
            startActivity(intent_yejin)
        }

        jinju.setOnClickListener {
            val intent_jinju = Intent(this, Jinju::class.java)
            startActivity(intent_jinju)
        }

    }


}



