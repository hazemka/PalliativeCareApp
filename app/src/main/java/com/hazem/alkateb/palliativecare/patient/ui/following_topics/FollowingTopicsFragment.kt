package com.hazem.alkateb.palliativecare.patient.ui.following_topics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hazem.alkateb.palliativecare.databinding.FragmentFollowingTopicsBinding

class FollowingTopicsFragment : Fragment() {

    private lateinit var binding: FragmentFollowingTopicsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val followingTopicsViewModel =
            ViewModelProvider(this).get(FollowingTopicsViewModel::class.java)

        binding = FragmentFollowingTopicsBinding.inflate(inflater, container, false)

        return binding.root
    }
}