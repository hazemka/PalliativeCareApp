package com.hazem.alkateb.palliativecare.patient.ui.following_topics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FollowingTopicsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is following_topics Fragment"
    }
    val text: LiveData<String> = _text
}