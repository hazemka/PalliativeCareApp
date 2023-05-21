package com.hazem.alkateb.palliativecare.doctor.ui.add_topic

import android.app.Activity
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hazem.alkateb.palliativecare.model.Topic
import java.io.ByteArrayOutputStream
import java.util.UUID

class AddTopicViewModel : ViewModel() {

    val isTopicAdded = MutableLiveData<Boolean>()

    fun addTopic(topic: Topic,bitmapImage: Bitmap, activity: Activity){
        val baos = ByteArrayOutputStream()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imagesRef = storageRef.child("topicsImages")
        bitmapImage.compress(Bitmap.CompressFormat.JPEG,80,baos)
        val data = baos.toByteArray()
        val imgRef = imagesRef.child(UUID.randomUUID().toString() + ".jpeg")
        val uploadTask = imgRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            imgRef.downloadUrl.addOnSuccessListener{
                addNewTopic(it.toString(),topic,activity)
            }
        }
            .addOnFailureListener {
                isTopicAdded.value = false
            }
    }



    private fun addNewTopic(imageUrl:String,topic: Topic, activity:Activity) {
        val sharedPreferences = activity.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        topic.imageUrl = imageUrl
        FirebaseFirestore.getInstance().collection("users")
            .document(sharedPreferences.getString("userId", "")!!)
            .collection("myTopics")
            .add(topic)
            .addOnSuccessListener {
                isTopicAdded.value = true
            }
            .addOnFailureListener {
                isTopicAdded.value = false
            }
    }
}