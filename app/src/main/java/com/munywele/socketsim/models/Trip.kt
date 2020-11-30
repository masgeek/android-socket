package com.munywele.socketsim.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.munywele.socketsim.enums.EnumTripStatus


class Trip {
    @SerializedName("id")
    var id: Long? = null

    @SerializedName("driverId")
    var driverId: String? = null

    @SerializedName("riderId")
    var riderId: String? = null

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
    var tripDuration: Long? = null

    @SerializedName("currency")
    var currency: String? = null

    @SerializedName("paymentMode")
    var paymentMode: String? = null

    @SerializedName("tripCost")
    var tripCost: Long? = null

    @SerializedName("tripStatus")
    var tripStatus: EnumTripStatus? = null

    @SerializedName("createdAt")
    var createdAt: String? = null

    @SerializedName("updatedAt")
    var updatedAt: String? = null
}