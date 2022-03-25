package com.adsum.camel_masterapplication.fragment

import android.app.Dialog
import android.app.FragmentManager
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
import androidx.fragment.app.FragmentTransaction
import com.adsum.camel_masterapplication.Adapter.AdminRaceScheduleAdapter
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.GetScheduleResponse
import com.adsum.camel_masterapplication.Model.RemoveRaceScheduleImage
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.FragmentAdminScheduleBinding
import com.adsum.camel_masterapplication.databinding.PopupImageDeleteBinding
import com.adsum.camel_masterapplication.databinding.PopupRaceSchedulAddBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.gson.Gson
import org.json.JSONObject

class FragmentAdminRaceSchedule : Fragment(), AdminRaceScheduleAdapter.OnItemClickListener {
    private lateinit var fragmentAdminScheduleBinding: FragmentAdminScheduleBinding
    private lateinit var adminRaceScheduleAdapter: AdminRaceScheduleAdapter
    private lateinit var popupImageDeleteBinding: PopupImageDeleteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentAdminScheduleBinding =
            FragmentAdminScheduleBinding.inflate(inflater, container, false)
        popupImageDeleteBinding = PopupImageDeleteBinding.inflate(LayoutInflater.from(requireContext()))

        return (fragmentAdminScheduleBinding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        init()

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
//                              initRace(res.data)
                                adminRaceScheduleAdapter = AdminRaceScheduleAdapter(
                                    ArrayList(res.data),
                                    this@FragmentAdminRaceSchedule
                                )
                                fragmentAdminScheduleBinding.RCAdminSchedule.adapter =
                                    adminRaceScheduleAdapter
                                adminRaceScheduleAdapter.notifyDataSetChanged()
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

    override fun ReselectingImage(rc_id: Int, image: String, position: Int) {
//        val image: String = imageList.rs_image
        val fragment = FragmentImageUpdate.newInstance(rc_id, image, position)
//        val args = Bundle()
//        args.putString("Image", image)
//        fragment.setArguments(args)
        val ft: FragmentTransaction = activity?.supportFragmentManager?.beginTransaction()!!
        ft.addToBackStack(null)
        ft.replace(R.id.fram, fragment)
        ft.commit()

    }

    override fun DeleteImage(imageList: GetScheduleResponse.Data, position: Int) {
        val dialog = activity?.let { Dialog(it) }
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.popup_image_delete)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val Done = dialog.findViewById(R.id.doneTVImage) as TextView
        val cancel = dialog.findViewById(R.id.cancelTextViewImage) as TextView
        Done.setOnClickListener {
            delete(imageList.rs_id.toInt(),position)
            dialog.dismiss()
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    fun delete(raceId: Int,position: Int){
        try {
                if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
                    var url: String = CamelConfig.WEBURL + CamelConfig.rmvRaceSchedule
                    Log.e("san", "url---" + url)
                    CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))
                    AndroidNetworking.post(url)
                        .addHeaders(Constants.Authorization, Constants.Authkey)
                        .addBodyParameter("rs_id", raceId.toString())
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
                                    RemoveRaceScheduleImage::class.java
                                )
                                if (res.status == 1) {
                                    CommonFunctions.destroyProgressBar()
                                    CommonFunctions.showToast(activity, "Successfully delete")
                                    adminRaceScheduleAdapter.DeleteImage(position)
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