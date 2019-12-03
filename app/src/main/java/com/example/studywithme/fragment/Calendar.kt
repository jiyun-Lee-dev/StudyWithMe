package com.example.studywithme.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.studywithme.R
import com.example.studywithme.scheduling.AddGoalDialog
import com.example.studywithme.scheduling.View_Todo_Acheivement
import kotlinx.android.synthetic.main.activity_view_todo_acheivement.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class Calendar :DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_calendar, container, false)

        // 상단바 이름 바꾸기
        var toolbarTitle: TextView = activity!!.findViewById(R.id.toolbar_title)
        toolbarTitle.text = "캘린더"

        val url = "http://10.0.2.2:8080/getjson.php"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("응답 fail", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                var data = response?.body?.string()
                var root = JSONArray(data)
                Log.d("goal_table", root.getJSONObject(0).toString())
            }
        })

        fun getJsonObject(jsonString: String) {
            val jObject = JSONObject(jsonString)
            var goal_table = jObject.getString("use_id")
            Log.d("json객체출력(goal_table", goal_table)
        }


        val button_view_todo_acheivement =
            view.findViewById<Button>(R.id.button_view_todo_acheivement)
        button_view_todo_acheivement.setOnClickListener {
            val intent = Intent(activity, View_Todo_Acheivement::class.java)
            startActivity(intent)
        }

        val button_add_goal = view.findViewById<Button>(R.id.button_add_goal)
        button_add_goal.setOnClickListener {

            val myDialogFragment = AddGoalDialog()
            myDialogFragment.setTargetFragment(this, 0)
            myDialogFragment.show(fragmentManager!!, "Search Filter")

            //val builder = AlertDialog.Builder(activity)
            //val dialogView = layoutInflater.inflate(R.layout.activity_goal_add, null)
            //val dialogText = dialogView.findViewById<EditText>(R.id.write_name_goal)
            //val dialogText2 = dialogView.findViewById<EditText>(R.id.write_dday_goal)

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

        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
