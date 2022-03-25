package com.adsum.camel_masterapplication.Model

import com.google.gson.annotations.SerializedName

data class AddCamelResponse(
    @SerializedName("camel_id")
    val camelId: Int,
    @SerializedName("response")
    val response: String,
    @SerializedName("status")
    val status: Int
)