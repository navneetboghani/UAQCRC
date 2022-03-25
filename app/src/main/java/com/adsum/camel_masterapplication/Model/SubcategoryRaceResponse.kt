package com.adsum.camel_masterapplication.Model

import com.google.gson.annotations.SerializedName

class SubcategoryRaceResponse : ArrayList<SubcategoryRaceResponse.SubcategoryRaceResponseItem>(){
    data class SubcategoryRaceResponseItem(
        @SerializedName("end_date")
        val endDate: Any?,
        @SerializedName("id")
        val id: String,
        @SerializedName("members")
        val members: List<Member>,
        @SerializedName("no_of_round")
        val noOfRound: String,
        @SerializedName("race_id")
        val raceId: String,
        @SerializedName("race_name")
        val raceName: String,
        @SerializedName("rounds")
        val rounds: Rounds,
        @SerializedName("start_date")
        val startDate: Any?,
        @SerializedName("status")
        val status: String
    ) {
        data class Member(
            @SerializedName("race_id")
            val raceId: String,
            @SerializedName("rc_camel")
            val rcCamel: String,
            @SerializedName("rc_gender")
            val rcGender: String,
            @SerializedName("rc_id")
            val rcId: String,
            @SerializedName("rc_status")
            val rcStatus: String,
            @SerializedName("rl_id")
            val rlId: String,
            @SerializedName("rl_time")
            val rlTime: String,
            @SerializedName("round_id")
            val roundId: String,
            @SerializedName("rp_id")
            val rpId: String,
            @SerializedName("user")
            val user: User,
            @SerializedName("user_id")
            val userId: String
        ) {
            data class User(
                @SerializedName("block_user")
                val blockUser: String,
                @SerializedName("id")
                val id: String,
                @SerializedName("mobile_no")
                val mobileNo: String,
                @SerializedName("name_of_participant")
                val nameOfParticipant: String,
                @SerializedName("user_id")
                val userId: String
            )
        }
    
        data class Rounds(
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