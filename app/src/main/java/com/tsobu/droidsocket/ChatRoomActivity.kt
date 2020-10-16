package com.tsobu.droidsocket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.tsobu.droidsocket.model.InitialData
import com.tsobu.droidsocket.model.MessageType
import com.tsobu.droidsocket.model.RequestMessage
import com.tsobu.droidsocket.model.SendMessage
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter

class ChatRoomActivity : AppCompatActivity(), View.OnClickListener {

    val TAG = ChatRoomActivity::class.java.simpleName

    lateinit var mSocket: Socket;
    lateinit var userName: String;
    lateinit var roomName: String;

    val gson: Gson = Gson()

    //For setting the recyclerView.
    val chatList: ArrayList<RequestMessage> = arrayListOf();
    lateinit var chatRoomAdapter: ChatRoomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
//        send.setOnClickListener(this)
//        leave.setOnClickListener(this)

        chatRoomAdapter = ChatRoomAdapter(this, chatList);
//        recyclerView.adapter = chatRoomAdapter;

        try {
            mSocket = IO.socket("http://10.0.2.2:3000")
            Log.d("success", mSocket.id())

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("fail", "Failed to connect", e)
        }

        mSocket.connect()
        mSocket.on(Socket.EVENT_CONNECT, onConnect)
        mSocket.on("newUserToChatRoom", onNewUser)
        mSocket.on("updateChat", onUpdateChat)
        mSocket.on("userLeftChatRoom", onUserLeft)
    }

    var onUserLeft = Emitter.Listener {
        val leftUserName = it[0] as String
        val chat = RequestMessage(leftUserName, "", "", MessageType.USER_LEAVE.index)
//        addItemToRecyclerView(chat)
    }

    var onUpdateChat = Emitter.Listener {
        val chat = gson.fromJson(it[0].toString(), RequestMessage::class.java)
        chat.viewType = MessageType.CHAT_PARTNER.index
//        addItemToRecyclerView(chat)
    }

    var onConnect = Emitter.Listener {
        val data = InitialData(userName, roomName)
        val jsonData = gson.toJson(data)
        mSocket.emit("subscribe", jsonData)

    }

    var onNewUser = Emitter.Listener {
        val name = it[0] as String //This pass the userName!
        val chat = RequestMessage(name, "", roomName, MessageType.USER_JOIN.index)
//        addItemToRecyclerView(chat)
        Log.d(TAG, "on New User triggered.")
    }

    private fun sendMessage() {
        val content = "Content was here" //editText.text.toString()
        val sendData = SendMessage(userName, content, roomName)
        val jsonData = gson.toJson(sendData)
        mSocket.emit("newMessage", jsonData)

        val message = RequestMessage(userName, content, roomName, MessageType.CHAT_MINE.index)
//        addItemToRecyclerView(message)
    }

    private fun addItemToRecyclerView(message: RequestMessage) {

        //Since this function is inside of the listener,
        // You need to do it on UIThread!
        runOnUiThread {
            chatList.add(message)
            chatRoomAdapter.notifyItemInserted(chatList.size)
//            editText.setText("")
//            recyclerView.scrollToPosition(chatList.size - 1) //move focus on last message
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