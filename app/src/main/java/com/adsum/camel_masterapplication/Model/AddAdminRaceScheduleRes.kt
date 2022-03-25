package com.adsum.camel_masterapplication.Model

data class AddAdminRaceScheduleRes(
    val `data`: DataX,
    val response: String,
    val status: Int
){
    data class DataX(
        val race_scedule_id: Int
    )
}