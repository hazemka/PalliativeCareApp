package com.hazem.alkateb.palliativecare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.hazem.alkateb.palliativecare.api_notification.ApiClient
import com.hazem.alkateb.palliativecare.api_notification.ApiRepository
import com.hazem.alkateb.palliativecare.api_notification.CustomResponse
import com.hazem.alkateb.palliativecare.api_notification.Data
import com.hazem.alkateb.palliativecare.api_notification.NotificationBody
import com.hazem.alkateb.palliativecare.api_notification.TopicResponse
import com.hazem.alkateb.palliativecare.databinding.ActivityChatBinding
import com.hazem.alkateb.palliativecare.model.MessageModel
import com.hazem.alkateb.palliativecare.patient.ui.chat.MessageAdapter
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receiverId = intent.getStringExtra("receiverId")
        val receiverName = intent.getStringExtra("receiverName")
        val title = intent.getStringExtra("title")
        binding.txtRecivierName.text = receiverName

        FirebaseFirestore.getInstance().collection("users")
            .document(receiverId!!)
            .get()
            .addOnSuccessListener {
                if (it.getString("photo")!!.isNotEmpty()){
                    Picasso.get().load(it.getString("photo")!!).placeholder(R.drawable.user_image).into(binding.imageView5)
                }
            }

        val sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE)
        val senderId = sharedPreferences.getString("userId","")
        val senderName = "${sharedPreferences.getString("userName","")} ${sharedPreferences.getString("secondName","")} ${sharedPreferences.getString("familyName","")}"

        val senderRoom = "$senderId$receiverId"
        val receiverRoom = "$receiverId$senderId"

        val databaseReferenceSender = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom)
        val databaseReferenceReceiver = FirebaseDatabase.getInstance().getReference("chats").child(receiverRoom)

        val messageAdapter = MessageAdapter(this)
        binding.rvMessages.adapter = messageAdapter
        binding.rvMessages.layoutManager = LinearLayoutManager(this)

        databaseReferenceSender.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageAdapter.clear()
                for (dataSnapshot in snapshot.children){
                    val meassage = dataSnapshot.getValue<MessageModel>()
                    messageAdapter.add(meassage!!)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        binding.btnSendMessage.setOnClickListener {
            if (binding.messageBox.text.isNotEmpty()){
                val messageId = UUID.randomUUID().toString()
                val message = MessageModel(messageId,senderId,binding.messageBox.text.toString(),
                    System.currentTimeMillis().toInt(),senderName,receiverName,receiverId,title
                )
                messageAdapter.add(message)
                databaseReferenceSender.child(messageId).setValue(message)
                databaseReferenceReceiver.child(messageId).setValue(message)
                binding.messageBox.text.clear()
//                sendNotification(,senderName)
            }
        }
    }

    private fun sendNotification(receiverToken:String,senderName:String){
        val repository: ApiRepository = ApiRepository(ApiClient.apiService)
        MainScope().launch(Dispatchers.IO){
            try{
                val call = repository.sendCustomNotification(NotificationBody(receiverToken, Data("لديك رسالة جديدة من ${senderName}","لديك رسالة جديدة")))
                call.enqueue(object : Callback<CustomResponse> {
                    override fun onResponse(
                        call: Call<CustomResponse>,
                        response: Response<CustomResponse>
                    ) {
                        Log.e("hzm", "onResponse Code: ${response.code()}")
                        Log.e("hzm", "onResponse: ${response.body().toString()}")
                    }

                    override fun onFailure(call: Call<CustomResponse>, t: Throwable) {
                        Log.e("hzm", "Error")
                    }


                })
            }catch (e:Exception){
                Log.e("hzm", "Error" +
                        "${e.message}")
            }
        }
    }
}