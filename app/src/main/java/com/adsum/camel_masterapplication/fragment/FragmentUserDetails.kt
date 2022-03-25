package com.adsum.camel_masterapplication.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.adsum.camel_masterapplication.Activity.LoginActivity
import com.adsum.camel_masterapplication.Adapter.UserDetailsAdapter
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Config.Constants.position
import com.adsum.camel_masterapplication.Config.Constants.userdetail
import com.adsum.camel_masterapplication.Model.*
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.FragmentUserDetailsBinding
import com.adsum.camel_masterapplication.databinding.ItemUserDetailsBinding
import com.adsum.camel_masterapplication.databinding.PopupDeleteBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONObject

import kotlin.properties.Delegates

class FragmentUserDetails : Fragment(), UserDetailsAdapter.OnUserDetailClickListener {

    private lateinit var adapter: UserDetailsAdapter
    private lateinit var binding: FragmentUserDetailsBinding
    private var popupDeleteBinding: PopupDeleteBinding? = null
    private lateinit var rootView: View


    private var user_id by Delegates.notNull<String>()
    private lateinit var userDetailsAdapter: UserDetailsAdapter
//    private var user_id by Delegates.notNull<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        rootView = binding.root
        popupDeleteBinding = PopupDeleteBinding.inflate(inflater, container, false)

        binding.rvUserDetails.setHasFixedSize(true)
        getUserDetails()

