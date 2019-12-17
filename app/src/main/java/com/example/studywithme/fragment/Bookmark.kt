package com.example.studywithme.fragment

import android.app.AlertDialog
import android.app.PendingIntent.getActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.*
import com.example.studywithme.DialogFragment.DialogAddCategory
import com.example.studywithme.HttpConnection
import com.example.studywithme.MainActivity
import com.example.studywithme.R
import com.example.studywithme.bookmark.BookmarkActivity_category
import com.example.studywithme.bookmark.BookmarkActivity_category_list_Adapter
import com.example.studywithme.data.App
import okhttp3.*
import org.json.JSONArray
import org.w3c.dom.Text
import java.io.IOException

/* 리사이클러뷰와 어댑터에 대한 주석 많음*/
class Bookmark : Fragment(){

    var bookmarkCategoryCreateBtn: Button? = null
    var bookmarkDialogBuilder: AlertDialog.Builder? = null
    var bookmarkCategoryList: RecyclerView? = null
    var categoryList = arrayListOf<BookmarkActivity_category>()
    var categorylist_no_item_msg: TextView? = null
    var categoryListAdapter: BookmarkActivity_category_list_Adapter? = null
    var category_list_linearLayoutManager: LinearLayoutManager? = null
    private val userID = App.prefs.myUserIdData
    private val userName = App.prefs.myUserNameData
    var httpConn: HttpConnection = HttpConnection()
    var getCategoryResult: String? = null
    var args: Bundle? = null
    var sharedLinkURL: String? = null
    var existSharedLinkURL: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 링크 공유받았을 경우
        if (arguments != null){
            args = arguments
            sharedLinkURL = args!!.getString("sharedLinkURL")
            Log.d("북마크 프래그먼트 응답", sharedLinkURL)
            existSharedLinkURL = true
        }
    }

    // 레이아웃을 inflate하는 곳, view 객체를 얻는 곳.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bookmark, container, false)
        // 상단바 메뉴 쓰는 것으로 설정
        setHasOptionsMenu(true)
        bookmarkCategoryCreateBtn = view.findViewById<Button>(R.id.bookmark_category_createBtn)
        // RecyclerView 사용할 것. 어댑터를 생성
        categoryListAdapter = BookmarkActivity_category_list_Adapter(view.context, categoryList, this)
        // 다이얼로그 빌더
        bookmarkDialogBuilder = AlertDialog.Builder(view.context)
        /* 카테고리 리스트 생성 */
        //어떤 데이터(ArrayList)와 어떤 RecylcerView를 쓸 것인지 설정한다.
        bookmarkCategoryList = view!!.findViewById<RecyclerView>(R.id.bookmark_category_list)
        /* 추가로, ListViewAdapter와는 다르게, RecylcerView Adapter에서는 레이아웃 매니저를 설정해주어야 한다.*/
        category_list_linearLayoutManager = LinearLayoutManager(view.context)
        categorylist_no_item_msg = view.findViewById<TextView>(R.id.bookmark_category_list_has_no_item_msg)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // 클릭 이벤트 리스너 붙이기
        setOnClickedListener()
        // 상단바 이름 바꾸기
        var toolbarTitle: TextView = activity!!.findViewById(R.id.toolbar_title)
        toolbarTitle.text = "BookMark"
        bookmarkCategoryList!!.adapter = categoryListAdapter
        bookmarkCategoryList!!.layoutManager = category_list_linearLayoutManager
        /* recyclerView에 setHasFixedSize 옵션에 true값을 준다.
        왜냐하면 item이 추가되거나 삭제될 때 RecylcerView의 크기가 변경될 수도 있고, 그렇게 되면 계층 구조의
        다른 View 크기가 변경될 가능성이 있기 때문. item이 자주 추가되거나 삭제되면 오류가 날 수도 있음. */
        bookmarkCategoryList!!.setHasFixedSize(true)
        // db에서 카테고리 데이터 가져오기
        getCategoryResult = httpConn.get_categoryData()
        // 카테고리 리스트 및 UI 업데이트
        if (getCategoryResult != null) {
            update_categoryList(getCategoryResult)
        }
        if (existSharedLinkURL){
            Toast.makeText(activity, "링크를 추가할 카테고리를 선택해주세요!", Toast.LENGTH_LONG)
        }
    }

    // 전면에 나오게 되면 onresume을 호출하게 된다.
    override fun onResume() {
        super.onResume()
        activity?.invalidateOptionsMenu()
        Log.d("북마크 프래그먼트 응답 ", "onResume")
    }

    // 클릭 이벤트
    private fun setOnClickedListener() {
        bookmarkCategoryCreateBtn!!.setOnClickListener{
            show_createCategoryDialog()
        }
    }

    private fun show_createCategoryDialog(){
        var dialog: DialogAddCategory = DialogAddCategory()
        this.fragmentManager!!.beginTransaction()
            .replace(R.id.content, dialog)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }


    // menu에 만든 xml파일을 인플레이트 해주는 코드
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.default_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        // searchView 설정
        var searchItem = menu?.findItem(R.id.search)
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
    }

    // menu에 있는 메뉴 클릭 시 이벤트 처리
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            //뒤로가기 버튼 클릭 시
            android.R.id.home -> {
                fragmentManager!!.beginTransaction()
                    .replace(R.id.content, Home())
                    .commitAllowingStateLoss()
            }
            R.id.search -> {

            }
            R.id.edit -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }


    fun update_categoryList(resultData: String?){
        // resultData가 실패일 경우 처리해주는 부분이 없어서 가끔 오류남.
        if (resultData == "실패"){
            //categorylist_no_item_msg!!.visibility = View.VISIBLE
        } else {
            //categorylist_no_item_msg!!.visibility = View.GONE
            var result_array: JSONArray = JSONArray(resultData)
            var jsonobj_index = result_array.length() - 1
            // db에서 가져온 데이터가 0 이상일 때만 카테고리 리스트 한번 리셋하고 리스트 업데이트
            if (jsonobj_index >= 0){
                categoryList.removeAll(categoryList)
                for (i in 0..jsonobj_index){
                    // 카테고리 이름
                    var categoryName = result_array.getJSONObject(i).getString("category_name").toString()
                    // 연결된 세부 목표 이름
                    var bookmarkGoal = result_array.getJSONObject(i).getString("goal_name").toString()
                    if (bookmarkGoal == ""){
                        bookmarkGoal = "연결된 세부 목표 없음"
                    }
                    /* 카테고리 리스트에 아이템 추가 */
                    var newCategoryItem =
                        BookmarkActivity_category(
                            categoryName,
                            bookmarkGoal
                        )
                    categoryList.add(newCategoryItem)
                }
                // 백그라운드에서 돌기 때문에 메인쓰레드로 ui에 접근할 수 있도록 해줘야 한다.
                activity!!.runOnUiThread {
                    // 어댑터에 데이터변경사항 알리기
                    bookmarkCategoryList!!.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

}

