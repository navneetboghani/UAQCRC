package com.adsum.camel_masterapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Model.CategoryNameResponse
import com.adsum.camel_masterapplication.databinding.ItemCategoryBinding

class CategoryNameAdapter(
    var ctx: Context?,
    var categoryllist:List<CategoryNameResponse.Data>,
    val categoryclickListner: OnCategoryClickListner):
    RecyclerView.Adapter<CategoryNameAdapter.ViewHolder>() {
    private lateinit var itemCategoryBinding: ItemCategoryBinding
    inner class ViewHolder internal constructor(itemCategoryBinding: ItemCategoryBinding):
        RecyclerView.ViewHolder(itemCategoryBinding.root){

            fun bind(
                category: CategoryNameResponse.Data,
                position: Int,
                clickListner:OnCategoryClickListner
            ){
                itemView.setOnClickListener{
                    clickListner.onCategoryClick(category, position)
                }
            }
        }

    interface OnCategoryClickListner {
        fun onCategoryClick(category: CategoryNameResponse.Data, position: Int)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryNameAdapter.ViewHolder {
        itemCategoryBinding = ItemCategoryBinding.inflate(LayoutInflater.from(ctx),parent,false)
        return ViewHolder(itemCategoryBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryName = categoryllist[position]
        itemCategoryBinding.tvCategoryName.text = categoryName.categoryName

        categoryclickListner.let{holder.bind(categoryName,position, it)}
    }

    override fun getItemCount(): Int {
        return categoryllist.size
    }
}