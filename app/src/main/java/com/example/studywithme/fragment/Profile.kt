package com.example.studywithme.fragment

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


class Profile : Fragment() {

    //val userid:String = App.prefs.myUserIdData
    val userid = "test"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view:View = inflater.inflate(R.layout.fragment_profile, container, false)

        val profile_username:TextView = view.findViewById(R.id.profile_username)
        val profile_comment:TextView = view.findViewById(R.id.profile_comment)

        var name=""
        var comment=""

        setHasOptionsMenu(true)
        var toolbarTitle: TextView = activity!!.findViewById(R.id.toolbar_title)


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
                }
                getActivity()?.runOnUiThread {
                    toolbarTitle.text = name
                    profile_username.text = name
                    profile_comment.text = comment
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
                    .replace(R.id.content, Home())
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



/*
    inner class MyAsyncTask: AsyncTask<Void, String, String>(){ //input, progress update type, result type
        private var result : String = ""

        override fun onPreExecute() {
            super.onPreExecute()
            //progressBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: Void?): String {
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
                        var name = result_array.getJSONObject(i).getString("user_name").toString()
                        var comment = result_array.getJSONObject(i).getString("user_comment").toString()
                        Log.d("name", name)
                        Log.d("comment", comment)
                    }
                }

            })


            return result
        }

        override fun onPostExecute(result: String?) {
            //progressBar.visibility = View.GONE
            //toolbarTitle.text = profileList.get(1).name
            //profile_username.text = profileList.get(1).name
            //profile_comment.text = profileList[1].comment

        }
    }
 */


}
