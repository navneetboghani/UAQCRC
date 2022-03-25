package com.adsum.camel_masterapplication.Model

data class categoryDetailsResponse3(
    val `data`: List<Data>,
    val response: String,
    val status: Int
){
    data class Data(
        val end_date: String,
        val id: String,
        val no_of_round: String,
        val race_id: String,
        val race_name: String,
        val round: List<Round>,
        val start_date: String,
        val status: String,
        val unique_category: String
    ){
        data class Round(
            val category: String,
            val description: String,
            val distance: String,
            val no_of_participants: List<NoOfParticipant>,
            val noofparticipants: Int,
            val price: String,
            val race_id: String,
            val race_name: String,
            val round_id: String,
            val round_name: String,
            val type: String
        ){
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