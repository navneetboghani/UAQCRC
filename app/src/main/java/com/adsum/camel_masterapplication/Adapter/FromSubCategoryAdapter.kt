package com.adsum.camel_masterapplication.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Model.ParticipateInRaceRoundRes
import com.adsum.camel_masterapplication.databinding.ItemFromSubCategoryBinding

class FromSubCategoryAdapter(val context: Context, val subList: ParticipateInRaceRoundRes) :
    RecyclerView.Adapter<FromSubCategoryAdapter.MyViewHolder>() {
    private lateinit var binding: ItemFromSubCategoryBinding

    inner class MyViewHolder(val binding: ItemFromSubCategoryBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FromSubCategoryAdapter.MyViewHolder {
        binding =
            ItemFromSubCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FromSubCategoryAdapter.MyViewHolder, position: Int) {
        val fromSub = subList[0]
        Log.e("size", "size" + fromSub)
        holder.binding.subNameText.text = fromSub.members.get(position).user.name_of_participant
        holder.binding.camelGenderText.text = fromSub.members.get(position).rc_gender
        holder.binding.camelNoText.text = fromSub.members.get(position).user.camel_no
    }
    override fun getItemCount(): Int = subList.get(0).members.size
}