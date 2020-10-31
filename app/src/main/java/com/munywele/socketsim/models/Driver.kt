package com.munywele.socketsim.models

import com.google.gson.annotations.SerializedName

class Driver {
    @SerializedName("id")
    var id: Long? = null

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
}