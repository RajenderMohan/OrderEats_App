package com.rajender.ordereats

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.rajender.ordereats.databinding.ActivitySignBinding

class SignActivity : AppCompatActivity() {
    private val binding: ActivitySignBinding by lazy {
        ActivitySignBinding.inflate(layoutInflater)
    }

    // --- Define Delays for Staggering for Sign Up Screen ---
    private val DELAY_LOGO = 100L
    private val DELAY_APP_NAME = DELAY_LOGO + 150L // Slightly faster reveal after logo
    private val DELAY_TAGLINE = DELAY_APP_NAME + 150L
    private val DELAY_SIGNUP_PROMPT = DELAY_TAGLINE + 150L // "Sign Up Here"
    private val DELAY_NAME_FIELD = DELAY_SIGNUP_PROMPT + 200L
    private val DELAY_EMAIL_FIELD = DELAY_NAME_FIELD + 100L // Quicker succession for fields
    private val DELAY_PASSWORD_FIELD = DELAY_EMAIL_FIELD + 100L
    private val DELAY_OR_TEXT = DELAY_PASSWORD_FIELD + 250L
    private val DELAY_SIGNUP_WITH_TEXT = DELAY_OR_TEXT // Converging, start at same time
    private val DELAY_GOOGLE_BUTTON = DELAY_OR_TEXT + 200L
    private val DELAY_FACEBOOK_BUTTON = DELAY_GOOGLE_BUTTON // Can start simultaneously or slightly offset
    private val DELAY_CREATE_ACCOUNT_BUTTON = DELAY_GOOGLE_BUTTON + 250L
    private val DELAY_ALREADY_HAVE_ACCOUNT_PROMPT = DELAY_CREATE_ACCOUNT_BUTTON + 200L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Good to call early
        setContentView(binding.root)

        // --- Click Listeners for SignActivity ---
        binding.createAccount.setOnClickListener {
            // Optional: Add click feedback animation
            // it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.click_scale))
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            // Optional: finish() // if you want to remove SignActivity from back stack
        }

        binding.alreadyhavebutton.setOnClickListener {
            // Optional: Add click feedback animation
            // it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.click_scale))
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            // Optional: finish() // if you want to remove SignActivity from back stack
        }

        // Optional: Add click listeners for Google/Facebook sign-up buttons if implementing them
        // binding.button2.setOnClickListener { /* TODO: Implement Google Sign Up */ }
        // binding.button3.setOnClickListener { /* TODO: Implement Facebook Sign Up */ }


        // --- Prepare Views (Set to INVISIBLE initially) ---
        val viewsToMakeInvisible = listOf(
            binding.imageView3, binding.textView6, binding.textView7, binding.textView8,
            binding.editTextText, // Name field
            binding.editTextTextEmailAddress2, // Email field
            binding.editTextTextPassword, // Password field
            binding.textView9, binding.textView10, binding.button2, binding.button3,
            binding.createAccount, binding.alreadyhavebutton
        )
        viewsToMakeInvisible.forEach { it.visibility = View.INVISIBLE }


        // --- Load ALL Unique Animations (Ensure these are in res/anim) ---
        val dropSettleAnim = AnimationUtils.loadAnimation(this, R.anim.drop_settle_fade_in)
        val zoomCenterAnim = AnimationUtils.loadAnimation(this, R.anim.zoom_center_fade_in)
        val slideUpRotateAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up_rotate_fade_in)
        val focusPulseAnim = AnimationUtils.loadAnimation(this, R.anim.focus_pulse_fade_in)

        val slideInFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left)
        // For the middle EditText, using a slide up. You can use fade_in_slide_up_delayed or a more specific one.
        val slideUpForEmail = AnimationUtils.loadAnimation(this, R.anim.fade_in_slide_up_delayed) // Or a custom slide_from_bottom
        val slideInFromRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right)

        val convergeLeftAnim = AnimationUtils.loadAnimation(this, R.anim.converge_fade_in_left)
        val convergeRightAnim = AnimationUtils.loadAnimation(this, R.anim.converge_fade_in_right)
        val flipInLeftAnim = AnimationUtils.loadAnimation(this, R.anim.flip_in_from_left_y)
        val flipInRightAnim = AnimationUtils.loadAnimation(this, R.anim.flip_in_from_right_y)
        val heroPulseAnim = AnimationUtils.loadAnimation(this, R.anim.hero_pulse_scale_fade_in)
        val subtleRiseAnim = AnimationUtils.loadAnimation(this, R.anim.subtle_rise_fade_in) // For "Already have account"

        val handler = Handler(Looper.getMainLooper())

        // --- Apply Unique Animations with Staggering using Handler ---

        // Logo
        applyAnimationWithDelay(binding.imageView3, dropSettleAnim, DELAY_LOGO, handler)

        // Top Text
        applyAnimationWithDelay(binding.textView6, zoomCenterAnim, DELAY_APP_NAME, handler) // "OrderEats"
        applyAnimationWithDelay(binding.textView7, slideUpRotateAnim, DELAY_TAGLINE, handler) // "Deliver Favorite Food"
        applyAnimationWithDelay(binding.textView8, focusPulseAnim, DELAY_SIGNUP_PROMPT, handler) // "Sign Up Here"

        // EditTexts - Staggered entrance
        applyAnimationWithDelay(binding.editTextText, slideInFromLeft, DELAY_NAME_FIELD, handler) // Name
        applyAnimationWithDelay(binding.editTextTextEmailAddress2, slideUpForEmail, DELAY_EMAIL_FIELD, handler) // Email
        applyAnimationWithDelay(binding.editTextTextPassword, slideInFromRight, DELAY_PASSWORD_FIELD, handler) // Password

        // "Or" and "Sign Up With" - Converging
        applyAnimationWithDelay(binding.textView9, convergeLeftAnim, DELAY_OR_TEXT, handler) // "Or"
        applyAnimationWithDelay(binding.textView10, convergeRightAnim, DELAY_SIGNUP_WITH_TEXT, handler) // "Sign Up With"

        // Social Buttons - Flipping
        applyAnimationWithDelay(binding.button2, flipInLeftAnim, DELAY_GOOGLE_BUTTON, handler)    // Google Button
        applyAnimationWithDelay(binding.button3, flipInRightAnim, DELAY_FACEBOOK_BUTTON, handler)    // Facebook Button

        // Create Account Button
        applyAnimationWithDelay(binding.createAccount, heroPulseAnim, DELAY_CREATE_ACCOUNT_BUTTON, handler)

        // "Already Have An Account?"
        applyAnimationWithDelay(binding.alreadyhavebutton, subtleRiseAnim, DELAY_ALREADY_HAVE_ACCOUNT_PROMPT, handler)
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
