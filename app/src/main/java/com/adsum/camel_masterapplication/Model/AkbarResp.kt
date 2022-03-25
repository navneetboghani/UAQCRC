package com.adsum.camel_masterapplication.Model

import com.google.gson.annotations.SerializedName

data class AkbarResp(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("response")
    val response: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("Subscriber")
    val Subscriber: String
)
{
    data class Data(
        @SerializedName("rc_camel")
        val rcCamel: String,
        @SerializedName("rc_gender")
        val rcGender: String,
        @SerializedName("rc_id")
        val rcId: String,
        @SerializedName("rc_status")
        val rcStatus: String,
        @SerializedName("user_id")
        val userId: String
    )
}
