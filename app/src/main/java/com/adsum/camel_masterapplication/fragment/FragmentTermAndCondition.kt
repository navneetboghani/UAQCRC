package com.adsum.camel_masterapplication.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adsum.camel_masterapplication.Adapter.TermConditionDetailAdapter
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.TermConditionResponse
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.FragmentTermAndConditionBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONObject


class FragmentTermAndCondition : Fragment(),TermConditionDetailAdapter.OnTermConditionClickListener {

    private lateinit var binding: FragmentTermAndConditionBinding
    private lateinit var rootView: View
    private lateinit var adapter: TermConditionDetailAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTermAndConditionBinding.inflate(inflater, container, false)
        rootView = binding.root

        binding.rvTermCondition.setHasFixedSize(true)

        getTermConditionDetail()

        return rootView
    }

    companion object {

        fun newInstance(
            param1: String?,
            param2: String?
        ): FragmentTermAndCondition {
            val fragment: FragmentTermAndCondition =
                FragmentTermAndCondition()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    private fun getTermConditionDetail(){
        try {
            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {

                val url: String = CamelConfig.WEBURL + CamelConfig.get_term_condition_details


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
                            Log.e("San","res"+response)

                            CommonFunctions.destroyProgressBar()
                            val gson = Gson()
                            try{
                                val res = gson.fromJson(response.toString(), TermConditionResponse::class.java)

                                if (res.status == 1) {
                                    CommonFunctions.destroyProgressBar()

                                    context?.let { initTermandConditionRv(it, res.data)}

                                }else{
                                    CommonFunctions.destroyProgressBar()

                                    CommonFunctions.showToast(activity,"else")
                                }

                            }catch (e : Exception){

//                                val res = gson.fromJson(response.toString(), NotificationErrorResponse::class.java)
//
//                                if (res.status == 0) {
//                                    CommonFunctions.destroyProgressBar()
//
//                                    CommonFunctions.showToast(activity, res.message)
//
//                                }else{
//                                    CommonFunctions.destroyProgressBar()
//
//                                    CommonFunctions.showToast(activity,res.message)
//                                }

                            }

                        }


                        override fun onError(anError: ANError?) {
                            CommonFunctions.destroyProgressBar()
                            // Log.e("San","res:--"+anError)
                            CommonFunctions.showToast(context, anError.toString())


                        }

                    })

            }


        }catch (e:Exception){

        }

    }

    private fun initTermandConditionRv(context: Context, data: List<TermConditionResponse.Data>) {
        adapter = TermConditionDetailAdapter(context, data, this)
        binding.rvTermCondition.adapter = adapter

    }

    override fun onClick(termCondition: TermConditionResponse.Data, position: Int) {
        TODO("Not yet implemented")
    }
}