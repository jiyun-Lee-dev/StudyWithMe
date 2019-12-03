package com.example.studywithme.DialogFragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.example.studywithme.HttpConnection
import com.example.studywithme.R
import com.example.studywithme.bookmark.BookmarkActivity_category

class DialogAddCategory : DialogFragment(){

    private var parent_fragment: Fragment? = null
    var args: Bundle? = null
    var positiveButton: Button? = null
    var negativeButton: Button? = null
    var dialogDetailedWorkSpinner: Spinner? = null
    var httpConn: HttpConnection = HttpConnection()

    fun DialogAddCategory() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.bookmark_popup_createcategory, container, false)
        positiveButton = view.findViewById(R.id.category_dialog_positive)
        negativeButton = view.findViewById(R.id.category_dialog_negative)
        // 레이아웃 인플레이터 선언
        // 띄울 엑티비티 안의 View 컨트롤 하기 위해 인플레이트
        dialogDetailedWorkSpinner = view.findViewById(R.id.bookmark_detailedWorkList)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setOnClickedListener()
    }

    private fun setOnClickedListener(){
        positiveButton!!.setOnClickListener {
            Log.d("카테고리 다이얼로그 응답", "확인")
            var categoryName_string = view!!.findViewById<EditText>(R.id.bookmark_categoryName).text.toString()
            var detailedWork_string = dialogDetailedWorkSpinner!!.selectedItem.toString()
            /* db에 데이터 추가 */
            httpConn.add_categoryData(categoryName_string, detailedWork_string)
            fragmentManager!!.beginTransaction().remove(this).commitAllowingStateLoss()
            fragmentManager!!.popBackStack()
        }
        negativeButton!!.setOnClickListener {
            Log.d("카테고리 다이얼로그 응답", "취소")
            fragmentManager!!.beginTransaction().remove(this).commitAllowingStateLoss()
            fragmentManager!!.popBackStack()
        }
    }
}

