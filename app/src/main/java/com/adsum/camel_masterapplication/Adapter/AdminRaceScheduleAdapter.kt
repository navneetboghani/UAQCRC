package com.adsum.camel_masterapplication.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Model.GetScheduleResponse
import com.adsum.camel_masterapplication.databinding.ItemAdminScheduleBinding
import com.bumptech.glide.Glide

class AdminRaceScheduleAdapter(val scheduleList: ArrayList<GetScheduleResponse.Data>,val clickListener: AdminRaceScheduleAdapter.OnItemClickListener): RecyclerView.Adapter<AdminRaceScheduleAdapter.ViewHolder>() {
    private lateinit var itemAdminScheduleBinding: ItemAdminScheduleBinding
    class ViewHolder(val binding: ItemAdminScheduleBinding): RecyclerView.ViewHolder(
        binding.root
    ){
        fun bind(imageList: GetScheduleResponse.Data, position: Int, clickListener: AdminRaceScheduleAdapter.OnItemClickListener)
        {
            binding.reSelectImage.setOnClickListener{
                clickListener.ReselectingImage(imageList.rs_id.toInt(),imageList.rs_image, position)
            }
            binding.deleteImage.setOnClickListener {
                clickListener.DeleteImage(imageList, position)
            }
        }
    }
    interface OnItemClickListener{
        fun ReselectingImage(rc_id:Int,image:String, position: Int)
        fun DeleteImage(imageList: GetScheduleResponse.Data, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         itemAdminScheduleBinding = ItemAdminScheduleBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(itemAdminScheduleBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = scheduleList[position]

        Glide.with(holder.binding.adminImage.context)
            .load(image.rs_image)
            .centerCrop()
            .into(holder.binding.adminImage)

        clickListener.let {
            holder.bind(image,position,clickListener)
        }
    }

    override fun getItemCount(): Int = scheduleList.size

    fun DeleteImage(position: Int){
        scheduleList.removeAt(position)

        notifyDataSetChanged()
    }
}