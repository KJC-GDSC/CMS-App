package com.kjc.cms.model

data class AboutData (
    val title : String?="",
    val tech : ArrayList<String> = arrayListOf(""),
    val contributor : ArrayList<String> = arrayListOf("")
)