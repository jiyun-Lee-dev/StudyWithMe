package com.example.studywithme.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.TextView

import com.example.studywithme.R
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import android.content.DialogInterface
import android.content.Intent
import android.app.AlertDialog
import com.example.studywithme.ui.login.LoginActivity
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import com.example.studywithme.data.App
import kotlinx.android.synthetic.main.fragment_profile.*
import org.w3c.dom.Text


class Profile : Fragment() {

    var userid=""
    var accoutid=App.prefs.myUserIdData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            userid = bundle.getString("userid")
        }
    }
    @SuppressLint("ResourceAsColor")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view:View = inflater.inflate(R.layout.fragment_profile, container, false)

        val home_add_goal:Button = view.findViewById(R.id.home_add_goal)
        val profile_add_or_edit_btn: Button = view.findViewById(R.id.profile_add_or_edit_btn)
        val profile_username:TextView = view.findViewById(R.id.profile_username)
        val profile_comment:TextView = view.findViewById(R.id.profile_comment)
        val profile_goal_count:TextView = view.findViewById(R.id.profile_goal_count)
        val profile_post_count:TextView = view.findViewById(R.id.profile_post_count)
        val profile_follow_count:TextView = view.findViewById(R.id.profile_follow_count)

        var name=""
        var comment=""
        var goalcount="0"
        var postcount="0"
        var followcount="0"

        if (userid==accoutid){
            home_add_goal.visibility = VISIBLE
            profile_add_or_edit_btn.text = "설정"
            profile_add_or_edit_btn.setBackgroundColor(R.color.editColor)
        }
        else {
            profile_add_or_edit_btn.setOnClickListener(){
                val dialog = AlertDialog.Builder(context)

                dialog
                    .setMessage("친구를 팔로우 했어요!")
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i -> })
                    .show()

                val url = "http://203.245.10.33:8888/recommend/UserFollow.php"
                val follow_id = userid
                val requestBody: RequestBody = FormBody.Builder()
                    .add("user_id", accoutid) // 사용자 id
                    .add("follow_id", follow_id) // 추가할 id
                    .build()

                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()

                val client = OkHttpClient()
                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        Log.d("응답", "팔로우완료")
                    }
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("요청 ", e.toString())
                    }
                })

                profile_add_or_edit_btn.text = "팔로우 취소"

            }
        }

        setHasOptionsMenu(true)
        var toolbarTitle: TextView = activity!!.findViewById(R.id.toolbar_title)

        val profile_goal_username:TextView = view.findViewById(R.id.profile_goal_username)


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
                var size = result_array.length() - 1
                for (i in 0..size) {
                    name = result_array.getJSONObject(i).getString("user_name").toString()
                    comment = result_array.getJSONObject(i).getString("user_comment").toString()
                    goalcount = result_array.getJSONObject(i).getString("goal_count").toString()
                    postcount = result_array.getJSONObject(i).getString("post_count").toString()
                    followcount = result_array.getJSONObject(i).getString("follow_count").toString()
                }
                getActivity()?.runOnUiThread {
                    profile_username.text = name
                    if(comment==""){
                        profile_comment.visibility = GONE
                    }
                    else {
                        profile_comment.text = comment
                    }
                    toolbarTitle.text = name
                    profile_goal_username.text = name + "의 목표 목록"
                    profile_goal_count.text = goalcount
                    profile_post_count.text = postcount
                    profile_follow_count.text = followcount

                }
            }

        })

        return view
    }



    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                fragmentManager!!.beginTransaction()
                    .replace(R.id.content, Recommend())
                    .commitAllowingStateLoss()
            }
            R.id.profile_logout -> {
                AlertDialog.Builder(activity/* 해당 액티비티를 가르킴 */)
                    .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                    .setPositiveButton("로그아웃", DialogInterface.OnClickListener { dialog, whichButton ->
                        val i = Intent(activity, LoginActivity::class.java)
                        //i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(i)
                    })
                    .setNegativeButton("취소",
                        DialogInterface.OnClickListener { dialog, whichButton -> })
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
