package com.munywele.socketsim.service

import android.telecom.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface UserService {
    @GET("v1/user/{id}")
    fun userDetail(@Path("id") id: Long): Call
}