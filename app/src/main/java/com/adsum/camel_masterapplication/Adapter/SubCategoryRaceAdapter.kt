package com.adsum.camel_masterapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Model.NoOfParticipateResponse
import com.adsum.camel_masterapplication.Model.ViewRoundList
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.ItemRaceSubcategoryBinding

class SubCategoryRaceAdapter(
        var ctx: Context, val subcategory:ArrayList<ViewRoundList>
        , val stroksubClickListener: OnsubdeleteClickListener,val from: String,val Role:String) : RecyclerView.Adapter<SubCategoryRaceAdapter.ViewHolder>() {
    private lateinit var itemRaceSubcategoryBinding: ItemRaceSubcategoryBinding

    inner class ViewHolder internal constructor(binding: View): RecyclerView.ViewHolder(binding)
    {
        var tvParticipant1: TextView =itemView.findViewById(R.id.tv_participant1)
        var tvCamelType1: TextView =itemView.findViewById(R.id.tv_camelType1)
        var tvCamelNo1: TextView =itemView.findViewById(R.id.tv_camelNo1)
        var btnDelete: Button =itemView.findViewById(R.id.btn_delete)

        fun bind(subcategory: ViewRoundList, position: Int, clickListener: OnsubdeleteClickListener) {
            btnDelete.setOnClickListener {
                subcategory.rl_id?.let { it1 ->
                    clickListener.OndeleteClick(subcategory, position)
                }
            }
        }
    }
    interface OnsubdeleteClickListener {
        fun OndeleteClick(subcategory: ViewRoundList, position: Int)

    }
    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryRaceAdapter.ViewHolder {
        var subcategoryrace = LayoutInflater.from(parent.context).inflate(R.layout.item_race_subcategory,parent,false)
        return ViewHolder(subcategoryrace)
    }
    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return subcategory.size
    }

    override fun onBindViewHolder(holder: SubCategoryRaceAdapter.ViewHolder, position: Int) {
        try {
            val subcategory = subcategory[position]
            holder.tvParticipant1.text = subcategory.name_of_participant
            holder.tvCamelType1.text = subcategory.rc_camel
            holder.tvCamelNo1.text = subcategory.camel_no
            if (Role == "normal_user") {
                holder.btnDelete.visibility = View.GONE
            }
            stroksubClickListener.let { holder.bind(subcategory, position, it) }

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun deleteSubCategory(position: Int){
        subcategory.removeAt(position)
        notifyDataSetChanged()
    }
}