package com.hazem.alkateb.palliativecare.patient.ui.show_posts


import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.api.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.hazem.alkateb.palliativecare.model.Post

class ShowPostsViewModel : ViewModel() {

    val dataPosts = MutableLiveData<ArrayList<Post>>()

    fun getTopicPosts(topicId: String,doctorId:String){
        val data = ArrayList<Post>()
        FirebaseFirestore.getInstance().collection("users")
            .document(doctorId)
            .collection("myTopics")
            .document(topicId)
            .collection("myPosts")
            .get()
            .addOnSuccessListener {posts ->
                if(posts.size() == 0) {
                    dataPosts.value = ArrayList()
                }else{
                    for (post in posts) {
                        data.add(
                            Post(post.id,post.getString("mediaUrl")!!,post.getString("description")!!
                            ,post.get("numOfLikes").toString().toInt(),post.get("numOfComments").toString().toInt()
                            ,post.getString("postType")!!)
                        )
                    }
                    dataPosts.value = data
                }
            }
            .addOnFailureListener {
                dataPosts.value = ArrayList()
            }
    }

    companion object{
        fun likePost(context: android.content.Context,doctorId:String,topicId:String,postId:String){
            Log.e("hzm", "likePost: ${doctorId} / ${topicId} / ${postId}")
            val sharedPreferences =  context.getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
            val map = HashMap<String,String>()
            map["userId"] = sharedPreferences.getString("userId","")!!
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(doctorId)
                .collection("myTopics")
                .document(topicId)
                .collection("myPosts")
                .document(postId)
                .collection("likes")
                .add(map)
                .addOnSuccessListener {
                    updateNumOfLikes(doctorId, topicId, postId)
                }
                .addOnFailureListener {

                }
        }

        private fun updateNumOfLikes(doctorId:String, topicId:String, postId:String) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(doctorId)
                .collection("myTopics")
                .document(topicId)
                .collection("myPosts")
                .document(postId)
                .get()
                .addOnSuccessListener {
                    val num = it.get("numOfLikes").toString().toInt()
                    FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(doctorId)
                        .collection("myTopics")
                        .document(topicId)
                        .collection("myPosts")
                        .document(postId)
                        .update("numOfLikes",num+1)
                }
        }
    }



}