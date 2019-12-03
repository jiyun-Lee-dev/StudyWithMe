package com.example.studywithme.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.studywithme.R
import com.example.studywithme.data.App
import com.jakewharton.rxbinding2.widget.text
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import android.content.DialogInterface
import android.content.Intent
import android.app.AlertDialog
import android.content.SharedPreferences
import android.widget.ImageButton
import com.example.studywithme.data.App.Companion.prefs
import com.example.studywithme.ui.login.LoginActivity
import android.content.Context.MODE_PRIVATE


class Profile : Fragment() {

    //val userid:String = App.prefs.myUserIdData
    val userid = "test";

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_profile, container, false)

        var profile_username = view.findViewById<TextView>(R.id.profile_username)
        var profile_comment = view.findViewById<TextView>(R.id.profile_comment)
        var name = ""
        var comment = ""

        val url = "http://203.245.10.33:8888/getProfile.php"
        val client = OkHttpClient()

        val requestBody: RequestBody = FormBody.Builder()
            .add("user_id", userid) // 사용자 id
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("응답 fail", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                var result_array: JSONArray = JSONArray(response.body!!.string())
                var size: Int = result_array.length() - 1
                for (i in 0..size) {
                    name = result_array.getJSONObject(i).getString("user_name")
                    comment = result_array.getJSONObject(i).getString("user_comment")
                    Log.d("name", name)
                    Log.d("comment", comment)
                }
            }

        })


        //profile_username.setText(name)
        //Log.d("usernameeee", profile_username.text.toString())
        //profile_comment.setText(comment)

        val logout_btn = view.findViewById<ImageButton>(R.id.toolbar_btn_setting)
        logout_btn.setOnClickListener{
            AlertDialog.Builder(activity/* 해당 액티비티를 가르킴 */)
                .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("로그아웃", DialogInterface.OnClickListener { dialog, whichButton ->
                    val i = Intent(activity, LoginActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(i)
                    //var editor: SharedPreferences.Editor = prefs

                    //var editor=App.prefs.edit().putString(PREF_KEY_MY_USERNAME, value).apply()
                    //val preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
                    //val editor = preferences.edit()
                    //App.prefs.myUserIdData.
                    //editor.commit()
                })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, whichButton -> })
                .show()
        }

        return view
    }


}
