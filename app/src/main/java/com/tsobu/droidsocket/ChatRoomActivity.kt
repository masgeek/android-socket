package com.tsobu.droidsocket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.tsobu.droidsocket.databinding.ActivityChatRoomBinding
import com.tsobu.droidsocket.model.InitialData
import com.tsobu.droidsocket.model.MessageType
import com.tsobu.droidsocket.model.RequestMessage
import com.tsobu.droidsocket.model.SendMessage
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_chat_room.*

class ChatRoomActivity : AppCompatActivity(), View.OnClickListener {

    val tag = ChatRoomActivity::class.java.simpleName


    lateinit var mSocket: Socket;
    lateinit var userName: String;
    lateinit var roomName: String;

    val gson: Gson = Gson()

    //For setting the recyclerView.
    val chatList: ArrayList<RequestMessage> = arrayListOf();
    lateinit var chatRoomAdapter: ChatRoomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityChatRoomBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val send = binding.send
        val leave = binding.leave

        send.setOnClickListener(this)
        leave.setOnClickListener(this)

        chatRoomAdapter = ChatRoomAdapter(this, chatList);
        recyclerView.adapter = chatRoomAdapter;

        userName = "barsamms@gmail.com"
        roomName = "104"
        try {
            mSocket = IO.socket("https://ac0791203c73.ngrok.io")
            if (mSocket.id() != null) {
                Log.d("success", mSocket.id())
            }
            mSocket.connect()
            mSocket.on(Socket.EVENT_CONNECT, onConnect)
            mSocket.on("driverMatchedResp", onDriverMatched)
            mSocket.on("updateChat", onUpdateChat)
            mSocket.on("userLeftChatRoom", onUserLeft)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("fail", "Failed to connect", e)
        }
    }

    var onUserLeft = Emitter.Listener {
        val leftUserName = it[0] as String
        val chat = RequestMessage(leftUserName, "", "", MessageType.USER_LEAVE.index)
        addItemToRecyclerView(chat)
    }

    var onUpdateChat = Emitter.Listener {
        val chat = gson.fromJson(it[0].toString(), RequestMessage::class.java)
        chat.viewType = MessageType.CHAT_PARTNER.index
        addItemToRecyclerView(chat)
    }

    var onConnect = Emitter.Listener {
        val data = InitialData(userName, roomName)
        val jsonData = gson.toJson(data)
        mSocket.emit("subscribe", jsonData)
    }

    var onDriverMatched = Emitter.Listener { socketData ->
        val name = socketData
//        val chat = RequestMessage(name, "", roomName, MessageType.USER_JOIN.index)
//        addItemToRecyclerView(chat)
        Log.d(tag, "on New User triggered.")
    }

    private fun sendMessage() {
        val content = editText.text.toString()
        val sendData = SendMessage(userName, content, roomName)
        val jsonData = gson.toJson(sendData)
        mSocket.emit("newMessage", jsonData)

        val message = RequestMessage(userName, content, roomName, MessageType.CHAT_MINE.index)
        addItemToRecyclerView(message)
    }

    private fun addItemToRecyclerView(requestMessage: RequestMessage) {

        //Since this function is inside of the listener,
        // You need to do it on UIThread!
        runOnUiThread {
            chatList.add(requestMessage)
            chatRoomAdapter.notifyItemInserted(chatList.size)
            editText.setText("")
            recyclerView.scrollToPosition(chatList.size - 1) //move focus on last message
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val data = InitialData(userName, roomName)
        val jsonData = gson.toJson(data)
        mSocket.emit("unsubscribe", jsonData)
        mSocket.disconnect()
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.send -> sendMessage()
            R.id.leave -> onDestroy()
        }
    }
}