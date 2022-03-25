package com.adsum.camel_masterapplication.Model

data class updateAdminRaceSchedul(
    val `data`: DataXX,
    val response: String,
    val status: Int
){
    data class DataXX(
        val race_scedule_id: String
    )
}