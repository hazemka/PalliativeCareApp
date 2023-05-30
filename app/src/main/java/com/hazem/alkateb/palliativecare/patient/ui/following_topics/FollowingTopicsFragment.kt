package com.hazem.alkateb.palliativecare.patient.ui.following_topics

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.databinding.FragmentFollowingTopicsBinding
import com.hazem.alkateb.palliativecare.patient.ui.topics.TopicPatientAdapter

class FollowingTopicsFragment : Fragment() {

    private lateinit var binding: FragmentFollowingTopicsBinding
    private lateinit var adapter:FollowingTopicsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val followingTopicsViewModel =
            ViewModelProvider(this)[FollowingTopicsViewModel::class.java]

        binding = FragmentFollowingTopicsBinding.inflate(inflater, container, false)

        if (isAdded){
            followingTopicsViewModel.getSubscriptionsIds(requireContext())
            binding.shimmerFrameLayout.startShimmer()
        }

        binding.txtSearchBar.setEndIconOnClickListener {
            binding.txtSearchBar.editText!!.text.clear()
            binding.txtSearchBar.clearFocus()
            hideKeyboard()
            adapter.stopSearch()
        }


        followingTopicsViewModel.allTopics.observe(viewLifecycleOwner){
            binding.shimmerFrameLayout.stopShimmer()
            binding.shimmerFrameLayout.visibility = View.INVISIBLE
            binding.rvShowTopics.visibility = View.VISIBLE
            adapter = FollowingTopicsAdapter(requireContext(),it)
            binding.rvShowTopics.adapter = adapter
            binding.rvShowTopics.layoutManager = LinearLayoutManager(requireContext())
            search()
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNav.visibility = View.VISIBLE
    }

    private fun hideKeyboard() {
        if(isAdded){
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.txtSearchBar.windowToken, 0)
        }
    }

    private fun search(){
        binding.txtSearchBar.editText!!.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                performSearch()
            }
            true
        }
    }

    private fun performSearch() {
        binding.txtSearchBar.clearFocus()
        hideKeyboard()
        adapter.search(binding.txtSearchBar.editText!!.text.toString())
    }
}