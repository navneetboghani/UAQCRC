package com.adsum.camel_masterapplication.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast

import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Config.Constants.NO_DATA
import com.adsum.camel_masterapplication.Config.Constants.SPACE
import com.adsum.camel_masterapplication.Model.LoginResponse
import com.adsum.camel_masterapplication.Model.LoginerrorResponse

import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.ActivityLoginBinding
import com.adsum.camel_masterapplication.utils.App
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.text.SimpleDateFormat

class LoginActivity : AppCompatActivity() {
    private lateinit var activityloginBinding: ActivityLoginBinding
    private lateinit var rootView: View

    companion object {
        fun startActivity(activity: Activity) {
            activity.startActivity(
                Intent(
                    activity,
                    LoginActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            activity.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityloginBinding = ActivityLoginBinding.inflate(layoutInflater)
        rootView = activityloginBinding.root

        setContentView(rootView)

        init()

    }

    private fun init() {
        supportActionBar?.hide()


        activityloginBinding.btnLogin.setOnClickListener {

            when {
                activityloginBinding.edtUsername.text.toString().trim { it <= ' ' }.isEmpty() -> {
                    Toast.makeText(
                        this,
                        getString(R.string.enter_your_user_id),
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    activityloginBinding.edtUsername.requestFocus()
                }
                activityloginBinding.edtPassword.text.toString()
                    .trim { it <= ' ' }.isEmpty() -> {

                    Toast.makeText(this, getString(R.string.enter_password), Toast.LENGTH_SHORT)
                        .show()

                    activityloginBinding.edtPassword.requestFocus()
                }
                else -> {
                    loginUser(false, this)
                }
            }
        }
    }

    private fun loginUser(b: Boolean, context: Context) {
        try {
            if (CommonFunctions.checkConnection(this)) {

                val url: String = CamelConfig.WEBURL + CamelConfig.login
                Log.e("tag","url:-"+url)
                val mParams: HashMap<String, String> = HashMap()

                CommonFunctions.createProgressBar(this, getString(R.string.please_wait))

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(context))
                    .build()

                AndroidNetworking.post(url)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
//                    .addPathParameter(mParams)
                    .addBodyParameter(
                        "username",
                        activityloginBinding.edtUsername.text.toString().trim { it <= ' ' })
                    .addBodyParameter(
                        "password",
                        activityloginBinding.edtPassword.text.toString().trim { it <= ' ' })
                    .setTag(url)
                    .setPriority(Priority.HIGH)
//                    .setOkHttpClient(okHttpClient)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        @SuppressLint("SimpleDateFormat")
                        override fun onResponse(response: JSONObject?) {
                            Log.e("Response", response.toString())
                            var flag = false
                            CommonFunctions.destroyProgressBar()
                            val gson = Gson()
                            try {
                                val res = gson.fromJson(
                                    response.toString(),
                                    LoginResponse::class.java
                                )
                                flag = true
                            } catch (e: Exception) {
                                val res = gson.fromJson(
                                    response.toString(),
                                    LoginerrorResponse::class.java
                                )
                                flag = false
                            }
                            if (flag) {
                                val res = gson.fromJson(
                                    response.toString(),
                                    LoginResponse::class.java
                                )
                                if (res.status.toString() == "1") {
                                    CommonFunctions.destroyProgressBar()
                                    CommonFunctions.setPreference(
                                        applicationContext,
                                        Constants.isLogin,
                                        true
                                    )
                                    CommonFunctions.setPreference(
                                        applicationContext,
                                        Constants.ID,
                                        res.data.Userid
                                    )
                                    CommonFunctions.setPreference(
                                        applicationContext,
                                        Constants.username,
                                        res.data.UserName
                                    )
                                    CommonFunctions.setPreference(
                                        applicationContext,
                                        Constants.role,
                                        res.data.Role
                                    )
                                    CommonFunctions.setPreference(
                                        applicationContext,
                                        Constants.last_login_time,
                                        res.data.last_login_time
                                    )
                                    val date =
                                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-01-24 09:42:46")
                                    //  Log.e("san" ,"dateformate:=="+ date.time)

                                    CommonFunctions.setPreference(
                                        applicationContext,
                                        Constants.logintime,
                                        res.data.last_login_time_timestamp_format
                                    )
                                    CommonFunctions.setPreference(
                                        applicationContext,
                                        Constants.subscription,
                                        res.data.subscription
                                    )
//                                    CommonFunctions.setPreference(
//                                        applicationContext, Constants.userdata, gson.toJson(
//                                            response
//                                        )
//                                    )
                                    DashboardActivity.startActivity(this@LoginActivity)
                                    finish()
                                } else {
                                    CommonFunctions.showToast(this@LoginActivity, res.response)
                                }
                            } else {
                                val res = gson.fromJson(
                                    response.toString(),
                                    LoginerrorResponse::class.java
                                )
                                CommonFunctions.destroyProgressBar()
                                CommonFunctions.showToast(this@LoginActivity, res.response)
                            }
                        }

                        override fun onError(anError: ANError?) {
                            CommonFunctions.destroyProgressBar()
                            CommonFunctions.showToast(applicationContext, anError.toString())
                        }
                    })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun validation(login: String, password: String) = when {
        login.replace(SPACE, NO_DATA).isEmpty() -> {
            CommonFunctions.showToast(this, App.instance.getString(R.string.enter_username))
            false
        }
        password.replace(SPACE, NO_DATA).isEmpty() -> {
            CommonFunctions.showToast(this, App.instance.getString(R.string.enter_password))
            false
        }
        else -> true
    }
}