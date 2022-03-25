package com.adsum.camel_masterapplication.Model

data class TermConditionResponse(
    val `data`: List<Data>,
    val response: String,
    val status: Int
){
    data class Data(
        val tc_desc: String,
        val tc_id: String,
        val tc_title: String
    )
}