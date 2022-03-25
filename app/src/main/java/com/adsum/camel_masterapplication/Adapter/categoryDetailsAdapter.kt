package com.adsum.camel_masterapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
//import com.adsum.camel_masterapplication.Model.categoryDetailsResponse
import com.adsum.camel_masterapplication.Model.categoryDetailsResponse3
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.ItemMainRvBinding
import com.adsum.camel_masterapplication.databinding.ItemParticipateraceBinding
import org.w3c.dom.Text

class categoryDetailsAdapter(
    var ctx: Context?,
    var categoryDetailsList: List<categoryDetailsResponse3.Data>,
    val categoryDetailsclickListner: OnCategoryDetilsclickListner
) : RecyclerView.Adapter<categoryDetailsAdapter.ViewHolder>() {
    private lateinit var itemMainRvBinding: ItemMainRvBinding
    private var racename = ""
    private val viewPool = RecyclerView.RecycledViewPool()
    private lateinit var itemparticipateraceBinding: ItemParticipateraceBinding

    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
//        val roundname: TextView = itemView.findViewById(R.id.tv_roundname)
//        val type: TextView = itemView.findViewById(R.id.tv_type)
//        val distance: TextView = itemView.findViewById(R.id.tv_distance)
//        val price: TextView = itemView.findViewById(R.id.tv_price)
//        val category: TextView = itemView.findViewById(R.id.tv_category)
//        val noOfParticipant: TextView = itemView.findViewById(R.id.tv_noofparticipant)

        fun bind(
            product: categoryDetailsResponse3.Data,
            position: Int,
            clickListener: OnCategoryDetilsclickListner
        ) {

//            roundname.setOnClickListener {
//                clickListener.onroundnameClick(product, position)
//            }
//            noOfParticipant.setOnClickListener {
//                clickListener.onnoofparticipantClick(product, position)
//            }
        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): categoryDetailsAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_participaterace, parent, false)
        return ViewHolder(itemView)
//        itemparticipateraceBinding = ItemParticipateraceBinding.inflate(LayoutInflater.from(ctx),parent,false)
//        return ViewHolder(itemparticipateraceBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val categorydetails = categoryDetailsList[position]
//            holder.roundname.text = categorydetails.round[0].round_name
//            holder.type.text = categorydetails.round[0].type
//            holder.distance.text = categorydetails.round[0].distance
//            holder.price.text = categorydetails.round[0].price
//            holder.category.text = categorydetails.round[0].category
//            holder.noOfParticipant.text = categorydetails.round[0].noofparticipants.toString()
//            itemparticipateraceBinding.tvRoundname.text = categorydetails.round_name
//            itemparticipateraceBinding.tvType.text = categorydetails.type
//            itemparticipateraceBinding.tvDistance.text= categorydetails.distance
//            itemparticipateraceBinding.tvPrice.text = categorydetails.price
//            itemparticipateraceBinding.tvCategory.text = categorydetails.category
//            itemparticipateraceBinding.tvNoofparticipant.text= categorydetails.noofparticipants.toString()
            //itemparticipateraceBinding.tvDescription.text = categorydetails.description
            categoryDetailsclickListner.let { holder.bind(categorydetails, position, it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return categoryDetailsList.size
    }

    interface OnCategoryDetilsclickListner {
        fun onroundnameClick(category: categoryDetailsResponse3.Data, position: Int)
        fun onnoofparticipantClick(category: categoryDetailsResponse3.Data, position: Int)

    }
}