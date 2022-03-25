package com.adsum.camel_masterapplication.Model

data class logoutResponce(
    val `data`: Data,
    val response: String,
    val status: Int
){
    data class Data(
        val user_id: String
    )
}