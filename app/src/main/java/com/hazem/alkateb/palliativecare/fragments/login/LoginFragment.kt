package com.hazem.alkateb.palliativecare.fragments.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.messaging.FirebaseMessaging
import com.hazem.alkateb.palliativecare.MainActivity
import com.hazem.alkateb.palliativecare.databinding.FragmentLoginBinding
import com.hazem.alkateb.palliativecare.doctor.DoctorActivity
import com.hazem.alkateb.palliativecare.fragments.choose_account.ChooseAccountFragment
import com.hazem.alkateb.palliativecare.fragments.create_new_account.CreateNewAccountFragment
import com.hazem.alkateb.palliativecare.fragments.forget_password.ForgetPasswordFragment
import com.hazem.alkateb.palliativecare.patient.PatientActivity
import kotlinx.coroutines.flow.combine


class LoginFragment : Fragment() {
    private lateinit var binding:FragmentLoginBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        if (isAdded){
            goToActivity()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        binding.dialog.hide()

        val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        binding.btnLogin.setOnClickListener {
            if (checkFields()){
                binding.dialog.show()
                viewModel.login(binding.txtLoginEmail.editText!!.text.toString(),
                    binding.txtLoginPass.editText!!.text.toString(),requireActivity())
            }
        }

        removeFocusWhenStartWriting()

        viewModel.isLoginSuccess.observe(viewLifecycleOwner){
            if (it){
                binding.dialog.hide()
                goToActivity()
            }else{
                binding.dialog.hide()
                Snackbar.make(binding.root,"خطأ في كلمة المرور",Snackbar.LENGTH_SHORT).show()
            }
        }


        binding.btnGoToCreateAccount.setOnClickListener {
            if (isAdded)
                (requireActivity() as MainActivity).replaceFragment(ChooseAccountFragment())
        }

        binding.btnForgetPass.setOnClickListener {
            if (isAdded)
                (requireActivity() as MainActivity).replaceFragment(ForgetPasswordFragment())
        }

    }

    private fun checkFields(): Boolean {
        if (binding.txtLoginEmail.editText!!.text.isNullOrEmpty()){
            binding.txtLoginEmail.error = "مطلوب"
            return false
        }else if (binding.txtLoginPass.editText!!.text.isNullOrEmpty()){
            binding.txtLoginPass.error = "مطلوب"
            return false
        }
        return true
    }


    private fun removeFocusWhenStartWriting(){
        binding.txtLoginEmail.editText!!.doAfterTextChanged {
            binding.txtLoginEmail.isErrorEnabled = binding.txtLoginEmail.editText!!.text.isEmpty()
        }
        binding.txtLoginPass.editText!!.doAfterTextChanged {
            binding.txtLoginPass.isErrorEnabled = binding.txtLoginPass.editText!!.text.isEmpty()
        }
    }

    private fun goToActivity(){
        val sharedPreferences =  requireActivity().getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        if (sharedPreferences.getString("userId","") != ""){
            when(sharedPreferences.getString("userType","")){
                "patient" -> {
                    val userProfileChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(sharedPreferences.getString("name","user")).build()
                    val firebaseUser = FirebaseAuth.getInstance().currentUser
                    firebaseUser!!.updateProfile(userProfileChangeRequest)
                    FirebaseMessaging.getInstance().subscribeToTopic("globalTopic")
                    requireActivity().startActivity(Intent(requireContext(),PatientActivity::class.java))
                    requireActivity().finish()
                }
                "doctor" -> {
                    val userProfileChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(sharedPreferences.getString("name","user")).build()
                    val firebaseUser = FirebaseAuth.getInstance().currentUser
                    firebaseUser!!.updateProfile(userProfileChangeRequest)
                    requireActivity().startActivity(Intent(requireContext(),DoctorActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }

}