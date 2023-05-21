package com.hazem.alkateb.palliativecare.model

import com.google.firebase.firestore.DocumentId

data class Post(@DocumentId var id:String,var mediaUrl:String,var description:String
,var numOfLikes:Int,var numOfComments:Int,var postType:String)