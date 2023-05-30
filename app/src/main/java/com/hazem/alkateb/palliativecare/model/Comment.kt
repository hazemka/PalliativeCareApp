package com.hazem.alkateb.palliativecare.model

import com.google.firebase.firestore.DocumentId

data class Comment(@DocumentId var id:String,var userId:String,var userName:String,var userImage:String,var comment:String)
