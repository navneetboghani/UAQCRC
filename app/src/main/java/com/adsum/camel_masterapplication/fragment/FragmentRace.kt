package com.adsum.camel_masterapplication.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.FragmentRaceBinding
import com.adsum.camel_masterapplication.Adapter.CustomAdapter
import com.adsum.camel_masterapplication.Adapter.SubCustomAdapter
import com.adsum.camel_masterapplication.Model.LoginResponse
import com.adsum.camel_masterapplication.Model.RaceResponse
import com.adsum.camel_masterapplication.Config.Constants

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONArray

class FragmentRace : Fragment(), CustomAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var subCustomAdapter: SubCustomAdapter
    private lateinit var customAdapter: CustomAdapter
    private lateinit var loginResponse: LoginResponse
    private lateinit var binding: FragmentRaceBinding
    private lateinit var rootView: View
    private var raceid = 0
    private var racename = ""
    companion object{
        fun newInstance(
        ): FragmentRace {
            val fragment: FragmentRace =
                FragmentRace()
            val args = Bundle()
            fragment.setArguments(args)
            return fragment
        }


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments!=null){}
    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRaceBinding.inflate(inflater, container, false)
        rootView = binding.root

        init()

        return rootView
    }



    private fun init() {
        try {

            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
                raceid = CommonFunctions.getPreference(activity, Constants.ID, 0)
                var url: String = CamelConfig.WEBURL + CamelConfig.racelist+raceid

//Progress start
                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()

                AndroidNetworking.get(url)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
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

    }

    fun openFragment(fragment: Fragment?) {
        val transaction = childFragmentManager.beginTransaction()
        transaction?.replace(R.id.fm_categories, fragment!!)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }

    override fun OnCustomClick(
        camellist: RaceResponse.RaceResponseItem.Round,
        position: Int,
        racename: String
    ) {
       // openFragment(FragmentSubcategoryRace.newInstance(camellist.id.toInt(),camellist.raceId.toInt(),camellist.type,camellist.roundName,camellist.customization,racename,Constants.isfromrace, position))
        CommonFunctions.setPreference(context, Constants.isFromHistory, true)
    }
}