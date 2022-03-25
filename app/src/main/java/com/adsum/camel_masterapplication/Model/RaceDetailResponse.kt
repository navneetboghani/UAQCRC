package com.adsum.camel_masterapplication.Model


import com.google.gson.annotations.SerializedName


data class RaceDetailResponse(
    @SerializedName("data") val `data`: ArrayList<Data> = arrayListOf(),
    @SerializedName("response") val response: String = "",
    @SerializedName("status") val status: Int
){
    data class Data(
        @SerializedName("end_date") var endDate: String = "",
        @SerializedName("id") val id: String = "",
        @SerializedName("no_of_round") val noOfRound: String = "",
        @SerializedName("race_id") val raceId: String = "",
        @SerializedName("race_name") val raceName: String = "",
        @SerializedName("start_date") var startDate: String = "",
        @SerializedName("status") val status: String = "",
        @SerializedName("unique_category") val uniqueCategory: String = ""
    )
}