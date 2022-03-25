package com.adsum.camel_masterapplication.Model


import com.google.gson.annotations.SerializedName


data class SubRaceDetailResponse(
    @SerializedName("data") val `data`: List<Data> ,
    @SerializedName("response") val response: String = "",
    @SerializedName("status") val status: Int
){
    data class Data(
        //@SerializedName("category") val category: String = "",
       // @SerializedName("distance") val distance: String = "",
       // @SerializedName("id") val id: String = "",
       // @SerializedName("price") val price: String = "",
        @SerializedName("race_id") val raceId: String = "",
        @SerializedName("round_name") val roundName: String = "",
        @SerializedName("type") val type: String = "",
        @SerializedName("customization") val customization: String = "",
        @SerializedName("description") val description: String = ""

    )
}