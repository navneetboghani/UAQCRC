package com.adsum.camel_masterapplication.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adsum.camel_masterapplication.Adapter.CustomAdapter
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.RaceResponse
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.FragmentRaceBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONArray

class FragmentRaceFromHistory : Fragment(), CustomAdapter.OnItemClickListener {


    private lateinit var customAdapter: CustomAdapter
    private lateinit var binding: FragmentRaceBinding
    private lateinit var rootView: View
    private var year: Int = 0
    private var month: Int = 0
    private var user_id: Int = 0
    private var raceid = 0
    companion object{


    fun newInstance(
        month: Int, year: Int
    ): FragmentRaceFromHistory {
        val fragment: FragmentRaceFromHistory =
            FragmentRaceFromHistory()
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
        binding = FragmentRaceBinding.inflate(inflater, container, false)
        rootView = binding.root
        month = requireArguments().getInt(Constants.month)
        year = requireArguments().getInt(Constants.year)
        user_id = CommonFunctions.getPreference(context, Constants.ID, 0)

        initfromHistory()

        return rootView
    }

    private fun initfromHistory(){
        try {

            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
                raceid = CommonFunctions.getPreference(activity, Constants.ID, 0)
                var url: String = CamelConfig.WEBURL + CamelConfig.Year_list

//Progress start
                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()

                AndroidNetworking.get(url)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .addPathParameter(Constants.year, year.toString())
                    .addPathParameter(Constants.month, month.toString())
                    .addPathParameter(Constants.user_id , user_id.toString())
//                    .setOkHttpClient(okHttpClient)
                    .setTag(url)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(object: JSONArrayRequestListener {
                        override fun onResponse(response: JSONArray?) {
                            //Destroy Progressbar
                            CommonFunctions.destroyProgressBar()
                            var gson = Gson()
                            val res = gson.fromJson(
                                response.toString(),
                                RaceResponse::class.java
                            )
                            context?.let { initRace(it, res)
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

    private fun initRace(context: Context, userList: List<RaceResponse.RaceResponseItem>){

        customAdapter = CustomAdapter(context, userList, this)
        binding.recyclerView.adapter = customAdapter
        if (customAdapter.itemCount == 0){
            CommonFunctions.showToast(context, getString(R.string.data_not_found))
        }

    }

    fun openFragment(fragment: Fragment?) {
        val transaction = childFragmentManager.beginTransaction()
        transaction?.replace(R.id.fm_categories, fragment!!)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }

    override fun OnCustomClick(camellist: RaceResponse.RaceResponseItem.Round, position: Int,racename: String) {
        //openFragment(FragmentSubcategoryRace.newInstance(camellist.id.toInt(),camellist.raceId.toInt(),camellist.type,camellist.roundName,camellist.raceId,racename,Constants.isFromHistory, position))
        CommonFunctions.setPreference(context, Constants.isFromHistory, true)
    }
}