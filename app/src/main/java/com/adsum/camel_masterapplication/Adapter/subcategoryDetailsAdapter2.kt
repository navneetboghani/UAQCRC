package com.adsum.camel_masterapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Model.categoryDetailsResponse3
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.ItemParticipateraceBinding

class subcategoryDetailsAdapter2(var ctx: Context?
, val userList: List<categoryDetailsResponse3.Data.Round>
, val roundClickListener: OnRoundClickListener, racename: String) : RecyclerView.Adapter<subcategoryDetailsAdapter2.ViewHolder>() {


    private lateinit var itemParticipateraceBinding: ItemParticipateraceBinding
    var ll = racename
    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): subcategoryDetailsAdapter2.ViewHolder {
        var userlist1=LayoutInflater.from(parent.context).inflate(R.layout.item_participaterace,parent,false)
       // itemParticipateraceBinding = ItemParticipateraceBinding.inflate(LayoutInflater.from(ctx), parent, false)
        return ViewHolder(userlist1)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: subcategoryDetailsAdapter2.ViewHolder, position: Int) {
        try {
            var T = ""
            val camellist = userList[position]
            if (camellist.type == "Male"){
                T = "جعدان"
            } else{
                T ="ابكار"
            }
            holder.tvroundname?.text=camellist.round_name
            holder.tvtype?.text= T
            holder.tvdistance?.text=camellist.distance
            holder.tvprice?.text=camellist.price
            holder.tvcategory?.text=camellist.category
            holder.tvnoofparticipant?.text= camellist.noofparticipants.toString()

//            itemParticipateraceBinding.tvRoundname.text = camellist.round_name
//            itemParticipateraceBinding.tvType.text = camellist.type
//            itemParticipateraceBinding.tvDistance.text = camellist.distance
//            itemParticipateraceBinding.tvPrice.text = camellist.price
//            itemParticipateraceBinding.tvCategory.text = camellist.category
//            itemParticipateraceBinding.tvNoofparticipant1.text = camellist.noofparticipants.toString()
            roundClickListener.let { holder.bind(camellist, position, it,ll) }

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }
    inner class ViewHolder internal constructor(binding: View): RecyclerView.ViewHolder(
        binding
    ){
        var tvroundname: TextView? =itemView.findViewById(R.id.tv_roundname)
        var tvtype: TextView? =itemView.findViewById(R.id.tv_type)
        var tvdistance: TextView? =itemView.findViewById(R.id.tv_distance)
        var tvprice: TextView? =itemView.findViewById(R.id.tv_price)
        var tvcategory: TextView? =itemView.findViewById(R.id.tv_category)
        var tvnoofparticipant: TextView? =itemView.findViewById(R.id.tv_noofparticipant1)



        fun bind(camellist: categoryDetailsResponse3.Data.Round, position: Int, clickListener: OnRoundClickListener, racename: String)
        {

            tvroundname?.setOnClickListener {
                clickListener.OnRoundClick(camellist, position,racename)
            }
            tvnoofparticipant?.setOnClickListener {
               clickListener.OnParticipateClick(camellist,position,racename)
            }
        }

    }
    interface OnRoundClickListener{
        fun OnRoundClick(camellist: categoryDetailsResponse3.Data.Round, position: Int, racename: String)
        fun OnParticipateClick(
            camellist: categoryDetailsResponse3.Data.Round,
            position: Int,
            racename: String
        )
    }

}