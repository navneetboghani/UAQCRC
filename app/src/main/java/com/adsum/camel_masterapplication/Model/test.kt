package com.adsum.camel_masterapplication.Model


import com.google.gson.annotations.SerializedName


data class test(
    @SerializedName("data") val `data`: Int = 0,
    @SerializedName("response") val response: String = "",
    @SerializedName("status") val status: Int = 0
)