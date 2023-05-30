package com.hazem.alkateb.palliativecare.model

data class MessageModel(var msgId:String?=null
,var senderId:String?=null,var message:String?=null,var time:Int?=null
,var senderName:String? = null,var receiverName:String? = null,var receiverId: String? = null
,var title:String? =null):Comparable<MessageModel> {
    override fun compareTo(other: MessageModel): Int {
        return time!!.compareTo(other.time!!)
    }
}