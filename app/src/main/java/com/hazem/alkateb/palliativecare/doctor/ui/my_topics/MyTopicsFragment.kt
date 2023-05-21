package com.hazem.alkateb.palliativecare.doctor.ui.my_topics

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.databinding.FragmentMyTopicsBinding

class MyTopicsFragment : Fragment() {

    private lateinit var binding: FragmentMyTopicsBinding
    private lateinit var adapter: MyTopicsDoctorAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val myTopicsViewModel = ViewModelProvider(this)[MyTopicsViewModel::class.java]

        binding = FragmentMyTopicsBinding.inflate(inflater, container, false)

        if (isAdded){
            myTopicsViewModel.getMyTopics(requireActivity())
            binding.shimmerFrameLayout.startShimmer()
        }

        binding.txtSearchBar.setEndIconOnClickListener {
            binding.txtSearchBar.editText!!.text.clear()
            binding.txtSearchBar.clearFocus()
            hideKeyboard()
            adapter.stopSearch()
        }

        myTopicsViewModel.dataTopics.observe(viewLifecycleOwner){
            binding.shimmerFrameLayout.stopShimmer()
            binding.shimmerFrameLayout.visibility = View.INVISIBLE
            binding.rvShowTopics.visibility = View.VISIBLE
            adapter = MyTopicsDoctorAdapter(requireContext(),it)
            binding.rvShowTopics.adapter = adapter
            binding.rvShowTopics.layoutManager = LinearLayoutManager(requireContext())
            search()
        }

        binding.rvShowTopics.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 10 && binding.btnAddTopic.isExtended) {
                    binding.btnAddTopic.shrink()
                }
                if (dy < -10 && !binding.btnAddTopic.isExtended) {
                    binding.btnAddTopic.extend()
                }
                if (!recyclerView.canScrollVertically(-1)) {
                    binding.btnAddTopic.extend()
                }
            }
        })

        binding.btnAddTopic.setOnClickListener {
            if (isAdded)
                findNavController().navigate(R.id.action_navigation_my_topics_doctor_to_addTopicFragment)
        }

        return binding.root
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

    private fun hideKeyboard() {
        if(isAdded){
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.txtSearchBar.windowToken, 0)
        }
    }

    override fun onResume() {
        super.onResume()
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNav.visibility = View.VISIBLE
    }
}