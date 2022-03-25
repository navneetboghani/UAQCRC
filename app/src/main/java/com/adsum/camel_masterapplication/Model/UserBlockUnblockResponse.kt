package com.adsum.camel_masterapplication.Model

import com.google.gson.annotations.SerializedName

data class UserBlockUnblockResponse(
    val `data`: Data,
    val response: String,
    val status: Int)
{
    data class Data(
        val user_id: String
    )
}