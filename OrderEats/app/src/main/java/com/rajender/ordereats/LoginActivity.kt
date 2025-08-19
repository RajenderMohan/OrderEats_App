package com.rajender.ordereats

// Remove unused ViewCompat and WindowInsetsCompat if not used elsewhere
// import androidx.core.view.ViewCompat
// import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.rajender.ordereats.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    // Define delays for staggering (adjust as you see fit)
    private val DELAY_LOGO = 100L
    private val DELAY_APP_NAME = DELAY_LOGO + 200L // Stagger after logo
    private val DELAY_TAGLINE = DELAY_APP_NAME + 200L
    private val DELAY_LOGIN_PROMPT = DELAY_TAGLINE + 200L
    private val DELAY_EMAIL_FIELD = DELAY_LOGIN_PROMPT + 250L
    private val DELAY_PASSWORD_FIELD = DELAY_EMAIL_FIELD // Can start simultaneously or slightly after
    private val DELAY_OR_TEXT = DELAY_PASSWORD_FIELD + 300L
    private val DELAY_CONTINUE_TEXT = DELAY_OR_TEXT // Converging, start at same time as "Or"
    private val DELAY_GOOGLE_BUTTON = DELAY_OR_TEXT + 250L
    private val DELAY_FACEBOOK_BUTTON = DELAY_GOOGLE_BUTTON // Can start simultaneously
    private val DELAY_LOGIN_BUTTON = DELAY_GOOGLE_BUTTON + 300L
    private val DELAY_SIGNUP_PROMPT = DELAY_LOGIN_BUTTON + 200L
    private val DELAY_DEV_NAME = DELAY_SIGNUP_PROMPT + 300L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.loginbutton.setOnClickListener {
            // Consider adding a click animation here too if desired
            // it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.click_scale))
            val intent = Intent(this, SignActivity::class.java)
            startActivity(intent)
        }
        binding.donthavebutton.setOnClickListener {
            // it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.click_scale))
            val intent = Intent(this, SignActivity::class.java)
            startActivity(intent)
        }
        // Add click listeners for Google and Facebook buttons if they have actions
        // binding.button2.setOnClickListener { /* Google sign-in */ }
        // binding.button3.setOnClickListener { /* Facebook sign-in */ }


        // --- Prepare Views (Set to INVISIBLE initially) ---
        // This is good practice so they don't flash before animating
        val viewsToMakeInvisible = listOf(
            binding.imageView3, binding.textView6, binding.textView7, binding.textView8,
            binding.editTextTextEmailAddress, binding.editTextTextPassword,
            binding.textView9, binding.textView10, binding.button2, binding.button3,
            binding.loginbutton, binding.donthavebutton, binding.textView12
        )
        viewsToMakeInvisible.forEach { it.visibility = View.INVISIBLE }


        // --- Load ALL Unique Animations ---
        val dropSettleAnim = AnimationUtils.loadAnimation(this, R.anim.drop_settle_fade_in)
        val zoomCenterAnim = AnimationUtils.loadAnimation(this, R.anim.zoom_center_fade_in)
        val slideUpRotateAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up_rotate_fade_in)
        val focusPulseAnim = AnimationUtils.loadAnimation(this, R.anim.focus_pulse_fade_in)

        // Using your original slide_in_from_left/right for EditTexts for simplicity here,
        // but you could use slide_in_overshoot_left/right if you created them
        val slideInFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left)
        val slideInFromRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right)

        val convergeLeftAnim = AnimationUtils.loadAnimation(this, R.anim.converge_fade_in_left)
        val convergeRightAnim = AnimationUtils.loadAnimation(this, R.anim.converge_fade_in_right)
        val flipInLeftAnim = AnimationUtils.loadAnimation(this, R.anim.flip_in_from_left_y)
        val flipInRightAnim = AnimationUtils.loadAnimation(this, R.anim.flip_in_from_right_y)
        val heroPulseAnim = AnimationUtils.loadAnimation(this, R.anim.hero_pulse_scale_fade_in)
        val subtleRiseAnim = AnimationUtils.loadAnimation(this, R.anim.subtle_rise_fade_in)
        // You might want a simple slide_up_fade_in for the "donthavebutton"
        val slideUpFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_slide_up_delayed) // Reusing your existing one for this

        val handler = Handler(Looper.getMainLooper())

        // --- Apply Unique Animations with Staggering using Handler ---

        // Logo
        applyAnimationWithDelay(binding.imageView3, dropSettleAnim, DELAY_LOGO, handler)

        // Top Text
        applyAnimationWithDelay(binding.textView6, zoomCenterAnim, DELAY_APP_NAME, handler) // "OrderEats"
        applyAnimationWithDelay(binding.textView7, slideUpRotateAnim, DELAY_TAGLINE, handler) // "Deliver Favorite Food"
        applyAnimationWithDelay(binding.textView8, focusPulseAnim, DELAY_LOGIN_PROMPT, handler) // "Login To Your Account"

        // EditTexts
        applyAnimationWithDelay(binding.editTextTextEmailAddress, slideInFromLeft, DELAY_EMAIL_FIELD, handler)
        applyAnimationWithDelay(binding.editTextTextPassword, slideInFromRight, DELAY_PASSWORD_FIELD, handler)

        // "Or" and "Continue With" - Converging
        applyAnimationWithDelay(binding.textView9, convergeLeftAnim, DELAY_OR_TEXT, handler) // "Or"
        applyAnimationWithDelay(binding.textView10, convergeRightAnim, DELAY_CONTINUE_TEXT, handler) // "Continue With"

        // Social Buttons - Flipping
        applyAnimationWithDelay(binding.button2, flipInLeftAnim, DELAY_GOOGLE_BUTTON, handler)    // Google Button
        applyAnimationWithDelay(binding.button3, flipInRightAnim, DELAY_FACEBOOK_BUTTON, handler)    // Facebook Button

        // Login Button
        applyAnimationWithDelay(binding.loginbutton, heroPulseAnim, DELAY_LOGIN_BUTTON, handler) // Login Button

        // "Don't have account"
        applyAnimationWithDelay(binding.donthavebutton, slideUpFadeIn, DELAY_SIGNUP_PROMPT, handler)

        // Developer Name
        applyAnimationWithDelay(binding.textView12, subtleRiseAnim, DELAY_DEV_NAME, handler)
    }

    /**
     * Helper function to apply an animation to a view after a delay.
     * Makes the view visible just before starting the animation.
     */
    private fun applyAnimationWithDelay(view: View, animation: Animation, delay: Long, handler: Handler) {
        handler.postDelayed({
            view.visibility = View.VISIBLE
            view.startAnimation(animation)
        }, delay)
    }
}
