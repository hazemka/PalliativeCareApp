package com.hazem.alkateb.palliativecare.fragments.create_new_account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.hazem.alkateb.palliativecare.MainActivity
import com.hazem.alkateb.palliativecare.databinding.FragmentCreateNewAccountBinding
import com.hazem.alkateb.palliativecare.fragments.login.LoginFragment
import com.hazem.alkateb.palliativecare.model.User

class CreateNewAccountFragment : Fragment() {
    private lateinit var binding: FragmentCreateNewAccountBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreateNewAccountBinding.inflate(inflater, container, false)

        val userType = requireArguments().getString("userType")

        binding.btnGoToLogin.setOnClickListener {
            if (isAdded)
                (requireActivity() as MainActivity).replaceFragment(LoginFragment())
        }

        binding.dialog.hide()

        val viewModel = ViewModelProvider(this)[CreateNewAccountViewModel::class.java]

        binding.btnCreateAccount.setOnClickListener {
            if (checkLogin() && isPasswordsMatch()){
                binding.dialog.show()
                binding.btnCreateAccount.isEnabled = false
                viewModel.createNewAccount(User(binding.txtFirstName.editText!!.text.toString()
                ,binding.txtSecondName.editText!!.text.toString(),binding.txtFamilyName.editText!!.text.toString()
                ,binding.txtBOD.editText!!.text.toString(),binding.txtCreateEmail.editText!!.text.toString()
                ,binding.txtLocation.editText!!.text.toString(),binding.txtPhoneNumber.editText!!.text.toString(),userType!!,""),
                    binding.txtCreatePassword.editText!!.text.toString())
            }
        }

        viewModel.isAccountCreated.observe(viewLifecycleOwner){
            if (it){
                binding.dialog.hide()
                clearFields()
                // show dialog to display that account was created and you can go to the login page

                (requireActivity() as MainActivity).replaceFragment(LoginFragment())
            }else{
                Toast.makeText(requireContext(), "تأكد من اتصال الانترنت", Toast.LENGTH_SHORT).show()
            }
        }

        removeErrorWhenStartWriting()

        return binding.root
    }

    private fun checkLogin():Boolean{
        if (binding.txtFirstName.editText!!.text.isNullOrEmpty()){
            binding.txtFirstName.error = "مطلوب"
            return false
        }else if (binding.txtSecondName.editText!!.text.isNullOrEmpty()){
            binding.txtSecondName.error = "مطلوب"
            return false
        }else if (binding.txtFamilyName.editText!!.text.isNullOrEmpty()){
            binding.txtFamilyName.error = "مطلوب"
            return false
        }else if (binding.txtBOD.editText!!.text.isNullOrEmpty()){
            binding.txtBOD.error = "تاريخ الميلاد مطلوب"
            return false
        }else if (binding.txtCreateEmail.editText!!.text.isNullOrEmpty()){
            binding.txtCreateEmail.error = "البريد الإلكتروني مطلوب"
            return false
        }else if (binding.txtLocation.editText!!.text.isNullOrEmpty()){
            binding.txtLocation.error = "العنوان مطلوب"
            return false
        }else if(binding.txtPhoneNumber.editText!!.text.isNullOrEmpty()){
            binding.txtPhoneNumber.error = "رقم الهاتف مطلوب"
            return false
        }else if(binding.txtCreatePassword.editText!!.text.isNullOrEmpty()){
            binding.txtCreatePassword.error = "كلمة المرور مطلوبة"
            return false
        }else if(binding.txtConfirmPassword.editText!!.text.isNullOrEmpty()){
            binding.txtConfirmPassword.error = "تأكيد كلمة المرور مطلوب"
            return false
        }
        return true
    }

    private fun isPasswordsMatch():Boolean{
        return if (binding.txtCreatePassword.editText!!.text.toString() == binding.txtConfirmPassword.editText!!.text.toString()){
            true
        }else{
            binding.txtConfirmPassword.error = "كلمة المرور غير متطابقة"
            false
        }
    }

    private fun removeErrorWhenStartWriting(){
        binding.txtCreateEmail.editText!!.doAfterTextChanged {
            binding.txtCreateEmail.isErrorEnabled = binding.txtCreateEmail.editText!!.text.isEmpty()
        }

        binding.txtFirstName.editText!!.doAfterTextChanged {
            binding.txtFirstName.isErrorEnabled = binding.txtFirstName.editText!!.text.isEmpty()
        }

        binding.txtSecondName.editText!!.doAfterTextChanged {
            binding.txtSecondName.isErrorEnabled = binding.txtSecondName.editText!!.text.isEmpty()
        }

        binding.txtFamilyName.editText!!.doAfterTextChanged {
            binding.txtFamilyName.isErrorEnabled = binding.txtFamilyName.editText!!.text.isEmpty()
        }

        binding.txtBOD.editText!!.doAfterTextChanged {
            binding.txtBOD.isErrorEnabled = binding.txtBOD.editText!!.text.isEmpty()
        }

        binding.txtLocation.editText!!.doAfterTextChanged {
            binding.txtLocation.isErrorEnabled = binding.txtLocation.editText!!.text.isEmpty()
        }

        binding.txtPhoneNumber.editText!!.doAfterTextChanged {
            binding.txtPhoneNumber.isErrorEnabled = binding.txtPhoneNumber.editText!!.text.isEmpty()
        }

        binding.txtCreatePassword.editText!!.doAfterTextChanged {
            binding.txtCreatePassword.isErrorEnabled = binding.txtCreatePassword.editText!!.text.isEmpty()
        }

        binding.txtConfirmPassword.editText!!.doAfterTextChanged {
            binding.txtConfirmPassword.isErrorEnabled = binding.txtConfirmPassword.editText!!.text.isEmpty()
        }

    }

    private fun clearFields() {
        binding.txtFirstName.editText!!.text.clear()
        binding.txtSecondName.editText!!.text.clear()
        binding.txtFamilyName.editText!!.text.clear()
        binding.txtCreateEmail.editText!!.text.clear()
        binding.txtBOD.editText!!.text.clear()
        binding.txtPhoneNumber.editText!!.text.clear()
        binding.txtConfirmPassword.editText!!.text.clear()
        binding.txtCreatePassword.editText!!.text.clear()
        binding.txtLocation.editText!!.text.clear()
    }
}