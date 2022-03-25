package com.adsum.camel_masterapplication.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.Adapter.MaleAdapter
import com.adsum.camel_masterapplication.Config.Constants

import com.adsum.camel_masterapplication.Model.*
import com.adsum.camel_masterapplication.databinding.*
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONObject
import kotlin.properties.Delegates


class MaleFragment : Fragment(), MaleAdapter.OndeleteClickListener {

    private lateinit var mainAkbarRes : ArrayList<AkbarResp.Data>
    private  var dataAkbarRes : ArrayList<AkbarResp.Data>? = null
    private lateinit var maleAdapter: MaleAdapter
    private lateinit var malebinding: FragmentMaleBinding
    private lateinit var alertPopupBinding: AlertPopupBinding
    private lateinit var addCamelPopupBinding: AddCamelPopupBinding
    private var popupDeleteBinding: PopupDeleteBinding? = null
    private lateinit var rootView: View
    private var user_id by Delegates.notNull<String>()
    var subscription by Delegates.notNull<String>()
    var count: Int = 0
    var sub = ""

    companion object {
        var resmain = MaleResponse()

        fun newInstance(
        ): MaleFragment {
            val fragment: MaleFragment =
                    MaleFragment()
            val args = Bundle()
            fragment.setArguments(args)
            return fragment
        }
    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        malebinding = FragmentMaleBinding.inflate(inflater, container, false)
        addCamelPopupBinding = AddCamelPopupBinding.inflate(LayoutInflater.from(context))
        rootView = malebinding.root
        popupDeleteBinding = PopupDeleteBinding.inflate(inflater, container, false)
        user_id = CommonFunctions.getPreference(activity, Constants.ID, "").toString()
        subscription = CommonFunctions.getPreference(activity,Constants.subscription,"").toString()

        init()
        return rootView
    }

