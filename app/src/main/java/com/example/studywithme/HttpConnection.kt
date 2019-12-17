package com.example.studywithme

import android.os.AsyncTask
import android.util.Log
import com.example.studywithme.data.App
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.lang.Exception
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HttpConnection {
    private val client: OkHttpClient = OkHttpClient()
    private var userID: String = App.prefs.myUserIdData
    //private val ipAddress: String = "http://10.0.2.2/"
    private val ipAddress: String = "http://203.245.10.33:8888/bookmark/"

    fun get_categoryData(): String? {
        class getCategoryData: AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg params: Void?): String {
                val url = URL(ipAddress + "search_bmCategoryData.php")
                var resultData: String = "실패"
                try{
                    var requestBody = FormBody.Builder()
                        .add("user_id", userID)
                        .build()
                    var request = Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build()
                    val response: Response = client.newCall(request).execute()
                    resultData = response.body?.string()!!
                } catch (e: Exception){
                    Log.d("getCateogryData 응답 오류", e.toString())
                }
                Log.d("getCategoryData 응답 결과1", resultData)
                return resultData
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
            }
        } // 통신 클래스
        var startHttpConn: getCategoryData = getCategoryData()
        return startHttpConn.execute().get()
    }

    fun add_categoryData(categoryName: String, detailName: String) {
        class addCategoryData: AsyncTask<Void, Void, String>() {

            override fun onPreExecute() {
                super.onPreExecute()
            }

            override fun doInBackground(vararg params: Void?): String {
                val url = URL(ipAddress + "insert_bmCategoryData.php")
                var resultData: String = "실패"
                try{
                    var requestBody = FormBody.Builder()
                        .add("user_id", userID)
                        .add("category_name", categoryName)
                        .add("detail_name", detailName)
                        .build()
                    var request = Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build()
                    val response: Response = client.newCall(request).execute()
                    resultData = response.body?.string()!!
                } catch (e: Exception){
                    Log.d("addCateogryData 응답 오류", e.toString())
                }
                Log.d("addCategoryData 응답 결과1", resultData)
                return resultData
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
            }
        } // 통신 클래스
        var startHttpConn: addCategoryData = addCategoryData()
        startHttpConn.execute()
    }

    fun add_bookmarkLinkData(linkURL: String, title: String, imageURL: String, description: String, categoryName: String?): String? {
        class addBookmarkLinkData: AsyncTask<Void, Void, String>() {

            override fun onPreExecute() {
                super.onPreExecute()
            }

            override fun doInBackground(vararg params: Void?): String {
                val url = URL(ipAddress + "insert_bmLinkData.php")
                var resultData: String = "실패"
                try{
                    var requestBody = FormBody.Builder()
                        .add("user_id", userID)
                        .add("link_url", linkURL)
                        .add("link_title", title)
                        .add("link_imageURL", imageURL)
                        .add("link_description", description)
                        .add("category_name", categoryName.toString())
                        .build()
                    var request = Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build()
                    val response: Response = client.newCall(request).execute()
                    resultData = response.body?.string()!!
                } catch (e: Exception){
                    Log.d("addBookmarkLinkData 응답 오류", e.toString())
                }
                Log.d("addBookmarkLinkData 응답1", resultData)
                return resultData
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
            }
        } // 통신 클래스

        var startHttpConn: addBookmarkLinkData = addBookmarkLinkData()
        return startHttpConn.execute().get()
    }

    fun get_bookmarkLinkData(categoryName: String?): String? {
        class getBookmarkLinkData: AsyncTask<Void, Void, String>() {

            override fun doInBackground(vararg params: Void?): String {
                val url = URL(ipAddress + "search_bmLinkData.php")
                var resultData: String = "실패"
                try{
                    var requestBody = FormBody.Builder()
                        .add("user_id", userID)
                        .add("category_name", categoryName.toString())
                        .build()
                    var request = Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build()
                    val response: Response = client.newCall(request).execute()
                    resultData = response.body?.string()!!
                    Log.d("getLinkData 응답 결과0", resultData)
                } catch (e: Exception){
                    Log.d("getLinkData 응답 오류", e.toString())
                }
                Log.d("getLinkData 응답 결과1", resultData)
                return resultData
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
            }
        } // 통신 클래스
        var startHttpConn: getBookmarkLinkData = getBookmarkLinkData()
        return startHttpConn.execute().get()
    }
    fun update_detailedWorkData(detailedWorkName: String, date: String): String? {
        class updateDetailedWorkData: AsyncTask<Void, Void, String>() {

            override fun onPreExecute() {
                super.onPreExecute()
            }

            override fun doInBackground(vararg params: Void?): String {
                val url = URL("http://203.245.10.33:8888/home/update_detailedWorkData.php")
                var resultData: String = "실패"
                try{
                    var requestBody = FormBody.Builder()
                        .add("user_id", userID)
                        .add("detail_name", detailedWorkName)
                        .add("date", date)
                        .build()
                    var request = Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build()
                    val response: Response = client.newCall(request).execute()
                    resultData = response.body?.string()!!
                } catch (e: Exception){
                    Log.d("updateDetailedWorkData 응답 오류", e.toString())
                }
                Log.d("updateDetailedWorkData 응답1", resultData)
                return resultData
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
            }
        } // 통신 클래스

        var startHttpConn: updateDetailedWorkData = updateDetailedWorkData()
        return startHttpConn.execute().get()
    }

    fun add_detailedWorkData(detailedWorkName: String, date: String): String? {
        class addDetailedWorkData: AsyncTask<Void, Void, String>() {

            override fun onPreExecute() {
                super.onPreExecute()
            }

            override fun doInBackground(vararg params: Void?): String {
                val url = URL("http://203.245.10.33:8888/home/insert_detailedWorkData.php")
                var resultData: String = "실패"
                try{
                    var requestBody = FormBody.Builder()
                        .add("user_id", userID)
                        .add("detail_name", detailedWorkName)
                        .add("date", date)
                        .build()
                    var request = Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build()
                    val response: Response = client.newCall(request).execute()
                    resultData = response.body?.string()!!
                } catch (e: Exception){
                    Log.d("addDetailedWorkData 응답 오류", e.toString())
                }
                Log.d("addDetailedWorkData 응답1", resultData)
                return resultData
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
            }
        } // 통신 클래스

        var startHttpConn: addDetailedWorkData = addDetailedWorkData()
        return startHttpConn.execute().get()
    }
    fun get_todayDetailData(): String? {
        var currentTime = LocalDate.now()
        var date_string = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        Log.d("getTodayDetailData 응답", date_string)
        class getTodayDetailData: AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg params: Void?): String {
                val url = URL("http://203.245.10.33:8888/home/search_todayDetailData.php")
                var resultData: String = "실패"
                try{
                    var requestBody = FormBody.Builder()
                        .add("user_id", userID)
                        .add("date_string", date_string)
                        .build()
                    var request = Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build()
                    val response: Response = client.newCall(request).execute()
                    resultData = response.body?.string()!!
                    Log.d("getTodayDetailData 응답 결과0", resultData)
                } catch (e: Exception){
                    Log.d("getTodayDetailData 응답 오류", e.toString())
                }
                Log.d("getTodayDetailData 응답 결과1", resultData)
                return resultData
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
            }
        } // 통신 클래스
        var startHttpConn: getTodayDetailData = getTodayDetailData()
        return startHttpConn.execute().get()
    }

}