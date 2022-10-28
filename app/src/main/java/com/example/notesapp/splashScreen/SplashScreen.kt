package com.example.notesapp.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.example.notesapp.MainActivity
import com.example.notesapp.R
import com.example.notesapp.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)

        // hide action bar
        supportActionBar?.hide()

        // Animation for logo and title
        val logoAnim = AnimationUtils.loadAnimation(this, R.anim.splash_logo_anim)
        binding.splashLogo.animation = logoAnim

        val titleAnim = AnimationUtils.loadAnimation(this, R.anim.splash_title_anim)
        binding.splashTitle.animation = titleAnim

        //go to next activity
        Handler(Looper.myLooper()!!).postDelayed(
            {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 2500
        )
    }
}