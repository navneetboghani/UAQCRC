package com.adsum.camel_masterapplication.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.adsum.camel_masterapplication.Model.NotificationResponse
import com.adsum.camel_masterapplication.databinding.ItemNotificationBinding
import com.adsum.camel_masterapplication.fragment.NotificationFragment

class NotificationAdapter(var ctx: Context, var list: List<NotificationResponse.Data>, val notificationClickListener: OnNotificationClickListener) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    private lateinit var itemNotificationBinding: ItemNotificationBinding
    var unReadNotification: ArrayList<String> = arrayListOf()

    inner class ViewHolder internal constructor(itemNotificationBinding: ItemNotificationBinding) : RecyclerView.ViewHolder(itemNotificationBinding.root) {

        fun bind(notification: NotificationResponse.Data, position: Int, clickListener: OnNotificationClickListener) {
            itemView.setOnClickListener {
                clickListener.onNotificationClick(notification, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.ViewHolder {
        itemNotificationBinding =
                ItemNotificationBinding.inflate(LayoutInflater.from(ctx), parent, false)

        return ViewHolder(itemNotificationBinding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = list[position]
        var des = notification.notificationDsc
        fun notify() {
            if (notification.is_read == "0") {
                notification.is_read = "1"
            }
        }
        itemNotificationBinding.tvTitle.text = notification.notifiTitle
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            itemNotificationBinding.tvDecription.setText(Html.fromHtml(notification.notificationDsc, Html.FROM_HTML_MODE_COMPACT))
        } else {
            itemNotificationBinding.tvDecription.setText(Html.fromHtml(notification.notificationDsc))
        }
        notificationClickListener.let { holder.bind(notification, position, it) }
    }

    @SuppressLint("LogNotTimber")
    fun unReadNotification(): ArrayList<String> {
        for (i in 0..list.size - 1) {
            if (list[i].is_read == 0.toString()) {
                this.unReadNotification = (unReadNotification + list[i].id) as ArrayList<String>
                Log.e("notifyCount", "size---" + unReadNotification)
            }
        }
        return unReadNotification
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnNotificationClickListener {
        fun onNotificationClick(notification: NotificationResponse.Data, position: Int)
    }
}