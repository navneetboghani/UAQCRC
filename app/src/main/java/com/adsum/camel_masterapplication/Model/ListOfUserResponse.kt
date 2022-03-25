package com.adsum.camel_masterapplication.Model


import com.google.gson.annotations.SerializedName


data class ListOfUserResponse(
    @SerializedName("data") val `data`: ArrayList<Data> = arrayListOf(),
    @SerializedName("response") val response: String = "",
    @SerializedName("status") val status: Int
){
    data class Data(
        @SerializedName("name_of_participant") val nameOfParticipant: String = "",
        var ischecked:Boolean = false,
        @SerializedName("user_id") val userId: String = "",
        //@SerializedName("block_user") val blockUser: String = "",
      //  @SerializedName("camel_no") val camelNo: String = "",
      //  @SerializedName("id") val id: String = "",
      // @SerializedName("mobile_no") val mobileNo: String = "",

      //  @SerializedName("role") val role: String = "",


    )
}