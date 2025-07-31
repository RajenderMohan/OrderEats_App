package com.rajender.ordereats

import android.content.Intent
import android.os.Bundle
import android.view.View // Required for the helper function
import android.view.animation.Animation // Required for Animation type
import android.view.animation.AnimationUtils // Required for loading animations
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
// ViewCompat and WindowInsetsCompat are fine, related to enableEdgeToEdge
// import androidx.core.view.ViewCompat
// import androidx.core.view.WindowInsetsCompat
import com.rajender.ordereats.databinding.ActivitySignBinding

class SignActivity : AppCompatActivity() {
    private val binding: ActivitySignBinding by lazy {
        ActivitySignBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Good to call early
        setContentView(binding.root)

        binding.alreadyhavebutton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            // Optional: finish() // if you want to remove SignActivity from back stack
        }





        // --- START OF ANIMATION CODE ---

        // Load Animations
        val fadeInSlideDown = AnimationUtils.loadAnimation(this, R.anim.fade_in_slide_down)
        val slideInFromLeftBase = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left)
        val slideInFromRightBase = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right)

        // Apply to Logo and Top Text
        binding.imageView3.startAnimation(fadeInSlideDown)
        binding.textView6.startAnimation(fadeInSlideDown) // "OrderEats"
        binding.textView7.startAnimation(fadeInSlideDown) // "Deliver Favorite Food"
        binding.textView8.startAnimation(fadeInSlideDown) // "Sign Up Here"

        // Apply to EditTexts with delays
        // Create new instances for each EditText to set different startOffsets if needed
        val animEditText1 = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left).apply {
            startOffset = 300
        }
        binding.editTextText.startAnimation(animEditText1) // Name

        val animEditText2 = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right).apply {
            startOffset = 450
        }
        binding.editTextTextEmailAddress2.startAnimation(animEditText2) // Email

        val animEditText3 = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left).apply {
            startOffset = 600
        }
        binding.editTextTextPassword.startAnimation(animEditText3) // Password


        // Apply to "Or", "Sign Up With", and Buttons with staggered delays
        applyAnimationWithDelay(binding.textView9, R.anim.fade_in_slide_up_delayed, 700) // "Or"
        applyAnimationWithDelay(binding.textView10, R.anim.fade_in_slide_up_delayed, 800) // "Sign Up With"

        applyAnimationWithDelay(binding.button2, R.anim.fade_in_slide_up_delayed, 900)    // Google Button
        applyAnimationWithDelay(binding.button3, R.anim.fade_in_slide_up_delayed, 1000)   // Facebook Button
        // Ensure your "Create Account" button has the ID "loginbutton" in activity_sign.xml
        // If it's different, change binding.loginbutton accordingly.
        applyAnimationWithDelay(binding.loginbutton, R.anim.fade_in_slide_up_delayed, 1100) // Create Account Button
        applyAnimationWithDelay(binding.alreadyhavebutton, R.anim.fade_in_slide_up_delayed, 1200) // "Already Have An Account?"

        // --- END OF ANIMATION CODE ---
    }

    // Helper function to apply animation with a specific delay
    private fun applyAnimationWithDelay(view: View, animationResId: Int, delay: Long) {
        val animation = AnimationUtils.loadAnimation(this, animationResId)
        animation.startOffset = delay
        view.startAnimation(animation)
    }
}
