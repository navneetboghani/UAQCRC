package com.adsum.camel_masterapplication.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.adsum.camel_masterapplication.Activity.AdminDashboardActivity
import com.adsum.camel_masterapplication.Activity.DashboardActivity
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.NotificationErrorResponse
import com.adsum.camel_masterapplication.Model.NotificationResponse
import com.adsum.camel_masterapplication.databinding.ActivityDashboardBinding
import com.adsum.camel_masterapplication.databinding.FragmentHomeBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONObject
import kotlin.properties.Delegates

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class FragmentHome : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var bindingprofile: ActivityDashboardBinding
    private lateinit var rootView: View
    private var lang = ""
    private var token = ""
    private var isRead: ArrayList<Int> = ArrayList()
    private lateinit var notification: TextView
    var Role by Delegates.notNull<String>()

    companion object {
        fun newInstance(
                param1: String?,
                param2: String?
        ): FragmentHome {
            val fragment: FragmentHome =
                    FragmentHome()
            val args = Bundle()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        Role=CommonFunctions.getPreference(activity,Constants.role,"").toString()
        rootView = binding.root
        notification = binding.badgeText

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationget()
        init()
    }

    private fun init() {

        if (Role == "subscriber"){
            binding.clControlPanel.visibility=View.GONE
        }else {
            binding.clControlPanel.visibility = View.VISIBLE
        }


        binding.cvCamel.setOnClickListener {
            (activity as DashboardActivity).FragmentCamel()
        }
        binding.cvUser.setOnClickListener {
            (activity as DashboardActivity).FragmentProfile()
        }
        binding.cvNotify.setOnClickListener {
            (activity as DashboardActivity).FragmentNotification()
        }
        binding.cvParticipant.setOnClickListener {
            (activity as DashboardActivity).FragmentParticipant()
        }
        binding.cvContolpanel.setOnClickListener {
            val intent = Intent(activity, AdminDashboardActivity::class.java)
            startActivity(intent)
        }
        binding.cvArchive.setOnClickListener {
            (activity as DashboardActivity).fragmentRaceArchive()
        }
        binding.cvScedule.setOnClickListener {
            (activity as DashboardActivity).fragmentRaceSchedule()
        }
        binding.cvTerms.setOnClickListener {
            (activity as DashboardActivity).fragmentTermsCondition()
        }
    }

    fun notificationget() {
        try {
            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
                val url: String = CamelConfig.WEBURL + CamelConfig.get_notification
                val data = JSONObject()
                //CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))
                val okHttpClient = OkHttpClient.Builder()
                        .addInterceptor(ChuckerInterceptor(requireActivity()))
                        .build()
                AndroidNetworking.post(url)
                        .setTag(url)
                        .addHeaders(Constants.Authorization, Constants.Authkey)
//                        .addHeaders("Accept", "application/json")
                        .addJSONObjectBody(data)
                        .setPriority(Priority.HIGH)
                        // .setOkHttpClient(okHttpClient)
                        .build()
                        .getAsJSONObject(object : JSONObjectRequestListener {
                            override fun onResponse(response: JSONObject?) {
                                Log.e("San", "res" + response)
                                val gson = Gson()
                                try {
                                    val res = gson.fromJson(response.toString(), NotificationResponse::class.java)
                                    if (res.status == 1) {
                                        CommonFunctions.destroyProgressBar()
                                        isRead.clear()
                                        for (i in 0..res.data.size - 1) {
                                            if (res.data[i].is_read == 0.toString()) {
                                                isRead = (isRead + res.data[i].id) as ArrayList<Int>
                                                Log.e("notifyCount", "size---" + isRead)
                                            }
                                        }
                                        if (isRead.size != 0) {
                                            notification.setText(java.lang.String.valueOf(Math.min(isRead.size, 99)))
                                            if (notification.getVisibility() !== View.VISIBLE) {
                                                notification.setVisibility(View.VISIBLE)
                                            }
                                        } else {
                                            if (notification.getVisibility() !== View.GONE) {
                                                notification.setVisibility(View.GONE)
                                            }
                                        }

                                    } else {
                                        CommonFunctions.destroyProgressBar()
                                        CommonFunctions.showToast(activity, "else")
                                    }

                                } catch (e: Exception) {
                                    val res = gson.fromJson(response.toString(), NotificationErrorResponse::class.java)
                                    if (res.status == 0) {
                                        CommonFunctions.destroyProgressBar()
                                        CommonFunctions.showToast(activity, res.message)
                                    } else {
                                        CommonFunctions.destroyProgressBar()
                                        CommonFunctions.showToast(activity, res.message)
                                    }
                                }
                            }

                            override fun onError(anError: ANError?) {
                                CommonFunctions.destroyProgressBar()
                                // Log.e("San","res:--"+anError)
                                CommonFunctions.showToast(context, anError.toString())
                            }
                        })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}




