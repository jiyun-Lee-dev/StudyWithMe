package com.example.studywithme

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import com.example.studywithme.R.id.*
import kotlinx.android.synthetic.main.bookmark_main.*
import org.w3c.dom.Text

class BookmarkActivity_main: AppCompatActivity() {
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 액티비티랑 레이아웃 연결
        setContentView(R.layout.bookmark_main)
        // 액션바 대신 툴바 사용
        setSupportActionBar(toolbar_bookmark)
        // 기본 타이틀 없애기
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        // 액션바에 홈버튼 추가
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    var searchInputWindow_is_visible: Boolean = false
    // menu에 만든 xml파일을 인플레이트 해주는 코드
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.default_menu, menu)
        // searchView 설정
        var searchItem = menu?.findItem(bookmark_search)
        var searchView = searchItem?.actionView as android.support.v7.widget.SearchView


        searchView.setOnQueryTextListener(object: android.support.v7.widget.SearchView.OnQueryTextListener{
            // 검색 입력창에 새로운 텍스트가 들어갈 때 마다 호출 - 지금은 사용 안함.
            override fun onQueryTextChange(searchInputString: String?): Boolean {
                return false
            }
            // 검색어를 다 입력하고 서치 버튼을 눌렀을 때
            override fun onQueryTextSubmit(searchInputString: String?): Boolean {
                Toast.makeText(this@BookmarkActivity_main, searchInputString + "에 대해 검색중", Toast.LENGTH_LONG).show()
                // 여기서 검색 요청하고 데이터 받아오는 걸 처리해야 함. AsyncTask?가 뭐지 암튼 나중에 db url 주소에다가 검색해야함
                return false
            }
        })
        return true
    }

    // menu에 있는 메뉴 클릭 시 이벤트 처리
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            // <- 버튼 클릭 이벤트
            home -> {
                if (searchInputWindow_is_visible) {
                    finish()
                }
                else {
                    Toast.makeText(this@BookmarkActivity_main, "상단바 검색입력창 모드", Toast.LENGTH_SHORT).show()
                    searchInputWindow_is_visible = true
                    invalidateOptionsMenu()
                    bookmark_title.visibility = View.VISIBLE
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // 마지막으로 뒤로가기 버튼이 터치된 시간
    private var lastTimeBackPressed:Long = 0L
    // 핸드폰 뒤로 가기 버튼 클릭 시 이벤트 처리
    override fun onBackPressed() {
        /* 사용자가 뒤로가기 버튼을 터치할 때마다 현재 시간을 저장해놓는다.
        System.currentTimeMills는 1970년 1월 1일부터 지금까지 경과한 시간을 말함.
         */
        if (System.currentTimeMillis() - lastTimeBackPressed < 1500){
            finish()
            return
        }
        Toast.makeText(this@BookmarkActivity_main, "'뒤로가기' 버튼을 한번 더 누르면 어플리케이션이 종료됩니다.", Toast.LENGTH_SHORT).show()
        lastTimeBackPressed = System.currentTimeMillis()
    }
}


