package com.example.studywithme.data

import android.os.AsyncTask
import android.util.Log
import com.example.studywithme.data.model.LoggedInUser
import okhttp3.*
import org.json.JSONObject
import java.lang.Exception

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */

class LoginDataSource {
    /* POST로 동기식 처리 */
    fun login(username: String, password: String): Result<LoggedInUser> {
        var userId: String
        class test : AsyncTask<Void, Void, String>(){
            val client: OkHttpClient = OkHttpClient()

            override fun onPreExecute() {
                super.onPreExecute()
            }

            override fun doInBackground(vararg params: Void?): String {
                // url 만들기
                val url="http://203.245.10.33/loginAuthenticate.php"
                var dataFrom : String = "실패"
                try {
                    // 데이터를 담아 보낼 바디 만들기
                    val requestBody : RequestBody = FormBody.Builder()
                        .add("user_name", username)
                        .add("user_password", password)
                        .build()
                    // okhttp request를 만든다.
                    val request = Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build()
                    // 클라이언트 생성
                    val response : Response = client.newCall(request).execute()
                    // 응답 받음
                    val data = response.body?.string().toString()
                    var result_array = JSONObject(data)
                    dataFrom = result_array.getString("user_id")
                } catch (e: Exception) {

                }
                Log.d("응답", dataFrom)
                return dataFrom
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                Log.d("응답", result)
            }
        }
        var StartTest : test = test()
        var result = StartTest.execute().get().toString()
        userId = result;

        if (result.isNotEmpty()){
            Log.d("응답", username)
            // 인증 성공
            var authenticatedUser = LoggedInUser(java.util.UUID.randomUUID().toString(), username)
            App.prefs.myUserNameData = username
            App.prefs.myPasswordData = password
            App.prefs.myUserIdData = userId
            return Result.Success(authenticatedUser)
        } else {
            // 인증 실패
            return Result.Error("Error logging")
        }
    }

    /*
    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            // url 만들기
            val url="http://10.0.2.2/loginAuthenticate.php"
            // 데이터를 담아 보낼 바디 만들기
            val requestBody : RequestBody = FormBody.Builder()
                .add("user_name", username)
                .add("user_password", password)
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
                    val data = response.body?.string().toString()
                    var result_array = JSONObject(data)
                    var result = result_array.getString("user_authenticate").toBoolean()
                    var authenticatedUser = if (result){
                        Log.d("응답", username)
                        // 인증 성공
                        LoggedInUser(java.util.UUID.randomUUID().toString(), username)
                    } else {
                        // 인증 실패
                        LoggedInUser("", "")
                    }
                    return Result.Success(authenticatedUser)
                }
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("요청", "요청 실패")
                }
            })
            //val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }
*/
    fun logout() {
        // TODO: revoke authentication
    }
}