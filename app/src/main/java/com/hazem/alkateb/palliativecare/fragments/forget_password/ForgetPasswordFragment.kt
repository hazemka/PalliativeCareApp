package com.hazem.alkateb.palliativecare.fragments.forget_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.hazem.alkateb.palliativecare.databinding.FragmentForgetPasswordBinding

class ForgetPasswordFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)

        val viewModel = ViewModelProvider(this)[ForgetPasswordViewModel::class.java]

        binding.dialog.hide()

        binding.btnSendLink.setOnClickListener {
            if (binding.txtForgetEmail.editText!!.text.isNotEmpty() && binding.txtForgetEmail.editText!!.text.contains("@")){
                binding.dialog.show()
                viewModel.sendEmailForResetPassword(binding.txtForgetEmail.editText!!.text.toString())
            }else{
                binding.txtForgetEmail.error = "البريد الإلكتروني مطلوب"
            }
        }

        binding.txtForgetEmail.editText!!.doAfterTextChanged {
            binding.txtForgetEmail.isErrorEnabled = binding.txtForgetEmail.editText!!.text.isEmpty()
        }

        binding.btnResendLink.setOnClickListener {
            if (binding.txtForgetEmail.editText!!.text.isNotEmpty()){
                binding.dialog.show()
                viewModel.sendEmailForResetPassword(binding.txtForgetEmail.editText!!.text.toString())
            }else{
                binding.txtForgetEmail.error = "البريد الإلكتروني مطلوب"
            }
        }

        viewModel.isEmailSent.observe(viewLifecycleOwner){
            if (it){
                binding.dialog.hide()
                Snackbar.make(binding.root,"تم إرسال رسالة تعيين كلمة المرور عبر البريد!",Snackbar.LENGTH_LONG).show()
            }else{
                binding.dialog.hide()
                Snackbar.make(binding.root,"تأكد من اتصال الانترنت وحاول مرة أخرى",Snackbar.LENGTH_LONG).show()
            }
        }

        binding.txtForgetEmail.editText!!.doAfterTextChanged {
            binding.txtForgetEmail.isErrorEnabled = binding.txtForgetEmail.editText!!.text.isEmpty()
        }

        return binding.root
    }
}