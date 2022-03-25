package com.adsum.camel_masterapplication.Adapter


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Config.CommonFunctions
import com.adsum.camel_masterapplication.Config.Constants
import com.adsum.camel_masterapplication.Model.CampleResp
import com.adsum.camel_masterapplication.Model.MaleResponse
import com.adsum.camel_masterapplication.R

import com.adsum.camel_masterapplication.databinding.ItemFemaleBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlin.properties.Delegates

class FemaleAdapter(var ctx: Context, val femalelist: ArrayList<CampleResp.Data>, val strokClickListener: FemaleAdapter.OnfedeleteClickListener,val subscription : String,val sub :String) : RecyclerView.Adapter<FemaleAdapter.ViewHolder>() {
    private lateinit var itemFemaleBinding: ItemFemaleBinding
    var camelno by Delegates.notNull<String>()
    //this method is returning the view for each item in the list


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FemaleAdapter.ViewHolder {
        itemFemaleBinding = ItemFemaleBinding.inflate(LayoutInflater.from(ctx), parent, false)
        camelno=CommonFunctions.getPreference(ctx,Constants.camelno,"").toString()
        //Log.e("tag","camelno:-"+camelno)
        return ViewHolder(itemFemaleBinding)
    }
    //this method is binding the data on the list
    override fun onBindViewHolder(holder: FemaleAdapter.ViewHolder, position: Int) {
        try{
            val female = femalelist[position]

            loadBannerPhoto(itemFemaleBinding.ivDelete, "")
            itemFemaleBinding.tvFesubname.text = female.rcCamel
            itemFemaleBinding.tvFetypeCamel.text = camelno
            if (sub == "0"){
                itemFemaleBinding.ivDelete.visibility= View.GONE
            }else{
                itemFemaleBinding.ivDelete.visibility= View.VISIBLE
            }
            if (subscription == "0"){
                itemFemaleBinding.ivDelete.visibility= View.GONE
            }else{
                itemFemaleBinding.ivDelete.visibility= View.VISIBLE
            }
            strokClickListener.let { holder.bind(female, position, it) }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
    private fun loadBannerPhoto(imageView: ImageView, image: String?) {
        Glide.with(ctx)
            .load(image)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_delete1)
            )
            .into(imageView)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return femalelist.size
    }

    inner class ViewHolder internal constructor(binding: ItemFemaleBinding): RecyclerView.ViewHolder(
        binding.root
    ){
        fun bind(
            femalelist: CampleResp.Data,
            position: Int,
            clickListener: OnfedeleteClickListener
        ) {

            itemFemaleBinding.ivDelete.setOnClickListener {
                clickListener.OndeleteClick(femalelist, position)
            }
        }


    }
    @SuppressLint("NotifyDataSetChanged")
    fun DeleteFemaleCamel(position: Int){
        femalelist.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position,femalelist.size)
    }
    interface OnfedeleteClickListener {
        fun OndeleteClick(malelist: CampleResp.Data, position: Int)

    }
}

