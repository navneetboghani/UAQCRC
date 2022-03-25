package com.adsum.camel_masterapplication.Adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Model.TermConditionResponse
import com.adsum.camel_masterapplication.databinding.ItemTermConditionBinding

class TermConditionDetailAdapter (var ctx: Context, val list: List<TermConditionResponse.Data>, val termConditionClickListener: OnTermConditionClickListener) : RecyclerView.Adapter<TermConditionDetailAdapter.ViewHolder>() {


    private lateinit var itemternConditionBinding: ItemTermConditionBinding


    inner class ViewHolder internal constructor(itemternConditionBinding: ItemTermConditionBinding) :
        RecyclerView.ViewHolder(itemternConditionBinding.root) {

        fun bind(
            termCondition: TermConditionResponse.Data,
            position: Int,
            clickListener: OnTermConditionClickListener
        ) {

            itemView.setOnClickListener {
                clickListener.onClick(termCondition, position)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TermConditionDetailAdapter.ViewHolder {
        itemternConditionBinding =
            ItemTermConditionBinding.inflate(LayoutInflater.from(ctx), parent, false)

        return ViewHolder(itemternConditionBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val termcondition = list[position]
        itemternConditionBinding.tvTermConditionTitle.text = termcondition.tc_title
       // itemternConditionBinding.tvTermConditionDescription.text = termcondition.tc_desc
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            itemternConditionBinding.tvTermConditionDescription.setText(Html.fromHtml(termcondition.tc_desc, Html.FROM_HTML_MODE_COMPACT))
        }else{
            itemternConditionBinding.tvTermConditionDescription.setText(Html.fromHtml(termcondition.tc_desc))
        }

        termConditionClickListener.let { holder.bind(termcondition, position, it) }

    }


    override fun getItemCount(): Int {
        return list.size
    }

    interface OnTermConditionClickListener {
        fun onClick(termCondition: TermConditionResponse.Data, position: Int)

    }
}
