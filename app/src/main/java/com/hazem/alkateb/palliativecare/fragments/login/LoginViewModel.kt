package com.hazem.alkateb.palliativecare.fragments.login

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel:ViewModel() {

    val isLoginSuccess = MutableLiveData<Boolean>()

    fun login(email:String,password:String,activity: Activity){
        loginWithFirebase(email, password,activity)
    }

    private fun loginWithFirebase(email:String,password:String,activity: Activity){
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                getUserInfo(it.user!!.uid,activity)
            }
            .addOnFailureListener {
                isLoginSuccess.value = false
            }
    }

    private fun getUserInfo(userId:String,activity: Activity){
        FirebaseFirestore.getInstance().collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener {
                if (saveUserData(activity,userId,it.getString("firstName")!!,it.getString("userType")!!))
                    isLoginSuccess.value = true
            }
            .addOnFailureListener {
                isLoginSuccess.value = false
            }
    }

    private fun saveUserData(activity: Activity,userId: String,userName:String,userType:String):Boolean{
        val sharedPreferences =  activity.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userId", userId)
        editor.putString("userName",userName)
        editor.putString("userType",userType)
        return editor.commit()
    }

}