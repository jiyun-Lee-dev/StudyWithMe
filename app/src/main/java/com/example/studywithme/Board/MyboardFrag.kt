package com.example.studywithme.Board

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.studywithme.R
import kotlinx.android.synthetic.main.fragment_myboard.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class MyboardFrag : Fragment(), AdapterView.OnItemSelectedListener {

    var list_of_goal = ArrayList<String>()

    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_myboard, container, false)

        list_of_goal.add("전체 선택")
        val url = "http://203.245.10.33:8888/goal.php"
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("응답 fail", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                var str_response = response.body!!.string()
                val jsonobj: JSONObject = JSONObject(str_response)
                //create json array
                var json_array: JSONArray = jsonobj.getJSONArray("goal_list")
                var i: Int = 0
                var size: Int = json_array.length()

                for (i in 0 until size) {
                    var json_objdetail: JSONObject = json_array.getJSONObject(i)
                    var goal = json_objdetail.getString("goal_name")
                    list_of_goal.add(goal)
                }
            }
        })


        val spinner = view.findViewById<Spinner>(R.id.spinner_goal)
        spinner?.prompt = "목표를 선택해주세요"
        spinner?.onItemSelectedListener = this
        val items = ArrayAdapter(activity,android.R.layout.simple_spinner_item,list_of_goal)
        items.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.adapter = items



        val write_button = view.findViewById<Button>(R.id.write_button)
        write_button.setOnClickListener{
            val intent= Intent(activity, Write_Board::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        // use position to know the selected item
        goal_text.text="Selected:"+list_of_goal[position]
    }

    override fun onNothingSelected(arg0: AdapterView<*>) {

    }
}
