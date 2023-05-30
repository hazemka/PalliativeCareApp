package com.hazem.alkateb.palliativecare.patient.ui.chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.hazem.alkateb.palliativecare.ChatActivity
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.databinding.DoctorCardBinding
import com.hazem.alkateb.palliativecare.model.MessageModel
import com.squareup.picasso.Picasso

class ChatContactAdapter(var context: Context, var data: ArrayList<MessageModel>, var userType:String) :
    RecyclerView.Adapter<ChatContactAdapter.ChatViewHolder>() {

    class ChatViewHolder(var binding: DoctorCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = DoctorCardBinding.inflate(LayoutInflater.from(context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        getUserImage(data[position].senderId!!,holder)
        if (userType == "patient")
            holder.binding.userName.text = data[position].senderName
        else
            holder.binding.userName.text = data[position].senderName
        holder.binding.root.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            if (userType == "patient"){
                intent.putExtra("receiverId",data[position].senderId)
                intent.putExtra("receiverName",data[position].senderName)
            } else{
                intent.putExtra("receiverId",data[position].senderId)
                intent.putExtra("receiverName",data[position].senderName)
            }
            intent.putExtra("title",data[position].title)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun getUserImage(userId:String,holder: ChatViewHolder){
        FirebaseFirestore.getInstance().collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener {
                if (it.getString("photo")!!.isNotEmpty()){
                    Picasso.get().load(it.getString("photo")!!).placeholder(R.drawable.user_image).into(holder.binding.userImage)
                }
            }
    }
}