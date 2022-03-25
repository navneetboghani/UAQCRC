package com.adsum.camel_masterapplication.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.ContextWrapper
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.ResponseImageUpload
import com.adsum.camel_masterapplication.Model.ResponseProfile
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.FragmentProfileBinding

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.io.*
import kotlin.properties.Delegates


class FragmentProfile : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var rootView: View
    private lateinit var filepath: String
    private lateinit var cpw: String
    private lateinit var pw: String
    private var user_id by Delegates.notNull<String>()
    companion object{
        fun newInstance(
            param1: String?,
            param2: String?
        ): FragmentProfile {
            val fragment: FragmentProfile =
                FragmentProfile()
            val args = Bundle()
            fragment.setArguments(args)
            return fragment
        }
    }
    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        rootView = binding.root
        user_id = CommonFunctions.getPreference(activity, Constants.ID, "").toString()
        setupPermissions()
        init()

        return rootView
    }
    private fun onSelectFromGalleryResult(data: Intent?) {

        var bm: Bitmap? = null
        try {
            bm = MediaStore.Images.Media.getBitmap(
                activity?.contentResolver,
                data?.data
            )

        } catch (e: IOException) {
            e.printStackTrace()
        }

        binding.profileImageView.setImageBitmap(bm)

    }

    private fun setupPermissions(): Boolean {
        val permission = ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        var flag = false
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        } else {
            flag = true
        }

        return flag
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )
    }


    private fun init() {
        try {
            getprofile()
            binding.profileImageView.setOnClickListener(View.OnClickListener {
                updateimage()

            })
            binding.btnUpdate.setOnClickListener(View.OnClickListener {
                update();
            })
//            binding.etCpassword.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(
//                    charSequence: CharSequence,
//                    i: Int,
//                    i1: Int,
//                    i2: Int
//                ) {
//                }
//
//                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
//                override fun afterTextChanged(editable: Editable) {
//                    val passwrd: String = binding.etPassword.getText().toString()
//                    if (editable.length > 0 && passwrd.length > 0) {
//                        if (!binding.etCpassword.equals(passwrd)) {
//                            Toast.makeText(getActivity(), getString(R.string.error_cpassword), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//            })

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun updateimage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Add Photo!")
        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item] == "Take Photo") {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePictureIntent, 2)
            } else if (options[item] == "Choose from Gallery") {
                if (setupPermissions()) {
                    var intent = Intent(Intent.ACTION_PICK)
                    intent.setType("image/*")
                    startActivityForResult(intent, 1)
                } else {
                    makeRequest()
                }
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        })
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            if (requestCode == 1) {
                if (resultCode == Activity.RESULT_OK) {
                    onSelectFromGalleryResult(data);
                    val selectedimage = data?.data
                    filepath = selectedimage?.let { getPath(it) }.toString()
                    imageupload()
                }
            }else if (requestCode == 2){
                    if (resultCode == RESULT_OK) {
                        val imageBitmap = data?.extras?.get("data") as Bitmap
                        binding.profileImageView.setImageBitmap(imageBitmap)
                        var path = saveToInternalStorage(imageBitmap).toString()  + "/profile.jpg"
                        filepath = path
                        imageupload()
                    }
            }
        }

    }

    private fun saveToInternalStorage(bitmapImage: Bitmap): String? {
        val cw = ContextWrapper(activity)
// path to /data/data/yourapp/app_data/imageDir
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
// Create imageDir
        val mypath = File(directory, "profile.jpg")
//        imgFile = mypath
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
// Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
// CommonFunctions.showToast(this, directory.absolutePath)
//        path = directory.absolutePath
        return directory.absolutePath
    }

    private fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        var cursor = activity?.managedQuery(uri, projection, null, null, null)

        var column_index = cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        if (cursor != null) {
            cursor.moveToFirst()
        }
        var imagePath = column_index?.let { cursor?.getString(it) }

        return imagePath
    }



    private fun imageupload() {
        try {
            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {

            var url: String = CamelConfig.WEBURL + CamelConfig.profileimage
            CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))
            val file=File(filepath)

            val mParams: HashMap<Any, Any> = HashMap()
            mParams.put(Constants.user_id, user_id)

            AndroidNetworking.upload(url)
                .addMultipartFile(Constants.profile_image, file)
                .addMultipartParameter(mParams)
                .addHeaders("Authorization", ")H@MbQeThWmZq4t7")
                .setTag(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        Log.e("response", response.toString())
                        val gson = Gson()
                        val (data, msg, status) = gson.fromJson(
                            response.toString(),
                            ResponseImageUpload::class.java
                        )
                        if (status.toString() == "1") {
                            CommonFunctions.destroyProgressBar()
                            CommonFunctions.showToast(context,"update successfully")
                          //  CommonFunctions.showToast(context, msg.toString())
// CommonFunctions.showToast(context, message)
                        } else {
                            CommonFunctions.showToast(context, msg.toString())
                        }
                    }

                    override fun onError(anError: ANError?) {
                        CommonFunctions.destroyProgressBar()
// CommonFunctions.showToast(context, anError.toString())
                    }
                })
        }
        } catch (e: Exception) {
            Log.e("Response", "e")
        }
    }

    private fun getprofile() {
        try {

            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {

                var url: String = CamelConfig.WEBURL + CamelConfig.profile+user_id


                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))

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
                                ResponseProfile::class.java
                            )
                            CommonFunctions.setPreference(
                                context,
                                Constants.camelno,
                                res.data.camelno
                            )
                            setData(res.data)
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


    private fun update(){
        try {
             if (binding.etSubscribername.getText().toString().trim().length == 0) {
                Toast.makeText(
                    getActivity(),
                    getString(R.string.error_subscriber),
                    Toast.LENGTH_SHORT
                ).show();
            } else if (binding.etEmail.getText().toString().trim().length == 0) {
                Toast.makeText(getActivity(), getString(R.string.error_email), Toast.LENGTH_SHORT).show();
            } else if (binding.etMobile.getText().toString().trim().length < 10) {
                 Toast.makeText(getActivity(), getString(R.string.error_mobile1), Toast.LENGTH_SHORT).show();
             } else if (binding.etMobile.getText().toString().trim().length == 0) {
                Toast.makeText(getActivity(), getString(R.string.error_mobile), Toast.LENGTH_SHORT).show();
            } else if(!binding.etPassword.getText().toString().equals( binding.etCpassword.getText().toString())){
                 Toast.makeText(getActivity(), getString(R.string.error_cpassword), Toast.LENGTH_SHORT).show();
            }
            else{
                updateprofile()

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateprofile() {
        try {

            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {

                var url: String = CamelConfig.WEBURL + CamelConfig.profileupdate


                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                    .build()

                AndroidNetworking.post(url)
                    .addHeaders(Constants.Authorization, Constants.Authkey)
                    .addBodyParameter(Constants.user_id, user_id.toString())
                    .addBodyParameter(
                        Constants.name_of_participant,
                        binding.etSubscribername.text.toString()
                    )
                    .addBodyParameter(Constants.email, binding.etEmail.text.toString())
                    .addBodyParameter(Constants.mobile_no, binding.etMobile.text.toString())
                    .addBodyParameter(Constants.user_pass, binding.etPassword.text.toString())
                    .setTag(url)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject?) {
                            //Destroy Progressbar
                            CommonFunctions.destroyProgressBar()
                            var gson = Gson()
                            val res = gson.fromJson(
                                response.toString(),
                                ResponseProfile::class.java
                            )
                            CommonFunctions.showToast(activity, "update successfully")
                            getprofile()
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

    private fun setData(data: ResponseProfile.Data) {
        try {
            binding.etUsername.setText(data.userName)
            binding.etSubscribername.setText(data.nameOfParticipant)
            binding.etCamelNo.setText(data.camelno)
            binding.etEmail.setText(data.email)
            binding.etMobile.setText(data.mobileNo)
            val url: String = data.image
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_account_circle_24))
            Glide.with(requireActivity())
                .load(url)
                .apply(requestOptions)
                .into(binding.profileImageView)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun openFragment(fragment: Fragment?) {
        val transaction = childFragmentManager.beginTransaction()
        transaction?.replace(R.id.fm_categories, fragment!!)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }
}