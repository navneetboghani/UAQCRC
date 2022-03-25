package com.adsum.camel_masterapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Model.categoryDetailsResponse3
import com.adsum.camel_masterapplication.databinding.ItemMainRvBinding

class categoryDetailsAdapter2(
    var ctx: Context?, val userList: List<categoryDetailsResponse3.Data>,
    val itemClickListener: OnCategoryDetilsclickListner
) : RecyclerView.Adapter<categoryDetailsAdapter2.ViewHolder>(),
    subcategoryDetailsAdapter2.OnRoundClickListener {
    private lateinit var itemMainRvBinding: ItemMainRvBinding
    private var racename = ""
    private val viewPool = RecyclerView.RecycledViewPool()

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): categoryDetailsAdapter2.ViewHolder {
        itemMainRvBinding = ItemMainRvBinding.inflate(LayoutInflater.from(ctx), parent, false)
        return ViewHolder(itemMainRvBinding)
    }

    override fun onBindViewHolder(holder: categoryDetailsAdapter2.ViewHolder, position: Int) {
        try {
            val camellist = userList[position]
            itemMainRvBinding.tvRaceid.text = camellist.race_id + ":" + camellist.race_name + ""
            racename = camellist.race_id + " : " + camellist.race_name + ""

            val subCategoryAdapter =
                subcategoryDetailsAdapter2(ctx, camellist.round, this, racename)
            itemMainRvBinding.rvMain.adapter = subCategoryAdapter
            itemMainRvBinding.rvMain.setRecycledViewPool(viewPool);

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ViewHolder internal constructor(binding: ItemMainRvBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {}

    interface OnCategoryDetilsclickListner {
        fun onroundnameClick(
            category: categoryDetailsResponse3.Data.Round,
            position: Int,
            racename: String
        )

        fun onnoofparticipantClick(
            category: categoryDetailsResponse3.Data.Round,
            position: Int,
            racename: String
        )
    }

    override fun OnRoundClick(
        camellist: categoryDetailsResponse3.Data.Round,
        position: Int,
        racename: String
    ) {
        itemClickListener.onroundnameClick(camellist, position, racename)
    }

    override fun OnParticipateClick(
        camellist: categoryDetailsResponse3.Data.Round,
        position: Int,
        racename: String
    ) {
        itemClickListener.onnoofparticipantClick(camellist, position, racename)
    }
}
