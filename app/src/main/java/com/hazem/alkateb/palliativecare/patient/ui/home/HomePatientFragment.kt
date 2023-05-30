package com.hazem.alkateb.palliativecare.patient.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.models.SlideModel
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.databinding.FragmentHomeBinding
import com.hazem.alkateb.palliativecare.databinding.FragmentHomePatientBinding
import com.hazem.alkateb.palliativecare.doctor.ui.home.MyTopicsAdapter
import com.hazem.alkateb.palliativecare.patient.ui.topics.TopicPatientAdapter

class HomePatientFragment : Fragment() {
    private lateinit  var binding: FragmentHomePatientBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val homePatientViewModel =
            ViewModelProvider(this)[HomePatientViewModel::class.java]

        binding = FragmentHomePatientBinding.inflate(inflater, container, false)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.image_1))
        imageList.add(SlideModel(R.drawable.image_2))
        imageList.add(SlideModel(R.drawable.image_3))

        binding.imageSlider.setImageList(imageList)

        homePatientViewModel.getAllTopics()


        homePatientViewModel.allTopics.observe(viewLifecycleOwner){
            binding.shimmerFrameLayout.stopShimmer()
            binding.shimmerFrameLayout.visibility = View.INVISIBLE
            binding.rvYourTopics.visibility = View.VISIBLE
            val adapter = MyTopicsAdapter(requireContext(),it,"patent")
            binding.rvYourTopics.adapter = adapter
            binding.rvYourTopics.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }

        return binding.root
    }
}