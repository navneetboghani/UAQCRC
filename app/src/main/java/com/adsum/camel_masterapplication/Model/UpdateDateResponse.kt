package com.adsum.camel_masterapplication.Model


import com.google.gson.annotations.SerializedName


data class UpdateDateResponse(
    @SerializedName("data") val `data`: List<String> = listOf(),
    @SerializedName("response") val response: String = "",
    @SerializedName("status") val status: Int
)
