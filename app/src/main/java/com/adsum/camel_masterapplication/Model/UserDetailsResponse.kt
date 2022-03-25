package com.adsum.camel_masterapplication.Model

data class UserDetailsResponse(
    val `data`: ArrayList<Data>,
    val response: String,
    val status: Int
){
    data class Data(
        val ID: String,
        var block_user: String,
        val camel_no: String,
        val id: String,
        val mobile_no: String,
        val name_of_participant: String,
        val role: String,
        val user_email: String,
        val user_id: String,
        val user_login: String,
        var subscription:String
    )
}