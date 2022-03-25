package com.adsum.camel_masterapplication.Model

import com.google.gson.annotations.SerializedName

data class AddRoundMemberResponse(
    @SerializedName("inserted_id")
    val insertedId: Int,
    @SerializedName("response")
    val response: String,
    @SerializedName("status")
    val status: Int
)