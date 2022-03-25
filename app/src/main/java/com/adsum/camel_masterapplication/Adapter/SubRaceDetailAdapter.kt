package com.adsum.camel_masterapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Model.RaceDetailResponse
import com.adsum.camel_masterapplication.Model.SubRaceDetailResponse
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.fragment.SubRaceDetailFragment
import java.lang.Exception

class SubRaceDetailAdapter(
    var ctx: Context,
    val sub_raceList: List<SubRaceDetailResponse.Data>,
    subRaceDetailFragment: SubRaceDetailFragment,
    //val itemClickListener:OnSubRaceDetailClickListener
) : RecyclerView.Adapter<SubRaceDetailAdapter.ViewHolder>() {


    class ViewHolder internal constructor(itemView: View): RecyclerView.ViewHolder(
        itemView
    ) {
//        var raceid:TextView=itemView.findViewById(R.id.tv_race_id)
        var round_name:TextView=itemView.findViewById(R.id.tv_round_name)
        var type:TextView=itemView.findViewById(R.id.tv_type)
        var customization:TextView=itemView.findViewById(R.id.tv_customization)
        var description:TextView=itemView.findViewById(R.id.tv_description1)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var SubraceDetail = LayoutInflater.from(parent.context).inflate(R.layout.item_sub_race_detail,parent,false)
        return ViewHolder(SubraceDetail)
    }

    override fun onBindViewHolder(holder: SubRaceDetailAdapter.ViewHolder, position: Int) {
        try{
            val subraceList =  sub_raceList[position]
           // holder.raceid.text=subraceList.raceId
            holder.round_name.text=subraceList.roundName
            holder.type.text=subraceList.type
            holder.customization.text=subraceList.customization
            holder.description.text=subraceList.description


        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
       return sub_raceList.size
    }

}