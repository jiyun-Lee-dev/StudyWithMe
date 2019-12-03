package com.example.studywithme.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.studywithme.R

import com.example.studywithme.scheduling.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class Calendar : Fragment() {

    /* 골 리스트 선언*/
    var todoList = arrayListOf<Todo_list_data>()
    var itemcnt1 = 0
    var todoListAdapter: Todo_list_adapter? = null
    // test용 사용자 아이디는 0으로 임시 설정했음
    val userID = "1"
    val done = 1 //일단 1으로 설정




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 홈에서 골네임 받아오기
        val Goal_name = view?.findViewById<TextView>(R.id.Goal_name)
        val args = getArguments()
        if (args != null) {
            //val mArgs = arguments
            var goal_name = args?.getString("goal_name")
            Log.d("get", goal_name)
            Goal_name?.setText(goal_name)
            Log.d("Goal_name", Goal_name?.text.toString())
           }
        // 홈에서 디데이 받아오기
        val Goal_day=view?.findViewById<TextView>(R.id.Goal_day)
        val args1=getArguments()
        if (args1 != null) {
            //val mArgs = arguments
            var goal_day= args1?.getString("d_day")
            Log.d("get", goal_day)
            Goal_day?.setText("D-" + goal_day)
            Log.d("Goal_day", Goal_day?.text.toString())
        }

    }




        override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view: View = inflater.inflate(R.layout.fragment_calendar, container, false)

        // RecyclerView 사용할 것. 어댑터를 생성
        todoListAdapter = Todo_list_adapter(view.context, todoList)

        /* 목표 추가 다이얼로그. */
        // 익명 객체 전달. 람다 형식.
        val home_add_todo_createBtn = view.findViewById<Button>(R.id.button_calendar_add_todo)
        home_add_todo_createBtn.setOnClickListener {

            /* 지윤이꺼 토대로 한 다이얼로그 빌더*/
            // 다이얼로그 빌더
            val add_todo_dialogBuilder = AlertDialog.Builder(view.context)

            // 레이아웃 인플레이터 선언
            //val bookmark_dialog : LayoutInflater = LayoutInflater.from(this)
            // 띄울 엑티비티 안의 View 컨트롤 하기 위해 인플레이트
            val add_todo_dialogView =
                layoutInflater.inflate(R.layout.fragment_add_todo_dialog, null)
            add_todo_dialogBuilder.setView(add_todo_dialogView)

                .setPositiveButton("확인") { dialogInterface, i ->

                    var dialog_todoName_string = add_todo_dialogView.findViewById<EditText>(R.id.write_name_todo).text.toString()
                    var dialog_dday_string = add_todo_dialogView.findViewById<EditText>(R.id.write_dday_todo).text.toString()


                    //var dialog_detailedWork_string = dialog_detailedWork_spinner.selectedItem.toString()
                    /* db에 데이터 추가 */
                    add_todoData_to_DB(userID,dialog_todoName_string,dialog_dday_string,done)

                    //추가하기전에
                    /*dday 계산해주기
                     dday바꿔주는 부분
                     받은 dday 형식은 string이므로 이걸 date 형식으로 바꿔줌*/
                    var datechange = LocalDate.parse(dialog_dday_string, DateTimeFormatter.ISO_DATE)
                    var localdate = LocalDate.now()
                    var period = datechange.toEpochDay() - localdate.toEpochDay()
                    var cal_dday = period.toString() //dday 계산*/

                    // 카테고리 리스트에 아이템 추가
                    var newTodoItem =
                        Todo_list_data(
                            dialog_todoName_string,
                            cal_dday,
                            done
                        )
                    todoList.add(newTodoItem)
                    // 어댑터에 데이터변경사항 알리기

                    //todo_list_recyclerview.adapter?.notifyDataSetChanged()
                    todo_list_recyclerview.adapter?.notifyDataSetChanged()

                }
                .setNegativeButton("취소") { dialogInterface, i ->
                    /*취소하면 실행해줄 거 없음*/
                }
            add_todo_dialogBuilder.show()
        }

        /* 카테고리 리스트 생성 */
        //어떤 데이터(ArrayList)와 어떤 RecylcerView를 쓸 것인지 설정한다.
        val todo_list_recyclerview = view.findViewById<RecyclerView>(R.id.todo_list_recyclerview)
        todo_list_recyclerview.adapter = todoListAdapter

        val todo_list_linearLayoutManager = LinearLayoutManager(view.context)
        todo_list_recyclerview.layoutManager = todo_list_linearLayoutManager

        todo_list_recyclerview.setHasFixedSize(true)
        // db에서 카테고리 데이터 가져오기
        getTodoList_from_DB("search_todo")

        //할일별 달성률 보여주기 액티비티로 이동
        val button_view_todo_acheivement =
            view.findViewById<Button>(R.id.button_view_todo_acheivement)
        button_view_todo_acheivement.setOnClickListener {
            val intent = Intent(activity, View_Todo_Acheivement::class.java)
            startActivity(intent)
        }

        //오늘 달성률 보여주기 액티비티로 이동
        val button_view_today_acheivement =
            view.findViewById<Button>(R.id.button_view_today_acheivement)
        button_view_today_acheivement.setOnClickListener {
            val intent = Intent(activity, View_Today_Acheivement::class.java)
            startActivity(intent)
        }

        //이번달_달성률 보여주기 액티비티로 이동
        val button_view_month_acheivement =
            view.findViewById<Button>(R.id.button_view_month_acheivement)
        button_view_month_acheivement.setOnClickListener {
            val intent = Intent(activity, View_Month_Acheivement::class.java)
            startActivity(intent)
        }


