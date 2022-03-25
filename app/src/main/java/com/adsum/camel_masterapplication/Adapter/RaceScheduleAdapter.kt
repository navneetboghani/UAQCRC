package com.adsum.camel_masterapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Config.CamelConfig
import com.adsum.camel_masterapplication.Model.GetScheduleResponse
import com.adsum.camel_masterapplication.databinding.ItemScheduleBinding
import com.bumptech.glide.Glide

class RaceScheduleAdapter(val scheduleList: List<GetScheduleResponse.Data>, val clickListener: RaceScheduleAdapter.OnItemClickListener): RecyclerView.Adapter<RaceScheduleAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemScheduleBinding): RecyclerView.ViewHolder(
        binding.root
    )
    {
        fun bind(imageList: GetScheduleResponse.Data, position: Int, clickListener: OnItemClickListener)
        {
            binding.scheduleImage.setOnClickListener{
                clickListener.OnCustomClick(imageList, position)
            }
        }
    }
    interface OnItemClickListener{
        fun OnCustomClick(imageList: GetScheduleResponse.Data, position: Int)
    }

    private lateinit var itemScheduleBinding: ItemScheduleBinding

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaceScheduleAdapter.ViewHolder {
        itemScheduleBinding = ItemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemScheduleBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try{
            val schedule = scheduleList[position]
            Glide.with(holder.binding.scheduleImage.context)
                .load(schedule.rs_image)
                .centerCrop()
                .into(holder.binding.scheduleImage)

            clickListener.let {
                holder.bind(schedule,position,clickListener)
            }

        }catch (e: Exception){
            e.printStackTrace()
        }
    }
    override fun getItemCount(): Int = scheduleList.size
}