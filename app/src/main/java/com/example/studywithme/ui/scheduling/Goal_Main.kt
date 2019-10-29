package com.example.studywithme.ui.scheduling


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.studywithme.R
import com.example.studywithme.ui.scheduling.View_Todo_Acheivement
import kotlinx.android.synthetic.main.activity_goal_main.*
import kotlinx.android.synthetic.main.activity_view_todo_acheivement.*
import java.io.IOException
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Callback
import okhttp3.Call
import org.json.JSONArray
import org.json.JSONObject
import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.TextView


class Goal_Main : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_main)

        val url="http://10.0.2.2:8080/getjson.php"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object:Callback {
            override fun onFailure(call: Call, e: IOException){
                Log.e("응답 fail",e.toString())
            }
            override fun onResponse(call: Call, response:Response){
                var data = response?.body?.string()
                var root = JSONArray(data)
                Log.d("goal_table",root.getJSONObject(0).toString())
            }
        })

        fun getJsonObject(jsonString: String){
            val jObject = JSONObject(jsonString)
            var goal_table = jObject.getString("use_id")
            Log.d("json객체출력(goal_table", goal_table)
        }


        button_view_todo_acheivement.setOnClickListener {
            val intent = Intent(this, View_Todo_Acheivement::class.java)
            startActivity(intent)

        }

        button_add_goal.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.activity_goal_add, null)
            val dialogText = dialogView.findViewById<EditText>(R.id.write_name_goal)
            val dialogText2 = dialogView.findViewById<EditText>(R.id.write_dday_goal)

/*
            builder.setView(dialogView)
                .setPositiveButton("확인") { dialogInterface, i ->
                    to_do1.setText = dialogText.text.toString()
                    to_do_dday1.setTExt = dialogText2.inputType
                    /* 확인일 때 main의 View의 값에 dialog View에 있는 값을 적용 */

                }
                .setNegativeButton("취소") { dialogInterface, i ->
                    /* 취소일 때 아무 액션이 없으므로 빈칸 */
                }
                .show() */
        }
    }





}