    fun onCreateDialog(): Dialog? {

        val builder: AlertDialog.Builder? = context?.let {
            AlertDialog.Builder(it)
        }
        builder?.setView(addCamelPopupBinding.root)
        addCamelPopupBinding.edtCamel.setHint("أدخل اسم الجمل")
        addCamelPopupBinding.edtCamel.setText("")

        val dialog = builder?.create()
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    fun onCreateDialog1(): Dialog? {

        val builder: AlertDialog.Builder? = context?.let {
            AlertDialog.Builder(it)
        }
        builder?.setView(alertPopupBinding.root)
        return builder?.create()

    }

    private fun addCamel(camelName: String, gender: String, status: Int, id: String) {
        var d = onCreateDialog()
        val url: String = CamelConfig.WEBURL + CamelConfig.addcamel

        //Progress start

        CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(ChuckerInterceptor(requireActivity()))
                .build()
        AndroidNetworking.get(url)
                .addHeaders(Constants.Authorization, Constants.Authkey)
                .addQueryParameter(Constants.camel, camelName)
                .addQueryParameter(Constants.gender, gender)
                .addQueryParameter(Constants.status, status.toString())
                .addQueryParameter(Constants.user_id, id.toString())
                .setTag(url)
                .setPriority(Priority.HIGH)
//                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        //  Log.e("san","responsee:--" +response)

                        //Destroy Progressbar
                        CommonFunctions.destroyProgressBar()
                        val gson = Gson()
                        val res = gson.fromJson(response.toString(), AddCamelResponse::class.java)

                        if (res.status == 1) {
                            getdata()
                        } else {
                            CommonFunctions.showToast(activity, res.response)
                        }
                    }

                    override fun onError(anError: ANError?) {
                        CommonFunctions.destroyProgressBar()
                        CommonFunctions.showToast(context, anError.toString())
                    }
                })


    }

    private fun init() {
        try {
            getdata()
            var d = onCreateDialog()
            malebinding.btnAddMaleCamel.setOnClickListener {
                addCamelPopupBinding.edtCamel.setText("")
                d?.show()
            }
            addCamelPopupBinding.tvAddCamelList.setOnClickListener {
                for (j in 0..resmain.size - 1) {
                    val subcategory = resmain[j]
                    if (subcategory.rcCamel.equals(addCamelPopupBinding.edtCamel.text.toString())) {
                        CommonFunctions.showToast(activity, "الجمل موجود بالفعل")
                        return@setOnClickListener
                    }
                }
                if (count < 10) {
                    if (addCamelPopupBinding.edtCamel.text.toString().isNotEmpty()) {
                        //filter()

                        addCamel(
                                addCamelPopupBinding.edtCamel.text.toString(),
                                Constants.male,
                                1,
                                user_id
                        )
                        d?.dismiss()
                    } else {
                        addCamelPopupBinding.edtCamel.requestFocus()
                        CommonFunctions.showToast(requireContext(), "Camel name should not blank")
                    }
                } else {
                    d?.dismiss()
                    CommonFunctions.showToast(requireContext(), "Cannot add more than 10 camel")
                }
            }

            addCamelPopupBinding.tvCancel.setOnClickListener {
                addCamelPopupBinding.edtCamel.setText("")
                d?.dismiss()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getdata() {

        if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
            // raceid = CommonFunctions.getPreference(context, Constants.ID, 0)
            val url: String = CamelConfig.WEBURL + CamelConfig.malelist + user_id
            //  Log.e("san", "url:---" + url)

//Progress start


            CommonFunctions.createProgressBar(context, getString(R.string.please_wait))

            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()

            AndroidNetworking.get(url)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .setTag(url)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onResponse(response: JSONObject?) {
                            // Log.e("san", "response3:---" + response)
                            CommonFunctions.destroyProgressBar()
                            val gson = Gson()
                            // CommonFunctions.showToast(context,"Data"+ )
                            val res = gson.fromJson(
                                    response.toString(),
                                    AkbarResp::class.java
                            )
                            sub = res.Subscriber
                            if (res.status == 1) {
                                mainAkbarRes  = res.data as ArrayList<AkbarResp.Data>
                                // Log.e("san","mainAkbarRes:---"+mainAkbarRes)
                                val filterlist = mainAkbarRes.filter  { it.rcGender == "Male" }
                                //  Log.e("san","filterList:---"+filterlist)
                                setadapterdata(filterlist)
                                maleAdapter.notifyDataSetChanged()
                                if (subscription == "0"){
                                    malebinding.btnAddMaleCamel.visibility=View.GONE
                                    malebinding.tvSurvey.visibility = View.GONE
                                }else{
                                    malebinding.btnAddMaleCamel.visibility=View.VISIBLE
                                    malebinding.tvSurvey.visibility = View.VISIBLE
                                }
                            }
                            if (res.Subscriber == "0") {
                                malebinding.btnAddMaleCamel.visibility=View.GONE
                                malebinding.tvSurvey.visibility = View.GONE
                            }else{
                                malebinding.btnAddMaleCamel.visibility=View.VISIBLE
                                malebinding.tvSurvey.visibility = View.VISIBLE
                            }

                        }
                        override fun onError(anError: ANError?) {
                            CommonFunctions.destroyProgressBar()
                        }

                    })
        }
    }
    private fun setadapterdata(res: List<AkbarResp.Data>) {
        maleAdapter = MaleAdapter(requireActivity(), res as ArrayList<AkbarResp.Data>,this,subscription,sub)
        malebinding.maleRecycle.adapter = maleAdapter
        count = maleAdapter.itemCount
    }
    override fun OndeleteClick(malelist: AkbarResp.Data, position: Int) {
        val dialog = activity?.let { Dialog(it) }
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.popup_delete)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val Done = dialog.findViewById(R.id.tv_deletecamel) as TextView
        val cancel = dialog.findViewById(R.id.tv_cancelcamel) as TextView
//        body.text = title
//        val yesBtn = dialog.findViewById(R.id.yesBtn) as Button
//        val noBtn = dialog.findViewById(R.id.noBtn) as TextView
        Done.setOnClickListener {
            DeleteCamel1(malelist.rcId.toInt(),position)
            dialog.dismiss()
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun DeleteCamel1(id: Int,position: Int) {
        try {

            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
                var url: String = CamelConfig.WEBURL + CamelConfig.removeCamel + id
//Progress start
                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))

                val okHttpClient = OkHttpClient.Builder()
                        .addInterceptor(ChuckerInterceptor(requireActivity()))
                        .build()

                AndroidNetworking.get(url)
                        .addHeaders(Constants.Authorization, Constants.Authkey)
                        .setTag(url)
//                    .setOkHttpClient(okHttpClient)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(object : JSONObjectRequestListener {
                            override fun onResponse(response: JSONObject?) {
                                Log.e("tag","delete res:-"+response)
                                //Destroy Progressbar
                                CommonFunctions.destroyProgressBar()
                                var gson = Gson()
                                val res = gson.fromJson(
                                        response.toString(),
                                        DeleteCamelResponse::class.java
                                )
                                if (res.status == 1) {
                                    //CommonFunctions.showToast(activity, res.response)
                                    maleAdapter.DeleteMaleCamel(position)
                                } else {
                                    CommonFunctions.showToast(activity, res.response)
                                }
                            }

                            override fun onError(anError: ANError?) {
                                CommonFunctions.destroyProgressBar()
                            }

                        })

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}