package com.rajender.adminordereats

import android.os.Bundle
// Removed enableEdgeToEdge and specific ViewCompat/WindowInsetsCompat imports
// if not strictly needed for other purposes, as basic animations don't require them.
// Keep them if you use them for edge-to-edge display.
import android.view.animation.AnimationUtils // Import for animations
import androidx.appcompat.app.AppCompatActivity
import com.rajender.adminordereats.databinding.ActivityAdminProfileBinding
import com.rajender.adminordereats.R // Import R for resources (R.anim)

class AdminProfileActivity : AppCompatActivity() {
    private val binding: ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }

    // Variable to track edit state, you already have this
    private var isEditingEnabled = false // Renamed for clarity from 'isEnable'

    // Helper function to set the editable state of fields, adapted from your logic
    private fun setFieldsEditable(editable: Boolean) {
        binding.name.isEnabled = editable
        binding.address.isEnabled = editable
        binding.email.isEnabled = editable
        binding.phone.isEnabled = editable
        binding.password.isEnabled = editable

        if (editable) {
            binding.name.requestFocus()
            binding.editProfileButton.text = "Editing..." // Visual feedback
            // Consider changing icon or disabling edit button temporarily
        } else {
            binding.editProfileButton.text = "Click Here To Edit" // Reset text
        }
        isEditingEnabled = editable // Update the tracking variable
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // --- Load Animations ---
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_input) // Assuming you have slide_up_input.xml
        val clickScale = AnimationUtils.loadAnimation(this, R.anim.click_scale)

        // --- Apply Entrance Animations ---
        // Top elements fade in
        binding.backButton.startAnimation(fadeIn)
        binding.textView13.startAnimation(fadeIn) // "Admin Profile" title
        binding.textView14.startAnimation(fadeIn) // "Edit Your Profile"
        binding.editProfileButton.startAnimation(fadeIn)

        // Form fields container slides up (with a slight delay)
        val formSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_input)
        formSlideUp.startOffset = 200 // ms delay
        binding.linearLayout.startAnimation(formSlideUp) // The LinearLayout holding all profile fields

        // Save button slides up (with a bit more delay)
        // Assuming your save button in XML has id "button4"
        val saveButtonSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_input)
        saveButtonSlideUp.startOffset = 400 // ms delay
        binding.button4.startAnimation(saveButtonSlideUp) // "Save Information" button


        // --- Initialize Field State ---
        // Set initial editable state based on your 'isEnable' logic (now 'isEditingEnabled')
        setFieldsEditable(isEditingEnabled) // This will set them to false initially

        // --- Click Listeners ---
        binding.backButton.setOnClickListener {
            it.startAnimation(clickScale)
            finish()
            // Activity exit transition
            overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity)
        }

        binding.editProfileButton.setOnClickListener {
            it.startAnimation(clickScale)
            // Toggle the editing state using your existing logic
            setFieldsEditable(!isEditingEnabled) // Toggle based on the current state
        }

        // Add OnClickListener for the Save button (button4)
        binding.button4.setOnClickListener {
            it.startAnimation(clickScale)
            // TODO: Add your logic to save the profile information from EditText fields.
            // For example, retrieve text from binding.name.text, etc.

            // After saving (or attempting to save), disable fields again
            setFieldsEditable(false)
            // Optionally, show a Toast message
            // Toast.makeText(this, "Information Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun finish() {
        super.finish()
        // Ensure consistent exit transition
        overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity)
    }
}
