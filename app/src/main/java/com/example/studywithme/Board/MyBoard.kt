package com.example.studywithme.Board

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import com.example.studywithme.R
import kotlinx.android.synthetic.main.myboard.*

class MyBoard : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.myboard)


        /*spinner_goal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }*/
        write_button.setOnClickListener{

            val intent= Intent(this, Write_Board::class.java)
            startActivity(intent)
        }





    }
}
