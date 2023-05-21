package com.hazem.alkateb.palliativecare.fragments.choose_account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hazem.alkateb.palliativecare.MainActivity
import com.hazem.alkateb.palliativecare.databinding.FragmentChooseAccountBinding
import com.hazem.alkateb.palliativecare.fragments.create_new_account.CreateNewAccountFragment

class ChooseAccountFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentChooseAccountBinding.inflate(inflater, container, false)

        binding.btnDoctor.setOnClickListener {
            goToCreateAccount("doctor")
        }

        binding.btnPatient.setOnClickListener {
            goToCreateAccount("patient")
        }

        return binding.root
    }

    private fun goToCreateAccount(userType:String){
        val fragment = CreateNewAccountFragment()
        val b = Bundle()
        b.putString("userType",userType)
        fragment.arguments = b
        if (isAdded)
            (requireActivity() as MainActivity).replaceFragment(fragment)
    }

}