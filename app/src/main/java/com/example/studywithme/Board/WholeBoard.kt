package com.example.studywithme.Board

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.AdapterView
import com.example.studywithme.R
import kotlinx.android.synthetic.main.wholeboard.*
import android.widget.*
import android.view.View
import com.bumptech.glide.Glide.init
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

class WholeBoard:AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wholeboard)


        btn_myboard.setOnClickListener {
            val intent = Intent(this@WholeBoard, MyBoard::class.java)
            startActivity(intent)
        }


    }
}

