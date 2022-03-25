package com.adsum.camel_masterapplication.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adsum.camel_masterapplication.Adapter.CategoryNameAdapter
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.CategoryNameResponse
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.FragmentCategoryBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONObject


class FragmentCategory : Fragment() {
    private lateinit var bindingcategory: FragmentCategoryBinding
    private lateinit var rootView: View
    private lateinit var adapter: CategoryNameAdapter

    companion object {
        fun newInstance(
            param1: String?,
            param2: String?
        ): FragmentCategory {
            val fragment: FragmentCategory =
                FragmentCategory()
            val args = Bundle()
            fragment.setArguments(args)
            return fragment
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    fun openFragment(fragment:Fragment?, name: String){
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.framee,fragment!!)
        transaction?.addToBackStack(name)
        transaction?.commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingcategory = FragmentCategoryBinding.inflate(inflater, container, false)
        rootView = bindingcategory.root
        bindingcategory.rvCategoryName.setHasFixedSize(true)
        getcategorylist()
        return rootView
    }

    var datamain: List<CategoryNameResponse.Data>? = null
    private fun getcategorylist() {
                var url: String = CamelConfig.WEBURL + CamelConfig.getcamelcategory
                Log.e("response", url)

                CommonFunctions.createProgressBar(activity, "Please Wait")

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()
                AndroidNetworking.get(url)
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
                                CategoryNameResponse::class.java
                            )

                            if (res.status == 1) {
                                datamain = res.data

                                adapter =
                                    CategoryNameAdapter(context,
                                        datamain!!,
                                        object : CategoryNameAdapter.OnCategoryClickListner {
                                            override fun onCategoryClick(
                                                categoryName: CategoryNameResponse.Data,
                                                position: Int,

                                            ) {
                                                //openFragment(FragmentcategoryDetail.newInstance(categoryName.id,position),"categorydetails")
                                                openFragment(
                                                    FragmentcategoryDetail.newInstance(
                                                        categoryName.categoryName,
                                                        categoryName.id,
                                                        position), "categorydetails"
                                                )


//                                        val bundle = Bundle()
//                                        bundle.putString("data", datamain!![position].id)
//                                        val fragment = FragmentcategoryDetail()
//                                        fragment.arguments = bundle
//                                        fragmentManager?.beginTransaction()?.replace(R.id.fragmentcategory,fragment)?.commit()

//                                    val intent = Intent(context,FragmentcategoryDetail ::class.java)
//                                       // intent.putExtra("CategoryName", datamain!![position].id)
//                                        //intent.putExtra("categoryid", res.status)
//                                        startActivity(intent)
                                                //CommonFunctions.showToast(context,"you clicked on: .$categoryName")
                                            }
                                        })
                                bindingcategory.rvCategoryName.adapter = adapter

                                bindingcategory.rvCategoryName.visibility = View.VISIBLE
                                CommonFunctions.destroyProgressBar()

                            } else {
                                CommonFunctions.destroyProgressBar()
                                CommonFunctions.showToast(
                                    context, res.msg
                                )
                            }
                        }


                        override fun onError(anError: ANError?) {
                            CommonFunctions.showToast(context, "error" + anError)
                        }

                    })
            }




}
