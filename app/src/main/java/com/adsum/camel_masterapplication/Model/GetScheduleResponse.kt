package com.adsum.camel_masterapplication.Model

data class GetScheduleResponse(
    val `data`: List<Data>,
    val response: String,
    val status: Int
) {
    data class Data(
        val rs_id: String,
        val rs_image: String
    )
}