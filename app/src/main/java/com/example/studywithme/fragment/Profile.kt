package com.example.studywithme.fragment

import android.annotation.SuppressLint
import android.app.Activity
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
import android.widget.LinearLayout
import com.example.studywithme.data.App
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.internal.notify
import org.w3c.dom.Text


class Profile : Fragment() {

    var userid=""
    var accountid=App.prefs.myUserIdData
    var check = "unfollow"
    var result = "unfollow"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            userid = bundle.getString("userid")
        }

        val checkurl = "http://203.245.10.33:8888/checkFollow.php"
        val checkrequestBody: RequestBody = FormBody.Builder()
            .add("user_id", accountid) // accountId = userid
            .add("follow_id", userid) // userid = followid
            .build()

        val checkrequest = Request.Builder()
            .url(checkurl)
            .post(checkrequestBody)
            .build()

        val checkclient = OkHttpClient()
        checkclient.newCall(checkrequest).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                check = response.body!!.string()
                Log.d("resulttt", check)
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.e("요청 ", e.toString())
            }
        })
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

        val profile_layout_goal:LinearLayout = view.findViewById(R.id.profile_layout_goal)
        val profile_layout_post:LinearLayout = view.findViewById(R.id.profile_layout_post)
        val profile_layout_following:LinearLayout = view.findViewById(R.id.profile_layout_following)

        var name=""
        var comment=""
        var goalcount="0"
        var postcount="0"
        var followcount="0"


        profile_add_or_edit_btn.setOnClickListener(){
            if(userid==accountid){

            }
            else {
                val url = "http://203.245.10.33:8888/recommend/UserFollow.php"
                val follow_id = userid
                val requestBody: RequestBody = FormBody.Builder()
                    .add("user_id", accountid) // 사용자 id
                    .add("follow_id", follow_id) // 추가할 id
                    .build()

                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()

                val client = OkHttpClient()
                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        result = response.body!!.string()
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("요청 ", e.toString())
                    }
                })

                if(profile_add_or_edit_btn.text=="팔로우"){
                    profile_add_or_edit_btn.text = "팔로우 취소"
                    profile_add_or_edit_btn.setBackgroundResource(R.drawable.unfollow_btn_shape)
                }
                else{
                    profile_add_or_edit_btn.text = "팔로우"
                    profile_add_or_edit_btn.setBackgroundResource(R.drawable.follow_btn_shape)
                }
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


        profile_layout_following.setOnClickListener(){
            val fragment = Following()
            val bundle = Bundle(1)
            bundle.putString("userid", userid)
            fragment.arguments=bundle
            val fm = fragmentManager
            val fmt = fm?.beginTransaction()
            fmt?.replace(R.id.content, fragment)?.addToBackStack(null)?.commit()
        }


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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /* button option
        1. 설정
        2. 팔로우
        3. 팔로우 취소
        */

        Log.d("resulttt1", check)
        if (userid==accountid){
            home_add_goal.visibility = VISIBLE
            profile_add_or_edit_btn.text = "설정"
            profile_add_or_edit_btn.setBackgroundResource(R.drawable.setting_btn)
        }
        else if (check.equals("follow")) {
            profile_add_or_edit_btn.text = "팔로우 취소"
            profile_add_or_edit_btn.setBackgroundResource(R.drawable.unfollow_btn_shape)
        }
        /*
        else {
            profile_add_or_edit_btn.setOnClickListener(){

                val dialog = AlertDialog.Builder(context)

                val url = "http://203.245.10.33:8888/recommend/UserFollow.php"
                val follow_id = userid
                val requestBody: RequestBody = FormBody.Builder()
                    .add("user_id", accountid) // 사용자 id
                    .add("follow_id", follow_id) // 추가할 id
                    .build()

                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()

                var result = ""
                val client = OkHttpClient()
                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        result=response.body!!.string()
                        Log.d("resulttt",result)
                    }
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("요청 ", e.toString())
                    }
                })

                Log.d("resulttt2",result)
                if(result.equals("follow")) {
                    dialog
                        .setMessage("친구를 팔로우 했어요!")
                        .setPositiveButton(
                            "OK",
                            DialogInterface.OnClickListener { dialogInterface, i -> })
                        .show()

                    profile_add_or_edit_btn.text = "팔로우 취소"
                    profile_add_or_edit_btn.setBackgroundResource(R.drawable.unfollow_btn_shape)
                }
                else{
                    dialog
                        .setMessage("팔로우를 취소했어요!")
                        .setPositiveButton(
                            "OK",
                            DialogInterface.OnClickListener { dialogInterface, i -> })
                        .show()

                    profile_add_or_edit_btn.text = "팔로우"
                    profile_add_or_edit_btn.setBackgroundResource(R.drawable.follow_btn_shape)
                }

            }
        }
         */

    }
}
