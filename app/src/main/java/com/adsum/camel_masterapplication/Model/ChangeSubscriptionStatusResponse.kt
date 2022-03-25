package com.adsum.camel_masterapplication.Model


import com.google.gson.annotations.SerializedName

data class ChangeSubscriptionStatusResponse(
    @SerializedName("data") val `data`: Data = Data(),
    @SerializedName("response") val response: String = "",
    @SerializedName("status") val status: Int = 0
){
    data class Data(
        @SerializedName("Subscription") val subscription: String = "",
        @SerializedName("user_id") val userId: String = ""
    )
}