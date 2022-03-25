package com.adsum.camel_masterapplication.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Model.RaceDetailResponse
import com.adsum.camel_masterapplication.R

import com.adsum.camel_masterapplication.databinding.ItemRaceDetailBinding
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class RaceDetailAdapter(
    var ctx: Context,
    var raceList: ArrayList<RaceDetailResponse.Data>,
    val raceDetailClickListener: OnRaceDetailClickListener,
    var Role:String
) : RecyclerView.Adapter<RaceDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        itemRaceDetailBinding=ItemRaceDetailBinding.inflate(LayoutInflater.from(ctx),parent,false)
//        return ViewHolder(itemRaceDetailBinding)
        val raceDetail = LayoutInflater.from(parent.context).inflate(R.layout.item_race_detail,parent,false)
        return ViewHolder(raceDetail)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try{
            val raceList =  raceList[position]
            holder.tvRaceName.text=raceList.raceName
            holder.tvRaceId.text=raceList.raceId
            holder.tvNoOfround.text=raceList.noOfRound
            holder.tvStartdate.text=raceList.startDate
            holder.tvEndDate.text=raceList.endDate

            val dateStr =raceList.startDate
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            val df1 = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
            df.timeZone = TimeZone.getTimeZone("UTC")
            val date = df.parse(dateStr)
            df.timeZone = TimeZone.getDefault()
            val formattedDate = df1.format(date)
            holder.tvStartdate.text=formattedDate
            // Log.e("san","startdate:-"+formattedDate)

            val dateStrED =raceList.endDate
            val dfED = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            val dfED1 = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
            df.timeZone = TimeZone.getTimeZone("UTC")
            val dateED = dfED.parse(dateStrED)
            df.timeZone = TimeZone.getDefault()
            val formattedDateED = dfED1.format(dateED)
            holder.tvEndDate.text=formattedDateED
            // Log.e("san","enddate:-"+formattedDateED)

            if (Role == "normal_user"){
                holder.imgDelete.visibility=View.GONE
                holder.imgSchedule.visibility=View.GONE
                holder.imgUsers.visibility=View.GONE
            }else{
                holder.imgDelete.visibility=View.VISIBLE
                holder.imgSchedule.visibility=View.VISIBLE
                holder.imgUsers.visibility=View.VISIBLE
            }

            raceDetailClickListener.let { holder.bind(raceList,position,it) }

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return raceList.size
    }

    inner class ViewHolder internal constructor(binding: View): RecyclerView.ViewHolder(
        binding

    ) {
        var tvRaceName:TextView=itemView.findViewById(R.id.tv_raceName)
        var tvRaceId:TextView=itemView.findViewById(R.id.tv_race_id)
        var tvNoOfround:TextView=itemView.findViewById(R.id.tv_noOfround)
        var tvStartdate:TextView=itemView.findViewById(R.id.tv_startdate)
        var tvEndDate:TextView=itemView.findViewById(R.id.tv_EndDate)
        var imgDelete:ImageView=itemView.findViewById(R.id.img_delete)
        var imgSchedule:ImageView=itemView.findViewById(R.id.img_schedule)
        var imgUsers:ImageView=itemView.findViewById(R.id.img_users)

        fun bind(
            raceList: RaceDetailResponse.Data, position: Int, clickListener: OnRaceDetailClickListener
        ){
            tvRaceName.setOnClickListener {
                clickListener.OnRaceDetailClickListener(raceList, position)
            }
            imgDelete.setOnClickListener{
                clickListener.OnDeleteRecord(raceList,position)
            }
            imgSchedule.setOnClickListener{
                clickListener.OnRaceScheduleClickListener(raceList,position,raceList.raceId)
            }
            imgUsers.setOnClickListener{
                clickListener.OnUserClickListener(raceList,position)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleterace(position: Int){
        raceList.removeAt(position)
        // notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    fun updateDate(startdate: String?, endDate: String?, position: Int?) {
        raceList[position!!].startDate= startdate!!;
        Log.e("tag","startdate:-"+startdate+"position:-"+position)
        raceList[position!!].endDate= endDate!!;
        Log.e("tag","endDate:-"+endDate+"position:-"+position)
        notifyItemChanged(position)
    }

    interface OnRaceDetailClickListener{
        fun OnRaceDetailClickListener(raceList: RaceDetailResponse.Data, position: Int)
        fun OnDeleteRecord(raceList: RaceDetailResponse.Data, position: Int)
        fun OnRaceScheduleClickListener(
            raceList: RaceDetailResponse.Data, position: Int,
            raceid: String
        )
        fun OnUserClickListener(raceList: RaceDetailResponse.Data, position: Int)

    }


}


