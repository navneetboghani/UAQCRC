package com.adsum.camel_masterapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.adsum.camel_masterapplication.Model.RaceResponse
import com.adsum.camel_masterapplication.databinding.ItemMainRvBinding


class CustomAdapter(var ctx: Context, val userList: List<RaceResponse.RaceResponseItem>,val itemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<CustomAdapter.ViewHolder>(),
    SubCustomAdapter.OnstrokClickListener {



    private lateinit var itemMainRvBinding: ItemMainRvBinding
    private var racename = ""
    private val viewPool = RecycledViewPool()
    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        itemMainRvBinding = ItemMainRvBinding.inflate(LayoutInflater.from(ctx), parent, false)
        return ViewHolder(itemMainRvBinding)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        try {
            val camellist = userList[position]
            itemMainRvBinding.tvRaceid.text = camellist.raceId+" : "+" السباق رقم  "+camellist.raceName+""
            racename= camellist.raceId+" : "+camellist.raceName+""
            val layoutManager = LinearLayoutManager(
                itemMainRvBinding.rvMain.getContext(),
                LinearLayoutManager.VERTICAL,
                false
            )

            val subCategoryAdapter = SubCustomAdapter(ctx, camellist.rounds, this, racename)
            itemMainRvBinding.rvMain.layoutManager = layoutManager
            itemMainRvBinding.rvMain.adapter = subCategoryAdapter
            itemMainRvBinding.rvMain.setRecycledViewPool(viewPool);


        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }
    inner class ViewHolder internal constructor(binding: ItemMainRvBinding): RecyclerView.ViewHolder(
        binding.root
    ){

    }

    override fun OnStokeClick(
        camellist: RaceResponse.RaceResponseItem.Round,
        position: Int,
        racename: String
    ) {
        itemClickListener.OnCustomClick(camellist, position,racename)
    }

    interface OnItemClickListener{
        fun OnCustomClick(camellist: RaceResponse.RaceResponseItem.Round, position: Int,racename:String)
    }

}
