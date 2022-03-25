package com.adsum.camel_masterapplication.Model

import com.google.gson.annotations.SerializedName

class RaceResponse : ArrayList<RaceResponse.RaceResponseItem>(){
    data class RaceResponseItem(
        @SerializedName("end_date")
        val endDate: Any?,
        @SerializedName("id")
        val id: String,
        @SerializedName("no_of_round")
        val noOfRound: String,
        @SerializedName("race_id")
        val raceId: String,
        @SerializedName("race_name")
        val raceName: String,
        @SerializedName("rounds")
        val rounds: List<Round>,
        @SerializedName("start_date")
        val startDate: Any?,
        @SerializedName("status")
        val status: String
    ) {
        data class Round(
            @SerializedName("customization")
            val customization: String,
            @SerializedName("description")
            val description: String,
            @SerializedName("id")
            val id: String,
            @SerializedName("race_id")
            val raceId: String,
            @SerializedName("round_name")
            val roundName: String,
            @SerializedName("type")
            val type: String
        )
    }
}