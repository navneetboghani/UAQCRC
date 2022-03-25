package com.adsum.camel_masterapplication.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.adsum.camel_masterapplication.Config.*
import com.adsum.camel_masterapplication.Model.AddRaceDetailRes
import com.adsum.camel_masterapplication.Model.SpinnerCategory
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.CustomViewBinding
import com.adsum.camel_masterapplication.databinding.FragmentAddRaceDetailBinding
import com.adsum.camel_masterapplication.databinding.PopupRaceAddedBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import org.json.JSONObject


class FragmentAddRaceDetail : Fragment() {
    private lateinit var binding: FragmentAddRaceDetailBinding
    private lateinit var popupRaceAddedBinding: PopupRaceAddedBinding
    private lateinit var customViewBinding: CustomViewBinding
    var myList: ArrayList<String> = arrayListOf()
    private var spinner1: ArrayList<SpinnerCategory.Data> = arrayListOf()
    var race_Id: Int = 0
    var count = 0
    var datamain: List<SpinnerCategory.Data>? = null
    private lateinit var bottomSheetDialog: BottomSheetDialog



    private var spinnerCategoryId: String = ""
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddRaceDetailBinding.inflate(inflater, container, false)
        popupRaceAddedBinding = PopupRaceAddedBinding.inflate(LayoutInflater.from(requireContext()))
        customViewBinding = CustomViewBinding.inflate(LayoutInflater.from(requireContext()))

        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getcategorylist()

        binding.getSpinnerCategory.setOnClickListener {
            bottomSheetDialog.show()
        }

