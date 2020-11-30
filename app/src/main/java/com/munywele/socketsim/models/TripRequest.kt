package com.munywele.socketsim.models

import com.google.gson.annotations.SerializedName
import com.munywele.socketsim.enums.EnumRequestType

data class TripRequest(
    @SerializedName("to")
    var to: String,
    @SerializedName("from")
    var from: String,
    @SerializedName("requestType")
    var requestType: EnumRequestType,
    @SerializedName("trip")
    var trip: Trip,
    @SerializedName("vehicle")
    var vehicle: Vehicle? = null,
    @SerializedName("driver")
    var driver: Driver? = null,
)