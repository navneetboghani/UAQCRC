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
import com.adsum.camel_masterapplication.Model.AkbarResp
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.ItemMaleBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.Locale.filter
import kotlin.properties.Delegates

class MaleAdapter(var ctx: Context, val malelist: ArrayList<AkbarResp.Data>,
                  val strokClickListener: OndeleteClickListener,val subscription : String,val sub:String) : RecyclerView.Adapter<MaleAdapter.ViewHolder>() {

    private lateinit var itemMaleBinding: ItemMaleBinding
    var camelno by Delegates.notNull<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaleAdapter.ViewHolder {
        itemMaleBinding = ItemMaleBinding.inflate(LayoutInflater.from(ctx), parent, false)
        camelno= CommonFunctions.getPreference(ctx, Constants.camelno,"").toString()
        return ViewHolder(itemMaleBinding)
    }

    override fun onBindViewHolder(holder: MaleAdapter.ViewHolder, position: Int) {
        try{
            val malelist = malelist[position]
            loadBannerPhoto(itemMaleBinding.ivDelete, "")
            itemMaleBinding.tvMsubname.text = malelist.rcCamel
            itemMaleBinding.tvMtypeCamel.text = camelno
            if (subscription == "0"){
                itemMaleBinding.ivDelete.visibility= View.GONE
            }else{
                itemMaleBinding.ivDelete.visibility= View.VISIBLE
            }
            if (sub == "0"){
                itemMaleBinding.ivDelete.visibility= View.GONE
            }else{
                itemMaleBinding.ivDelete.visibility= View.VISIBLE
            }
            strokClickListener.let { holder.bind(malelist, position, it) }

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
    override fun getItemCount(): Int {
        return malelist.size
    }
    inner class ViewHolder internal constructor(binding: ItemMaleBinding): RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(
            malelist: AkbarResp.Data,
            position: Int,
            clickListener: OndeleteClickListener
        ) {
            itemMaleBinding.ivDelete.setOnClickListener {
             //   Log.e("San"," i m click delete")
                clickListener.OndeleteClick(malelist, position)
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun DeleteMaleCamel(position: Int){
        malelist.removeAt(position)
        Log.e("tag","position:-"+position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position,malelist.size)
    }
    interface OndeleteClickListener {
        fun OndeleteClick(malelist: AkbarResp.Data, position: Int)
    }
}