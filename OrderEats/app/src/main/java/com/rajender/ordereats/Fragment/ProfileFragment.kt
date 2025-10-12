package com.rajender.ordereats.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rajender.ordereats.R
import com.rajender.ordereats.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    // Use ViewBinding
    private var _binding: FragmentProfileBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    // Animation Delays
    private val DELAY_FIELD_INITIAL = 50L // Start a bit later for smoother perceived transition
    private val DELAY_FIELD_INCREMENT = 120L // Stagger delay between fields
    // Calculate delay for save button to appear after the last field
    private val DELAY_SAVE_BUTTON = DELAY_FIELD_INITIAL + (DELAY_FIELD_INCREMENT * 3) + DELAY_FIELD_INCREMENT // *3 because 4 fields (0,1,2,3 index)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Load Animations ---
        val slideInField = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_right_profile_field)
        val slideInButton = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_bottom_button)
        val clickScale = AnimationUtils.loadAnimation(requireContext(), R.anim.click_scale)

        // --- Prepare Views for Animation ---
        // Ensure you have given these IDs in your fragment_profile.xml
        val profileFields = listOf(
            binding.nameFieldLayout,
            binding.addressFieldLayout,
            binding.emailFieldLayout,
            binding.phoneFieldLayout
        )

        profileFields.forEach { it.visibility = View.INVISIBLE }
        binding.saveButton.visibility = View.INVISIBLE

        val handler = Handler(Looper.getMainLooper())

        // --- Apply Staggered Entrance Animations for Profile Fields ---
        profileFields.forEachIndexed { index, fieldLayout ->
            handler.postDelayed({
                fieldLayout.visibility = View.VISIBLE
                fieldLayout.startAnimation(slideInField)
            }, DELAY_FIELD_INITIAL + (index * DELAY_FIELD_INCREMENT))
        }

        // --- Animate Save Button ---
        handler.postDelayed({
            binding.saveButton.visibility = View.VISIBLE
            binding.saveButton.startAnimation(slideInButton)
        }, DELAY_SAVE_BUTTON)

        // --- Setup Click Listener for Save Button ---
        binding.saveButton.setOnClickListener {
            it.startAnimation(clickScale) // Apply click animation
            saveProfileInformation()
            Toast.makeText(requireContext(), "Profile Information Saved!", Toast.LENGTH_SHORT).show()
        }

        // Load existing profile data (if any)
        loadProfileData()
    }

    private fun loadProfileData() {
        // This is where you would load user data from SharedPreferences, ViewModel, Database, etc.
        // and populate the EditText fields.
        // Example:
        // binding.nameEditText.setText(viewModel.getUserName())
        // binding.addressEditText.setText(viewModel.getUserAddress())
        // binding.emailEditText.setText(viewModel.getUserEmail())
        // binding.phoneEditText.setText(viewModel.getUserPhone())

        // For now, the EditTexts will show the default text from your XML.
        // If you gave IDs to your EditTexts (e.g., nameEditText, addressEditText),
        // you can access them via binding:
        // binding.nameEditText.setText("Admin (Loaded from code)") // Example
    }

    private fun saveProfileInformation() {
        // This is where you would get the text from EditText fields and save it.
        // Example:
        // val name = binding.nameEditText.text.toString()
        // val address = binding.addressEditText.text.toString()
        // val email = binding.emailEditText.text.toString()
        // val phone = binding.phoneEditText.text.toString()
        // viewModel.saveUserProfile(name, address, email, phone)

        // Placeholder action
        val name = binding.nameEditText.text.toString() // Assuming nameEditText is the ID of the EditText in nameFieldLayout
        val address = binding.addressEditText.text.toString() // Assuming addressEditText is the ID of the EditText in addressFieldLayout
        // ... and so on for email and phone
        println("Saving Name: $name, Address: $address")
        Toast.makeText(requireContext(), "Saving data (see Logcat)", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Important to avoid memory leaks
    }

    // You can remove the companion object if newInstance with parameters isn't needed.
    // companion object {
    //     @JvmStatic
    //     fun newInstance() = ProfileFragment() // Simplified newInstance if no params
    // }
}
