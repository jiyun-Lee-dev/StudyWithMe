package com.example.studywithme.signup

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.example.studywithme.R


class SignUpActivity: AppCompatActivity() {

    private var RegisterURL="https://127.0.0.1/register.php"
    private var id_sign: EditText? = null
    private var password_sign: EditText? = null
    private var name_sign: EditText? = null
    private var btn_signup: Button? = null
    val loading: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

    }

}