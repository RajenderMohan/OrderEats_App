package com.rajender.adminordereats // Ensure this is your correct package name

// Ensure this binding class name matches your layout file (activity_splash_screen.xml)
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.rajender.adminordereats.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {

    private val binding: ActivitySplashScreenBinding by lazy {
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }

    // Durations and Delays
    private val SPLASH_ANIMATION_TOTAL_DURATION = 2000L // Approximate time for all animations to complete
    private val SPLASH_EXTRA_HOLD_DURATION = 500L    // Extra time to show the fully animated screen
    private val SPLASH_DISPLAY_LENGTH = SPLASH_ANIMATION_TOTAL_DURATION + SPLASH_EXTRA_HOLD_DURATION // Total time

    private val DELAY_LOGO_SPLASH = 0L
    private val DELAY_MAIN_TITLE_SPLASH = 400L  // After logo starts
    private val DELAY_SUB_TITLE_SPLASH = 700L   // After main title starts
    private val DELAY_DEV_NAME_SPLASH = 1000L   // After sub title starts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Use ViewBinding to set the content view
        setContentView(binding.root)

        // --- Load Animations (Ensure these XML files are in res/anim) ---
        val logoAnim = AnimationUtils.loadAnimation(this, R.anim.drop_settle_fade_in)
        val mainTitleAnim = AnimationUtils.loadAnimation(this, R.anim.zoom_center_fade_in)
        val subTitleAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up_fade_in)
        val devNameAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up_fade_in)

        // --- Prepare Views for Staggered Animation ---
        // Make sure your activity_splash_screen.xml has these IDs:
        // imageLogo, textView (for "Order Eats"), adminDashboardText (for "Admin Dashboard"), textView3 (for dev name)
        val viewsToAnimate = listOf(
            binding.imageLogo,
            binding.textView,
            binding.adminDashboardText, // Ensure this ID exists in your XML
            binding.textView3
        )
        viewsToAnimate.forEach { it.visibility = View.INVISIBLE }

        val handler = Handler(Looper.getMainLooper())

        // --- Start Staggered Entrance Animations ---
        applyAnimationWithDelay(binding.imageLogo, logoAnim, DELAY_LOGO_SPLASH, handler)
        applyAnimationWithDelay(binding.textView, mainTitleAnim, DELAY_MAIN_TITLE_SPLASH, handler)
        applyAnimationWithDelay(binding.adminDashboardText, subTitleAnim, DELAY_SUB_TITLE_SPLASH, handler)
        applyAnimationWithDelay(binding.textView3, devNameAnim, DELAY_DEV_NAME_SPLASH, handler)

        // --- Navigate to Next Activity after animations and hold time ---
        handler.postDelayed({
            val mainIntent = Intent(this@SplashScreenActivity, LoginActivity::class.java)

            // Optional: Add Shared Element Transition for the logo
            // Ensure 'logo_transition' is also set as android:transitionName on the logo in activity_login.xml
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                binding.imageLogo, // The shared view in the current activity
                "logo_transition"  // The transitionName defined in XML
            )
            startActivity(mainIntent, options.toBundle())

            // If not using shared element, use this for a simple fade transition:
            // startActivity(mainIntent)
            // overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity)

            finish() // Close the splash activity so it's not in the back stack
        }, SPLASH_DISPLAY_LENGTH)
    }

    /**
     * Helper function to apply an animation to a view after a delay.
     * Makes the view visible just before starting the animation.
     */
    private fun applyAnimationWithDelay(view: View, animationToApply: Animation, delay: Long, animHandler: Handler) {
        animHandler.postDelayed({
            view.visibility = View.VISIBLE
            view.startAnimation(animationToApply)
        }, delay)
    }
}