        binding.enterNumberTv.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                    s: CharSequence, start: Int, before: Int,
                    count: Int
            ) {
                if (s.isNotEmpty()) {
                    addNewView()
                } else {
                }
            }

            override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int,
                    after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.AddButton.setOnClickListener {
            getData(count)
        }
    }

    fun getData(position: Int) {
        val categoryId = binding.getSpinnerCategory.text.toString()
        val noOfRound = binding.enterNumberTv.text.toString()
        val racename = binding.raceNameText.text.toString()
        val parentLayout = binding.linearLayout as LinearLayout
        var view: View
        view = parentLayout.getChildAt(position)
        val roundNo: TextView? = view?.findViewById(R.id.roundNo) as TextView?
        val male = view?.findViewById(R.id.maleButton) as RadioButton
        val gender = if (male.isChecked) {
            "Male"
        } else
            "Female"
        val distance: EditText? = view.findViewById(R.id.distance_tv) as EditText?
        val disc: EditText? = view.findViewById(R.id.enter_discription_tv) as EditText?
        val price: EditText? = view.findViewById(R.id.the_price_tv) as EditText?
        val category: EditText? = view.findViewById(R.id.category_tv) as EditText?
//        var count: Int = Integer.valueOf(binding.enterNumberTv.text.toString())
//        if (binding.enterNumberTv.text.isBlank()) {
//            count = 1
//        }
        when {
            categoryId.isEmpty() -> {
                CommonFunctions.showToast(requireContext(), "Select Category")
            }
            noOfRound.isEmpty() -> {
                CommonFunctions.showToast(requireContext(), "Enter Round")
            }
            racename.isEmpty() -> {
                CommonFunctions.showToast(requireContext(), "Enter Race Name")
            }
            distance?.text.toString().isEmpty() -> {
                CommonFunctions.showToast(requireContext(), "Enter Distance")
            }
            disc?.text.toString().isEmpty() -> {
                CommonFunctions.showToast(requireContext(), "Enter Description")
            }
            price?.text.toString().isEmpty() -> {
                CommonFunctions.showToast(requireContext(), "Enter price")
            }
            category?.text.toString().isEmpty() -> {
                CommonFunctions.showToast(requireContext(), "Enter category")
            }
            else -> {
                if (position == 0) {
                    addRaceDetail("", roundNo?.text.toString(), gender.toString(), distance?.text.toString(), disc?.text.toString(), price?.text.toString(), category?.text.toString())
                } else {
                    addRaceDetail(race_Id.toString(), roundNo?.text.toString(), gender.toString(), distance?.text.toString(), disc?.text.toString(), price?.text.toString(), category?.text.toString())
                }
            }
        }
    }

    private fun getcategorylist() {
        try {
            var url: String = CamelConfig.WEBURL + CamelConfig.spinnerCategory
            Log.e("response", url)
            CommonFunctions.createProgressBar(activity, "Please Wait")
            AndroidNetworking.get(url)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .setTag(url)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            Log.e("response", response.toString())
                            CommonFunctions.destroyProgressBar()
                            var gson = Gson()
                            val res = gson.fromJson(
                                    response.toString(),
                                    SpinnerCategory::class.java
                            )
                            if (res.status == 1) {
                                datamain = res.data
//                                Log.e("data", "DatMainSize:- " + datamain?.size.toString())
//                                Log.e("data", datamain.toString())
                                showBottomSheetDialog()
                                CommonFunctions.destroyProgressBar()
                            } else {
                                CommonFunctions.destroyProgressBar()
                                CommonFunctions.showToast(
                                        context, res.response
                                )
                            }
                        }

                        override fun onError(anError: ANError?) {
                            CommonFunctions.showToast(context, "error" + anError)
                        }
                    })

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("SetTextI18n")
    fun addNewView() {
        val parentLayout = binding.linearLayout as LinearLayout
        val layoutInflater = layoutInflater
        var view: View
        var count: Int = Integer.valueOf(binding.enterNumberTv.text.toString())
        if (binding.enterNumberTv.text.isBlank()) {
            count = 1
        }
        parentLayout.removeAllViews()
        for (i in 0..count - 1) {
            view = layoutInflater.inflate(R.layout.custom_view, parentLayout, false)
            val textView = view.findViewById<View>(R.id.enter_number_tv) as? TextView
            textView?.text = "Column $i"
            val roundNo = view.findViewById<View>(R.id.roundNo) as? TextView
            roundNo?.text = "الشوط" + " " + i.inc()
            parentLayout.addView(view, i)
        }
    }

    private fun showBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(com.adsum.camel_masterapplication.R.layout.spinner_dialog)
        val done = bottomSheetDialog.findViewById<View>(R.id.done_text)
        val spinner: Spinner = bottomSheetDialog.findViewById<View>(R.id.spinner_list) as Spinner
        if (datamain != null) {
            Log.e("data", "datamain:--- " + datamain?.size)

            for (i in 0..datamain?.size!! - 1) {
                myList.add(datamain?.get(i)?.category_name!!)
//                myList.add(datamain?.get(i)?.category_id!!)
            }

            Log.e("data", "datamain" + datamain?.size)
            val adapter1 = datamain?.let {
                ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        myList
                )
            }
            Log.e("adapter", adapter1.toString())
            done?.setOnClickListener {
                spinnerCategoryId = datamain?.get(spinner.selectedItemPosition)!!.category_id
                Log.e("categoryID", "ID-----"+spinnerCategoryId.toString())
                val text: String = spinner.getSelectedItem().toString()
                binding.getSpinnerCategory.text = text
                bottomSheetDialog.dismiss()
            }
            spinner.adapter = adapter1
            adapter1?.notifyDataSetChanged()
        }
    }

    private fun addRaceDetail(raceId1: String, roundNo: String, gender: String, distance: String, disc: String, price: String, category: String) {
        var url: String = CamelConfig.WEBURL + CamelConfig.addRaceDetail
        Log.e("url", url.toString())
        val mParams: HashMap<Any, Any> = HashMap()
        mParams.put(
                "race_category",spinnerCategoryId)
        mParams.put(
                "race_name", binding.raceNameText.text.toString().trim())
        mParams.put(
                "no_of_round", binding.enterNumberTv.text.toString().trim())
        mParams.put(
                "race_id",raceId1)
        mParams.put(
                "round_name", roundNo)
        mParams.put(
                "type", gender)
        mParams.put(
                "description", disc)
        mParams.put(
                "distance", distance)
        mParams.put(
                "price", price)
        mParams.put(
                "category", category)
        Log.e("parem", mParams.toString())
        CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))
        AndroidNetworking.post(url)
                .addBodyParameter(mParams)
                .addHeaders(Constants.Authorization, Constants.Authkey)
                .setTag(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        Log.e("san", "responce1:--" + response)
                        //Destroy Progressbar
                        CommonFunctions.destroyProgressBar()
                        val gson = Gson()
                        val res = gson.fromJson(response.toString(), AddRaceDetailRes::class.java)

                        if (res.status == 1) {
                            race_Id = res.data.race_id
                            if (count < binding.linearLayout.childCount - 1) {
                                count++
                                getData(count)
                            } else {
                                val builder: AlertDialog.Builder? = context?.let {
                                    AlertDialog.Builder(it)
                                }
                                val view = builder?.setView(popupRaceAddedBinding.root)
                                val dialog = builder?.create()
                                dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                dialog.show()
                                popupRaceAddedBinding.doneTV.setOnClickListener {
                                    dialog.dismiss()
                                    CommonFunctions.showToast(requireContext(), "Successfully added")
                                    var fragment: Fragment
                                    fragment= RaceDetailFragment()
                                    val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
                                    ft.replace(R.id.frameLayout, fragment)
                                    ft.commit()
                                }
                            }
                        } else {

                            CommonFunctions.showToast(activity, "states 0")
                        }
                    }

                    override fun onError(anError: ANError?) {
                        CommonFunctions.destroyProgressBar()
                        CommonFunctions.showToast(context, anError.toString())
                        Log.e("error", "anError" + anError.toString())
                    }
                })
    }
}