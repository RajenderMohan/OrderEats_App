package com.rajender.adminordereats

import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.rajender.adminordereats.databinding.ActivityAdminProfileBinding

class AdminProfileActivity : AppCompatActivity() {

    private val binding: ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }

    private var isEditingEnabled = false

    private lateinit var editableFields: List<EditText>

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            binding.profileImage.setImageURI(it)
        }
    }

    private val logoRotationHandler = Handler(Looper.getMainLooper())
    private lateinit var logoRotationRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 1. Toolbar को एक्शन बार के रूप में सेट करें
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Hide default title

        // 2. सभी एडिटेबल फील्ड्स को एक लिस्ट में डालें
        editableFields = listOf(binding.name, binding.address, binding.email, binding.phone, binding.password)

        // 3. शुरू में सभी फील्ड्स को नॉन-एडिटेबल रखें और सेव बटन को छिपाएं
        setFieldsEditable(false)
        binding.saveInfoButton.visibility = View.GONE

        // 4. क्लिक लिसनर्स सेट करें
        setupClickListeners()

        // 5. रोटेशन एनिमेशन शुरू करें
        val rotate = AnimationUtils.loadAnimation(this, R.anim.continuous_rotate)
        binding.rotatingBorder.startAnimation(rotate)

        // 6. पल्स एनिमेशन शुरू करें
        startPulseAnimation(binding.profileImage)
        startPulseAnimation(binding.nameCard)
        startPulseAnimation(binding.addressCard)
        startPulseAnimation(binding.emailCard)
        startPulseAnimation(binding.phoneCard)
        startPulseAnimation(binding.passwordCard)
        startPulseAnimation(binding.editProfileButton)

        // 7. लोगो के लिए रोटेशन शुरू करें
        setupLogoRotation()
    }

    private fun setupLogoRotation() {
        val rotateLogo = AnimationUtils.loadAnimation(this, R.anim.continuous_rotate)

        logoRotationRunnable = object : Runnable {
            override fun run() {
                binding.logoImage.startAnimation(rotateLogo)
                // 1 सेकंड के लिए घुमाएँ (एक राउंड)
                logoRotationHandler.postDelayed({
                    binding.logoImage.clearAnimation()
                    // 5 सेकंड के लिए रुकें
                    logoRotationHandler.postDelayed(this, 5000)
                }, 3000)
            }
        }
        logoRotationHandler.post(logoRotationRunnable) // लूप शुरू करें
    }

    private fun setupClickListeners() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.profileImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.editProfileButton.setOnClickListener {
            isEditingEnabled = !isEditingEnabled
            setFieldsEditable(isEditingEnabled)
        }

        binding.saveInfoButton.setOnClickListener {
            isEditingEnabled = false
            setFieldsEditable(false)
            Toast.makeText(this, "Information Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFieldsEditable(isEditable: Boolean) {
        editableFields.forEach { field ->
            field.isEnabled = isEditable
        }

        if (isEditable) {
            binding.name.requestFocus()
            binding.name.setSelection(binding.name.text.length)

            binding.editProfileButton.setImageResource(R.drawable.ic_close)

            binding.saveInfoButton.visibility = View.VISIBLE
            startPulseAnimation(binding.saveInfoButton)

        } else {
            binding.editProfileButton.setImageResource(R.drawable.edit_icon)

            binding.saveInfoButton.visibility = View.GONE
            stopPulseAnimation(binding.saveInfoButton)
        }
    }

    private fun startPulseAnimation(view: View) {
        val pulseAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.05f).apply {
            duration = 1500
            repeatCount = Animation.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }
        val pulseAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.05f).apply {
            duration = 1500
            repeatCount = Animation.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }
        pulseAnimator.start()
        pulseAnimatorY.start()
        view.setTag(R.id.pulse_animator_x, pulseAnimator)
        view.setTag(R.id.pulse_animator_y, pulseAnimatorY)
    }

    private fun stopPulseAnimation(view: View) {
        val pulseAnimatorX = view.getTag(R.id.pulse_animator_x) as? ObjectAnimator
        val pulseAnimatorY = view.getTag(R.id.pulse_animator_y) as? ObjectAnimator
        pulseAnimatorX?.cancel()
        pulseAnimatorY?.cancel()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        logoRotationHandler.removeCallbacks(logoRotationRunnable) // मेमोरी लीक से बचें
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
