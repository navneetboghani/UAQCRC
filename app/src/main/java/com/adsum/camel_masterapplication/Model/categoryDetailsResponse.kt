package com.adsum.camel_masterapplication.Model

import com.google.gson.annotations.SerializedName

//data class categoryDetailsResponse(
//    @SerializedName("data")
//    val `data`: List<Data>,
//    @SerializedName("msg")
//    val msg: String,
//    @SerializedName("status")
//    val status: Int
//
//){ data class Data(
//    @SerializedName("round_name")
//    val roundname: String,
//    @SerializedName("type")
//    val type : String,
//    @SerializedName("description")
//    val description : String,
//    @SerializedName("distance")
//    val distance : String,
//    @SerializedName("price")
//    val price : String,
//    @SerializedName("category")
//    val category : String,
//    @SerializedName("noofparticipants")
//    val noofparticipants : Int,
//    @SerializedName("no_of_participants")
//    val no_of_participants : List<participants>
//){
//    data class participants(
//        @SerializedName("rl_id")
//        val rl_id: String,
//        @SerializedName("rp_id")
//        val rp_id: String,
//        @SerializedName("user_id")
//        val user_id: String,
//        @SerializedName("rl_time")
//        val rl_time: String,
//        @SerializedName("race_id")
//        val race_id: String,
//        @SerializedName("round_id")
//        val round_id: String,
//        @SerializedName("rl_status")
//        val rl_status: String,
//
//    )
//}
class categoryDetailsResponse : ArrayList<categoryDetailsResponse.Data>() {
    data class Data(
        val category: String,
        val description: String,
        val distance: String,
        val no_of_participants: List<categoryDetailsResponse2.Data.NoOfParticipant>,
        val noofparticipants: Int,
        val price: String,
        val race_id: String,
        val race_name: String,
        val round_name: String,
        val type: String
    ) {
        data class Data(
            val category: String,
            val description: String,
            val distance: String,
            val no_of_participants: List<categoryDetailsResponse2.Data.NoOfParticipant>,
            val noofparticipants: Int,
            val price: String,
            val race_id: String,
            val race_name: String,
            val round_name: String,
            val type: String
        ) {
            data class NoOfParticipant(
                val race_id: String,
                val rl_id: String,
                val rl_status: String,
                val rl_time: String,
                val round_id: String,
                val rp_id: String,
                val user_id: String
            )
        }
    }
}

