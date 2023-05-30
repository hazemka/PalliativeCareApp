package com.hazem.alkateb.palliativecare.doctor.ui.add_post

import android.app.Activity
import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hazem.alkateb.palliativecare.api_notification.ApiClient
import com.hazem.alkateb.palliativecare.api_notification.ApiRepository
import com.hazem.alkateb.palliativecare.api_notification.Data
import com.hazem.alkateb.palliativecare.api_notification.NotificationBody
import com.hazem.alkateb.palliativecare.api_notification.TopicResponse
import com.hazem.alkateb.palliativecare.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID


class AddPostViewModel : ViewModel() {
    val isMediaUploaded = MutableLiveData<Boolean>()
    val isPostUploaded = MutableLiveData<Boolean>()
    var mediaUrl = ""
    var postType = ""
    private val repository: ApiRepository = ApiRepository(ApiClient.apiService)

    fun uploadMedia(mediaUri:Uri,activity: Activity,mediaRef:String){
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val mediasRef = storageRef.child(mediaRef)
        val fileRef = mediasRef.child("${UUID.randomUUID()}.${getFileType(mediaUri,activity)}")
        fileRef.putFile(mediaUri)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener {
                    mediaUrl = it.toString()
                    isMediaUploaded.value = true
                    getPostType(mediaRef)
                }
            }
            .addOnFailureListener {
                isMediaUploaded.value = false
            }
    }

    fun addNewPost(topicId:String,topicName:String,post: Post, activity: Activity) {
        val sharedPreferences = activity.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        post.mediaUrl = mediaUrl
        post.postType = postType
        Log.e("hzm", "addNewPost: $topicId / $topicName")
        FirebaseFirestore.getInstance().collection("users")
            .document(sharedPreferences.getString("userId", "")!!)
            .collection("myTopics")
            .document(topicId)
            .collection("myPosts")
            .add(post)
            .addOnSuccessListener {
                getNumberOfPosts(topicId,topicName, activity)
            }
            .addOnFailureListener {
                isPostUploaded.value = false
            }
    }

    private fun getFileType(videoUri: Uri,activity: Activity): String? {
        val r: ContentResolver = activity.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(r.getType(videoUri))
    }

    private fun getPostType(mediaType:String){
        when(mediaType){
            "postsImages" -> postType = "image"
            "pdfFiles" -> postType = "pdf"
            "postsVideos" -> postType = "video"
        }
    }

    private fun getNumberOfPosts(topicId: String,topicName:String,activity: Activity){
        Log.e("hzm", "getNumberOfPosts: $topicId / $topicName")
        val sharedPreferences = activity.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        FirebaseFirestore.getInstance().collection("users")
            .document(sharedPreferences.getString("userId", "")!!)
            .collection("myTopics")
            .document(topicId)
            .get()
            .addOnSuccessListener {
                updateNumberOfPosts(it.get("numOfPosts").toString().toInt(),activity, topicId,topicName)
            }
            .addOnFailureListener {
                isPostUploaded.value = false
            }
    }

    private fun updateNumberOfPosts(pastNum:Int,activity: Activity,topicId: String,topicName:String){
        Log.e("hzm", "updateNumberOfPosts: $topicId / $topicName")
        val sharedPreferences = activity.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        FirebaseFirestore.getInstance().collection("users")
            .document(sharedPreferences.getString("userId", "")!!)
            .collection("myTopics")
            .document(topicId)
            .update("numOfPosts",pastNum+1)
            .addOnSuccessListener {
                viewModelScope.launch(Dispatchers.IO){
                    try{
                        val call = repository.sendNotification(NotificationBody("/topics/$topicId", Data("تم إضافة منشور جديد في موضوع ${topicName}","تم إضافة منشور جديد")))
                        call.enqueue(object :Callback<TopicResponse>{
                            override fun onResponse(
                                call: Call<TopicResponse>,
                                response: Response<TopicResponse>
                            ) {
                                Log.e("hzm", "onResponse: ${response.code()}")
                                Log.e("hzm", "onResponse: ${response.body().toString()}")
                                isPostUploaded.value = response.isSuccessful && response.code() == 200
                            }

                            override fun onFailure(call: Call<TopicResponse>, t: Throwable) {
                                Log.e("hzm", "onResponse: ${t.message}")
                            }

                        })
                    }catch (e:Exception){
                        Log.e("hzm", "updateNumberOfPosts: Error" +
                                "${e.message}")
                    }
                }


                Log.e("hzm", "notification Success:")
            }
            .addOnFailureListener {
                isPostUploaded.value = false
            }
    }
}