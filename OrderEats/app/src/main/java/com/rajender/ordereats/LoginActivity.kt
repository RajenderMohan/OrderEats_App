package com.rajender.ordereats

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rajender.ordereats.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.loginbutton.setOnClickListener {
            val intent = Intent(this, SignActivity::class.java)
            startActivity(intent)
        }
        binding.donthavebutton.setOnClickListener {
            val intent = Intent(this, SignActivity::class.java)
            startActivity(intent)
        }






        // Load Animations
        val fadeInSlideDown = AnimationUtils.loadAnimation(this, R.anim.fade_in_slide_down)
        val slideInFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left)
        val slideInFromRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right)
        val fadeInSlideUp = AnimationUtils.loadAnimation(this, R.anim.fade_in_slide_up_delayed) // Base animation

        // --- Apply to Logo and Top Text ---
        binding.imageView3.startAnimation(fadeInSlideDown)
        binding.textView6.startAnimation(fadeInSlideDown) // "OrderEats"
        binding.textView7.startAnimation(fadeInSlideDown) // "Deliver Favorite Food"
        binding.textView8.startAnimation(fadeInSlideDown) // "Login To Your Account"

        // --- Apply to EditTexts ---
        binding.editTextTextEmailAddress.startAnimation(slideInFromLeft)
        binding.editTextTextPassword.startAnimation(slideInFromRight)

        // We'll clone the base animation and modify its startOffset for each
        applyAnimationWithDelay(binding.textView9, fadeInSlideUp, 600) // "Or"
        applyAnimationWithDelay(binding.textView10, fadeInSlideUp, 700) // "Continue With"

        applyAnimationWithDelay(binding.button2, fadeInSlideUp, 800)    // Google Button
        applyAnimationWithDelay(binding.button3, fadeInSlideUp, 900)    // Facebook Button
        applyAnimationWithDelay(binding.loginbutton, fadeInSlideUp, 1000) // Login Button
        applyAnimationWithDelay(binding.donthavebutton, fadeInSlideUp, 1100) // Don't have account

        // You can choose not to animate the bottom "Designed by" text or give it a simple fade
        // binding.textView12.startAnimation(fadeInSlideUp.apply { startOffset = 1200 })
    }

    private fun applyAnimationWithDelay(view: View, baseAnimation: Animation, delay: Long) {
        val newAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in_slide_up_delayed) // Reload to get a fresh instance
        newAnimation.startOffset = delay
        view.startAnimation(newAnimation)
    }


}