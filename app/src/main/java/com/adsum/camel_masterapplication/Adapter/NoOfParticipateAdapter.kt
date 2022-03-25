package com.adsum.camel_masterapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Model.NoOfParticipateResponse
import com.adsum.camel_masterapplication.Model.ViewRoundList
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.ItemNoofparticipateBinding

class NoOfParticipateAdapter(
    var ctx:Context,
    var NoOfParticipateList: ArrayList<ViewRoundList>,
    val noOfparticipateClickListner: OnnoOfparticipateclickListner):RecyclerView.Adapter<NoOfParticipateAdapter.ViewHolder>() {
    private lateinit var itemNoofparticipateBinding: ItemNoofparticipateBinding
    inner class ViewHolder internal constructor(itemView : View):RecyclerView.ViewHolder(itemView){
            val rccamel : TextView = itemView.findViewById(R.id.tv_rccamel)
            val camelno : TextView = itemView.findViewById(R.id.tv_camelno)
            val participatename : TextView = itemView.findViewById(R.id.tv_paricipatename)

            fun bind(participate: ViewRoundList, position: Int, clickListner: OnnoOfparticipateclickListner) {

            }
    }

    interface OnnoOfparticipateclickListner {
        fun OnClick(participate: NoOfParticipateResponse.Data.Members.Member,position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoOfParticipateAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_noofparticipate,parent,false)
        return ViewHolder(itemView)
//        itemNoofparticipateBinding = ItemNoofparticipateBinding.inflate(LayoutInflater.from(ctx),parent,false)
//        return ViewHolder(itemNoofparticipateBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val Participate = NoOfParticipateList[position]
            holder.rccamel.text = Participate.rc_camel
            holder.camelno.text = Participate.camel_no
            holder.participatename.text = Participate.name_of_participant
//        itemNoofparticipateBinding.tvRccamel.text = Participate.rc_camel
//        itemNoofparticipateBinding.tvCamelno.text = Participate.user.camel_no
//        itemNoofparticipateBinding.tvParicipatename.text = Participate.user.name_of_participant
            noOfparticipateClickListner.let { holder.bind(Participate,position,it) }

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return NoOfParticipateList.size
    }


}