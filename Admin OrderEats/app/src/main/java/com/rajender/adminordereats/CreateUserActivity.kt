package com.rajender.adminordereats

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.rajender.adminordereats.databinding.ActivityCreateUserBinding

class CreateUserActivity : AppCompatActivity() {
    private val binding: ActivityCreateUserBinding by lazy {
        ActivityCreateUserBinding.inflate(layoutInflater)
    }

    // Define NEW animation start delays for the unique sequence
    private val DELAY_BACK_BUTTON = 0L
    private val DELAY_LOGO = 150L // After back button
    private val DELAY_MAIN_TITLE = 450L
    private val DELAY_SUB_HEADER = 650L
    private val DELAY_ADMIN_NAME_FIELD = 850L
    private val DELAY_EMAIL_FIELD = 950L
    private val DELAY_PASSWORD_FIELD = 1050L
    private val DELAY_CREATE_BUTTON = 1200L
    // private val DELAY_DEV_NAME = 1350L // If you have textView12 for dev name from previous example

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge() // Call this if you intend to use edge-to-edge
        setContentView(binding.root)

        // Activity Enter Transition (from wherever it's launched)
        // You might want to customize this based on where CreateUserActivity is launched from
        overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity)

        // --- Load UNIQUE Animations ---
        val slideInFromLeftGentle = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left_gentle)
        val dropBounceLogo = AnimationUtils.loadAnimation(this, R.anim.drop_bounce_logo)
        val titleRevealUp = AnimationUtils.loadAnimation(this, R.anim.title_reveal_up)
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in) // For elements like sub-header
        val slideInLeftOvershoot = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left_overshoot)
        val slideInRightOvershoot = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right_overshoot)
        val buttonEmergeRotate = AnimationUtils.loadAnimation(this, R.anim.button_emerge_rotate)
        val clickScale = AnimationUtils.loadAnimation(this, R.anim.click_scale)


        // --- Prepare Views for Staggered Animation ---
        // (Set visibility to INVISIBLE to prevent flash)
        val viewsToAnimate = listOfNotNull(
            binding.backButton, binding.imageView3, binding.textView6, binding.textView8,
            binding.adminName, binding.editTextTextEmailAddress,
            binding.editTextTextPassword, binding.Createbutton
            // binding.textView12 // Add if you have this view from the XML
        )
        viewsToAnimate.forEach { it.visibility = View.INVISIBLE }

        val handler = Handler(Looper.getMainLooper())

        // --- Apply Unique Entrance Animations ---

        // 1. Back button
        handler.postDelayed({
            binding.backButton.visibility = View.VISIBLE
            binding.backButton.startAnimation(slideInFromLeftGentle)
        }, DELAY_BACK_BUTTON)

        // 2. Logo
        handler.postDelayed({
            binding.imageView3.visibility = View.VISIBLE
            binding.imageView3.startAnimation(dropBounceLogo)
        }, DELAY_LOGO)

        // 3. Main Title ("OrderEats")
        handler.postDelayed({
            binding.textView6.visibility = View.VISIBLE
            binding.textView6.startAnimation(titleRevealUp)
        }, DELAY_MAIN_TITLE)

        // 4. Sub-Header ("Create New User Admin")
        handler.postDelayed({
            binding.textView8.visibility = View.VISIBLE
            binding.textView8.startAnimation(fadeIn) // Using simple fade for sub-header
        }, DELAY_SUB_HEADER)

        // 5. Form elements - animated individually for unique effects
        handler.postDelayed({
            binding.adminName.visibility = View.VISIBLE
            binding.adminName.startAnimation(slideInLeftOvershoot)
        }, DELAY_ADMIN_NAME_FIELD)

        handler.postDelayed({
            binding.editTextTextEmailAddress.visibility = View.VISIBLE
            binding.editTextTextEmailAddress.startAnimation(slideInRightOvershoot)
        }, DELAY_EMAIL_FIELD)

        handler.postDelayed({
            binding.editTextTextPassword.visibility = View.VISIBLE
            binding.editTextTextPassword.startAnimation(slideInLeftOvershoot)
        }, DELAY_PASSWORD_FIELD)

        // 6. Create Button
        handler.postDelayed({
            binding.Createbutton.visibility = View.VISIBLE
            binding.Createbutton.startAnimation(buttonEmergeRotate)
        }, DELAY_CREATE_BUTTON)

        // If you have a textView12 for developer name at the bottom:
        // handler.postDelayed({
        //     binding.textView12.visibility = View.VISIBLE
        //     binding.textView12.startAnimation(fadeIn)
        // }, DELAY_DEV_NAME)


        // --- Click Listeners & Transforms ---
        binding.backButton.setOnClickListener {
            it.startAnimation(clickScale) // Apply click animation
            finish()
            // The exit transition is handled in the finish() override
        }

        binding.Createbutton.setOnClickListener {
            it.startAnimation(clickScale) // Apply click animation
            // TODO: Add your user creation logic here.
            // val name = binding.adminName.text.toString()
            // val email = binding.editTextTextEmailAddress.text.toString()
            // val password = binding.editTextTextPassword.text.toString()
            // if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            //     // Call your function to create the user
            //     // Toast.makeText(this, "Creating User...", Toast.LENGTH_SHORT).show()
            //     // finish() or navigate to another screen
            // } else {
            //     // Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show()
            // }
        }
    }

    override fun finish() {
        super.finish()
        // Ensure consistent exit transition
        overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity)
    }
}
