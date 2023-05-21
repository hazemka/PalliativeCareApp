package com.hazem.alkateb.palliativecare.fragments.forget_password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForgetPasswordViewModel:ViewModel() {

    val isEmailSent = MutableLiveData<Boolean>()

    fun sendEmailForResetPassword(email:String){
        sendEmail(email)
    }

    private fun sendEmail(email:String){
        viewModelScope.launch {
            withContext(Dispatchers.Default){
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        isEmailSent.value = task.isSuccessful
                    }
            }
        }
    }
}