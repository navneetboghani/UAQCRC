package com.adsum.camel_masterapplication.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Adapter.NotificationAdapter
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.NotificationErrorResponse
import com.adsum.camel_masterapplication.Model.NotificationResponse
import com.adsum.camel_masterapplication.Model.UpdateNotificationRes
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.FragmentNotificationBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONObject


class NotificationFragment : Fragment(), NotificationAdapter.OnNotificationClickListener {
    private lateinit var binding: FragmentNotificationBinding
    private lateinit var rootView: View
    private var lang = ""
    private lateinit var notificationAdapter: NotificationAdapter
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<NotificationAdapter.ViewHolder>? = null
    var notification_count = ArrayList<String>()

    companion object {
        fun newInstance(
                param1: String?,
                param2: String?
        ): NotificationFragment {
            val fragment: NotificationFragment =
                    NotificationFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)

        binding.rvNotification.setHasFixedSize(true)
        init()

        return (binding.root)
    }

    private fun init() {
        try {
            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
                val url: String = CamelConfig.WEBURL + CamelConfig.get_notification
                val data = JSONObject()
               // CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))
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
                                Log.e("not", "res--------" + response)
                                val gson = Gson()
                                try {
                                    val res = gson.fromJson(response.toString(), NotificationResponse::class.java)
                                    if (res.status == 1) {
                                        CommonFunctions.destroyProgressBar()
                                        context?.let {
                                            initnotificationRv(it, res.data)
                                        }
                                        for (i in 0..res.data.size) {
                                            if (res.data[i].is_read.equals("0")) {
                                                res.data[i].is_read = "1"
                                                updateNotification()
                                            }else {
                                                //Toast.makeText(activity,"Already read",Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    } else {
                                        CommonFunctions.destroyProgressBar()
                                    }

                                } catch (e: Exception) {
                                    val res = gson.fromJson(response.toString(), NotificationErrorResponse::class.java)
                                    if (res.status == 0) {
                                        CommonFunctions.destroyProgressBar()
//                                        CommonFunctions.showToast(activity, res.message)
                                    } else {
                                        CommonFunctions.destroyProgressBar()
//                                        CommonFunctions.showToast(activity, res.message)
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
            Log.e("e","e----"+e)
        }
    }
    private fun initnotificationRv(context: Context, data: List<NotificationResponse.Data>) {
        notificationAdapter = NotificationAdapter(context, data, this)
        notification_count = notificationAdapter.unReadNotification()
        binding.rvNotification.adapter = notificationAdapter
        notificationAdapter.notifyDataSetChanged()
    }

    fun updateNotification() {

        Log.e("San",""+notificationAdapter.unReadNotification().toString())
        val string = notificationAdapter.unReadNotification().joinToString(",")
        Log.e("San",""+string)
        try {
            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
                val url: String = CamelConfig.WEBURL + CamelConfig.updateNotification
                Log.e("update","notification----" + url)
                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))
                AndroidNetworking.post(url)
                        .setTag(url)
                        .addBodyParameter("id",string)
                        .addHeaders(Constants.Authorization, Constants.Authkey)
//                        .addHeaders("Accept", "application/json")
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(object : JSONObjectRequestListener {
                            override fun onResponse(response: JSONObject?) {
                                Log.e("update", "update------" + response)
                                val gson = Gson()
                                val res = gson.fromJson(response.toString(), UpdateNotificationRes::class.java)
                                    if (res.status == 1) {
                                        CommonFunctions.destroyProgressBar()
                                        CommonFunctions.showToast(activity,"Notification read successfully")
                                    } else {
                                        CommonFunctions.destroyProgressBar()
                                    }
                            }

                            override fun onError(anError: ANError?) {
                                CommonFunctions.destroyProgressBar()
                                 Log.e("update","error:----"+anError)
                                CommonFunctions.showToast(context, anError.toString())
                            }
                        })
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("update" , "e-----"+e.toString())
        }
    }

    override fun onNotificationClick(notification: NotificationResponse.Data, position: Int) {
        TODO("Not yet implemented")
    }
}








