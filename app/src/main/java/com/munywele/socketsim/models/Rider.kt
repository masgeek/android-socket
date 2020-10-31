package com.munywele.socketsim.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Rider(
    @SerializedName("id")
    val id: Long,
    @SerializedName("emailAddress")
    val emailAddress: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("socketRoom")
    val socketRoom: String
) {
    @SerializedName("firstName")
    var firstName: String? = null

    @SerializedName("lastName")
    var lastName: String? = null

    @SerializedName("gender")
    @Expose
    var gender: String? = null

    @SerializedName("rating")
    var rating: Double? = 0.0
}