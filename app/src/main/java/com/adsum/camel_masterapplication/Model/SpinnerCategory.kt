package com.adsum.camel_masterapplication.Model

data class SpinnerCategory(
    val `data`: List<Data>,
    val response: String,
    val status: Int
){
    data class Data(
        val category_id: String,
        val category_name: String
    )
}