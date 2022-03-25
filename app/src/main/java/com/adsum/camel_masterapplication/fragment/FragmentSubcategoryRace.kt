package com.adsum.camel_masterapplication.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.adsum.camel_masterapplication.Activity.DashboardActivity
import com.adsum.camel_masterapplication.Activity.PdfViewActivity
import com.adsum.camel_masterapplication.Adapter.SubCategoryRaceAdapter
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.*
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.CamelListSpinnerBinding
import com.adsum.camel_masterapplication.databinding.FragmentSubcategoryRaceBinding
import com.adsum.camel_masterapplication.databinding.PopupCamelAddBinding
import com.adsum.camel_masterapplication.databinding.PopupDeleteBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonArray
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import kotlin.properties.Delegates


class FragmentSubcategoryRace : Fragment(), SubCategoryRaceAdapter.OnsubdeleteClickListener,
    AdapterView.OnItemSelectedListener {
    private lateinit var rootView: View
    private lateinit var fragmentSubcategoryRaceBinding: FragmentSubcategoryRaceBinding
    private var popupDeleteBinding: PopupDeleteBinding? = null
    private var popupCamelAddBinding :PopupCamelAddBinding? = null
    private var binding: CamelListSpinnerBinding? = null
    private var subCategoryRaceAdapter: SubCategoryRaceAdapter? = null
    private var roundid: Int = 0
    private var race_id: Int = 0
    private var userid by Delegates.notNull<String>()
    private var rl_id: Int = 0
    private lateinit var res: AkbarResp
    private var position: Int = 0
    private var gender: String? = null
    private var racename = ""
    private var customization: String? = null
    private var roundname: String? = null
    private var arrayAdapter: ArrayAdapter<String>? = null
    private lateinit var malelist: ArrayList<AkbarResp.Data>
    private lateinit var femalelist: ArrayList<AkbarResp.Data>
    var Role by Delegates.notNull<String>()
    val malelist2 = ArrayList<String>()
    var malelisttemp = ArrayList<String>()
    var malelistID = ArrayList<String>()
    var malelistIDtemp = ArrayList<String>()
    val femalelist2 = ArrayList<String>()
    var femaletemp = ArrayList<String>()
    var femalelistID = ArrayList<String>()
    var femalelistIDtemp = ArrayList<String>()
    var from: String? = null
    private var spinnerid: String = ""
//    var response1 = ArrayList<String>()
//    private var response2: ArrayList<NoOfParticipateResponse.Data> = ArrayList()
    var listdata = ArrayList<ViewRoundList>()
    private lateinit var printList :String
    var data = JSONArray()
    lateinit var arrayData : String
    var dashboardActivity : DashboardActivity? = null


    companion object {
        fun newInstance(
            param1: Int,
            param2: Int,
            param3: String,
            param4: String,
            param5: String,
            param6: String,
            from: String,
            position: Int
        ): FragmentSubcategoryRace {
            val fragment = FragmentSubcategoryRace()
            val args = Bundle()
            args.putInt(Constants.id, param1)
            args.putInt(Constants.race_id, param2)
            args.putString(Constants.type, param3)
            args.putString(Constants.round_name, param4)
            args.putString(Constants.race_name, param6)
            args.putString(Constants.customization, param5)
            args.putString(Constants.from, from)
            args.putInt(Constants.position, position)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardActivity = (activity as DashboardActivity)
        fragmentSubcategoryRaceBinding =
            FragmentSubcategoryRaceBinding.inflate(inflater, container, false)
        popupDeleteBinding = PopupDeleteBinding.inflate(inflater, container, false)
        popupCamelAddBinding = PopupCamelAddBinding.inflate(inflater, container, false)
        rootView = fragmentSubcategoryRaceBinding.root
        roundid = requireArguments().getInt(Constants.id)
        race_id = requireArguments().getInt(Constants.race_id)
        racename = requireArguments().getString(Constants.race_name).toString()
        position = requireArguments().getInt(Constants.position)
        gender = requireArguments().getString(Constants.type)
        from = requireArguments().getString(Constants.from)
        customization = requireArguments().getString(Constants.customization)
        roundname = requireArguments().getString(Constants.round_name)
        Role=CommonFunctions.getPreference(activity, Constants.role, "").toString()
        malelist = ArrayList<AkbarResp.Data>()
        femalelist = ArrayList<AkbarResp.Data>()
        Log.e("id", "race = " + race_id)
        Log.e("id", "round = " + roundid)

       var g =""
        if (gender == "Male") {
            g = "جعدان"
        } else {
            g = "ابكار"
        }
        if (Role == "normal_user"){
            fragmentSubcategoryRaceBinding.btnAddCamel.visibility = View.GONE
            fragmentSubcategoryRaceBinding.tvCamelname.visibility = View.GONE
        }
        fragmentSubcategoryRaceBinding.tvSubSex.text = g
        fragmentSubcategoryRaceBinding.tvSubCostomization.text = customization
        fragmentSubcategoryRaceBinding.tvStrok.text = roundname
        fragmentSubcategoryRaceBinding.tvRaceid.text = race_id.toString() + ":" +racename
        userid = CommonFunctions.getPreference(context, Constants.ID, "").toString()
        init()
        dashboardActivity!!.tvBtnPrint.setOnClickListener {
            val intent = Intent(context, PdfViewActivity::class.java)
            intent.putExtra("data", arrayData)
            intent.putExtra("Name", racename)
            startActivity(intent)
        }
        return rootView
    }

    private fun initMale(context: Context, gender: String) {
        try {
            var url: String = CamelConfig.WEBURL + CamelConfig.malelist
            CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(ChuckerInterceptor(requireActivity()))
                .build()
            AndroidNetworking.post(url)
                .addHeaders("Accept", "application/json")
                .addQueryParameter(Constants.user_id, userid)
//            .addQueryParameter(Constants.user_id, userid.toString())
                .addHeaders(Constants.Authorization, Constants.Authkey)
                .setTag(url)
//                .setOkHttpClient(okHttpClient)
                .setPriority(Priority.HIGH)
                .build()
//            .getAsJSONArray(object : JSONArrayRequestListener {
//                override fun onResponse(response: JSONArray?) {
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        //Log.e("Tag","initmale"+response)
                        CommonFunctions.destroyProgressBar()
                        var gson = Gson()
                        res = gson.fromJson(
                            response.toString(),
                            AkbarResp::class.java
                        )
                        var i = 0
                        malelistID.clear()
                        malelistIDtemp.clear()
                        femalelistIDtemp.clear()
                        femalelistID.clear()
                        malelist2.clear()
                        femalelist2.clear()
                        while (i < res.data.size) {
                            if (res.data[i].rcGender == Constants.male || res.data[i].rcGender == "جعدان") {
                                malelist.add(res.data[i])
                                malelist2.add(res.data[i].rcCamel)
                                Log.e("tag", "maleList:" + i + "---" + res.data[i].rcId)
                                malelistID.add(res.data[i].rcId)
                            } else {
                                femalelist.add(res.data[i])
                                femalelist2.add(res.data[i].rcCamel)
                                Log.e("tag", "femaleList:" + i + "---" + res.data[i].rcId)
                                femalelistID.add(res.data[i].rcId)
                            }
                            i++
                        }
                    }

                    override fun onError(anError: ANError?) {
                        CommonFunctions.destroyProgressBar()
                        CommonFunctions.showToast(context, anError.toString())
                    }
                })
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("ResourceType")
    private fun init() {
        try {
            showList()
            fragmentSubcategoryRaceBinding.tvCamelname.setOnClickListener {
                showBottomSheetDialog()
            }
            //addcamel
            fragmentSubcategoryRaceBinding.btnAddCamel.setOnClickListener {
                var url: String = CamelConfig.WEBURL + CamelConfig.addCamelMember
                Log.e("tag", "url:-" + url)
//Progress start
                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))
                AndroidNetworking.post(url)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .addBodyParameter(Constants.race_id, race_id.toString())
                    .addBodyParameter(Constants.round_id, roundid.toString())
                    .addBodyParameter(Constants.user_id, userid.toString())
                    .addBodyParameter(Constants.camel_id, spinnerid)
                    .setTag(url)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            Log.e("tag", "response:-" + response)
                            //Destroy Progressbar
                            CommonFunctions.destroyProgressBar()
                            var gson = Gson()
                            val res = gson.fromJson(
                                response.toString(),
                                AddRoundMemberResponse::class.java
                            )
                            if (res.status == 1) {
                                val dialog = activity?.let { Dialog(it) }
                                dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                dialog.setCancelable(false)
                                dialog.setContentView(R.layout.popup_camel_add)
                                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                val Done = dialog.findViewById(R.id.doneTVAddCamel) as TextView?
                                Done?.setOnClickListener {
                                    showList()
                                    dialog.dismiss()
                                }
                                dialog.show()

                                CommonFunctions.showToast(activity, res.response)
                            } else {
                                CommonFunctions.showToast(activity, res.response)
                            }
                        }

                        override fun onError(anError: ANError?) {
                            fragmentSubcategoryRaceBinding.btnAddCamel.isEnabled = true
                            CommonFunctions.destroyProgressBar()
                        }
                    })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showList() {
        try {
            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
                val url: String = CamelConfig.WEBURL + CamelConfig.ViewRoundMemberlisting
                //   val data = JSONObject()
                CommonFunctions.createProgressBar(context, "Please Wait")
                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()
                AndroidNetworking.post(url)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .addQueryParameter(Constants.race_id, race_id.toString())
                    .addQueryParameter(Constants.round_id, roundid.toString())
                    //   .addJSONObjectBody(data)
                    .setTag(url)
                    .setPriority(Priority.HIGH)
//                    .setOkHttpClient(okHttpClient)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            listdata.clear()
                            Log.e("Tag", "response:-" + response)
                            CommonFunctions.destroyProgressBar()
                            var array: JSONArray = response!!.getJSONArray("data")
                            for (i in 0..array.length() - 1) {
                                var dataObj: JSONObject? = array.getJSONObject(i)
                                try {
                                    var members: JSONObject = dataObj!!.getJSONObject("members")
                                    Log.e("San", "Gender " + members.getString("rc_gender"))
                                    var memberlistArray: JSONArray =
                                        members.getJSONArray("member_list")
                                    Log.e("San", "length " + memberlistArray.length())
                                    for (j in 0..memberlistArray.length() - 1) {
                                        var item: JSONObject = memberlistArray.getJSONObject(j)
                                        var itemUser: JSONObject = item.getJSONObject("user")

                                        var arayItem = ViewRoundList()
                                        arayItem.rc_camel = item.getString("rc_camel")
                                        arayItem.rl_id = item.getString("rl_id")
                                        arayItem.name_of_participant =
                                            itemUser.getString("name_of_participant")
                                        arayItem.camel_no = itemUser.getString("camel_no")
                                        listdata.add(arayItem)
                                    }
                                    Log.e("San", "length " + listdata.size)
                                    Log.e("San", "listdata " + listdata)
                                } catch (e: JSONException) {
                                    Log.e("San", "no membar  " + e)
                                }
                            }
//                            data = JSONArray(listdata)
//                            Log.e("array", "dataSize = " + data.length())

                            subCategoryRaceAdapter =
                                context?.let {
                                    SubCategoryRaceAdapter(
                                        it,
                                        listdata,
                                        this@FragmentSubcategoryRace,
                                        from.toString(), Role
                                    )
                                }
                            fragmentSubcategoryRaceBinding.subRaceRecyclerView.adapter =
                                subCategoryRaceAdapter
                            subCategoryRaceAdapter?.notifyDataSetChanged()

                            val gson = Gson()
                            arrayData = gson.toJson(listdata)
                            Log.e("res...", "res------" + arrayData)
                            gender?.let {
                                initMale(requireActivity(), it)
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

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = context?.let { BottomSheetDialog(it) }
        bottomSheetDialog?.setContentView(R.layout.camel_list_spinner)
        bottomSheetDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val spinner: Spinner = bottomSheetDialog?.findViewById<View>(R.id.spinner_list) as Spinner
        val done = bottomSheetDialog.findViewById<View>(R.id.tv_done)
        if (gender == "Male" || gender == "جعدان") {
            arrayAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                malelist2
            )
        } else {
            arrayAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                femalelist2
            )
        }
        done?.setOnClickListener {
            val text: String = spinner.getSelectedItem().toString()
            if (gender == "Male" || gender == "جعدان")
                spinnerid = malelist.get(spinner.selectedItemPosition).rcId
            else
                spinnerid = femalelist.get(spinner.selectedItemPosition).rcId
            fragmentSubcategoryRaceBinding.tvCamelname.text = text
            bottomSheetDialog.dismiss()
        }
        spinner.adapter = arrayAdapter
        arrayAdapter!!.notifyDataSetChanged()
        bottomSheetDialog?.show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        binding?.tvDone!!.text = "Selected : " + malelist2
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun OndeleteClick(subcategory: ViewRoundList, position: Int) {
        val dialog = activity?.let { Dialog(it) }
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.popup_delete)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val Done = dialog.findViewById(R.id.tv_deletecamel) as TextView
        val cancel = dialog.findViewById(R.id.tv_cancelcamel) as TextView
        Done.setOnClickListener {
            Delete(subcategory.rl_id!!.toInt(), position)
            dialog.dismiss()
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun Delete(rl_id: Int, position: Int) {
        try {
            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
                var url: String = CamelConfig.WEBURL + CamelConfig.removecamel
//Progress start
                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))
                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()
                AndroidNetworking.post(url)
                    .addBodyParameter("rl_id", rl_id.toString())
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .setTag(url)
                    //.setOkHttpClient(okHttpClient)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            //Destroy Progressbar
                            CommonFunctions.destroyProgressBar()
                            var gson = Gson()
                            val res = gson.fromJson(
                                response.toString(),
                                SubRaceResponse::class.java
                            )
                            if (res.status == 1) {
                                CommonFunctions.showToast(activity, res.response)
                                subCategoryRaceAdapter?.deleteSubCategory(position)
//                                gender?.let {
//                                    initMale(requireActivity(), it)
//                                }
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