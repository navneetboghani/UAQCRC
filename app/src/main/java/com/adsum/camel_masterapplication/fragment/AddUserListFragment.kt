package com.adsum.camel_masterapplication.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.adsum.camel_masterapplication.Adapter.AddUserListAdapter
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.AddUserListResponse
import com.adsum.camel_masterapplication.Model.ListOfUserResponse
import com.adsum.camel_masterapplication.Model.SelectedUserResponse
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.FragmentAddUserlistBinding


import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONObject
import com.adsum.camel_masterapplication.databinding.ItemAddUsersBinding
import com.androidnetworking.interfaces.JSONArrayRequestListener
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList


class AddUserListFragment : Fragment(),AddUserListAdapter.OnCheckedChangeListener{

    private lateinit var addUserListAdapter: AddUserListAdapter
    private  var itemAddUsersBinding: ItemAddUsersBinding? =null
    private var addUsersBinding: FragmentAddUserlistBinding?=null
    private lateinit var rootView: View
    lateinit var button : Button
   // var data = addUserListAdapter.selectUser()
   // var  position : Int=0;
    private lateinit var raceid : String



    companion object {
        var resmain=AddUserListResponse()

        fun newInstance(raceId: String, position: Int): AddUserListFragment {
            val fragment: AddUserListFragment =
                AddUserListFragment()
            val args = Bundle()
            fragment.setArguments(args)
            args.putString(Constants.race_id,raceId)
            //args.putInt(Constants.position, position)
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addUsersBinding = FragmentAddUserlistBinding.inflate(inflater, container, false)
        rootView = addUsersBinding!!.root

        raceid = requireArguments().getString(Constants.race_id).toString()
       // position=requireArguments().getInt(position)

        // getUser()
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getUser()

        addUsersBinding?.tvSelectAll?.setTag(1)
        addUsersBinding?.tvSelectAll?.setOnClickListener {
            addUserListAdapter.selectUserList(addUsersBinding?.tvSelectAll?.getTag() as Int?)
            if(addUsersBinding?.tvSelectAll?.getTag()==1){
                addUsersBinding?.tvSelectAll?.setTag(0)
                addUsersBinding?.tvSelectAll?.setText(getString(R.string.deselect))
            }
            else
            {
                addUsersBinding?.tvSelectAll?.setText(getString(R.string.select))
                addUsersBinding?.tvSelectAll?.setTag(1)
            }
        }

        addUsersBinding?.tvAdd?.setOnClickListener {
           // addUserListAdapter.selectUser()
            SelectedUsers()




        }
    }
    private fun SelectedUsers() {
        try {

            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
                var url:String = CamelConfig.WEBURL + CamelConfig.selectedUserList+raceid


                Log.e("san", "url:--" + url)
//Progress start
               // CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()

                AndroidNetworking.post(url)
                    .addBodyParameter("aselect_user", addUserListAdapter.selectUser().toString())
                    .addBodyParameter("race_id", raceid.toString())
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .setTag(url)
                    //.setOkHttpClient(okHttpClient)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {

                            Log.e("san", "response:--" + response)
                            //Destroy Progressbar
                            CommonFunctions.destroyProgressBar()
//                            var gson = Gson()
//
//                            val res =  gson.fromJson(
//                                response.toString(),
//                                SelectedUserResponse::class.java
//                            )
                            if (response?.getInt(Constants.status)==1) {
                               // CommonFunctions.showToast(activity, response.getString(Constants.response))
                                getUser()

                               // raceDetailAdapter.deleterace(position)
                            } else {
                                CommonFunctions.showToast(activity,response?.getString(Constants.response) )
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

    private fun getUser(){
        try {

            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
                val url: String = CamelConfig.WEBURL + CamelConfig.listofuser
                Log.e("san", "userlist:----" + url)


//Progress start
                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()

                AndroidNetworking.post(url)
                    .addBodyParameter("raceid", raceid.toString())
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .setTag(url)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onResponse(response: JSONObject?) {
                           // Log.e("san", "response:---" + response)
                            CommonFunctions.destroyProgressBar()
                            var gson = Gson()

                            val res = gson.fromJson(
                                response.toString(),
                                ListOfUserResponse::class.java
                            )
                            if (res.status== 1) {

                                context?.let {
                                    inituser(it, res.data)
                                }

                                addUserListAdapter.notifyDataSetChanged()
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

    private fun inituser(context: Context, userList: ArrayList<ListOfUserResponse.Data>){

        addUserListAdapter = AddUserListAdapter(context, userList,this)
        addUsersBinding?.userRecyclerview?.adapter = addUserListAdapter

    }

    override fun OnCheckedChangeListener(userList: ListOfUserResponse.Data, position: Int) {
        //addUserListAdapter.selectUser()

    }

//    override fun getSelectedOrderList(list: ArrayList<String>) {
//        addUserListAdapter.selectUser()
//    }


}