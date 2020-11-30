package com.munywele.socketsim.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Vehicle {
    @SerializedName("id")
    var id: Long? = null

    @SerializedName("driverId")
    var driverId: String? = null

    @SerializedName("regNo")
    var regNo: String? = null

    @SerializedName("vehicleMake")
    var vehicleMake: String? = null

    @SerializedName("vehicleModel")
    var vehicleModel: String? = null

    @SerializedName("vehicleColor")
    var vehicleColor: String? = null

    @SerializedName("vehicleCapacity")
    var vehicleCapacity: Long? = null

    @SerializedName("vehicleType")
    var vehicleType: String? = null

    @SerializedName("vehicleLat")
    var vehicleLat: String? = null

    @SerializedName("vehicleLon")
    var vehicleLon: String? = null

    @SerializedName("vehicleBearing")
    var vehicleBearing: String? = null

    @SerializedName("onTrip")
    var onTrip: Boolean? = null
}