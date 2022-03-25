package com.adsum.camel_masterapplication.Model

import com.google.gson.annotations.SerializedName

data class ResponseProfile(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("status")
    val status: String
) { data class Data(
    @SerializedName("block_user")
    val blockUser: String,
    @SerializedName("count")
    val count: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("mobile_no")
    val mobileNo: String,
    @SerializedName("name_of_participant")
    val nameOfParticipant: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("camel_no")
    val camelno: String
)
}
// {
//    data class Data(
//        @SerializedName("user")
//        val user: User
//    ) {
//        data class User(
//            @SerializedName("block_user")
//            val blockUser: String,
//            @SerializedName("count")
//            val count: Int,
//            @SerializedName("email")
//            val email: String,
//            @SerializedName("image")
//            val image: String,
//            @SerializedName("mobile_no")
//            val mobileNo: String,
//            @SerializedName("name_of_participant")
//            val nameOfParticipant: String,
//            @SerializedName("user_id")
//            val userId: String,
//            @SerializedName("user_name")
//            val userName: String,
//            @SerializedName("camel_no")
//            val camelNo: String
//
//
//
//        )
//    }
//}