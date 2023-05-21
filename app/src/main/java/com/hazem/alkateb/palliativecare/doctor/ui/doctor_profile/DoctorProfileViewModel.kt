package com.hazem.alkateb.palliativecare.doctor.ui.doctor_profile

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hazem.alkateb.palliativecare.model.User
import java.io.ByteArrayOutputStream
import java.util.UUID

class DoctorProfileViewModel : ViewModel() {

    val data = MutableLiveData<User>()
    val isLogout = MutableLiveData<Boolean>()
    val isUserPhotoUploaded = MutableLiveData<Boolean>()
    val imageUrl = MutableLiveData<String>()

    fun getUserData(activity:Activity){
        val sharedPreferences =  activity.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        FirebaseFirestore.getInstance().collection("users")
            .document(sharedPreferences.getString("userId","")!!)
            .get()
            .addOnSuccessListener {
                data.value = User(it.getString("firstName")!!,it.getString("secondName")!!,it.getString("familyName")!!
                ,it.getString("dob")!!,it.getString("email")!!,it.getString("location")!!,it.getString("phone")!!,"doctor"
                ,it.getString("photo")!!)
            }
    }

    fun logout(activity: Activity) {
        val sharedPreferences =  activity.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        isLogout.value =
            sharedPreferences.edit().remove("userId").remove("userName").remove("userType").commit()
    }

    fun uploadImage(bitmap: Bitmap,activity: Activity){
        val baos = ByteArrayOutputStream()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imagesRef = storageRef.child("usersImages")
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val data = baos.toByteArray()
        val imgRef = imagesRef.child(UUID.randomUUID().toString() + ".jpeg")
        val uploadTask = imgRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            imgRef.downloadUrl.addOnSuccessListener{
                updateUserPhoto(it.toString(),activity)
            }
        }
            .addOnFailureListener {
                isUserPhotoUploaded.value = false
            }
    }

    private fun updateUserPhoto(imageUri:String,activity: Activity){
        val sharedPreferences = activity.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        FirebaseFirestore.getInstance().collection("users")
            .document(sharedPreferences.getString("userId","")!!)
            .update("photo", imageUri)
            .addOnSuccessListener {
                isUserPhotoUploaded.value = true
            }
            .addOnFailureListener {
                isUserPhotoUploaded.value = false
            }
    }

    fun getImage(activity: Activity){
        val sharedPreferences = activity.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        FirebaseFirestore.getInstance().collection("users")
            .document(sharedPreferences.getString("userId","")!!)
            .get()
            .addOnSuccessListener {
                if (it.getString("photo")!!.isNotEmpty())
                    imageUrl.value = it.getString("photo")
                else
                    imageUrl.value = ""
            }
            .addOnFailureListener {
                imageUrl.value = ""
            }
    }

}