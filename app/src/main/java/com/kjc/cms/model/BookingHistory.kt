package com.kjc.cms.model

import java.util.Date

data class BookingHistory (
    val name : String,
    val Component : Map<String,String>,
    val EventName : String,
    val BookedOn: Date
)