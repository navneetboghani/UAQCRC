package com.adsum.camel_masterapplication.Model

data class AddRaceDetailRes(
    val `data`: Data,
    val response: String,
    val status: Int
) {
    data class Data(
        val race_id: Int
    )
}