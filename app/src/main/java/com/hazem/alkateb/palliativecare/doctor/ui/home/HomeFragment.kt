package com.hazem.alkateb.palliativecare.doctor.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.databinding.FragmentHomeBinding
import com.hazem.alkateb.palliativecare.model.Topic
import java.util.regex.Pattern

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        getUserName()

        if (isAdded){
            homeViewModel.getMyTopics(requireActivity())
            binding.shimmerFrameLayout.startShimmer()
        }

        homeViewModel.dataTopics.observe(viewLifecycleOwner){data->
            binding.shimmerFrameLayout.stopShimmer()
            binding.shimmerFrameLayout.visibility = View.INVISIBLE
            binding.rvYourTopics.visibility = View.VISIBLE
            val myTopicsAdapter = MyTopicsAdapter(requireContext(),data)
            binding.rvYourTopics.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            binding.rvYourTopics.adapter = myTopicsAdapter
        }


        val list :ArrayList<PieEntry> = ArrayList()
        list.add(PieEntry(50f,"تعليق"))
        list.add(PieEntry(100f,"منشور"))
        list.add(PieEntry(200f,"متابعة"))
        list.add(PieEntry(50f,"زيارة"))

        val pieDataSet = PieDataSet(list,"")

        pieDataSet.valueTextSize = 20f
        pieDataSet.valueTextColor = Color.WHITE
        pieDataSet.colors = mutableListOf(resources.getColor(R.color.c1)
        ,resources.getColor(R.color.c2),resources.getColor(R.color.c3)
        ,resources.getColor(R.color.c4))

        val pieData = PieData(pieDataSet)

        binding.pieChart.data = pieData

        binding.pieChart.description.text = ""

        binding.pieChart.setEntryLabelTextSize(17f)

        binding.pieChart.isDrawHoleEnabled = false

        binding.pieChart.animateY(2000)

        return binding.root
    }

    private fun getUserName(){
        val sharedPreferences =  requireActivity().getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)
        binding.txtDoctorName.text = sharedPreferences.getString("userName","")
    }

    override fun onResume() {
        super.onResume()
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNav.visibility = View.VISIBLE
    }
}