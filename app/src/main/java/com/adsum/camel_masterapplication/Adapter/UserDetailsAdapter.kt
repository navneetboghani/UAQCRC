package com.adsum.camel_masterapplication.Adapter

import android.R.id
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Model.UserDetailsResponse
import com.adsum.camel_masterapplication.R
import android.widget.CompoundButton
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.snackbar.Snackbar
import kotlin.properties.Delegates
import android.content.Context.MODE_PRIVATE

import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE
import android.R.id.toggle

import android.content.Context.MODE_PRIVATE
import com.adsum.camel_masterapplication.databinding.ItemUserDetailsBinding


class UserDetailsAdapter(var ctx: Context, var list: ArrayList<UserDetailsResponse.Data>, val userDetailClickListener: OnUserDetailClickListener) : RecyclerView.Adapter<UserDetailsAdapter.ViewHolder>() {

    private lateinit var iteamUserDetailsBinding: ItemUserDetailsBinding
    private lateinit var status: String


    inner class ViewHolder internal constructor(itemView:View) :
        RecyclerView.ViewHolder(itemView) {

        val tvUserName: TextView = itemView.findViewById(R.id.tv_user_name)
        val tvParticipantName: TextView = itemView.findViewById(R.id.tv_participant_name)
        val tvEmail: TextView = itemView.findViewById(R.id.tv_email)
        val tvMobileNo: TextView = itemView.findViewById(R.id.tv_mobile_no)
        val tvCamelno: TextView = itemView.findViewById(R.id.tv_camelno)
        val ivDelete: ImageView = itemView.findViewById(R.id.iv_delete)
        val ivLogout: ImageView = itemView.findViewById(R.id.iv_logout)
        val image: Switch = itemView.findViewById(R.id.switch1)
        val ivsubscription:Switch=itemView.findViewById(R.id.switch_subscription)


        fun bind(
            userdetail: UserDetailsResponse.Data,
            position: Int,
            clickListener: OnUserDetailClickListener

        ) {
            ivDelete.setOnClickListener {
                clickListener.OnClick(userdetail, position)

            }
            ivLogout.setOnClickListener{
                clickListener.logoutuser(userdetail,position)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val iteamview = LayoutInflater.from(parent.context).inflate(R.layout.item_user_details,parent,false)

        return ViewHolder(iteamview)
    }

    @SuppressLint("ResourceAsColor", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var details = list[position]
        holder.tvUserName.text = details.user_login
        holder.tvParticipantName.text = details.name_of_participant
        holder.tvEmail.text = details.user_email
        holder.tvMobileNo.text = details.mobile_no
        holder.tvCamelno.text = details.camel_no

        if (details.block_user.equals("0")){
           holder.image.isChecked=true
        }else{
            holder.image.isChecked=false
        }
        holder.image.setOnClickListener {

            if (details.block_user.equals("1")) {
                list[position].block_user = "0"
            } else {
                list[position].block_user = "1"
            }
            userDetailClickListener.blockuser(details, position, list[position].block_user)
            notifyDataSetChanged()
        }
//
//        holder.image.setOnClickListener{
//            if (details.block_user.equals("1")){
//                list[position].block_user = "0"
//            }else{
//                list[position].block_user = "1"
//            }
//            userDetailClickListener.blockuser(details,position,list[position].block_user)
//            notifyDataSetChanged()
//        }

        if (details.subscription.equals("1")){
            holder.ivsubscription.isChecked=true
        }else{
            holder.ivsubscription.isChecked=false
        }
        holder.ivsubscription.setOnClickListener{
            if (details.subscription.equals("1")){
                list[position].subscription="0"
            }
            else{
                list[position].subscription="1"
            }
            userDetailClickListener.subscription(details,position,list[position].subscription)
            notifyDataSetChanged()
        }


        userDetailClickListener.let { holder.bind(details, position, it,) }

    }

    override fun getItemCount(): Int {
        return list.size
    }

     @SuppressLint("NotifyDataSetChanged")
     fun deleteuser(position: Int){
         list.removeAt(position)
         notifyDataSetChanged()
     }

    interface OnUserDetailClickListener {
        fun OnClick(userdetail: UserDetailsResponse.Data, position: Int)
        fun logoutuser(userdetail: UserDetailsResponse.Data, position: Int)
        fun blockuser(userdetail: UserDetailsResponse.Data, position: Int,status: String)
        fun subscription(userdetail: UserDetailsResponse.Data,position: Int,subscription: String)
    }

}

