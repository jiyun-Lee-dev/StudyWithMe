package com.example.studywithme.fragment

import android.app.AlertDialog
import android.content.Context
import android.databinding.DataBindingUtil.setContentView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.*
import com.example.studywithme.MainActivity
import com.example.studywithme.R
import com.example.studywithme.bookmark.BookmarkActivity_category
import com.example.studywithme.bookmark.BookmarkActivity_category_list_Adapter
import com.example.studywithme.data.categorylist
import kotlinx.android.synthetic.main.bookmark_main.*
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class Bookmark : Fragment(){

    /* 북마크 리스트. 일단은 예시로 하드코딩.
       추후에 사용자로부터 입력받아서 db 저장하고 db에서 가져오는 걸로 수정해야함*/
    var categoryList = arrayListOf<BookmarkActivity_category>()
    var itemcnt = 0
    var categoryListAdapter: BookmarkActivity_category_list_Adapter? = null
    // test용 사용자 아이디는 0으로 임시 설정했음
    val userID = "0"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_bookmark, container, false)

        // RecyclerView 사용할 것. 어댑터를 생성
        categoryListAdapter = BookmarkActivity_category_list_Adapter(view.context, categoryList)


/*
        /* 상단에 있는 메뉴바 */
        // 액티비티랑 레이아웃 연결
        setContentView(R.layout.bookmark_main)
        // 액션바 대신 툴바 사용
        setSupportActionBar(toolbar_bookmark)
        // 기본 타이틀 없애기
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        // 액션바에 홈버튼 추가 (<- 버튼임)
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
*/


        /* 카테고리 추가 다이얼로그. */
        // 익명 객체 전달. 람다 형식.
        val bookmark_category_createBtn = view.findViewById<Button>(R.id.bookmark_category_createBtn)
        bookmark_category_createBtn.setOnClickListener {
            // 다이얼로그 빌더
            val bookmark_dialogBuilder = AlertDialog.Builder(view.context)
            // 레이아웃 인플레이터 선언
            //val bookmark_dialog : LayoutInflater = LayoutInflater.from(this)
            // 띄울 엑티비티 안의 View 컨트롤 하기 위해 인플레이트
            val bookmark_dialogView = layoutInflater.inflate(R.layout.bookmark_popup_createcategory, null)
            val dialog_detailedWork_spinner = bookmark_dialogView.findViewById<Spinner>(R.id.bookmark_detailedWorkList)
            bookmark_dialogBuilder.setView(bookmark_dialogView)
                .setPositiveButton("확인") {dialogInterface, i ->
                    var dialog_categoryName_string = bookmark_dialogView.findViewById<EditText>(R.id.bookmark_categoryName).text.toString()
                    var dialog_detailedWork_string = dialog_detailedWork_spinner.selectedItem.toString()
                    /* db에 데이터 추가 */
                    add_categoryData_to_DB(userID, dialog_categoryName_string, dialog_detailedWork_string)
                    /* 카테고리 리스트에 아이템 추가 */
                    var newCategoryItem =
                        BookmarkActivity_category(
                            dialog_categoryName_string,
                            dialog_detailedWork_string
                        )
                    categoryList.add(newCategoryItem)
                    // 어댑터에 데이터변경사항 알리기
                    bookmark_category_list.adapter?.notifyDataSetChanged()
                }
                .setNegativeButton("취소") {dialogInterface, i ->
                    /*취소하면 실행해줄 거 없음*/
                }
            bookmark_dialogBuilder.show()
        }

        /* 카테고리 리스트 생성 */
        //어떤 데이터(ArrayList)와 어떤 RecylcerView를 쓸 것인지 설정한다.
        val bookmark_category_list = view.findViewById<RecyclerView>(R.id.bookmark_category_list)
        bookmark_category_list.adapter = categoryListAdapter
        /* 추가로, ListViewAdapter와는 다르게, RecylcerView Adapter에서는 레이아웃 매니저를 설정해주어야 한다.
        LayoutManager는 RecyclerView의 각 item들을 배치하고, item이 더 이상 보이지 않을 때 재사용할 것인지
        결정하는 역할을 한다. item을 재사용할 때, LayoutManager 사용을 통해 불필요한 findViewById를 수행하지 않아도 되고, 앱 성능을 향상시킬 수 있다.
        기본적으로 안드로이드에서 3가지의 LayoutManager 라이브러리를 지원한다.
        - LinearLayoutManager
        - GridLayoutManager
        - StaggeredGridLayoutManager
        이 외에도 사용자가 추상 클래스를 확장하여 임의의 레이아웃을 지정할 수도 있다.
        LinearLayoutManager는 RecylcerView를 불러올 액티비티에 LayoutManager를 추가한다.*/
        val category_list_linearLayoutManager = LinearLayoutManager(view.context)
        bookmark_category_list.layoutManager = category_list_linearLayoutManager
        /* recyclerView에 setHasFixedSize 옵션에 true값을 준다.
        왜냐하면 item이 추가되거나 삭제될 때 RecylcerView의 크기가 변경될 수도 있고, 그렇게 되면 계층 구조의
        다른 View 크기가 변경될 가능성이 있기 때문. item이 자주 추가되거나 삭제되면 오류가 날 수도 있음. */
        bookmark_category_list.setHasFixedSize(true)
        // db에서 카테고리 데이터 가져오기
        getCategoryList_from_DB("search")

        return view
    }

    /* optionsMenu 생성 */
    var searchInputWindow_is_visible: Boolean = false
    // menu에 만든 xml파일을 인플레이트 해주는 코드
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.default_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        // searchView 설정
        var searchItem = menu?.findItem(R.id.bookmark_search)
        var searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            // 검색 입력창에 새로운 텍스트가 들어갈 때 마다 호출 - 지금은 사용 안함.
            override fun onQueryTextChange(searchInputString: String?): Boolean {
                return false
            }
            // 검색어를 다 입력하고 서치 버튼을 눌렀을 때
            override fun onQueryTextSubmit(searchInputString: String?): Boolean {
                Toast.makeText(view?.context, searchInputString + "에 대해 검색중", Toast.LENGTH_LONG).show()
                // 여기서 검색 요청하고 데이터 받아오는 걸 처리해야 함. AsyncTask?가 뭐지 암튼 나중에 db url 주소에다가 검색해야함
                return false
            }
        })
        //return true
    }


    // menu에 있는 메뉴 클릭 시 이벤트 처리
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.home -> {
                if (searchInputWindow_is_visible) {
                    getActivity()!!.finish()
                }
                else {
                    Toast.makeText(view?.context, "상단바 검색입력창 모드", Toast.LENGTH_SHORT).show()
                    searchInputWindow_is_visible = true
                    getActivity()!!.invalidateOptionsMenu()
                    bookmark_title.visibility = View.VISIBLE
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // 마지막으로 뒤로가기 버튼이 터치된 시간
    private var lastTimeBackPressed:Long = 0L
    // 핸드폰 뒤로 가기 버튼 클릭 시 이벤트 처리
    fun onBackPressed() {
        /* 사용자가 뒤로가기 버튼을 터치할 때마다 현재 시간을 저장해놓는다.
        System.currentTimeMills는 1970년 1월 1일부터 지금까지 경과한 시간을 말함.
         */
        if (System.currentTimeMillis() - lastTimeBackPressed < 1500){
            getActivity()!!.finish()
            return
        }
        Toast.makeText(view?.context, "'뒤로가기' 버튼을 한번 더 누르면 어플리케이션이 종료됩니다.", Toast.LENGTH_SHORT).show()
        lastTimeBackPressed = System.currentTimeMillis()
    }

    // 카테고리 데이터 db 추가
    fun add_categoryData_to_DB(user_id:String, category_name:String, detailedWork_name:String){
        // url 만들기
        val url="http://10.0.2.2/insert.php"
        // 데이터를 담아 보낼 바디 만들기
        val requestBody : RequestBody = FormBody.Builder()
            .add("user_id", user_id) // user_id 일단 임의로 1 저장
            .add("category_name", category_name)
            .add("detail_name", detailedWork_name)
            .build()
        // okhttp request를 만든다.
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        // 클라이언트 생성
        val client = OkHttpClient()
        // 요청 전송
        client.newCall(request).enqueue(object : Callback{
            override fun onResponse(call: Call, response: Response) {
                Log.d("요청", "요청 완료")
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("요청", "요청 실패")
            }
        })
    }

    // db에서 php로 데이터 가져오기 (+userID 체크 추가)
    fun getCategoryList_from_DB(phpName: String){
        // url 만들기
        val url="http://10.0.2.2/" + phpName + ".php"
        // POST로 보낼 데이터 설정
        // 데이터를 담아 보낼 바디 만들기
        val requestBody : RequestBody = FormBody.Builder()
            .add("user_id", userID) // user_id 일단 임의로 0 저장
            .build()
        // okhttp request를 만든다.
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        // 클라이언트 생성
        val client = OkHttpClient()
        // 요청 전송
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("요청 ", e.toString())
            }
            override fun onResponse(call: Call, response: Response) {
                var data = response?.body?.string().toString()
                var result_array: JSONArray = JSONArray(data)
                var jsonobjCnt = result_array.length() - 1
                for (i in 0..jsonobjCnt){
                    // 카테고리 이름
                    var categoryName = result_array.getJSONObject(i).getString("category_name").toString()
                    // 연결된 세부 목표 이름
                    var detailedWork = result_array.getJSONObject(i).getString("detail_name").toString()
                    if (detailedWork == ""){
                        detailedWork = "연결된 세부 목표 없음"
                    }
                    /* 카테고리 리스트에 아이템 추가 */
                    var newCategoryItem =
                        BookmarkActivity_category(
                            categoryName,
                            detailedWork
                        )
                    categoryList.add(newCategoryItem)
                }
                // 백그라운드에서 돌기 때문에 메인쓰레드로 ui에 접근할 수 있도록 해줘야 한다.
                getActivity()!!.runOnUiThread {
                    // 어댑터에 데이터변경사항 알리기
                    bookmark_category_list.adapter?.notifyDataSetChanged()
                    // categorylist에 아이템이 하나도 없을 경우 안내메시지 view에 출력
                    itemcnt = categoryListAdapter!!.getItemCount()
                    Log.d("요청 ", itemcnt.toString())
                    if (itemcnt <= 0){
                        bookmark_category_list_has_no_item_msg.text = "아직 카테고리가 없습니다. \n카테고리를 생성해서 북마크를 관리해보세요"
                        bookmark_category_list_has_no_item_msg.visibility = View.VISIBLE
                    } else {
                        bookmark_category_list_has_no_item_msg.visibility = View.GONE
                    }
                }
            }
        })
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}