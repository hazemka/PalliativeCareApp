package com.hazem.alkateb.palliativecare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hazem.alkateb.palliativecare.databinding.ActivityMainBinding
import com.hazem.alkateb.palliativecare.fragments.login.LoginFragment
import com.hazem.alkateb.palliativecare.fragments.welcome.WelcomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE)
        if (sharedPreferences.getBoolean("firstTime", true)) {
            addFragment(WelcomeFragment())
            sharedPreferences.edit().putBoolean("firstTime", false).apply()
        }else{
            addFragment(LoginFragment())
        }
    }

    private fun addFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container,fragment)
            .commit()
    }

    fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_right_to_left,R.anim.exit_right_to_left
                ,R.anim.enter_left_to_right,R.anim.exit_left_to_right)
            .replace(R.id.main_container,fragment)
            .addToBackStack(null)
            .commit()
    }
}