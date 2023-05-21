package com.hazem.alkateb.palliativecare.patient.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hazem.alkateb.palliativecare.databinding.FragmentHomeBinding
import com.hazem.alkateb.palliativecare.databinding.FragmentHomePatientBinding

class HomePatientFragment : Fragment() {
    private lateinit  var binding: FragmentHomePatientBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val homePatientViewModel =
            ViewModelProvider(this)[HomePatientViewModel::class.java]

        binding = FragmentHomePatientBinding.inflate(inflater, container, false)


        return binding.root
    }
}