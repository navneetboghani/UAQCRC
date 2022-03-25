package com.adsum.camel_masterapplication.Model

import com.google.gson.annotations.SerializedName

data class DeleteRaceDetailResponse(
    @SerializedName("response")
    val response: String,
    @SerializedName("status")
    val status: Int
)
