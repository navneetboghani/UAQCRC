package com.adsum.camel_masterapplication.Model


import com.google.gson.annotations.SerializedName

data class CategoryNameResponse(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: Int

){ data class Data(
    @SerializedName("category_id")
    val id: Int,
    @SerializedName("category_name")
    val categoryName : String
)
}