package com.rajender.ordereats // Make sure this matches your package name

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button // Or androidx.appcompat.widget.AppCompatButton
import android.widget.ImageView // Keep if used directly, otherwise can be removed
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.rajender.ordereats.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    // binding property is correctly defined here
    private val binding: ActivityStartBinding by lazy {
        ActivityStartBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Call this before setContentView if needed
        setContentView(binding.root) // Use ViewBinding to set content view

        binding.nextButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            // Optional: finish() this activity if you don't want users to come back to it
            // finish()
        }

        // Load Animations
        val fadeInAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val slideUpButtonAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_button)


        val imageCardView: CardView = binding.root.findViewById(R.id.circularCardView)
        val mainText: TextView = binding.root.findViewById(R.id.textView4)


        // Start Animations
        imageCardView.startAnimation(fadeInAnimation)
        mainText.startAnimation(fadeInAnimation) // Re-using fade_in for the text
        binding.nextButton.startAnimation(slideUpButtonAnimation) // Animate the button accessed via binding

    }
}
