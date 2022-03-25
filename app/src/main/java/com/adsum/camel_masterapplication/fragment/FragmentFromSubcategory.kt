package com.adsum.camel_masterapplication.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adsum.camel_masterapplication.Adapter.FromSubCategoryAdapter
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.ParticipateInRaceRoundRes
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.FragmentFromSubCategoryBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import kotlin.properties.Delegates

class FragmentFromSubcategory : Fragment(){
    private lateinit var binding: FragmentFromSubCategoryBinding
    private lateinit var fromSubCategoryAdapter: FromSubCategoryAdapter
    private var userid by Delegates.notNull<String>()
    private var rl_id = 0
    private var race_id: Int = 0
    private var position: Int = 0
    private var gender: String? = null
    private var racename = ""
    private var roundid: Int = 0
    private var customization: String? = null
    private var roundname: String? = null

    companion object {
        fun newInstance(
            id: Int,
            raceId: Int,
            roundName: String,
            raceName: String,
            type: String,
            customization : String,
            position: Int
        ): FragmentFromSubcategory {
            val fragment = FragmentFromSubcategory()
            val args = Bundle()
            args.putInt(Constants.id, id)
            args.putInt(Constants.race_id, raceId)
            args.putString(Constants.round_name, roundName)
            args.putString(Constants.race_name, raceName)
            args.putString(Constants.type, type)
            args.putString(Constants.customization,customization)
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
        binding = FragmentFromSubCategoryBinding.inflate(inflater, container, false)

        roundid = requireArguments().getInt(Constants.id)
        userid = requireArguments().getInt("user_id").toString()
        roundname = requireArguments().getString(Constants.round_name)
        race_id = requireArguments().getInt(Constants.race_id)
        gender = requireArguments().getString(Constants.type)
        customization = requireArguments().getString(Constants.customization)
        racename = requireArguments().getString(Constants.race_name).toString()
        binding.fragmentFromSubGender.text = gender
        binding.fromSubCostumization.text = customization
        binding.raceIdFromSub.text = racename
        binding.roundNameFromSub.text = roundname

        showList()

        return (binding.root)
    }

    private fun showList() {
        if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
            val url: String = CamelConfig.WEBURL + CamelConfig.participate_in_Raceround
            Log.e("san", "url---" + url)
            //Progress start
            val data = JSONObject()
            CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(ChuckerInterceptor(requireActivity()))
                .build()

            AndroidNetworking.post(url)
                .addHeaders(Constants.Authorization, Constants.Authkey)
                .addQueryParameter(Constants.race_id, race_id.toString())
                .addQueryParameter(Constants.round_id, roundid.toString())
                .setTag(url)
                //.addJSONObjectBody(data)
                .setPriority(Priority.HIGH)
//                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONArray(object : JSONArrayRequestListener {
                    override fun onResponse(response: JSONArray?) {
                        Log.e("san", "res---" + response)
                        //Destroy Progressbar
                        CommonFunctions.destroyProgressBar()
                        val gson = Gson()
                        val res = gson.fromJson(
                            response.toString(), ParticipateInRaceRoundRes::class.java
                        )
                        context?.let {
                            initFromSub(it,res)
                            fromSubCategoryAdapter.notifyDataSetChanged()
                        }
                        fromSubCategoryAdapter.notifyDataSetChanged()

                    }

                    override fun onError(anError: ANError?) {
                        CommonFunctions.destroyProgressBar()
                        Log.e("msg", "An ERROR" + anError)
                    }
                })
        }
    }

    private fun initFromSub(context: Context, subList: ParticipateInRaceRoundRes) {
        fromSubCategoryAdapter = FromSubCategoryAdapter(context,subList)
        binding.fromSubRc.adapter = fromSubCategoryAdapter
        if (fromSubCategoryAdapter.itemCount == 0) {
            CommonFunctions.showToast(context, "Data not found")
        }
    }

}