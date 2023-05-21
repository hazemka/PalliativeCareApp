package com.hazem.alkateb.palliativecare.fragments.welcome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hazem.alkateb.palliativecare.MainActivity
import com.hazem.alkateb.palliativecare.databinding.FragmentWelcomeBinding
import com.hazem.alkateb.palliativecare.fragments.login.LoginFragment

class WelcomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentWelcomeBinding.inflate(inflater,container,false)

        binding.button.setOnClickListener {
            if (isAdded)
                (requireActivity() as MainActivity).replaceFragment(LoginFragment())
        }

        return binding.root
    }
}