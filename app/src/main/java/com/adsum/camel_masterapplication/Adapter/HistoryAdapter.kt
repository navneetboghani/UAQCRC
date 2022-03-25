package com.adsum.camel_masterapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.adsum.camel_masterapplication.Model.HistoryModel
import com.adsum.camel_masterapplication.databinding.ItemHistoryBinding


class HistoryAdapter(var ctx: Context, val userList: ArrayList<HistoryModel>, val clickListener: OnItemClickListener) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    private lateinit var itemMainRvBinding: ItemHistoryBinding

    private val viewPool = RecycledViewPool()
    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        itemMainRvBinding = ItemHistoryBinding.inflate(LayoutInflater.from(ctx), parent, false)
        return ViewHolder(itemMainRvBinding)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
        try {
            val date = userList[position]
            itemMainRvBinding.tvYear.text = date.month+"  "+date.year

            clickListener.let {
                holder.bind(date, position, it, date.list, date.year)
            }

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }
    inner class ViewHolder internal constructor(binding: ItemHistoryBinding): RecyclerView.ViewHolder(
        binding.root
    ){
        fun bind(camellist: HistoryModel, position: Int, clickListener: OnItemClickListener , month: String, year: String)
        {

            itemMainRvBinding.tvYear.setOnClickListener {
                clickListener.OnCustomClick(camellist, position, month, year)
            }
        }
    }
    interface OnItemClickListener{
        fun OnCustomClick(history: HistoryModel, position: Int, month: String, year: String)
    }

}
