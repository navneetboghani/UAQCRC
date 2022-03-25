package com.adsum.camel_masterapplication.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.Adapter.IGNORE_ITEM_VIEW_TYPE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.AddUserResponse
import com.adsum.camel_masterapplication.Model.ResponseProfile
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.FragmentAddUserBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.internal.bind.ArrayTypeAdapter
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap

class AddUserFragment : Fragment() {

    private lateinit var binding: FragmentAddUserBinding
    private lateinit var rootView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddUserBinding.inflate(inflater, container, false)
        rootView = binding.root

        init()
        setupSpinner()

        return rootView

    }

    companion object {

        fun newInstance(param1: String, param2: String):
                AddUserFragment {
            val fragment: AddUserFragment =
                AddUserFragment()
            val args = Bundle()
            fragment.setArguments(args)
            return fragment
        }
    }

    private fun init() {
        binding.btnAdd.setOnClickListener {
            when{

                binding.etEmail.getText().toString().trim()  { it <= ' ' }.isEmpty() -> {
                    Toast.makeText(getActivity(), getString(R.string.error_email), Toast.LENGTH_SHORT)
                    .show()

                }
                binding.etMobile.getText().toString().trim() { it <= ' ' }.isEmpty() ->{
               Toast.makeText(getActivity(), getString(R.string.error_mobile1), Toast.LENGTH_SHORT)
                    .show()
                }
                else -> {
                    if (CommonFunctions.validateEmailAddress(
                            binding.etEmail.text.toString().trim()
                        )
                    ) {
                       addUser()

                    } else {
                        Toast.makeText(getActivity(), getString(R.string.error_mobile), Toast.LENGTH_SHORT)
                    .show()
                    }
                }
            }
        }
    }

    private fun addUser() {
        try {
            var role=if (binding.switch1.isChecked){
                "1"
            }else{
                "0"
            }
//            Log.e("tag","role:-"+role)

            var Role = binding.roleSpinner.selectedItem.toString().trim { it <= ' ' }
            var R = " "
            when {
                Role == "المسؤول" -> {
                    R = "administrator"
                }
                Role == "المذيع" -> {
                    R = "normal_user"
                }
                Role == "المشترك" -> {
                    R = "subscriber"
                }
                else -> { }
            }
            Log.e("tag","R:-"+R)
            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
                var url: String = CamelConfig.WEBURL + CamelConfig.addUser
                val mParams: HashMap<Any, Any> = HashMap()
                mParams.put(
                    Constants.username,
                    binding.etUsername.text.toString().trim { it <= ' ' })
                mParams.put(
                    Constants.password,
                    binding.etPassword.text.toString().trim { it <= ' ' })
                mParams.put(
                    Constants.email,
                    binding.etEmail.text.toString().trim { it <= ' ' })
                mParams.put(
                    Constants.name_of_participant,
                    binding.etParticipantName.text.toString().trim { it <= ' ' })
                mParams.put(
                    Constants.mobileno,
                    binding.etMobile.text.toString().trim { it <= ' ' })
                mParams.put(
                    Constants.camelno,
                    binding.etCamelNo.text.toString().trim { it <= ' ' })
                mParams.put(
                    Constants.role,
                    R)
                mParams.put(
                    Constants.subscription,
                    role)


                CommonFunctions.createProgressBar(activity, getString(com.adsum.camel_masterapplication.R.string.please_wait))

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()

                AndroidNetworking.post(url)
                    .addBodyParameter(mParams)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .setTag(url)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            CommonFunctions.destroyProgressBar()
                            var gson = Gson()
                            val res = gson.fromJson(
                                response.toString(),
                                AddUserResponse::class.java
                            )
                            if (res.status == 1) {
                                val builder = activity?.let { AlertDialog.Builder(it) }

                                builder?.setTitle("Successfull")
                                builder?.setMessage("Insert Successfully")
                                builder?.setPositiveButton("Ok") { dialog, which -> // Do nothing but close the dialog
                                }
                                val alert = builder?.create()
                                alert?.show()
                               // CommonFunctions.showToast(activity, res.response)

                            } else {
                                CommonFunctions.showToast(activity, res.response)
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

    private fun setupSpinner() {
        val role = arrayOf("المسؤول","المشترك","المذيع")
        val spinner = binding.roleSpinner
        val arrayAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, role)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
//                  Toast.makeText(
//                    this@MainActivity,
//                    getString(R.string.selected_item) + " " + role[position],
//                    Toast.LENGTH_SHORT
//                ).show()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }
}