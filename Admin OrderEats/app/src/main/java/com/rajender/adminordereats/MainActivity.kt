package com.rajender.adminordereats

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.rajender.adminordereats.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth

    // Handler and Runnable for image rotation
    private val rotateHandler = Handler(Looper.getMainLooper())
    private lateinit var rotateRunnable: Runnable
    private val ROTATE_DELAY = 5000L // 5 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Animate the layout on startup
        animateLayout()

        // Set up click listeners for all buttons
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        // Start the automatic image rotation when the screen is visible
        startImageRotation()
    }

    override fun onPause() {
        super.onPause()
        // Stop the automatic image rotation to save resources when the screen is not visible
        stopImageRotation()
    }

    private fun startImageRotation() {
        val rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_once)
        rotateRunnable = object : Runnable {
            override fun run() {
                binding.imageView.startAnimation(rotateAnim)
                // Schedule the next rotation
                rotateHandler.postDelayed(this, ROTATE_DELAY)
            }
        }
        // Start the first rotation after the initial delay
        rotateHandler.postDelayed(rotateRunnable, ROTATE_DELAY)
    }

    private fun stopImageRotation() {
        rotateHandler.removeCallbacks(rotateRunnable)
    }

    private fun animateLayout() {
        val unfoldEnter = AnimationUtils.loadAnimation(this, R.anim.unfold_enter)
        val pulse = AnimationUtils.loadAnimation(this, R.anim.pulse)
        val childCount = binding.mainContainer.childCount
        val handler = Handler(Looper.getMainLooper())

        for (i in 0 until childCount) {
            val view = binding.mainContainer.getChildAt(i)
            view.visibility = View.INVISIBLE // Hide views initially
            handler.postDelayed({
                view.visibility = View.VISIBLE
                view.startAnimation(unfoldEnter)

                // Add pulse animation to all CardViews
                if (view is CardView) {
                    view.startAnimation(pulse)
                }
            }, (i * 120L)) // Staggered delay
        }
    }

    private fun setupClickListeners() {
        val clickableViews = listOf(
            binding.addMenu,
            binding.allItemMenu,
            binding.outForDeliveryButton,
            binding.profile,
            binding.createNewUser,
            binding.pendingOrderLayout,
            binding.logoutButton
        )

        clickableViews.forEach { view ->
            view.setOnClickListener {
                // The grow/shrink animation is handled by the StateListAnimator in XML
                when (it.id) {
                    R.id.addMenu -> navigateTo(AddItemActivity::class.java)
                    R.id.allItemMenu -> navigateTo(AllItemActivity::class.java)
                    R.id.outForDeliveryButton -> navigateTo(OutForDeliveryActivity::class.java)
                    R.id.profile -> navigateTo(AdminProfileActivity::class.java)
                    R.id.createNewUser -> navigateTo(CreateUserActivity::class.java)
                    R.id.pendingOrderLayout -> navigateTo(PendingOrderActivity::class.java)
                    R.id.logoutButton -> {
                        auth.signOut()
                        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
                        navigateTo(LoginActivity::class.java, true)
                    }
                }
            }
        }
    }

    private fun navigateTo(activityClass: Class<*>, clearTask: Boolean = false) {
        val intent = Intent(this, activityClass)
        if (clearTask) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        if (!clearTask) {
            overridePendingTransition(R.anim.unfold_enter, R.anim.unfold_exit)
        }
    }
}
