package com.rajender.ordereats

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.rajender.ordereats.databinding.ActivitySplashScreenBinding

// Rename your class to follow Kotlin conventions (PascalCase)
// Also, the @Suppress("DEPRECATION") might not be needed if Handler(Looper) is used.
@SuppressLint("CustomSplashScreen") // It's good practice to acknowledge this
class SplashScreenActivity : AppCompatActivity() { // 2. Rename class

    private lateinit var binding: ActivitySplashScreenBinding // 3. Declare binding variable

    // Animation Delays (can be adjusted)
    private val LOGO_ANIM_DELAY = 100L
    private val TITLE_ANIM_DELAY = 400L // Stagger after logo
    private val SUBTITLE_ANIM_DELAY = 700L // Stagger after title
    private val DEV_NAME_ANIM_DELAY = 1000L // Stagger after subtitle

    // Total time splash screen is visible. Should be long enough for animations + a brief view.
    // Make sure this is longer than your longest animation sequence.
    private val SPLASH_DISPLAY_LENGTH: Long = 2800 // Adjusted from 1000ms

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Keep if you use edge-to-edge

        // 4. Initialize ViewBinding
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root) // Use binding.root

        // 5. Hide views initially to animate them in
        binding.imageView.visibility = View.INVISIBLE
        binding.textView.visibility = View.INVISIBLE
        binding.textView2.visibility = View.INVISIBLE
        binding.textView3.visibility = View.INVISIBLE

        // 6. Load animations
        val logoAnim = AnimationUtils.loadAnimation(this, R.anim.splash_logo_animation)
        val titleAnim = AnimationUtils.loadAnimation(this, R.anim.splash_title_animation)
        val subtitleAnim = AnimationUtils.loadAnimation(this, R.anim.splash_subtitle_animation)
        val devNameAnim = AnimationUtils.loadAnimation(this, R.anim.splash_dev_name_animation)

        val handler = Handler(Looper.getMainLooper()) // Good practice to use Looper.getMainLooper()

        // 7. Start animations with delays
        handler.postDelayed({
            binding.imageView.visibility = View.VISIBLE
            binding.imageView.startAnimation(logoAnim)
        }, LOGO_ANIM_DELAY)

        handler.postDelayed({
            binding.textView.visibility = View.VISIBLE
            binding.textView.startAnimation(titleAnim)
        }, TITLE_ANIM_DELAY)

        handler.postDelayed({
            binding.textView2.visibility = View.VISIBLE
            binding.textView2.startAnimation(subtitleAnim)
        }, SUBTITLE_ANIM_DELAY)

        handler.postDelayed({
            binding.textView3.visibility = View.VISIBLE
            binding.textView3.startAnimation(devNameAnim)
        }, DEV_NAME_ANIM_DELAY)

        // 8. Navigate to the next activity after the splash display length
        handler.postDelayed({
            val intent = Intent(this, StartActivity::class.java) // Your target activity
            startActivity(intent)
            finish() // Finish this activity
        }, SPLASH_DISPLAY_LENGTH) // Use the defined constant
    }
}

