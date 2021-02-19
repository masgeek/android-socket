package com.munywele.socketsim

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.munywele.socketsim.databinding.ActivityMainBinding
import com.munywele.socketsim.enums.EnumRequestType
import com.munywele.socketsim.enums.EnumTripStatus
import com.munywele.socketsim.models.TripRequest
import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketAdapter
import com.neovisionaries.ws.client.WebSocketFactory
import com.neovisionaries.ws.client.WebSocketFrame
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class WsSocketActivity : AppCompatActivity() {

    private val userId: String = "26f911a266a573400aca54f90314ce4a"

        private val socketUrl = "ws://dev.garihub.com/api/socket-service"
//    private val socketUrl = "ws://40ba1400781a.ngrok.io"
    private lateinit var ws: WebSocket
    private lateinit var tripRequest: TripRequest
    private val gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val es: ExecutorService = Executors.newSingleThreadExecutor()

        reconnect.setOnClickListener {
            connectToSocket(es)
        }

        driverAcceptRide.setOnClickListener {
            updateTripStatus(EnumTripStatus.ACCEPTED, EnumRequestType.ACCEPT_RIDE)
        }

        driverRejectRide.setOnClickListener {
            updateTripStatus(EnumTripStatus.REJECTED, EnumRequestType.REJECT_RIDE)
        }

        connectToSocket(es)
    }

    private fun connectToSocket(es: ExecutorService) {
        try {
            val factory = WebSocketFactory()
            ws = factory.createSocket(socketUrl)
//            ws.addHeader("client-id", userId)
            ws.addListener(object : WebSocketAdapter() {
                @Throws(Exception::class)
                override fun onTextMessage(websocket: WebSocket, jsonData: String) {
                    tripRequest = gson.fromJson(jsonData, TripRequest::class.java)
                    updateRiderRequest(tripRequest)
                }

                @Throws(Exception::class)
                override fun onConnected(websocket: WebSocket, headers: Map<String, List<String>>) {
                    super.onConnected(websocket, headers)
                    runOnUiThread {
                        reconnect.isEnabled = false
                    }
                }

                override fun onDisconnected(
                    websocket: WebSocket?,
                    serverCloseFrame: WebSocketFrame?,
                    clientCloseFrame: WebSocketFrame?,
                    closedByServer: Boolean
                ) {
                    super.onDisconnected(
                        websocket,
                        serverCloseFrame,
                        clientCloseFrame,
                        closedByServer
                    )
                    runOnUiThread {
                        reconnect.isEnabled = true
                    }
                }
            })
            ws.connect(es)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("fail", "Failed to connect", e)
        }
    }

    private fun updateTripStatus(tripStatus: EnumTripStatus, requestType: EnumRequestType) {
        val data = tripRequest
        data.requestType = requestType
        data.trip.tripStatus = tripStatus
        
        val jsonData = gson.toJson(data)
        ws.sendText(jsonData)
    }

    private fun updateRiderRequest(tripRequest: TripRequest?) {
        //Since this function is inside of the listener, You need to do it on UIThread!
        runOnUiThread {
            if (tripRequest != null) {
                val info = StringBuilder()
                    .append("Ride request from ")
                    .append(tripRequest.trip.riderId)
                    .append("\nFrom ")
                    .append(tripRequest.trip.pickupAddress)
                    .append(" To ")
                    .append(tripRequest.trip.dropOffAddress)
                    .toString()
                rideRequest.text = info


                toggleState(tripRequest.trip.tripStatus!!)
            }
        }
    }

    private fun toggleState(enumTripStatus: EnumTripStatus = EnumTripStatus.PENDING) {
        riderCancelRide.isEnabled = enumTripStatus == EnumTripStatus.ACCEPTED

        driverAcceptRide.isEnabled = enumTripStatus == EnumTripStatus.PENDING
        driverRejectRide.isEnabled = enumTripStatus == EnumTripStatus.PENDING
        driverStartTrip.isEnabled = enumTripStatus == EnumTripStatus.ACCEPTED
        driverCancelRide.isEnabled = enumTripStatus == EnumTripStatus.ACCEPTED
        driverEndTrip.isEnabled = enumTripStatus == EnumTripStatus.STARTED
    }

}