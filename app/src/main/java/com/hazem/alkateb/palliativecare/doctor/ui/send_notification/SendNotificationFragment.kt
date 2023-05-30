package com.hazem.alkateb.palliativecare.doctor.ui.send_notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.devhoony.lottieproegressdialog.LottieProgressDialog
import com.google.android.material.snackbar.Snackbar
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.api_notification.Data
import com.hazem.alkateb.palliativecare.api_notification.NotificationBody
import com.hazem.alkateb.palliativecare.databinding.FragmentSendNotificationBinding

class SendNotificationFragment : Fragment() {
    private lateinit var binding: FragmentSendNotificationBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        binding = FragmentSendNotificationBinding.inflate(inflater, container,false)

        val topicId = requireArguments().getString("topicId")

        val viewModel = ViewModelProvider(this)[SendNotificationViewModel::class.java]

        val dialog = LottieProgressDialog(requireContext(), false, 150, 200, null,
            null, LottieProgressDialog.SAMPLE_6, "جار الإرسال..",View.VISIBLE)

        binding.btnSendNotification.setOnClickListener {
            if (checkFields()){
                dialog.show()
                viewModel.sendNotification(NotificationBody("/topics/$topicId", Data(binding.txtNotificationBody.editText!!.text.toString()
                    ,binding.txtNotificationTitle.editText!!.text.toString())))
            }
        }

        viewModel.isNotificationSent.observe(viewLifecycleOwner){
            if (it && dialog.isShowing){
                dialog.dismiss()
                Snackbar.make(binding.root,"تم إرسال الإشعار",Snackbar.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }else{
                Snackbar.make(binding.root,"حدث خطأ ما!",Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.btnGoBack.setOnClickListener {
            if (isAdded)
                findNavController().popBackStack()
        }

        removeFocusWhenStartWriting()

        return binding.root
    }

    private fun checkFields():Boolean{
        if (binding.txtNotificationBody.editText!!.text.isNullOrEmpty()){
            binding.txtNotificationBody.error = "مطلوب"
            return false
        }
        if (binding.txtNotificationTitle.editText!!.text.isNullOrEmpty()){
            binding.txtNotificationTitle.error = "مطلوب"
            return false
        }
        return true
    }

    private fun removeFocusWhenStartWriting(){
        binding.txtNotificationBody.editText!!.doAfterTextChanged {
            binding.txtNotificationBody.isErrorEnabled = binding.txtNotificationBody.editText!!.text.isEmpty()
        }
        binding.txtNotificationTitle.editText!!.doAfterTextChanged {
            binding.txtNotificationTitle.isErrorEnabled = binding.txtNotificationTitle.editText!!.text.isEmpty()
        }
    }
}