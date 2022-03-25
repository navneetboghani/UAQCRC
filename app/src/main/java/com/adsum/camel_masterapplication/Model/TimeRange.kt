package com.adsum.camel_masterapplication.Model

import com.google.gson.annotations.SerializedName

data class TimeRange(
    var start: TimeValue? =TimeValue(),
    var end: TimeValue?
){
    data class TimeValue(
        var hour: Int=0,
        var minute: Int=0
    )
}
