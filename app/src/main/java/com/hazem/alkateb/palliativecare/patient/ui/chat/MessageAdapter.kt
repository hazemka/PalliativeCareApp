package com.hazem.alkateb.palliativecare.patient.ui.chat

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.databinding.MessageDesignBinding
import com.hazem.alkateb.palliativecare.model.MessageModel
import java.util.*
import kotlin.collections.ArrayList

class MessageAdapter(var context: Context) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    var data = ArrayList<MessageModel>()

    class MessageViewHolder(var binding: MessageDesignBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {

        val binding = MessageDesignBinding.inflate(LayoutInflater.from(context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.binding.txtMeassge.text = data[position].message
        val sharedPreferences = context.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        val senderId = sharedPreferences.getString("userId","")
        if (data[position].senderId == senderId){
            holder.binding.layoutMessage.setBackgroundResource(R.drawable.round_button)
            holder.binding.txtMeassge.setTextColor(context.getColor(R.color.black))
        }else{
            holder.binding.layoutMessage.setBackgroundResource(R.drawable.round_button_solid)
            holder.binding.txtMeassge.setTextColor(context.getColor(R.color.white))
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun add(messageModel: MessageModel){
        data.add(messageModel)
        val sortedData = data.sortedWith(compareBy { it.time })
        data = ArrayList(sortedData)
        notifyDataSetChanged()
    }

    fun clear(){
        data.clear()
        notifyDataSetChanged()
    }
}