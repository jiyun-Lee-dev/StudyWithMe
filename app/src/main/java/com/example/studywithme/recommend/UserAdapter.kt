package com.example.studywithme.recommend

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.studywithme.R
import com.example.studywithme.data.UserRecommend
import kotlinx.android.synthetic.main.recommend_user_item.view.*
import okhttp3.*
import java.io.IOException

class UserAdapter(val context: Context, val items: MutableList<UserRecommend>): RecyclerView.Adapter<UserAdapter.UserViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = UserViewHolder(parent)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        items[position].let { item ->
            with(holder) {
                if (item.img != "") {
                    val resourceId = context.resources.getIdentifier(
                        item.img,
                        "drawable",
                        context.packageName
                    )
                    Log.d("리소스", resourceId.toString())
                    tvImg?.setImageResource(resourceId)
                } else {
                    tvImg?.setImageResource(R.mipmap.ic_launcher)
                }
                tvName.text = item.name
                tvGoal.text = item.goal
            }
        }


        val button = holder.follow_button

        button?.setOnClickListener(View.OnClickListener {
            val dialog = AlertDialog.Builder(context)

            dialog
                .setMessage("친구를 팔로우 했어요!")
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i -> })
                .show()

            val url = "http://203.245.10.33:8888/recommend/UserFollow.php"
            var userid = "test"
            //val userid:String = App.prefs.myUserIdData
            val follow_id:String = items[position].id
            Log.d("follow_id", follow_id)
            val requestBody: RequestBody = FormBody.Builder()
                .add("user_id", userid) // 사용자 id
                .add("follow_id", follow_id) // 추가할 id
                .build()

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val client = OkHttpClient()
            // 요청 전송
            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    Log.d("응답", "팔로우완료")
                    (context as Activity).runOnUiThread {
                        //change View Data
                        items.removeAt(position)
                        notifyItemRemoved(position)
                        notifyDataSetChanged()
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("요청 ", e.toString())
                }
            })


        })


    }

    inner class UserViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.recommend_user_item, parent, false)) {
        val tvImg:ImageView = itemView.recommend_user_img
        val tvName = itemView.recommend_user_name
        val tvGoal = itemView.recommend_user_goal
        val follow_button = itemView.recommend_user_button

    }


}

