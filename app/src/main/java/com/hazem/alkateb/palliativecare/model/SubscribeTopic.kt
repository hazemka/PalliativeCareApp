package com.hazem.alkateb.palliativecare.model

import com.google.firebase.firestore.DocumentId

data class SubscribeTopic(@DocumentId var id:String, var doctorId:String, var topicId:String)
