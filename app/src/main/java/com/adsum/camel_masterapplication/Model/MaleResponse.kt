package com.adsum.camel_masterapplication.Model

import com.google.gson.annotations.SerializedName

class MaleResponse : ArrayList<MaleResponse.MaleResponseItem>()
{
    data class MaleResponseItem(
        @SerializedName("rc_camel")
        val rcCamel: String,
        @SerializedName("rc_gender")
        val rcGender: String,
        @SerializedName("rc_id")
        val rcId: String,
        @SerializedName("rc_status")
        val rcStatus: String,
        @SerializedName("user_id")
        val userId: String
    )
}