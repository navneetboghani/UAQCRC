package com.adsum.camel_masterapplication.fragment

import android.content.Intent
import android.content.Intent.getIntent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adsum.camel_masterapplication.Activity.ImagePreviewActivity
import com.adsum.camel_masterapplication.Adapter.RaceScheduleAdapter
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.GetScheduleResponse
import com.adsum.camel_masterapplication.databinding.FragmentRaceScheduleBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.gson.Gson
import org.json.JSONObject


class FragmentRaceSchedule : Fragment() {

    private lateinit var binding: FragmentRaceScheduleBinding
    private lateinit var raceScheduleAdapter: RaceScheduleAdapter

    companion object {
        fun newInstance(
            param1: String?,
            param2: String?
        ): FragmentRaceSchedule {
            val fragment: FragmentRaceSchedule =
                FragmentRaceSchedule()
            val args = Bundle()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRaceScheduleBinding.inflate(inflater, container, false)

        init()

        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun init() {
        try {
            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
                var url: String = CamelConfig.WEBURL + CamelConfig.getSchedulImage
                Log.e("san", "url---" + url)
                CommonFunctions.createProgressBar(
                    activity,
                    getString(com.adsum.camel_masterapplication.R.string.please_wait)
                )

                AndroidNetworking.get(url)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .setTag(url)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            Log.e("san", "res---" + response)
                            //Destroy Progressbar
                            CommonFunctions.destroyProgressBar()
                            var gson = Gson()
                            val res = gson.fromJson(
                                response.toString(),
                                GetScheduleResponse::class.java
                            )
                            if (res.status == 1) {
//                                initRace(res.data)
                                raceScheduleAdapter = RaceScheduleAdapter(res.data,
                                    object : RaceScheduleAdapter.OnItemClickListener {
                                        override fun OnCustomClick(
                                            imageList: GetScheduleResponse.Data,
                                            position: Int
                                        ) {
                                            val images : String = imageList.rs_image
                                            val intent = Intent(
                                                activity,
                                                ImagePreviewActivity::class.java
                                            )
                                            intent.putExtra("images", images)
                                            startActivity(intent)
                                        }

                                    })
                                binding.rsRecycleView.adapter = raceScheduleAdapter
                                raceScheduleAdapter.notifyDataSetChanged()
                            }
                            else{
                                CommonFunctions.showToast(requireContext(),"Data not found")
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

//    private fun initRace(scheduleList: List<GetScheduleResponse.Data>) {
//        raceScheduleAdapter = RaceScheduleAdapter(scheduleList, this)
//        binding.rsRecycleView.adapter = raceScheduleAdapter
//    }
//
//    override fun OnCustomClick(
//        imagelist: GetScheduleResponse.Data,
//        position: Int,
//        id: String,
//        image: String
//    ) {
//        val intent = Intent(activity, ImagePreviewActivity::class.java)
//        intent.putExtra("image", image)
//        startActivity(intent)
//    }
}