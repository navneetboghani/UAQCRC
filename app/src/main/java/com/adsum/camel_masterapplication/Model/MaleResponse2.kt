package com.adsum.camel_masterapplication.Model

data class MaleResponse2(
    val `data`: List<Data>,
    val response: String,
    val status: Int
){
    data class Data(
        val rc_camel: String,
        val rc_gender: String,
        val rc_id: String,
        val rc_status: String,
        val user_id: String
    )
}