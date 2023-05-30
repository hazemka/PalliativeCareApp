package com.hazem.alkateb.palliativecare.patient.ui.show_posts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.databinding.FragmentShowPostsBinding
import com.hazem.alkateb.palliativecare.model.UserTopic

class ShowPostsFragment : Fragment() {

    companion object{
        lateinit var topic: UserTopic
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentShowPostsBinding.inflate(inflater, container,false)

        val viewModel = ViewModelProvider(this)[ShowPostsViewModel::class.java]
        topic = requireArguments().getSerializable("topic") as UserTopic

        if (isAdded){
            viewModel.getTopicPosts(topic.id,topic.doctorId)
            binding.shimmerLayout.startShimmer()
        }

        viewModel.dataPosts.observe(viewLifecycleOwner){
            binding.shimmerLayout.stopShimmer()
            binding.shimmerLayout.visibility = View.INVISIBLE
            binding.rvShowPosts.visibility = View.VISIBLE
            val adapter = ShowPostsAdapter(requireContext(),it)
            binding.rvShowPosts.adapter = adapter
            binding.rvShowPosts.layoutManager = LinearLayoutManager(requireContext())
        }

        binding.btnGoBack.setOnClickListener {
            if (isAdded)
                findNavController().popBackStack()
        }

        return binding.root
    }
}