package com.example.studywithme

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.studywithme.bookmark.BookmarkActivity_main
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        jiyun.setOnClickListener {
            val nextIntent = Intent(this, BookmarkActivity_main::class.java)
            startActivity(nextIntent)
        }
    }

}
