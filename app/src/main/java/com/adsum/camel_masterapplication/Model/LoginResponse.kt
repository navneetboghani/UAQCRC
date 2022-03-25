package com.adsum.camel_masterapplication.Model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("response")
    val response: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @SerializedName("Userid")
        val Userid: String,
        @SerializedName("UserName")
        val UserName: String,
        @SerializedName("Role")
        val Role: String,
        @SerializedName("last_login_time_timestamp_format")
        val last_login_time_timestamp_format: String,
        @SerializedName("last_login_time")
        val last_login_time: String,
        @SerializedName("Subscription")
        val subscription: String = "",
        )
}

