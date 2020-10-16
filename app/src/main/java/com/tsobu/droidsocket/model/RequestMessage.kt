package com.tsobu.droidsocket.model

data class RequestMessage(
    val userName: String,
    val messageContent: String,
    val roomName: String,
    var viewType: Int
)