       // user_id = CommonFunctions.getPreference(activity, Constants.user_id, "").toString()
       // status = requireArguments().getString(Constants.status).toString()
        return rootView
    }


    companion object {
        fun newInstance(
            param1: String?,
            param2: String?
        ): FragmentUserDetails {
            val fragment: FragmentUserDetails =
                FragmentUserDetails()
            val args = Bundle()
            fragment.setArguments(args)
            return fragment
        }

    }


    private fun getUserDetails(){
        try {
            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
                var url:String = CamelConfig.WEBURL + CamelConfig.viewuser

                //val url: String =  "https://uaqcrc.com/wp-json/camel/v1/viewuser"
              // Log.e("San","res" +url)

                val data = JSONObject()
                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))


                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()
                AndroidNetworking.post(url)
                    .setTag(url)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .addHeaders("Accept", "application/json")
                    .addJSONObjectBody(data)
                    .setPriority(Priority.HIGH)
                    // .setOkHttpClient(okHttpClient)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {

                        override fun onResponse(response: JSONObject?) {

                            CommonFunctions.destroyProgressBar()
                            val gson = Gson()
                                val res = gson.fromJson(response.toString(), UserDetailsResponse::class.java)

                                if (res.status == 1) {
                                    CommonFunctions.destroyProgressBar()

                                    context?.let { inituserdetailRv(it, res.data)}


                                }else{
                                    CommonFunctions.destroyProgressBar()

                                    CommonFunctions.showToast(activity,res.response)
                                }
                        }

                        override fun onError(anError: ANError?) {
                            CommonFunctions.destroyProgressBar()
                            CommonFunctions.showToast(context, anError.toString())


                        }

                    })

            }


        }catch (e:Exception){

       }

    }



    private fun inituserdetailRv(context: Context, data: ArrayList<UserDetailsResponse.Data>) {
        adapter = UserDetailsAdapter(context, data ,this)
        binding.rvUserDetails.adapter = adapter
    }

    override fun OnClick(userdetail: UserDetailsResponse.Data, position: Int){
        val dialog = activity?.let { Dialog(it) }
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.popup_delete_user)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val Done = dialog.findViewById(R.id.tv_doneuser) as TextView
        val cancel = dialog.findViewById(R.id.tv_canceluser) as TextView

        Done.setOnClickListener {
            DeleteUser(userdetail.user_id.toInt(),position)
            dialog.dismiss()
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }



    private fun DeleteUser(id: Int,position: Int){

        try {
            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {


        var url: String = "https://uaqcrc.com/wp-json/camel/v1/rmvuser?="
                //Log.e("san","res" +url)

        CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor(requireActivity()))
            .build()

                AndroidNetworking.post(url)
                    .addBodyParameter("user_id",id.toString())
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .addHeaders("Accept", "application/json")
                    .setTag(url)
                    .setPriority(Priority.HIGH)
//                    .setOkHttpClient(okHttpClient)
                    .build()

            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.e("SAn","responce:--"+response)

                    CommonFunctions.destroyProgressBar()
                    val gson = Gson()
                    val res = gson.fromJson(response.toString(), UserDeleteResponse::class.java)


                    if (res.status == 1) {

                        CommonFunctions.showToast(activity,res.response)
                        adapter.deleteuser(position)

                    }else{
                       // CommonFunctions.destroyProgressBar()
                           CommonFunctions.showToast(activity,res.response)
                    }
                }
                override fun onError(anError: ANError?) {
                    CommonFunctions.destroyProgressBar()
                    CommonFunctions.showToast(context, anError.toString())


                }
            })
            }

        }catch (e:Exception){

        }

    }

    override fun logoutuser(userdetail: UserDetailsResponse.Data, position: Int) {
        logout(userdetail.user_id.toInt())

    }

    private fun logout(id: Int) {
        try {

            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {

                var url: String = CamelConfig.WEBURL + CamelConfig.LogOut + id
                Log.e("SAn","logresponce:--"+url)
                val mParams: HashMap<String, String> = HashMap()
                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))
                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()

                AndroidNetworking.post(url)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .addHeaders("Accept", "application/json")
                    .setTag(url)
                    .setPriority(Priority.HIGH)
//                    .setOkHttpClient(okHttpClient)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            Log.e("SAn","logresponce:--"+response)
                            CommonFunctions.destroyProgressBar()
                            var gson = Gson()
                            val res = gson.fromJson(response.toString(),
                                logoutResponce::class.java)

                            if (res.status == 1) {
                                CommonFunctions.showToast(activity,res.response)
//                                CommonFunctions.setPreference(
//                                    this@FragmentUserDetails,
//                                    Constants.isLogin,
//                                    false
//                                )
                              //  LoginActivity.startActivity(requireActivity())
                            } else {
                                CommonFunctions.showToast(activity, res.response)
                            }

                        }

                        override fun onError(anError: ANError?) {
                            CommonFunctions.destroyProgressBar()
                            CommonFunctions.showToast(activity, anError.toString())
                        }
                    })

            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun blockuser(userdetail: UserDetailsResponse.Data, position: Int,status: String) {
        blockunblockUser(userdetail.user_id.toInt(),status.toInt())
    }

    private fun blockunblockUser(id: Int,status: Int){

        try {

            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {

                var url: String = CamelConfig.WEBURL + CamelConfig.blockUser

               // var url: String ="https://uaqcrc.com/wp-json/camel/v1/block_user"
               // Log.e("SAn","logresponce:--"+url)
                val mParams: HashMap<String, String> = HashMap()
                mParams[Constants.status] = status.toString()
                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))
                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()

                AndroidNetworking.post(url)
                    .addBodyParameter(mParams)
                    .addBodyParameter("user_id",id.toString())
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .addHeaders("Accept", "application/json")
                    .setTag(url)
                    .setPriority(Priority.HIGH)
//                    .setOkHttpClient(okHttpClient)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                          //  Log.e("SAn","Response:--"+response)
                            CommonFunctions.destroyProgressBar()
                            var gson = Gson()
                            val res = gson.fromJson(response.toString(), UserBlockUnblockResponse::class.java)

                            if (res.status == 1) {
                                CommonFunctions.showToast(activity,res.response)


                            } else {
                                CommonFunctions.showToast(activity, res.response)
                            }

                        }

                        override fun onError(anError: ANError?) {
                            CommonFunctions.destroyProgressBar()
                            CommonFunctions.showToast(activity, anError.toString())
                        }
                    })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun subscription(
        userdetail: UserDetailsResponse.Data,
        position: Int,
        subscription: String
    ) {
        subscriptionUser(userdetail.user_id.toInt(),subscription)
    }

    private fun subscriptionUser(id: Int,subscription: String)
    {
        try{
            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {

                var url: String = CamelConfig.WEBURL + CamelConfig.change_subscrition_status

                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))
                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()

                AndroidNetworking.post(url)
                 //   .addBodyParameter(mParams)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .addBodyParameter("user_id",id.toString())
                    .addBodyParameter("subscription",subscription)
                    .addHeaders("Accept", "application/json")
                    .setTag(url)
                    .setPriority(Priority.HIGH)
//                    .setOkHttpClient(okHttpClient)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            //  Log.e("SAn","Response:--"+response)
                            CommonFunctions.destroyProgressBar()
                            var gson = Gson()
                            val res = gson.fromJson(
                                response.toString(),
                                ChangeSubscriptionStatusResponse::class.java)

                            if (res.status == 1) {
                                CommonFunctions.showToast(activity,res.response)


                            } else {
                                CommonFunctions.showToast(activity, res.response)
                            }

                        }

                        override fun onError(anError: ANError?) {
                            CommonFunctions.destroyProgressBar()
                            CommonFunctions.showToast(activity, anError.toString())
                        }
                    })


            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }


}