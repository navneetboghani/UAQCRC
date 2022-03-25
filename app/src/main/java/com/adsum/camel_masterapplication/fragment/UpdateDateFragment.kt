package com.adsum.camel_masterapplication.fragment

import android.app.*
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.renderscript.RenderScript
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.adsum.camel_masterapplication.Adapter.RaceDetailAdapter
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.RaceDetailResponse
import com.adsum.camel_masterapplication.Model.UpdateDateResponse
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.FragmentRaceDetailBinding
import com.adsum.camel_masterapplication.databinding.FragmentUpdateDateBinding
import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog


class UpdateDateFragment : DialogFragment() {

    private var updateDateBinding: FragmentUpdateDateBinding? = null
    private var raceDetailAdapter: RaceDetailAdapter? = null
    private lateinit var raceDetailBinding: FragmentRaceDetailBinding
    var position: Int = 0;
    var treadingContentList: ArrayList<RaceDetailResponse.Data>? = ArrayList()
    var raceid: String? = null
    var strStartDateTime: String? = ""
    var strEndDateTime: String? = " "

    var cal = Calendar.getInstance()
    val currentDateTime = Calendar.getInstance()

    companion object {
        var updateCallback: UpdateDialogInterface? = null
        fun newInstance(
            raceId: String,
            position: Int,
            updateCallbackMain: UpdateDialogInterface
        ): UpdateDateFragment {
            val fragment: UpdateDateFragment =
                UpdateDateFragment()
            val args = Bundle()
            fragment.setArguments(args)
            args.putString(Constants.race_id, raceId)
            args.putInt(Constants.position, position)
            updateCallback = updateCallbackMain

            //args.putString("raceid",raceId)
            return fragment
        }


    }

    interface UpdateDialogInterface {
        fun onFinishEditDialog(startdate: String?, endDate: String?, position: Int?)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        // Toast.makeText(context,"onCancel",Toast.LENGTH_LONG).show()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        //Toast.makeText(context,"onDismiss",Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        var rootView: View = inflater.inflate(R.layout.fragment_update_date, container, false)
        raceid = requireArguments().getString(Constants.race_id).toString()
        // Log.e("tag", "raceid--" + raceid)
        position = requireArguments().getInt(Constants.position)
        Log.e("tag", "position--" + position)



