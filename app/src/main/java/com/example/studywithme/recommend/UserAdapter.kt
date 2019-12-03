package com.example.studywithme.recommend

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.studywithme.R
import com.example.studywithme.data.UserRecommend
import kotlinx.android.synthetic.main.recommend_user_item.view.*
/*
class UserAdapter : RecyclerView.Adapter<UserAdapter.MainViewHolder>() {

    var items: MutableList<UserRecommend> = mutableListOf(UserRecommend("s", "name1", "goal1"),
        UserRecommend("img2", "name2", "goal2"),UserRecommend("img3", "name3", "goal"))

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = MainViewHolder(parent)


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holer: MainViewHolder, position: Int) {
        items[position].let { item ->
            with(holer) {
                if (UserRecommend.img != "") {
                    val resourceId = context.resources.getIdentifier(UserRecommend.img, "drawable", context.packageName)
                    recommend_user_img?.setImageResource(resourceId)
                } else {
                    recommend_user_img?.setImageResource(R.mipmap.ic_launcher)
                }
                name.text = item.name
                goal.text = item.goal
            }
        }
    }

    inner class MainViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.recommend_user_item, parent, false)) {
        val img = itemView.recommend_user_img
        val name = itemView.recommend_user_name
        val goal = itemView.recommend_user_goal
    }
}

 */