package com.rajender.ordereats // Make sure this matches your package name

// No need for explicit CardView or TextView imports if only accessed via binding
// import android.app.ActivityOptions // Remove this import
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.rajender.ordereats.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    // binding property is correctly defined here using lazy initialization
    private val binding: ActivityStartBinding by lazy {
        ActivityStartBinding.inflate(layoutInflater)
    }

    // Animation Delays (can be adjusted for desired effect)
    private val DELAY_IMAGE_CARD = 200L
    private val DELAY_TEXT_PRIMARY = 500L
    private val DELAY_NEXT_BUTTON = 800L
    private val DELAY_TEXT_SECONDARY = 1200L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Call this before setContentView if needed
        setContentView(binding.root) // Use ViewBinding to set content view

        // --- 1. Load Animations ---
        val scaleBounceFadeInImageAnim = AnimationUtils.loadAnimation(this, R.anim.scale_bounce_fade_in_image)
        val slideUpFadeInTextAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up_fade_in_text)
        val slideUpButtonAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up_from_bottom_button)
        val fadeInSlowAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in_slow)
        val clickScaleAnim = AnimationUtils.loadAnimation(this, R.anim.click_scale)

        // --- 2. Prepare Views for Animation (Set initial visibility to INVISIBLE) ---
        binding.circularCardView.visibility = View.INVISIBLE
        binding.textView4.visibility = View.INVISIBLE
        binding.nextButton.visibility = View.INVISIBLE
        binding.textView5.visibility = View.INVISIBLE

        val handler = Handler(Looper.getMainLooper())

        // --- 3. Apply Staggered Entrance Animations ---
        handler.postDelayed({
            binding.circularCardView.visibility = View.VISIBLE
            binding.circularCardView.startAnimation(scaleBounceFadeInImageAnim)
        }, DELAY_IMAGE_CARD)

        handler.postDelayed({
            binding.textView4.visibility = View.VISIBLE
            binding.textView4.startAnimation(slideUpFadeInTextAnim)
        }, DELAY_TEXT_PRIMARY)

        handler.postDelayed({
            binding.nextButton.visibility = View.VISIBLE
            binding.nextButton.startAnimation(slideUpButtonAnim)
        }, DELAY_NEXT_BUTTON)

        handler.postDelayed({
            binding.textView5.visibility = View.VISIBLE
            binding.textView5.startAnimation(fadeInSlowAnim)
        }, DELAY_TEXT_SECONDARY)

        // --- 4. Setup Click Listener for "NEXT" Button (Shared Element Transition REMOVED) ---
        binding.nextButton.setOnClickListener {
            it.startAnimation(clickScaleAnim) // Apply click feedback

            // Delay navigation slightly to allow click animation to be seen
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this@StartActivity, LoginActivity::class.java) // Your next activity
                startActivity(intent) // Start activity WITHOUT transition options
                // Optional: finish() this activity if you don't want users to come back to it
                // finish()
            }, 200) // 200ms delay after click animation starts
        }
    }
}
