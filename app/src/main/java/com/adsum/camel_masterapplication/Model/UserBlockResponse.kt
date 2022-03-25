package com.adsum.camel_masterapplication.Model

data class UserBlockResponse(
    val `data`: ArrayList<Data>,
    val response: String,
    val status: Int)
{
    data class Data(
        val user_id: String
    )
}