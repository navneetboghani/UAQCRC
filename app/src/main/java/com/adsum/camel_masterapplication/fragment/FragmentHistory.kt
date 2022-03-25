package com.adsum.camel_masterapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.FragmentHistoryBinding
import com.adsum.camel_masterapplication.Adapter.HistoryAdapter
import com.adsum.camel_masterapplication.Model.HistoryModel

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FragmentHistory: Fragment(), HistoryAdapter.OnItemClickListener{

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var rootView: View

    companion object{
        fun newInstance(
            param1: String?,
            param2: String?
        ): FragmentHistory {
            val fragment: FragmentHistory=
                FragmentHistory()
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
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        rootView = binding.root
        val users = ArrayList<HistoryModel>()
        val allDates: MutableList<String> = ArrayList()
        //val maxDate = "Jan-2016"
        val maxDate = SimpleDateFormat("MMM-yyyy").format(Calendar.getInstance().time)
        val monthDate = SimpleDateFormat("MMM-yyyy")
        val monthDate1 = SimpleDateFormat("MMM", Locale("ar"))
        val monthDate2 = SimpleDateFormat("yyyy")
        val monthDate3 = SimpleDateFormat("MM")
        val cal: Calendar = Calendar.getInstance()
        cal.setTime(monthDate.parse(maxDate))
        for (i in 1..12) {
            val month_name1: String = monthDate1.format(cal.getTime())
            val year: String = monthDate2.format(cal.getTime())
            val month: String = monthDate3.format(cal.getTime())
            users.add(HistoryModel(month, month_name1, year))

            allDates.add(month_name1)
            cal.add(Calendar.MONTH, -1)
        }
        val adapter = context?.let { HistoryAdapter(it, users, this) }
        binding.historyRecyclerView.adapter = adapter
        return rootView
    }
    override fun OnCustomClick(history: HistoryModel, position: Int, month: String, year: String) {
        openFragment(FragmentRaceFromHistory.newInstance(month.toInt(), year.toInt()))
    }

    fun openFragment(fragment: Fragment?) {
        val transaction = childFragmentManager.beginTransaction()
        transaction?.replace(R.id.fm_history, fragment!!)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }
}
