package com.adsum.camel_masterapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Model.MonthWiseArchiveRes
import com.adsum.camel_masterapplication.Model.RaceResponse
import com.adsum.camel_masterapplication.databinding.ItemFromCustomArchiveBinding

class CustomArchiveAdapter(val ctx : Context,val archiveList: List<MonthWiseArchiveRes.DataX.Round> ,val itemClickListener:OnstrokClickListener ,raceName:String)
    :RecyclerView.Adapter<CustomArchiveAdapter.ViewHolder>(){
    private lateinit var itemFromCustomArchiveBinding: ItemFromCustomArchiveBinding
    var ll = raceName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        itemFromCustomArchiveBinding = ItemFromCustomArchiveBinding.inflate(LayoutInflater.from(ctx),parent,false)
        return ViewHolder(itemFromCustomArchiveBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val archive = archiveList[position]
            holder.binding.tvRoundnameArc.text = archive.round_name
            holder.binding.tvTypeArc.text = archive.type
            holder.binding.tvDistanceArc.text = archive.distance
            holder.binding.tvPriceArc.text = archive.price
            holder.binding.tvCategoryArc.text = archive.category
            itemClickListener.let { holder.bind(archive, position, it, ll) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int = archiveList.size

    inner class ViewHolder internal constructor(val binding: ItemFromCustomArchiveBinding): RecyclerView.ViewHolder(
        binding.root
    ){
        fun bind(archiveList: MonthWiseArchiveRes.DataX.Round, position: Int, clickListener: CustomArchiveAdapter.OnstrokClickListener, racename: String)
        {
            binding.tvRoundnameArc.setOnClickListener {
                clickListener.OnStokeClick(archiveList, position, racename)
            }
        }
    }
    interface OnstrokClickListener {
        fun OnStokeClick(
            archiveList: MonthWiseArchiveRes.DataX.Round,
            position: Int,
            racename: String
        )
    }
}