package com.hazem.alkateb.palliativecare.doctor.ui.edit_topic

import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hazem.alkateb.palliativecare.model.Topic
import java.io.ByteArrayOutputStream
import java.util.UUID

class EditTopicViewModel : ViewModel() {

    val isTopicEdited = MutableLiveData<Boolean>()

    fun editTopicNow(topic: Topic, bitmapImage: Bitmap, activity: Activity){
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
                Log.e("hzm", "editTopicNow: new image : $it")
                editTopic(it.toString(),topic,activity)
            }
        }
            .addOnFailureListener {
                isTopicEdited.value = false
            }
    }



    private fun editTopic(imageUrl:String, topic: Topic, activity: Activity) {
        val oldImage = topic.imageUrl
        val sharedPreferences = activity.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        topic.imageUrl = imageUrl
        FirebaseFirestore.getInstance().collection("users")
            .document(sharedPreferences.getString("userId", "")!!)
            .collection("myTopics")
            .document(topic.id)
            .set(topic)
            .addOnSuccessListener {
                Log.e("hzm", "editTopic: Success", )
                deleteImageTopic(oldImage)
            }
            .addOnFailureListener {
                isTopicEdited.value = false
            }
    }

    private fun deleteImageTopic(imageUrl: String){
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
        storageReference.delete()
            .addOnSuccessListener {
                Log.e("hzm", "deleteImageTopic: Success")
                isTopicEdited.value = true
            }
    }
}