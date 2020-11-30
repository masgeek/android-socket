package com.munywele.socketsim.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Driver {
    @SerializedName("firstName")
    var firstName: String? = null

    @SerializedName("lastName")
    var lastName: String? = null

    @SerializedName("gender")
    var gender: String? = null

    @SerializedName("phoneNumber")
    var phoneNumber: String? = null

    @SerializedName("emailAddress")
    var emailAddress: String? = null

    @SerializedName("emailStatus")
    var emailStatus: String? = null

    @SerializedName("accountStatus")
    var accountStatus: String? = null

    @SerializedName("onDuty")
    var onDuty: Boolean? = null

    @SerializedName("identifier")
    var identifier: String? = null

    @SerializedName("rating")
    var rating: Double? = null

    @SerializedName("fcmToken")
    var fcmToken: String? = null
}