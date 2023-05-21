package com.hazem.alkateb.palliativecare.fragments.create_new_account

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hazem.alkateb.palliativecare.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateNewAccountViewModel :ViewModel(){

    val isAccountCreated = MutableLiveData<Boolean>()

    fun createNewAccount(user: User,password:String){
        createAccount(user,password)
    }

    private fun createAccount(user: User,password:String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.email,password)
                    .addOnSuccessListener {
                        saveAccount(user,it.user!!.uid)
                        Log.e("hzm", "createAccount: ${Thread.currentThread().name}")
                    }
                    .addOnFailureListener {
                        isAccountCreated.value = false
                    }
            }
        }
    }

    private fun saveAccount(user: User,userId:String){
        Log.e("hzm", "saveAccount: ${Thread.currentThread().name}")
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                isAccountCreated.value = true
            }
            .addOnFailureListener {
                isAccountCreated.value = false
            }
    }
}