//        //목표 추가
//        val button_add_goal = view.findViewById<Button>(R.id.button_calendar_add_todo)
//        button_add_goal.setOnClickListener {
//
//            val myDialogFragment = AddGoalDialog()
//            myDialogFragment.setTargetFragment(this, 0)
//            myDialogFragment.show(fragmentManager!!, "Search Filter")
//
//        }

//        //할일 추가
//        val button_add_todo = view.findViewById<Button>(R.id.button_calendar_add_todo)
//        button_add_todo.setOnClickListener {
//
//            val myDialogFragment1 = AddTodoDialog()
//            myDialogFragment1.setTargetFragment(this, 0)
//            myDialogFragment1.show(fragmentManager!!, "Search Filter")
//
//        }

        return view
    }

    // 카테고리 데이터 db 추가
    //userID,dialog_todoName_string,dialog_dday_string,done
    fun add_todoData_to_DB(user_id: String, todo_name: String, dday: String, done : Int) {
        // url 만들기
        val url = "http://10.0.2.2/insert_todo.php"
        val goal_id = 0 // 일단 0으로 설정
        // 데이터를 담아 보낼 바디 만들기
        val requestBody: RequestBody = FormBody.Builder()
            .add("user_id",user_id) // user_id 일단 임의로 1으로 저장
            .add("goal_id",goal_id.toString())
            .add("todo_content",todo_name)
            .add("dday",dday)
            .add("done",done.toString())
            .build()
        // okhttp request를 만든다.
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        // 클라이언트 생성
        val client = OkHttpClient()
        // 요청 전송
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                Log.d("요청", "요청 하이2")
            }
            override fun onFailure(call: Call, e: IOException) {
                Log.d("요청", "요청 그만2")
            }
        })
    }

    // db에서 php로 데이터 가져오기 (+userID 체크 추가)
    fun getTodoList_from_DB(phpName: String) {
        // url 만들기
        val url = "http://10.0.2.2/" + phpName + ".php"
        // POST로 보낼 데이터 설정
        // 데이터를 담아 보낼 바디 만들기
        val requestBody: RequestBody = FormBody.Builder()
            .add("user_id", userID) // user_id 일단 임의로 1 저장
            .build()
        // okhttp request를 만든다.
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        // 클라이언트 생성
        val client = OkHttpClient()
        // 요청 전송
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("요청 ", e.toString())
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: Response) {
                var data_todo = response?.body?.string().toString()
                var result_array_todo: JSONArray = JSONArray(data_todo)
                var jsonobjCnt_todo = result_array_todo.length() - 1
                if (jsonobjCnt_todo >= 0){
                    todoList.removeAll(todoList)
                    for (i in 0..jsonobjCnt_todo) {
                        // 목표 이름
                        var todoName = result_array_todo.getJSONObject(i).getString("todo_content").toString()
                        var dday = result_array_todo.getJSONObject(i).getString("dday").toString()
                        var done = 1

                        //dday바꿔주는 부분
                        //받은 dday 형식은 string이므로 이걸 date 형식으로 바꿔줌
                        var datechange = LocalDate.parse(dday, DateTimeFormatter.ISO_DATE)
                        var localdate = LocalDate.now()
                        var period = datechange.toEpochDay() - localdate.toEpochDay()
                        var cal_dday = period.toString() //dday 계산 */
                        /* 목표 리스트에 아이템 추가 */
                        var newTodoItem =
                            Todo_list_data(
                                todoName,
                                cal_dday,
                                done
                            )
                        todoList.add(newTodoItem)
                    }
                    // 백그라운드에서 돌기 때문에 메인쓰레드로 ui에 접근할 수 있도록 해줘야 한다.
                    getActivity()!!.runOnUiThread {
                        // 어댑터에 데이터변경사항 알리기
                        itemcnt1 = todoListAdapter!!.getItemCount()
                        todo_list_recyclerview.adapter?.notifyDataSetChanged()
                        // todolist에 아이템이 하나도 없을 경우 안내메시지 view에 출력
                        Log.d("요청item ", itemcnt1.toString())

                        if (itemcnt1 <= 0) {
                            scheduling_todo_list_has_no_item_msg.text = "아직 할일이 없습니다. \n할일을 추가해보시죠!"
                            scheduling_todo_list_has_no_item_msg.visibility = View.VISIBLE
                        } else {
                            scheduling_todo_list_has_no_item_msg.visibility = View.GONE
                        }
                    }
                }

            }
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}