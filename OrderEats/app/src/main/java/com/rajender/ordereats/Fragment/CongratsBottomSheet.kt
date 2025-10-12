package com.rajender.ordereats.Fragment // Ensure this is your correct package

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rajender.ordereats.MainActivity
import com.rajender.ordereats.R
import com.rajender.ordereats.databinding.FragmentCongratsBottomSheetBinding

class CongratsBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCongratsBottomSheetBinding
    // Animation Delays
    private val DELAY_TEXT = 100L
    private val DELAY_IMAGE = 250L
    private val DELAY_BUTTON = 450L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // Return type should be non-nullable View
        binding = FragmentCongratsBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // --- 1. Load Animations ---
        val fadeInScaleTextAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_scale_text)
        val scaleBounceImageAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_bounce_fade_in_image)
        val slideUpButtonAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up_fade_in_button)
        val clickScaleAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.click_scale)

        // --- 2. Prepare Views for Animation (Set initial visibility to INVISIBLE) ---
        binding.congratsTextView.visibility = View.INVISIBLE // Make sure congratsTextView ID is in your XML
        binding.imageView5.visibility = View.INVISIBLE
        binding.goHome.visibility = View.INVISIBLE

        val handler = Handler(Looper.getMainLooper())

        // --- 3. Apply Staggered Entrance Animations ---
        handler.postDelayed({
            binding.congratsTextView.visibility = View.VISIBLE
            binding.congratsTextView.startAnimation(fadeInScaleTextAnim)
        }, DELAY_TEXT)

        handler.postDelayed({
            binding.imageView5.visibility = View.VISIBLE
            binding.imageView5.startAnimation(scaleBounceImageAnim)
        }, DELAY_IMAGE)

        handler.postDelayed({
            binding.goHome.visibility = View.VISIBLE
            binding.goHome.startAnimation(slideUpButtonAnim)
        }, DELAY_BUTTON)

        // --- 4. Setup Click Listener for "Go Home" Button ---
        binding.goHome.setOnClickListener {
            it.startAnimation(clickScaleAnim) // Apply click feedback

            Handler(Looper.getMainLooper()).postDelayed({
                dismiss() // Close the bottom sheet

                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

            }, 400) // Adjust delay as needed
        }
    }

    companion object {
        const val TAG = "CongratsBottomSheet"
        // Optional: A newInstance pattern is good practice for fragments
        fun newInstance(): CongratsBottomSheet {
            return CongratsBottomSheet()
        }
    }
}
