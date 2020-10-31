package com.munywele.socketsim.models

import com.google.gson.annotations.SerializedName

data class TripRequest(
    @SerializedName("riderId")
    var riderId: String,
    @SerializedName("rider")
    var rider: Rider? = null,
    @SerializedName("vehicle")
    var vehicle: Vehicle? = null,
    @SerializedName("driver")
    var driver: Driver? = null,
    @SerializedName("trip")
    var trip: Trip,
)