package com.example.studywithme.recommend

import android.content.Context
import android.databinding.DataBindingUtil.setContentView
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.studywithme.R
import com.example.studywithme.data.InfoRecommend
import kotlinx.android.synthetic.main.fragment_info.*
import okhttp3.*
import org.json.JSONArray
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException

class InfoFrag : Fragment() {

    val goalList = ArrayList<String>()
    val infoList = mutableListOf<InfoRecommend>()
    //val userid:String = App.prefs.myUserIdData
    var chosenGoal:String = "%"
    var userid = "test"

    var query = "텟슷흐중"
    var weburl = "https://www.google.com/search?q=" + query + "&num=30"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_info, container, false)

        MyAsyncTask().execute(weburl)

        /*
        //아이템 사이에 구분선 넣어 주기
        rv_info_list.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        */


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

        val spinner = view.findViewById<Spinner>(R.id.info_goal_spinner)
        val items = ArrayAdapter(activity, android.R.layout.simple_spinner_item, goalList)
        items.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.adapter = items
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                infoList.clear()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                chosenGoal = goalList[position]
                Log.d("goal", chosenGoal)
                //query=chosenGoal
                //MyAsyncTask().execute(weburl)
            }
        }

        return view
    }



    inner class MyAsyncTask: AsyncTask<String, String, String>(){ //input, progress update type, result type
        private var result : String = ""

        override fun onPreExecute() {
            super.onPreExecute()
            //progressBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): String {
            val doc: Document = Jsoup.connect("$weburl").get()
            val link: Elements = doc.select("div.rc")

            link.forEachIndexed { index, elem ->
                val header: String = elem.select("span.S3Uucc").text()
                val content: String = elem.select("span.st").text()
                val url: String = elem.select("a").attr("href")
                //Log.d(TAG, "$index: $header / $content / $url")

                var newInfoItem =
                    InfoRecommend(
                        header,
                        content,
                        url
                    )
                infoList.add(newInfoItem)

                getActivity()?.runOnUiThread {
                    // 어댑터에 데이터변경사항 알리기
                    rv_info_list.adapter?.notifyDataSetChanged()
                }
            }

            return doc.title()
        }

        override fun onPostExecute(result: String?) {
            //progressBar.visibility = View.GONE



            val activity = activity as Context
            var adapter = InfoAdapter(activity, infoList)
            rv_info_list.setAdapter(adapter)
            rv_info_list.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)

        }
    }

}