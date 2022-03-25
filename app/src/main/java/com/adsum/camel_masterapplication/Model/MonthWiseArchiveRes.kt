package com.adsum.camel_masterapplication.Model

data class MonthWiseArchiveRes(
    val `data`: ArrayList<DataX>,
    val response: String,
    val status: Int
) {
    data class DataX(
        val end_date: String,
        val id: String,
        val no_of_round: String,
        val race_id: String,
        val race_name: String,
        val rounds: List<Round>,
        val start_date: String,
        val status: String,
        val unique_category: String
    ) {
        data class Round(
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