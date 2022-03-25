package com.adsum.camel_masterapplication.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adsum.camel_masterapplication.Adapter.ArchiveFromMonthAdapter
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.MonthWiseArchiveRes
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.FragmentFromArchiveBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.gson.Gson
import org.json.JSONObject
import kotlin.properties.Delegates


class FragmentFromArchive : Fragment(), ArchiveFromMonthAdapter.OnItemClickListener {
    private lateinit var binding: FragmentFromArchiveBinding
    private lateinit var archiveFromMonthAdapter: ArchiveFromMonthAdapter
    private var year: Int = 0
    private var month: Int = 0
    //private var user_id = 295
    private var user_id by Delegates.notNull<String>()

    companion object {
        fun newInstance(
            month: Int,
            year: Int,
        ): FragmentFromArchive {
            val fragment: FragmentFromArchive =
                FragmentFromArchive()
            val args = Bundle()
            args.putInt(Constants.month, month)
            args.putInt(Constants.year, year)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFromArchiveBinding.inflate(layoutInflater)
        month = requireArguments().getInt(Constants.month, month)
        year = requireArguments().getInt(Constants.year, year)
        user_id = CommonFunctions.getPreference(context, Constants.ID, "").toString()

        initFromArchive()

        return (binding.root)
    }

    private fun initFromArchive() {
        try {
            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
//                raceid = CommonFunctions.getPreference(activity, Constants.ID, 0).toString()
                val url: String = CamelConfig.WEBURL + CamelConfig.racelistArchiveMonthYear
                Log.e("san", "url---" + url)
                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))
                AndroidNetworking.post(url)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .addBodyParameter(Constants.year, year.toString())
                    .addBodyParameter(Constants.month, month.toString())
                    .addBodyParameter(Constants.user_id,user_id)
                    .setTag(url)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            Log.e("san", "res---" + response)
                            CommonFunctions.destroyProgressBar()
                            var gson = Gson()
                            val res = gson.fromJson(
                                response.toString(),
                                MonthWiseArchiveRes::class.java
                            )
                            if (res.status == 1) {
                                context?.let {
                                    initRace(it, res.data)
                                }
                                    archiveFromMonthAdapter.notifyDataSetChanged()

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

    private fun initRace(context: Context, archiveList: ArrayList<MonthWiseArchiveRes.DataX>) {

        archiveFromMonthAdapter = ArchiveFromMonthAdapter(context, archiveList, this)
        binding.rcArchiveFromMonth.adapter = archiveFromMonthAdapter
        if (archiveFromMonthAdapter.itemCount == 0) {
            CommonFunctions.showToast(context,"Data not found")
        }
    }

    fun openFragment(fragment: Fragment?) {
        val transaction = childFragmentManager.beginTransaction()
        transaction?.replace(R.id.sub_frame, fragment!!)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }

    override fun OnCustomClick(
        archiveList: MonthWiseArchiveRes.DataX.Round,
        position: Int,
        racename: String
    ) {
        openFragment(
            FragmentFromSubcategory.newInstance(
                archiveList.id.toInt(),
                archiveList.race_id.toInt(),
                archiveList.round_name, racename,
                archiveList.type,
                archiveList.customization,
                position
            )
        )
    }
}