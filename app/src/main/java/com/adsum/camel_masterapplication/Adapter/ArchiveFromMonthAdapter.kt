package com.adsum.camel_masterapplication.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Model.MonthWiseArchiveRes
import com.adsum.camel_masterapplication.databinding.ItemFromArchiveBinding

class ArchiveFromMonthAdapter(val ctx: Context,val archiveData: ArrayList<MonthWiseArchiveRes.DataX>,val itemClickListener: ArchiveFromMonthAdapter.OnItemClickListener) :
    RecyclerView.Adapter<ArchiveFromMonthAdapter.MyViewHolder>(), CustomArchiveAdapter.OnstrokClickListener{

    data class MyViewHolder(val binding: ItemFromArchiveBinding) :
        RecyclerView.ViewHolder(binding.root)

    private lateinit var itemFromArchiveBinding: ItemFromArchiveBinding
    private var racename = ""
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        itemFromArchiveBinding =
            ItemFromArchiveBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(itemFromArchiveBinding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            val archive = archiveData[position]
            holder.binding.raceId.text = archive.race_id+" : "+" السباق رقم  "+archive.race_name+""
            racename = archive.race_id+" : "+archive.race_name+""

            val customArchiveAdapter = CustomArchiveAdapter(ctx, archive.rounds, this, racename)
            holder.binding.rcArchiveFrom.adapter = customArchiveAdapter
            holder.binding.rcArchiveFrom.setRecycledViewPool(viewPool)
            customArchiveAdapter.notifyDataSetChanged()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override fun getItemCount(): Int = archiveData.size

    override fun OnStokeClick(
        archiveList: MonthWiseArchiveRes.DataX.Round,
        position: Int,
        racename: String
    ) {
        itemClickListener.OnCustomClick(archiveList,position,racename)
    }

    interface OnItemClickListener{
        fun OnCustomClick(archiveList: MonthWiseArchiveRes.DataX.Round, position: Int, racename:String)
    }
}