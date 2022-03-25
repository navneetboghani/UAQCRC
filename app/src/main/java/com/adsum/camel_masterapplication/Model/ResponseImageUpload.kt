package com.adsum.camel_masterapplication.Model

import com.google.gson.annotations.SerializedName

data class ResponseImageUpload(
    @SerializedName("data")
    val `data`: String,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: Int
)