package com.hazem.alkateb.palliativecare.doctor.ui.chat_doctor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.devhoony.lottieproegressdialog.LottieProgressDialog
import com.google.firebase.database.FirebaseDatabase
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.databinding.FragmentChatDoctorBinding
import com.hazem.alkateb.palliativecare.model.MessageModel
import com.hazem.alkateb.palliativecare.patient.ui.chat.ChatContactAdapter

class ChatDoctorFragment : Fragment() {

    private lateinit var dialog: LottieProgressDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentChatDoctorBinding.inflate(inflater, container, false)

        dialog = LottieProgressDialog(requireContext(), false, 150, 200, null,
            null, LottieProgressDialog.SAMPLE_6, "جار التحميل..",View.VISIBLE)

        val sharedPreferences = requireActivity().getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId","")!!
        val data = ArrayList<MessageModel>()

        val database = FirebaseDatabase.getInstance().getReference("chats")
        database.get().addOnSuccessListener {
            for (i in it.children){
                if (i.key!!.startsWith(userId)){
                    val room = i.value as HashMap<*,*>
                    for (msg in room){
                        val msgData = msg.value as HashMap<*,*>
                        if(msgData["receiverId"].toString() == userId){
                            data.add(
                                MessageModel(msgData["msgId"].toString(),msgData["senderId"].toString()
                                    ,msgData["message"].toString(),msgData["time"].toString().toInt(),msgData["senderName"].toString()
                                    ,msgData["receiverName"].toString(),msgData["receiverId"].toString(),msgData["title"].toString())
                            )
                            break
                        }
                    }
                }
                val chatContactAdapter = ChatContactAdapter(requireContext(),data,"doctor")
                binding.rvChat.adapter = chatContactAdapter
                binding.rvChat.layoutManager = LinearLayoutManager(requireContext())
            }
            if (dialog.isShowing) dialog.dismiss()
        }
            .addOnFailureListener {
                if (dialog.isShowing) dialog.dismiss()
                Toast.makeText(requireContext(), "تأكد من اتصال الشبكة", Toast.LENGTH_SHORT).show()
            }

        return binding.root
    }
}