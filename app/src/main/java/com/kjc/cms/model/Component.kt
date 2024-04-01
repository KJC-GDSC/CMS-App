package com.kjc.cms.model

import java.util.Date

data class Component (
    val Id: String ="",
    var AvailableQuantity: String ="",
    val Image: String="",
    var LastUpdated: String ="",
    var Model: String?="",
    val Name: String="",
    var Price: String ="",
    val Quantity: String ="",
)