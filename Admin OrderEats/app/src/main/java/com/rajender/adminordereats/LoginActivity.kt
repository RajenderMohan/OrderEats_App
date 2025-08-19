package com.rajender.adminordereats

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
import com.rajender.adminordereats.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    // Define NEW animation start delays for the unique sequence
    private val DELAY_LOGO = 0L
    private val DELAY_MAIN_TITLE = 200L       // "OrderEats" title
    private val DELAY_SUB_HEADER = 350L     // "Login To Your Admin Dashboard"
    private val DELAY_EMAIL_FIELD = 500L
    private val DELAY_PASSWORD_FIELD = 600L // Slightly after email
    private val DELAY_OR_TEXT = 750L        // "Or" text
    private val DELAY_CONTINUE_TEXT = 750L  // "Continue With" - same time as "Or"
    private val DELAY_SOCIAL_BUTTONS_GROUP = 900L // Delay for the start of the social button group
    private val DELAY_LOGIN_BUTTON = 1050L
    private val DELAY_SIGNUP_TEXT = 1200L
    // Assuming textView12 (Dev Name) is also to be animated
    private val DELAY_DEV_NAME = 1350L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Call early
        setContentView(binding.root)

        // Activity Enter Transition (if LoginActivity is NOT the launcher)
        // Ensure fade_in_activity and fade_out_activity exist in res/anim
        overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity)

        // --- Load ALL Unique Animations (Ensure these are in res/anim) ---
        // These names must match your actual animation XML files
        val dropBounceLogo = AnimationUtils.loadAnimation(this, R.anim.drop_bounce_logo)
        val titleRevealUp = AnimationUtils.loadAnimation(this, R.anim.title_reveal_up)
        val slideInFromLeftOvershoot = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left_overshoot)
        val slideInFromRightOvershoot = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right_overshoot)
        val buttonEmergeRotate = AnimationUtils.loadAnimation(this, R.anim.button_emerge_rotate)
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in) // General purpose fade
        val subtleRise = AnimationUtils.loadAnimation(this, R.anim.subtle_rise_fade_in) // For less prominent text
        val clickScale = AnimationUtils.loadAnimation(this, R.anim.click_scale) // For button clicks


        // --- Prepare Views for Staggered Animation (Set to INVISIBLE initially) ---
        val viewsToAnimate = listOf(
            binding.imageView3, binding.textView6, binding.textView8,
            binding.editTextTextEmailAddress, binding.editTextTextPassword,
            binding.textView9, binding.textView10, binding.button2, binding.button3,
            binding.loginbutton, binding.donthavebutton, binding.textView12 // Added textView12
        )
        viewsToAnimate.forEach { it.visibility = View.INVISIBLE }

        val handler = Handler(Looper.getMainLooper())

        // --- Start UNIQUE Staggered Entrance Animations ---

        // Logo
        applyAnimationWithDelay(binding.imageView3, dropBounceLogo, DELAY_LOGO, handler)

        // Main Title
        applyAnimationWithDelay(binding.textView6, titleRevealUp, DELAY_MAIN_TITLE, handler)

        // Sub Header "Login To Your Admin Dashboard" (using a generic fadeIn or subtleRise)
        applyAnimationWithDelay(binding.textView8, subtleRise, DELAY_SUB_HEADER, handler)

        // Email Field
        applyAnimationWithDelay(binding.editTextTextEmailAddress, slideInFromLeftOvershoot, DELAY_EMAIL_FIELD, handler)

        // Password Field
        applyAnimationWithDelay(binding.editTextTextPassword, slideInFromRightOvershoot, DELAY_PASSWORD_FIELD, handler)

        // "Or" Text
        applyAnimationWithDelay(binding.textView9, fadeIn, DELAY_OR_TEXT, handler)

        // "Continue With" Text
        applyAnimationWithDelay(binding.textView10, fadeIn, DELAY_CONTINUE_TEXT, handler)

        // Social Login Buttons - Grouped with internal stagger
        handler.postDelayed({
            // Google Button
            binding.button2.visibility = View.VISIBLE
            binding.button2.startAnimation(buttonEmergeRotate)

            // Facebook Button - Staggered slightly after Google button
            Handler(Looper.getMainLooper()).postDelayed({
                binding.button3.visibility = View.VISIBLE
                binding.button3.startAnimation(buttonEmergeRotate)
            }, 150) // 150ms delay for the second social button relative to the first
        }, DELAY_SOCIAL_BUTTONS_GROUP)

        // Login Button
        applyAnimationWithDelay(binding.loginbutton, buttonEmergeRotate, DELAY_LOGIN_BUTTON, handler) // Can reuse buttonEmergeRotate or have a specific one

        // "Don't have account?" Text
        applyAnimationWithDelay(binding.donthavebutton, subtleRise, DELAY_SIGNUP_TEXT, handler)

        // Developer Name (textView12)
        applyAnimationWithDelay(binding.textView12, subtleRise, DELAY_DEV_NAME, handler)


        // --- Click Listeners with Animations and Transitions ---
        binding.loginbutton.setOnClickListener {
            it.startAnimation(clickScale)
            // TODO: Add your actual admin login logic here

            // Example: Navigate to AdminMainActivity (replace with your actual target)
            // For testing shared element transition, temporarily point to SignUpActivity
            // if it has the 'logo_transition' name on its logo.
            // val intent = Intent(this, AdminMainActivity::class.java)
            val intent = Intent(this, SignUpActivity::class.java) // FOR TESTING SHARED ELEMENT

            // Shared Element Transition for the logo
            // The ImageView in the target Activity must also have android:transitionName="logo_transition"
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                binding.imageView3, // The shared view in the current activity
                "logo_transition"   // The transitionName defined in XML
            )
            startActivity(intent, options.toBundle())
            // finish() // Optional: Finish LoginActivity after navigating
        }

        binding.donthavebutton.setOnClickListener {
            it.startAnimation(clickScale)
            // TODO: Decide navigation. If admin sign-up exists, navigate there.
            // Otherwise, this button might not be needed for an admin app.
            // Assuming SignUpActivity exists for the admin app for now.
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            // Ensure slide_in_right_activity and slide_out_left_activity exist
            overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity)
        }

        binding.button2.setOnClickListener { // Google Button
            it.startAnimation(clickScale)
            // TODO: Implement Google Sign-In for Admin
        }

        binding.button3.setOnClickListener { // Facebook Button
            it.startAnimation(clickScale)
            // TODO: Implement Facebook Sign-In for Admin
        }
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

    override fun finish() {
        super.finish()
        // Activity Exit Transition (when back button is pressed or finish() is called)
        // Ensure fade_in_activity and fade_out_activity exist
        overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity)
    }
}
