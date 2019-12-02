package com.example.studywithme.scheduling


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

import com.example.studywithme.R
import kotlinx.android.synthetic.main.fragment_calendar.*
import com.example.studywithme.fragment.Calendar




class AddGoalDialog : DialogFragment() {

    companion object {
        private const val FRAGMENT_TAG = "custom_dialog"

        fun newInstance() = AddGoalDialog()

        fun show(fragmentManager: FragmentManager): AddGoalDialog {
            val dialog = newInstance()
            // dialog.isCancelable = false
            dialog.show(fragmentManager, FRAGMENT_TAG)
            return dialog
        }
    }

    private lateinit var customView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return customView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // return super.onCreateDialog(savedInstanceState)

        val view = activity!!.layoutInflater.inflate(R.layout.fragment_add_goal_dialog, null)
        customView = view

        val set_goal_name=view.findViewById<EditText>(R.id.write_name_goal)
        val set_goal_dday=view.findViewById<EditText>(R.id.write_dday_goal)



        val builder = AlertDialog.Builder(context!!)
            .setTitle("Add Goal")
            .setView(view)
            .setPositiveButton(android.R.string.ok) { _, _ ->


                val fragment = Calendar() // Fragment 생성
                val bundle = Bundle() // 파라미터는 전달할 데이터 개수
                bundle.putString("goal_name", "hi") // key , value

                val fm = fragmentManager
                val fmt = fm?.beginTransaction()
                fragment.setArguments(bundle)
                fmt?.replace(R.id.content, fragment)?.addToBackStack(null)?.commit()

                /*
                val Goal_name=findViewById<TextView>(R.id.Goal_name)
                Goal_name.setText("hi");
                Goal_name.text=set_goal_name.text.toString()
                D_day.text="123"
                D_day.text=set_goal_dday.text.toString()
                */

                // do something
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                // do something
            }

        val dialog = builder.create()

        // optional
        dialog.setOnShowListener {
            // do something
        }

        return dialog
    }

}