        updateDateBinding?.tvStartDateTime?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                showBottomSheetDialog()


            }
        })

        updateDateBinding?.tvEndDateTime?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {

                showBottomSheetDialog1()

            }

        })


        updateDateBinding?.cardView2?.setOnClickListener {
            update()
        }

        return rootView
    }

    private fun showBottomSheetDialog1() {
        try {
            val bottomSheetDialog = context?.let { BottomSheetDialog(it) }
            bottomSheetDialog?.setContentView(R.layout.fragment_change_date_diaog)
            val picker = bottomSheetDialog!!.findViewById<SingleDateAndTimePicker>(R.id.picker)
            val buttonOk = bottomSheetDialog!!.findViewById<TextView>(R.id.buttonOk)
            var dateTime: Date? = null
            val changeListener =
                SingleDateAndTimePicker.OnDateChangedListener { displayed: String?, date: Date? ->
                    dateTime = date
                }
            picker!!.addOnDateChangedListener(changeListener)

            buttonOk!!.setOnClickListener {
                //updateDateInView()
                var simpleDateFormat: SimpleDateFormat
                var simpleDateFormat1: SimpleDateFormat
                Log.e("San", " i m button ok cilck dateTime: " + dateTime)

                simpleDateFormat =
                    SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.getDefault())
                simpleDateFormat1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                updateDateBinding?.tvEndDateTime!!.text = simpleDateFormat.format(dateTime)
                strEndDateTime = simpleDateFormat1.format(dateTime)
                bottomSheetDialog.dismiss()
            }
            bottomSheetDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


//    private fun updateDateInView() {
//        val myFormat = "yyyy-MM-dd hh:mm:ss" // mention the format you need
//        val sdf = SimpleDateFormat(myFormat, Locale.US)
//        updateDateBinding?.tvStartDateTime!!.text = sdf.format(cal.getTime())
//        //updateDateBinding?.tvEndDateTime!!.text = sdf.format(cal.getTime())
//    }

//    private fun updateDateInView1() {
//        val myFormat = "yyyy-MM-dd hh:mm:ss" // mention the format you need
//        val sdf = SimpleDateFormat(myFormat, Locale.US)
//        updateDateBinding?.tvEndDateTime!!.text = sdf.format(cal.getTime())
//        //updateDateBinding?.tvEndDateTime!!.text = sdf.format(cal.getTime())
//    }

    fun openFragment(fragment: Fragment?, name: String) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(com.adsum.camel_masterapplication.R.id.frameLayout1, fragment!!)
        transaction?.addToBackStack(name)
        transaction?.commit()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        updateDateBinding = FragmentUpdateDateBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
        isCancelable = true
        builder.setView(updateDateBinding!!.root)
        updateDateBinding!!.root.setOnClickListener {
            requireActivity().finish()
        }
        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = context?.let { BottomSheetDialog(it) }
        bottomSheetDialog?.setContentView(R.layout.fragment_change_date_diaog)
        val picker = bottomSheetDialog!!.findViewById<SingleDateAndTimePicker>(R.id.picker)
        val buttonOk = bottomSheetDialog!!.findViewById<TextView>(R.id.buttonOk)
        var dateTime: Date? = null
        val changeListener =
            SingleDateAndTimePicker.OnDateChangedListener { displayed: String?, date: Date? ->
                dateTime = date
            }
        picker!!.addOnDateChangedListener(changeListener)
        buttonOk!!.setOnClickListener {
            //updateDateInView()
            var simpleDateFormat: SimpleDateFormat
            var simpleDateFormat1: SimpleDateFormat

            Log.e("San", " i m button ok cilck dateTime: " + dateTime)

            simpleDateFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.getDefault())
            simpleDateFormat1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            updateDateBinding?.tvStartDateTime!!.text = simpleDateFormat.format(dateTime)
            strStartDateTime = simpleDateFormat1.format(dateTime)

            bottomSheetDialog.dismiss()

        }
        bottomSheetDialog?.show()
    }

    private fun update() {
        try {
            if (strStartDateTime!!.trim().length == 0) {
                Toast.makeText(
                    getActivity(),
                    "enter startDate",
                    Toast.LENGTH_SHORT
                ).show();
            } else if (strEndDateTime!!.toString().trim().length == 0) {
                Toast.makeText(getActivity(), "enter endDate", Toast.LENGTH_SHORT)
                    .show();
            } else {

                updateDate()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateDate() {
        try {

            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {

                var url: String = CamelConfig.WEBURL + CamelConfig.updateRaceSchedule
                Log.e("tag", "url:-" + url)

                //CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))
                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()
                AndroidNetworking.post(url)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .addBodyParameter(
                        "fromdate",
                        updateDateBinding?.tvStartDateTime?.text.toString()
                    )
                    .addBodyParameter("todate", updateDateBinding?.tvEndDateTime?.text.toString())
                    .addBodyParameter("raceid", raceid)
                    .setTag(url)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            Log.e("tag", "res:-" + response)
                            //Destroy Progressbar
                            CommonFunctions.destroyProgressBar()
                            var gson = Gson()
                            val res = gson.fromJson(
                                response.toString(),
                                UpdateDateResponse::class.java
                            )
                            if (res.status == 1) {
                                // CommonFunctions.showToast(context, res.response)
                                // getData()


                                updateCallback?.onFinishEditDialog(
                                    updateDateBinding?.tvStartDateTime?.text.toString(),
                                    updateDateBinding?.tvEndDateTime?.text.toString(), position
                                )
                                // raceDetailAdapter?.notifyDataSetChanged()
                                dialog?.dismiss()
                            }

                        }

                        override fun onError(anError: ANError?) {
                            // CommonFunctions.destroyProgressBar()
                        }
                    })

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener) {
        Toast.makeText(context, "abx", Toast.LENGTH_LONG).show()
    }
}




