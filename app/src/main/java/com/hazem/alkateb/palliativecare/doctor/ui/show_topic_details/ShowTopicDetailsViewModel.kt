package com.hazem.alkateb.palliativecare.doctor.ui.show_topic_details

import android.app.Activity
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hazem.alkateb.palliativecare.model.Post
import com.hazem.alkateb.palliativecare.model.Topic

class ShowTopicDetailsViewModel : ViewModel() {

    val isDeleted = MutableLiveData<Boolean>()
    val isHideShowTopic = MutableLiveData<Boolean>()
    val topic = MutableLiveData<Topic>()
    val dataPosts = MutableLiveData<ArrayList<Post>>()

    fun deleteTopic(topicId:String,imageUrl: String,activity:Activity){
        val sharedPreferences = activity.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        FirebaseFirestore.getInstance().collection("users")
            .document(sharedPreferences.getString("userId", "")!!)
            .collection("myTopics")
            .document(topicId)
            .delete()
            .addOnSuccessListener {
                deleteImageTopic(imageUrl)
            }
            .addOnFailureListener {
                isDeleted.value = false
            }
    }

    private fun deleteImageTopic(imageUrl: String){
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
        storageReference.delete()
            .addOnSuccessListener {
                isDeleted.value = true
            }
            .addOnFailureListener {
                isDeleted.value  = false
            }
    }

    fun showHideTopic(activity: Activity,topicId: String,isShow:Boolean){
        val sharedPreferences = activity.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        FirebaseFirestore.getInstance().collection("users")
            .document(sharedPreferences.getString("userId", "")!!)
            .collection("myTopics")
            .document(topicId)
            .update("show",isShow)
            .addOnSuccessListener {
                isHideShowTopic.value = true
            }
            .addOnFailureListener {
                isHideShowTopic.value = false
            }
    }

    fun getCurrentUserTopic(topicId: String,activity: Activity){
        val sharedPreferences = activity.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        FirebaseFirestore.getInstance().collection("users")
            .document(sharedPreferences.getString("userId", "")!!)
            .collection("myTopics")
            .document(topicId)
            .get()
            .addOnSuccessListener {
                topic.value = Topic(it.id,it.getString("name")!!,it.getString("description")!!
                    ,it.getString("imageUrl")!!,it.get("numOfComments").toString().toInt()
                    ,it.get("numOfPosts").toString().toInt(),it.get("numOfFollowers").toString().toInt()
                    ,it.getBoolean("show")!!)
            }
            .addOnFailureListener {
                topic.value = Topic("","جار التحميل","جار التحميل","",0,0
                ,0,false)
            }
    }

    fun getTopicPosts(topicId: String,activity: Activity){
        val data = ArrayList<Post>()
        val sharedPreferences = activity.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        FirebaseFirestore.getInstance().collection("users")
            .document(sharedPreferences.getString("userId", "")!!)
            .collection("myTopics")
            .document(topicId)
            .collection("myPosts")
            .get()
            .addOnSuccessListener {posts ->
                if(posts.size() == 0) {
                    dataPosts.value = ArrayList()
                }else{
                    for (post in posts) {
                        data.add(Post(post.id,post.getString("mediaUrl")!!,post.getString("description")!!
                            ,post.get("numOfLikes").toString().toInt(),post.get("numOfComments").toString().toInt()
                            ,post.getString("postType")!!))
                    }
                    dataPosts.value = data
                }
            }
            .addOnFailureListener {
                dataPosts.value = ArrayList()
            }
    }

    companion object{
        val isPostDeleted = MutableLiveData<Boolean>()
        fun deletePost(topicId: String,postId:String,mediaUrl: String,activity:Activity){
            val sharedPreferences = activity.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
            FirebaseFirestore.getInstance().collection("users")
                .document(sharedPreferences.getString("userId", "")!!)
                .collection("myTopics")
                .document(topicId)
                .collection("myPosts")
                .document(postId)
                .delete()
                .addOnSuccessListener {
                    deletePostMedia(mediaUrl)
                }
                .addOnFailureListener {
                    isPostDeleted.value = false
                }
        }

        private fun deletePostMedia(mediaUrl:String){
            val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(mediaUrl)
            storageReference.delete()
                .addOnSuccessListener {
                    isPostDeleted.value = true
                }
                .addOnFailureListener {
                    isPostDeleted.value = false
                }
        }
    }

}