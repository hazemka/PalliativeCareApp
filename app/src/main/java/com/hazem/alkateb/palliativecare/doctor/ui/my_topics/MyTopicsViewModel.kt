package com.hazem.alkateb.palliativecare.doctor.ui.my_topics

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.hazem.alkateb.palliativecare.model.Topic

class MyTopicsViewModel : ViewModel() {

    val dataTopics = MutableLiveData<ArrayList<Topic>>()

    fun getMyTopics(activity:Activity){
        val data = ArrayList<Topic>()
        val sharedPreferences = activity.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        FirebaseFirestore.getInstance().collection("users")
            .document(sharedPreferences.getString("userId", "")!!)
            .collection("myTopics")
            .get()
            .addOnSuccessListener {
                for (topic in it) {
                    data.add(Topic(topic.id,topic.getString("name")!!,topic.getString("description")!!
                    ,topic.getString("imageUrl")!!,topic.get("numOfComments").toString().toInt()
                    ,topic.get("numOfPosts").toString().toInt(),topic.get("numOfFollowers").toString().toInt()
                    ,topic.getBoolean("show")!!))
                }
                dataTopics.value = data
            }
            .addOnFailureListener {
                val data = ArrayList<Topic>()
                dataTopics.value = data
            }
    }
}