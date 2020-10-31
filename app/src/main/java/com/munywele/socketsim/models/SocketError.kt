package com.munywele.socketsim.models

import com.google.gson.annotations.SerializedName

class SocketError(
    var errorStatus: Int,
    var errorType: String,
    var errorMessage: String,
)