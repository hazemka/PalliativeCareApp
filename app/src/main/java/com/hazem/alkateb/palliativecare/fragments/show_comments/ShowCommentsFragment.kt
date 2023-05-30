package com.hazem.alkateb.palliativecare.fragments.show_comments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.databinding.FragmentShowCommentsBinding
import com.hazem.alkateb.palliativecare.model.Comment

class ShowCommentsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentShowCommentsBinding.inflate(inflater,container,false)

        // from DoctorPostAdapter && ShowPostsAdapter
        val userId = requireArguments().getString("userId")
        val topicId = requireArguments().getString("topicId")
        val postId = requireArguments().getString("postId")

        val viewModel = ViewModelProvider(this)[ShowCommentsViewModel::class.java]

        if (isAdded && !userId.isNullOrEmpty() && !topicId.isNullOrEmpty() && !postId.isNullOrEmpty()) {
            viewModel.getComments(topicId, userId, postId)
        }

        viewModel.dataComments.observe(viewLifecycleOwner){
            val showCommentAdapter = ShowCommentsAdapter(requireContext(),it)
            binding.rvComments.adapter = showCommentAdapter
            binding.rvComments.layoutManager = LinearLayoutManager(requireContext())
        }

        binding.btnSendMessage.setOnClickListener {
            if (binding.messageBox.text.isNotEmpty()){
                viewModel.getUserInfoAndSendComment(topicId!!,userId!!,postId!!,binding.messageBox.text.toString())
            }
            viewModel.getComments(topicId!!, userId!!, postId!!)
        }


        return binding.root
    }

}