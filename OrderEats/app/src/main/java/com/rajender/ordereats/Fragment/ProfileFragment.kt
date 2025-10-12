package com.rajender.ordereats.Fragment

import android.content.Intent // Import karo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth // Import karo
import com.rajender.ordereats.LoginActivity // Import karo
import com.rajender.ordereats.R
import com.rajender.ordereats.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // +++ ADD: Firebase Auth instance +++
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Animation Delays
    private val DELAY_FIELD_INITIAL = 50L
    private val DELAY_FIELD_INCREMENT = 120L
    private val DELAY_BUTTON_START = DELAY_FIELD_INITIAL + (DELAY_FIELD_INCREMENT * 4) // Delay for buttons

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
        val profileFields = listOf(
            binding.nameFieldLayout,
            binding.addressFieldLayout,
            binding.emailFieldLayout,
            binding.phoneFieldLayout
        )

        // Hide all views initially
        profileFields.forEach { it.visibility = View.INVISIBLE }
        // XML mein button ID 'editProfileButton' aur 'logoutButton' honi chahiye
        binding.saveButton.visibility = View.INVISIBLE
        binding.logoutButton.visibility = View.INVISIBLE

        val handler = Handler(Looper.getMainLooper())

        // --- Apply Staggered Entrance Animations for Profile Fields ---
        profileFields.forEachIndexed { index, fieldLayout ->
            handler.postDelayed({
                fieldLayout.visibility = View.VISIBLE
                fieldLayout.startAnimation(slideInField)
            }, DELAY_FIELD_INITIAL + (index * DELAY_FIELD_INCREMENT))
        }

        // --- Animate Both Buttons ---
        handler.postDelayed({
            binding.saveButton.visibility = View.VISIBLE
            binding.saveButton.startAnimation(slideInButton)

            // Logout button ko thodi der baad animate karo
            handler.postDelayed({
                binding.logoutButton.visibility = View.VISIBLE
                binding.logoutButton.startAnimation(slideInButton)
            }, 100) // 100ms delay

        }, DELAY_BUTTON_START)


        // --- Setup Click Listener for Save Button ---
        binding.saveButton.setOnClickListener {
            it.startAnimation(clickScale) // Apply click animation
            saveProfileInformation()
            Toast.makeText(requireContext(), "Profile Information Saved!", Toast.LENGTH_SHORT).show()
        }

        // --- ADD: Click Listener for Logout Button ---
        // 'logoutButton' ID seedhe binding se access karo
        binding.logoutButton.setOnClickListener { view ->
            view.startAnimation(clickScale) // Use 'clickScale' animation

            // Firebase se sign out karo
            auth.signOut()

            // Optional: Logout ka message dikhao
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()

            // LoginActivity pe jao aur pichli saari activity band kar do
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        // Load existing profile data (if any)
        loadProfileData()
    }

    private fun loadProfileData() {
        // Aapka existing function
    }

    private fun saveProfileInformation() {
        // Aapka existing function
        val name = binding.nameEditText.text.toString()
        val address = binding.addressEditText.text.toString()
        println("Saving Name: $name, Address: $address")
        Toast.makeText(requireContext(), "Saving data (see Logcat)", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
