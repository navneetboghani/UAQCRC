package com.adsum.camel_masterapplication.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.AddAdminRaceScheduleRes
import com.adsum.camel_masterapplication.Model.updateAdminRaceSchedul
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.ActivityAdminDashboardBinding
import com.adsum.camel_masterapplication.databinding.FragmentAdminImageAddBinding
import com.adsum.camel_masterapplication.databinding.PopupRaceSchedulAddBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.FieldPosition

class FragmentImageUpdate : Fragment() {
    private lateinit var fragmentAdminImageAddBinding: FragmentAdminImageAddBinding
    private lateinit var pupupRaceSchedulAddBinding: PopupRaceSchedulAddBinding
    private lateinit var adminDashboardBinding: ActivityAdminDashboardBinding
    val REQUEST_CODE = 1000
    private lateinit var filepath: String
    var race_id = 0
    var image = ""

    companion object {
        fun newInstance(
                raceId: Int,
                image: String,
                position: Int
        ): FragmentImageUpdate {
            val fragment: FragmentImageUpdate =
                    FragmentImageUpdate()
            val args = Bundle()
            args.putInt("rs_id",raceId)
            args.putString("image",image)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        fragmentAdminImageAddBinding =
                FragmentAdminImageAddBinding.inflate(inflater, container, false)
        pupupRaceSchedulAddBinding = PopupRaceSchedulAddBinding.inflate(inflater,container,false)
        adminDashboardBinding = ActivityAdminDashboardBinding.inflate(inflater,container,false)

        race_id = requireArguments().getInt("rs_id")
        image = requireArguments().getString("image").toString()
//        val bitmap = BitmapFactory.decodeFile(File(image).toString())
//        fragmentAdminImageAddBinding.selectImageFromGellary.setImageBitmap(bitmap)
        Glide.with(this).asBitmap().load(image).into(fragmentAdminImageAddBinding.selectImageFromGellary)


        setupPermissions()

        return (fragmentAdminImageAddBinding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentAdminImageAddBinding.floatingActionButton.setOnClickListener {
            openGalleryForImage()
        }
        fragmentAdminImageAddBinding.addScheduleTv.setOnClickListener {
            updateImage()
        }
//        val imgPath = requireArguments().getString("Image")
//        val bitmap = BitmapFactory.decodeFile(File(imgPath).toString())
//        fragmentAdminImageAddBinding.selectImageFromGellary.setImageBitmap(bitmap)
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

    private fun openGalleryForImage() {
        if (setupPermissions()) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        } else {
            makeRequest()
        }
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
        fragmentAdminImageAddBinding.selectImageFromGellary.setImageBitmap(bm)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            onSelectFromGalleryResult(data)
            val selectedimage = data?.data
            filepath = selectedimage?.let { getPath(it) }.toString()
            Log.e("File" , "Path" + filepath)
        }
    }


    private fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        var cursor = activity?.managedQuery(uri, projection, null, null, null)

        var column_index = cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        cursor?.moveToFirst()
        var imagePath = column_index?.let { cursor?.getString(it) }

        return imagePath

    }
    private fun saveToInternalStorage(bitmapImage: Bitmap): String? {
        val cw = ContextWrapper(activity)
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val mypath = File(directory, "profile.jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
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
        return directory.absolutePath
    }

    fun updateImage() {
        try {
            if (activity?.let { CommonFunctions.checkConnection(it) } == true) {
                var url: String = CamelConfig.WEBURL + CamelConfig.updateadminRaceSchedule
                Log.e("url", "image---" + url)
                CommonFunctions.createProgressBar(activity, getString(R.string.please_wait))
                val file = File(filepath)
                val mParams: HashMap<Any, Any> = HashMap()
                mParams.put("rs_id", race_id.toString())
//                mParams.put("image",image)
                val okHttpClient = OkHttpClient.Builder()
//                    .addInterceptor(ChuckerInterceptor(requireActivity()))
                        .build()

                AndroidNetworking.upload(url)
                        .addMultipartFile("image", file)
                        .addMultipartParameter(mParams)
                        .addHeaders("Authorization", ")H@MbQeThWmZq4t7")
                        .setTag(url)
//                        //
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(object : JSONObjectRequestListener {
                            override fun onResponse(response: JSONObject?) {
                                Log.e("response", response.toString())
                                val gson = Gson()
                                val res = gson.fromJson(
                                        response.toString(),
                                        updateAdminRaceSchedul::class.java
                                )
                                if (res.status == 1) {
                                    CommonFunctions.destroyProgressBar()
                                    CommonFunctions.showToast(context, "update successfully")
                                } else {
                                    CommonFunctions.showToast(context, res.response)
                                }
                            }

                            override fun onError(anError: ANError?) {
                                Log.e("error" , "AnError---" + anError.toString())
                                CommonFunctions.destroyProgressBar()
                            }
                        })
            }
        } catch (e: Exception) {
            Log.e("Response", "e"+e.toString())
        }
    }
}