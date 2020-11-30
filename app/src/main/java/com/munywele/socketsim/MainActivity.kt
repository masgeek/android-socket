package com.munywele.socketsim

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.munywele.socketsim.databinding.ActivityMainBinding
import com.munywele.socketsim.enums.EnumRequestType
import com.munywele.socketsim.enums.EnumTripStatus
import com.munywele.socketsim.models.Rider
import com.munywele.socketsim.models.SocketError
import com.munywele.socketsim.models.Trip
import com.munywele.socketsim.models.TripRequest
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val tag = MainActivity::class.java.simpleName

    private val userId: Long = 100;
    private val email = "barsamms@gmail.com"
    private val phone = "254713196504"
    private val socketRoom = "RIDER-100"

    lateinit var mSocket: Socket;

    private val gson: Gson = Gson()

//    private lateinit var driverAcceptRide: Button
//    private lateinit var driverRejectRide: Button
//    private lateinit var driverCancelRide: Button
//    private lateinit var driverEndTrip: Button
//    private lateinit var rideRequest: TextView
//    private lateinit var rideStatus: TextView

    private lateinit var tripRequest: TripRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        driverAcceptRide = binding.driverAcceptRide
//        driverRejectRide = binding.driverRejectRide
//        driverCancelRide = binding.driverCancelRide
//        driverEndTrip = binding.driverEndTrip
//        rideRequest = binding.rideRequest
//        rideStatus = binding.rideStatus

        tripRequest = TripRequest("1", "", EnumRequestType.ACCEPT_RIDE,  Trip())
        driverAcceptRide.setOnClickListener {
            updateTripStatus(EnumTripStatus.ACCEPTED)
        }
        driverRejectRide.setOnClickListener {
            updateTripStatus(EnumTripStatus.REJECTED)
        }

        try {
            val options = IO.Options()
//            mSocket = IO.socket("http://dev.api.garihub.com/api/socket-service/",options)
//            mSocket = IO.socket("http://dev.api.garihub.com/socket.io"
            mSocket = IO.socket("https://5bb537988dc9.ngrok.io")
            if (mSocket.id() != null) {
                Log.d("success", mSocket.id())
            }
            mSocket.connect()
            mSocket.on(Socket.EVENT_CONNECT, onConnect)
            mSocket.on("newRideRequest", onNewRideRequest)
            mSocket.on("rideUpdated", onRideUpdated)
            mSocket.on("gariHubErrorResp", onGariHubErrorResp)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("fail", "Failed to connect", e)
        }
    }


    var onConnect = Emitter.Listener {
        val data = Rider(userId, email, phone, email, socketRoom)
        val jsonData = gson.toJson(data)
        mSocket.emit("subscribe", jsonData)
    }

    var onNewRideRequest = Emitter.Listener { socketData ->
        val jsonData = socketData[0] as String
        tripRequest = gson.fromJson(jsonData, TripRequest::class.java)
        updateRiderRequest(tripRequest)
    }
    var onRideUpdated = Emitter.Listener { socketData ->
        val jsonData = socketData[0] as String
        tripRequest = gson.fromJson(jsonData, TripRequest::class.java)
        updateRideStatus(tripRequest)
    }


    var onGariHubErrorResp = Emitter.Listener { errorData ->
        val jsonData = errorData[0] as String
        val error = gson.fromJson(jsonData, SocketError::class.java)
        runOnUiThread {
            Toast.makeText(this, error.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateRiderRequest(tripRequest: TripRequest?) {
        //Since this function is inside of the listener,
        // You need to do it on UIThread!
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


    private fun updateRideStatus(tripRequest: TripRequest?) {
        runOnUiThread {
            if (tripRequest != null) {
                val info = StringBuilder()
                    .append(tripRequest.trip.tripStatus)
                    .toString()
                rideStatus.text = info
                toggleState(tripRequest.trip.tripStatus!!)
            }
        }
    }

    private fun toggleState(enumTripStatus: EnumTripStatus = EnumTripStatus.PENDING) {
        driverAcceptRide.isEnabled = enumTripStatus == EnumTripStatus.PENDING
        driverRejectRide.isEnabled = enumTripStatus == EnumTripStatus.PENDING
        driverStartTrip.isEnabled = enumTripStatus == EnumTripStatus.ACCEPTED
        driverCancelRide.isEnabled = enumTripStatus == EnumTripStatus.ACCEPTED
        riderCancelRide.isEnabled = enumTripStatus == EnumTripStatus.ACCEPTED
        driverEndTrip.isEnabled = enumTripStatus == EnumTripStatus.STARTED
    }

    private fun updateTripStatus(tripStatus: EnumTripStatus) {
        val data = tripRequest.trip
        data.tripStatus = tripStatus
        val jsonData = gson.toJson(data)
        mSocket.emit("updateTripStatus", jsonData)
    }

    override fun onDestroy() {
        super.onDestroy()
        val data = Rider(userId, email, phone, email, socketRoom)
        val jsonData = gson.toJson(data)
        mSocket.emit("unsubscribe", jsonData)
        mSocket.disconnect()
    }
}