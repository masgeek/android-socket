package com.tsobu.droidsocket

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket
import java.net.UnknownHostException


class MainActivity : AppCompatActivity() {

    private lateinit var mSocket: Socket
    private val SERVER_PORT = 5000
    private val SERVER_IP = "10.0.2.2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun onClick(view: View) {
        try {
            val et = findViewById<View>(R.id.message_input) as EditText
            val str = et.text.toString()
            val out = PrintWriter(
                BufferedWriter(OutputStreamWriter(mSocket.getOutputStream())), true
            )
            out.println(str)
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}