package com.hazem.alkateb.palliativecare.fragments.show_comments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.hazem.alkateb.palliativecare.model.Comment

class ShowCommentsViewModel : ViewModel() {

    val isCommentSent = MutableLiveData<Boolean>()
    val dataComments = MutableLiveData<ArrayList<Comment>>()

    fun getComments(topicId:String,userId:String,postId:String){
        val data = ArrayList<Comment>()
        FirebaseFirestore.getInstance().collection("users")
            .document(userId)
            .collection("myTopics")
            .document(topicId)
            .collection("myPosts")
            .document(postId)
            .collection("comments")
            .get()
            .addOnSuccessListener {
                for (comment in it){
                    data.add(
                        Comment(comment.id,comment.getString("userId")!!,
                    comment.getString("userName")!!, comment.getString("userImage")!!
                    , comment.getString("comment")!!))
                }
                dataComments.value = data
            }
            .addOnFailureListener {
                dataComments.value = ArrayList()
            }
    }

    private fun sendComment(topicId:String,userId:String,postId:String,comment: Comment){
        FirebaseFirestore.getInstance().collection("users")
            .document(userId)
            .collection("myTopics")
            .document(topicId)
            .collection("myPosts")
            .document(postId)
            .collection("comments")
            .add(comment)
            .addOnSuccessListener {
                isCommentSent.value = true
            }
            .addOnFailureListener {
                isCommentSent.value = false
            }
    }

    fun getUserInfoAndSendComment(topicId:String,userId:String,postId:String,comment:String){
        FirebaseFirestore.getInstance().collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener {
                val name = "${it.getString("firstName")} ${it.getString("secondName")} ${it.getString("familyName")}"
                sendComment(topicId,userId,postId,Comment("",it.id,name,it.getString("photo")!!
                ,comment))
            }
            .addOnFailureListener {
                isCommentSent.value = false
            }
    }

}