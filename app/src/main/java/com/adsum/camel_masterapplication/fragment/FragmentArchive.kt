package com.adsum.camel_masterapplication.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adsum.camel_masterapplication.Activity.DashboardActivity
import com.adsum.camel_masterapplication.Adapter.ArchiveAdapter
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.ArchiveData
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.ActivityDashboardBinding
import com.adsum.camel_masterapplication.databinding.FragmentArchiveBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.gson.Gson
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


@Suppress("UNREACHABLE_CODE")
class FragmentArchive : Fragment() {
    private lateinit var binding: FragmentArchiveBinding
    private lateinit var historyAdapter: ArchiveAdapter
    private lateinit var rootView: View
    private lateinit var dashboardBinding: ActivityDashboardBinding

    companion object {
        fun newInstance(
            param1: String?,
            param2: String?
        ): FragmentArchive {
            val fragment: FragmentArchive =
                FragmentArchive()
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
        binding = FragmentArchiveBinding.inflate(inflater, container, false)
//        dashboardBinding = ActivityDashboardBinding.inflate(LayoutInflater.from(requireContext()))

        init()

        return (binding.root)
//        val users = ArrayList<ArchiveData.Data>()
//        val allDates: MutableList<String> = ArrayList()
//        //val maxDate = "Jan-2016"
//        val maxDate = SimpleDateFormat("MM-yyyy").format(Calendar.getInstance().time)
//        val monthDate = SimpleDateFormat("MM-yyyy")
//        val monthDate1 = SimpleDateFormat("MM", Locale("ar"))
//        val monthDate2 = SimpleDateFormat("yyyy")
//        val monthDate3 = SimpleDateFormat("MM")
//        val cal: Calendar = Calendar.getInstance()
//        cal.setTime(monthDate.parse(maxDate))
//        for (i in 1..12) {
//            val month_name1: String = monthDate1.format(cal.getTime())
//            val year: String = monthDate2.format(cal.getTime())
//            val month: String = monthDate3.format(cal.getTime())
//            users.add(ArchiveData.Data(month,year))
//
//            allDates.add(month_name1)
//            cal.add(Calendar.MONTH, +1)
//        }
//        val adapter = context?.let { HistoryAdapter(it,users, this) }
//        binding.historyRecyclerView.adapter = adapter
//        return rootVie
        childFragmentManager.addOnBackStackChangedListener {
            val fr = childFragmentManager.findFragmentById(R.id.fm_archive)
            if (fr is FragmentFromArchive){
//                dashboardBinding.titlePage.visibility = View.GONE
            }
        }
    }

    private fun init() {
        try {
            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
                var url: String = CamelConfig.WEBURL + CamelConfig.Year_list
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
                                ArchiveData::class.java
                            )

                            fun getMonthByNumber(monthnum: Int): String {
                                val c = Calendar.getInstance()
                                val month_date = SimpleDateFormat("MMM")
                                c[Calendar.MONTH] = monthnum - 1
                                return month_date.format(c.time)
                            }

                            if (res.status == 1) {
                                getMonthByNumber(res.data[0].month)
                                historyAdapter =
                                    ArchiveAdapter(res.data,
                                        object : ArchiveAdapter.OnItemClickListener {
                                            override fun OnCustomClick(
                                                history: ArchiveData.Data,
                                                position: Int, month: Int,
                                                year: Int
                                            ) {
                                                openFragment(
                                                    FragmentFromArchive.newInstance(
                                                        month, year
                                                    ), "FragmentFromArchive"
                                                )
                                            }


                                        })
                                binding.archiveRecycleView.adapter = historyAdapter
                                historyAdapter.notifyDataSetChanged()
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
//    private fun initRace(archiveList: List<ArchiveData.Data>) {
//        historyAdapter = HistoryAdapter(archiveList, OnCustomClick())
//        binding.historyRecyclerView.adapter = historyAdapter
//    }

    fun openFragment(fragment: Fragment?, name: String) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.framee, fragment!!)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }
//    override fun OnCustomClick(
//        history: ArchiveData.Data,
//        position: Int
//    ) {
//        openFragment(FragmentFromArchive.newInstance(month.toInt(), year.toInt()))
//    }
}
