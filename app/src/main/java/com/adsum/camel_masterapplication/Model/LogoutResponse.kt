package com.adsum.camel_masterapplication.Model

import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
)