package com.hazem.alkateb.palliativecare.api_notification

import com.squareup.moshi.Json

data class CustomResponse(
    @Json(name = "multicast_id")
    val multicast_id:Int,
    @Json(name = "success")
    val success:Int,
    @Json(name = "failure")
    val failure:Int,
    @Json(name = "canonical_ids")
    val canonical_ids:Int,
)

data class TopicResponse(
    @Json(name= "message_id")
    val message_id:Int
)

