package com.example.studywithme.recommend

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.studywithme.R
import com.example.studywithme.data.UserRecommend
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.studywithme.data.App
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class UserFrag : Fragment(){

    val goalList = ArrayList<String>()
    val userList = mutableListOf<UserRecommend>()
    //val userid:String = App.prefs.myUserIdData
    val user:String = App.prefs.myUserIdData

    var chosenGoal:String = ""
    var userid = "test"



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_user, container, false)
        val recyclerview = view.findViewById(R.id.rv_user_list) as RecyclerView

        Log.d("prefs", user)
        goalList.add("전체 보기")

        fun getUserList() {
            userList.clear()

            if (chosenGoal == "전체 보기") chosenGoal = "%"

            val user_url = "http://203.245.10.33:8888/recommend/getUserList.php"
            val user_requestBody: RequestBody = FormBody.Builder()
                .add("user_id", userid) // 사용자 id
                .add("goal_name", chosenGoal) // 선택한 목표
                .build()

            val user_request = Request.Builder()
                .url(user_url)
                .post(user_requestBody)
                .build()

            val user_client = OkHttpClient()

            user_client.newCall(user_request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("요청 ", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    var result_array: JSONArray = JSONArray(response.body!!.string())
                    var size = result_array.length() - 1

                    /*
                    if(userList.size >= 0)
                       userList.removeAll(userList)
                    */

                    for (i in 0..size) {
                        var UserId =
                            result_array.getJSONObject(i).getString("user_id").toString()
                        var UserName =
                            result_array.getJSONObject(i).getString("user_name").toString()

                        var GoalName =
                            result_array.getJSONObject(i).getString("goal_name").toString()

                        var newUserItem =
                            UserRecommend(
                                "",
                                UserId,
                                UserName,
                                GoalName
                            )

                        Log.d("userid",UserId)
                        Log.d("username", UserName)
                        Log.d("GoalName", GoalName)
                        userList.add(newUserItem)
                    }

                    getActivity()?.runOnUiThread {
                        // 어댑터에 데이터변경사항 알리기
                        recyclerview.adapter?.notifyDataSetChanged()
                    }

                }
            })

            val activity = activity as Context
            recyclerview.adapter = UserAdapter(activity, userList)
            recyclerview.layoutManager = GridLayoutManager(activity, 2)

        }

        getUserList()


        /* user별 goal list spinner */
        val goal_url = "http://203.245.10.33:8888/recommend/getGoalList.php"
        val goal_client = OkHttpClient()

        val goal_requestBody: RequestBody = FormBody.Builder()
            .add("user_id", userid) // 사용자 id
            .build()

        val goal_request = Request.Builder()
            .url(goal_url)
            .post(goal_requestBody)
            .build()

        goal_client.newCall(goal_request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("응답 fail", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                var goal_result_array: JSONArray = JSONArray(response.body!!.string())
                var size: Int = goal_result_array.length() - 1
                for (i in 0..size) {
                    var goal = goal_result_array.getJSONObject(i).getString("goal_name")
                    goalList.add(goal)
                }
            }
        })

        //val spinner = view.findViewById(R.id.goal_spinner)
        val spinner:Spinner=view.findViewById(R.id.user_goal_spinner)
        val items = ArrayAdapter(activity, android.R.layout.simple_spinner_item, goalList)
        items.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.adapter = items
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                userList.clear()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                chosenGoal = goalList[position]
                getUserList()
            }
        }


        return view
    }


}