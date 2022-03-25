package com.adsum.camel_masterapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Model.RaceResponse

import com.adsum.camel_masterapplication.databinding.ListLayoutBinding

class SubCustomAdapter(var ctx:Context, val userList: List<RaceResponse.RaceResponseItem.Round>, val strokClickListener: OnstrokClickListener, racename: String) : RecyclerView.Adapter<SubCustomAdapter.ViewHolder>() {
    private lateinit var listLayoutBinding: ListLayoutBinding
    var ll = racename
    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCustomAdapter.ViewHolder {
        listLayoutBinding = ListLayoutBinding.inflate(LayoutInflater.from(ctx), parent, false)
        return ViewHolder(listLayoutBinding)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: SubCustomAdapter.ViewHolder, position: Int) {
        try {
            val camellist = userList[position]
            listLayoutBinding.tvStrock.text = camellist.roundName
            listLayoutBinding.tvSex.text = camellist.type
            listLayoutBinding.tvCustomization.text = camellist.customization
            listLayoutBinding.tvPrize.text = camellist.description
            strokClickListener.let { holder.bind(camellist, position, it,ll) }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }
    inner class ViewHolder internal constructor(binding: ListLayoutBinding): RecyclerView.ViewHolder(
        binding.root
    ){

        fun bind(camellist: RaceResponse.RaceResponseItem.Round, position: Int ,clickListener: OnstrokClickListener,racename: String)
        {

            listLayoutBinding.tvStrock.setOnClickListener {
                clickListener.OnStokeClick(camellist, position,racename)
            }
        }

    }
    interface OnstrokClickListener{
        fun OnStokeClick(camellist: RaceResponse.RaceResponseItem.Round, position: Int,racename: String)
    }

}