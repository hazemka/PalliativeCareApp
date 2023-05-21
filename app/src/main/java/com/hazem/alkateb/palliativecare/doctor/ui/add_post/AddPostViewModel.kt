package com.hazem.alkateb.palliativecare.doctor.ui.add_post

import android.app.Activity
import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hazem.alkateb.palliativecare.api_notification.ApiClient
import com.hazem.alkateb.palliativecare.api_notification.ApiRepository
import com.hazem.alkateb.palliativecare.api_notification.Body
import com.hazem.alkateb.palliativecare.api_notification.NotificationBody
import com.hazem.alkateb.palliativecare.api_notification.TopicResponse
import com.hazem.alkateb.palliativecare.model.Post
import retrofit2.Call
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.security.auth.callback.Callback


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
                val call = repository.sendTopicNotification(Body(topicId, NotificationBody("تم إضافة منشور جديد في موضوع ${topicName}","تم إضافة منشور جديد")))
                call.enqueue(object :retrofit2.Callback<TopicResponse> {
                    override fun onResponse(
                        call: Call<TopicResponse>,
                        response: Response<TopicResponse>) {
                        Log.e("hzm", "onResponse: ${response.body()!!.message_id}")
                        Log.e("hzm", "onResponse: ${response.code()}")
                        isPostUploaded.value = true
                    }
                    override fun onFailure(call: Call<TopicResponse>, t: Throwable) {
                        isPostUploaded.value = false
                    }
                })
            }
            .addOnFailureListener {
                isPostUploaded.value = false
            }
    }
}