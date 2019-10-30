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

import com.example.studywithme.R


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

        val builder = AlertDialog.Builder(context!!)
            .setTitle("Add Goal")
            .setView(view)
            .setPositiveButton(android.R.string.ok) { _, _ ->
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
