package com.example.studywithme.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.studywithme.DialogFragment.DialogAddCategory
import com.example.studywithme.DialogFragment.DialogAddTodayDetailedWork
import com.example.studywithme.HttpConnection
import com.example.studywithme.MainActivity
import com.example.studywithme.R
import com.example.studywithme.bookmark.BookmarkActivity_category
import com.example.studywithme.data.App
import com.example.studywithme.scheduling.*
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.utils.ColorTemplate
import io.reactivex.internal.util.BackpressureHelper.add
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_home.*
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import lecho.lib.hellocharts.view.PieChartView
import okhttp3.*
import okio.Utf8
import org.json.JSONArray
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class Home : Fragment() {

    var activity: MainActivity? = null
    val userID = App.prefs.myUserIdData
    val userName = App.prefs.myUserNameData
    var todayDetailedWorkPieChart: PieChartView? = null
    var pieData = arrayListOf<SliceValue>()
    var pieChartData: PieChartData? = null
    var todayDetailCreateBtn: Button? = null
    var todayDetailListAdapter: Detail_list_adapter? = null
    var todayDetailList = arrayListOf<Detail_list_data>()
    var todayDetailListrecyclerview: RecyclerView? = null
    var todayDetailListLinearLayoutManager: LinearLayoutManager? = null
    var getTodayDetailResult: String? = null
    var httpConn: HttpConnection = HttpConnection()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = getActivity() as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        // 상단바 메뉴 쓰는 것으로 설정
        setHasOptionsMenu(true)

        // 상단바 이름 바꾸기
        var toolbarTitle: TextView = activity!!.findViewById(R.id.toolbar_title)
        toolbarTitle.text = "StudyWithMe"

        todayDetailListAdapter = Detail_list_adapter(view.context, todayDetailList, this)
        todayDetailListrecyclerview = view!!.findViewById(R.id.home_today_list)
        todayDetailListLinearLayoutManager = LinearLayoutManager(view.context)
        todayDetailCreateBtn = view.findViewById<Button>(R.id.home_create_today_detailedWork_btn)

        todayDetailedWorkPieChart = view.findViewById(R.id.home_today_detailedWork_piechart)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // 클릭 이벤트 리스너 붙이기
        setOnClickedListener()
        todayDetailListrecyclerview!!.adapter = todayDetailListAdapter
        todayDetailListrecyclerview!!.layoutManager = todayDetailListLinearLayoutManager
        todayDetailListrecyclerview!!.setHasFixedSize(true)
        getTodayDetailResult = httpConn.get_todayDetailData()
        if (getTodayDetailResult != null){
            update_todayDetailList_and_pieChart(getTodayDetailResult)
        }
    }


    override fun onDetach() {
        super.onDetach()
        activity = null
    }


    private fun setOnClickedListener() {
        todayDetailCreateBtn!!.setOnClickListener {
            show_createTodayDetailDialog()
        }
    }

    // todo리스트 추가 다이얼로그 띄우는 함수
    private fun show_createTodayDetailDialog(){
        var dialog: DialogAddTodayDetailedWork = DialogAddTodayDetailedWork()
        this.fragmentManager!!.beginTransaction()
            .replace(R.id.content, dialog)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // menu에 있는 메뉴 클릭 시 이벤트 처리
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.home_profile -> {
                val fragment = Profile()
                val bundle = Bundle(1)
                bundle.putString("userid", userID)
                fragment.arguments=bundle
                val fm = fragmentManager
                val fmt = fm?.beginTransaction()
                fmt?.replace(R.id.content, fragment)?.addToBackStack(null)?.commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun update_todayDetailList_and_pieChart(resultData: String?) {
        var doneWorkCount: Int = 0
        if (resultData == "실패"){
            // 일단 보류. 딱히 할게 없음
        } else {
            var result_array: JSONArray = JSONArray(resultData)
            var jsonobj_index = result_array.length() - 1
            // db에서 가져온 데이터가 0 이상일 때만 카테고리 리스트 한번 리셋하고 리스트 업데이트
            if (jsonobj_index >= 0){
                todayDetailList.removeAll(todayDetailList)
                for (i in 0..jsonobj_index){
                    var todayDetailName = result_array.getJSONObject(i).getString("detail_name").toString()
                    var todayDetailDone = result_array.getJSONObject(i).getString("done").toInt()
                    var todayDetailDate = result_array.getJSONObject(i).getString("date").toString()
                    if (todayDetailDone == 1){
                        doneWorkCount++
                        Log.d("응답", doneWorkCount.toString())
                    }
                    /* 카테고리 리스트에 아이템 추가 */
                    var newDetailItem =
                        Detail_list_data(
                            todayDetailName,
                            todayDetailDate,
                            todayDetailDone
                        )
                    todayDetailList.add(newDetailItem)
                }
                // 백그라운드에서 돌기 때문에 메인쓰레드로 ui에 접근할 수 있도록 해줘야 한다.
                activity!!.runOnUiThread {
                    // 어댑터에 데이터변경사항 알리기
                    todayDetailListrecyclerview!!.adapter?.notifyDataSetChanged()
                }
            }
            // 파이 차트 갱신
            var done_rate = ( doneWorkCount.toFloat() / result_array.length() ) * 100
            Log.d("응답", done_rate.toString())
            changeTodayPieChart(done_rate)
        }

    }

    fun changeTodayPieChart(doneRate: Float){
        Log.d("파이 차트 갱신 응답", doneRate.toString())
        if (pieData.size > 0){
            pieData.removeAll(pieData)
        }
        // 오늘 할일 관련 달성률 데이터 (list에 들어가는 데이터 값은 두가지 달성한거, 달성하지 못한거)
        pieData.add(SliceValue(doneRate, Color.parseColor("#83cac8")))
        pieData.add(SliceValue(100 - doneRate,Color.parseColor("#e0e0e0") ))

        pieChartData = PieChartData(pieData)
        pieChartData!!.setHasCenterCircle(true).setCenterText1(doneRate.toInt().toString())
        todayDetailedWorkPieChart!!.pieChartData = pieChartData
    }


}
