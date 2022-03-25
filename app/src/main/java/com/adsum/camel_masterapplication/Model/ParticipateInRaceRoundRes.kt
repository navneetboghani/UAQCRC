package com.adsum.camel_masterapplication.Model

class ParticipateInRaceRoundRes : ArrayList<ParticipateInRaceRoundRes.ParticipateInRaceRoundResItem>() {
    data class ParticipateInRaceRoundResItem(
        val end_date: String,
        val id: String,
        val members: List<Member>,
        val no_of_round: String,
        val race_id: String,
        val race_name: String,
        val rounds: RoundsX,
        val start_date: String,
        val status: String,
        val unique_category: String
    ) {
        data class Member(
            val race_id: String,
            val rc_camel: String,
            val rc_gender: String,
            val rc_id: String,
            val rc_status: String,
            val rl_id: String,
            val rl_status: String,
            val rl_time: String,
            val round_id: String,
            val rp_id: String,
            val user: User,
            val user_id: String
        ) {
            data class User(
                val block_user: String,
                val camel_no: String,
                val id: String,
                val mobile_no: String,
                val name_of_participant: String,
                val role: String,
                val user_id: String
            )
        }
        data class RoundsX(
            val category: String,
            val customization: String,
            val description: String,
            val distance: String,
            val id: String,
            val price: String,
            val race_id: String,
            val round_name: String,
            val type: String
        )
    }
}