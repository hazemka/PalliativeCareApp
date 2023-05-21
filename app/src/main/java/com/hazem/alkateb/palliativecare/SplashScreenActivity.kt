package com.hazem.alkateb.palliativecare

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging


class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val sharedPreferences =  getSharedPreferences("userData", AppCompatActivity.MODE_PRIVATE)

        FirebaseMessaging.getInstance()
            .token
            .addOnSuccessListener {
                if (sharedPreferences.edit().putString("token",it.toString()).commit()){
                    goToMain(1000)
                }
            }
            .addOnFailureListener {
                goToMain(1500)
            }
    }

    private fun goToMain(delay:Int){
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        },delay.toLong())
    }
}