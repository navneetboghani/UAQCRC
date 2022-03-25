package com.adsum.camel_masterapplication.Model

data class ArchiveData(
    val `data`: List<Data>,
    val response: String,
    val status: Int
){
    data class Data(
        val month: Int,
        val year: Int,
    )
}