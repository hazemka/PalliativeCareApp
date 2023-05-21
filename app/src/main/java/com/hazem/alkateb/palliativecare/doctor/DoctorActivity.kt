package com.hazem.alkateb.palliativecare.doctor

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.hazem.alkateb.palliativecare.R
import com.hazem.alkateb.palliativecare.databinding.ActivityDoctorBinding

class DoctorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_doctor)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home_doctor, R.id.navigation_my_topics_doctor, R.id.navigation_profile_doctor
            )
        )
        navView.setupWithNavController(navController)

    }

    fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_right_to_left
                ,R.anim.enter_left_to_right,R.anim.exit_left_to_right)
            .replace(R.id.nav_host_fragment_activity_doctor,fragment)
            .addToBackStack(null)
            .commit()
    }
}