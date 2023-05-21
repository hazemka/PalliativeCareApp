package com.hazem.alkateb.palliativecare.model

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

data class Topic(@DocumentId var id:String ,var name:String, var description:String,var imageUrl:String
,var numOfComments:Int,var numOfPosts:Int,var numOfFollowers:Int,var isShow:Boolean):Serializable
