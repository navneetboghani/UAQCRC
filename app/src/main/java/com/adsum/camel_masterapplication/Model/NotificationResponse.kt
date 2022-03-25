package com.adsum.camel_masterapplication.Model

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
        @SerializedName("data")
        val `data`: List<Data>,
        @SerializedName("message")
        val message: String,
        @SerializedName("status")
        val status: Int
) {
    data class Data(
            val id: String,
            @SerializedName("notifi_title")
            val notifiTitle: String,
            @SerializedName("notification_dsc")
            val notificationDsc: String,
            @SerializedName("is_read")
            var is_read: String

    )
}
