package com.munywele.socketsim.models

import com.google.gson.annotations.SerializedName
import com.munywele.socketsim.EnumTripStatus

class Trip {
    @SerializedName("id")
    var id: Long? = null

    @SerializedName("driverId")
    var driverId: Long? = null

    @SerializedName("riderId")
    var riderId: Long? = null

    @SerializedName("vehicleId")
    var vehicleId: Long? = null

    @SerializedName("licensePlate")
    var licensePlate: String? = null

    @SerializedName("pickupLat")
    var pickupLat: String? = null

    @SerializedName("pickupLon")
    var pickupLon: String? = null

    @SerializedName("pickupAddress")
    var pickupAddress: String? = null

    @SerializedName("dropOffLat")
    var dropOffLat: String? = null

    @SerializedName("dropOffLon")
    var dropOffLon: String? = null

    @SerializedName("dropOffAddress")
    var dropOffAddress: String? = null

    @SerializedName("tripDistance")
    var tripDistance: Double? = null

    @SerializedName("tripDuration")
    var tripDuration: Int? = null

    @SerializedName("currency")
    var currency: String? = null

    @SerializedName("paymentMode")
    var paymentMode: String? = null

    @SerializedName("tripStatus")
    var tripStatus: EnumTripStatus? = null
}