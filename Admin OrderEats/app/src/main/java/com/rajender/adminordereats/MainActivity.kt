package com.rajender.adminordereats

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast // Import Toast for messages
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth // Import FirebaseAuth
import com.rajender.adminordereats.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth // Declare FirebaseAuth instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // --- Load Animations ---
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val slideInBottom = AnimationUtils.loadAnimation(this, R.anim.fade_in_slide_up_delayed)
        val clickScaleAnimation = AnimationUtils.loadAnimation(this, R.anim.click_scale)
        val rotateOnceAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_once)

        // --- Entrance Animations ---
        binding.imageView.startAnimation(fadeIn)
        binding.textView2.startAnimation(fadeIn)
        binding.cardView.startAnimation(slideInBottom)

        val slideInLeft1 = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left)
        binding.cardView2.startAnimation(slideInLeft1)

        val slideInRight1 = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right)
        slideInRight1.startOffset = 200
        binding.cardView3.startAnimation(slideInRight1)

        val slideInLeft2 = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left)
        slideInLeft2.startOffset = 400
        binding.cardView4.startAnimation(slideInLeft2)

        val slideInRight2 = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right)
        slideInRight2.startOffset = 400
        binding.cardView5.startAnimation(slideInRight2)

        val slideInLeft3 = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left)
        slideInLeft3.startOffset = 600
        binding.cardView6.startAnimation(slideInLeft3)


        binding.logoutCardView?.let {
            val slideInRight3 = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right)
            slideInRight3.startOffset = 600
            it.startAnimation(slideInRight3)
        }
        // --- End Entrance Animations ---

        // --- Click Listeners with Animations and Activity Transitions ---

        binding.imageView.setOnClickListener {
            it.startAnimation(rotateOnceAnim)
        }

        binding.addMenu.setOnClickListener { view ->
            view.startAnimation(clickScaleAnimation)
            navigateTo(AddItemActivity::class.java)
        }

        binding.allItemMenu.setOnClickListener { view ->
            view.startAnimation(clickScaleAnimation)
            navigateTo(AllItemActivity::class.java)
        }

        binding.outForDeliveryButton.setOnClickListener { view ->
            view.startAnimation(clickScaleAnimation)
            navigateTo(OutForDeliveryActivity::class.java)
        }

        binding.profile.setOnClickListener { view ->
            view.startAnimation(clickScaleAnimation)
            navigateTo(AdminProfileActivity::class.java)
        }

        binding.createNewUser.setOnClickListener { view ->
            view.startAnimation(clickScaleAnimation)
            navigateTo(CreateUserActivity::class.java)
        }

        binding.pendingOrder.setOnClickListener { view ->
            view.startAnimation(clickScaleAnimation)
            navigateTo(PendingOrderActivity::class.java)
        }

        // --- Logout Button Click Listener ---
        // Assuming your logout button has the ID "logoutButton" and is inside "logoutCardView"
        // If "logoutCardView" itself is the clickable logout element,
        // you can set the listener directly on binding.logoutCardView
        // For this example, I'm assuming a distinct Button with R.id.logoutButton

        val logoutButton = binding.logoutCardView?.findViewById<View>(R.id.logoutButton)
        logoutButton?.setOnClickListener { view ->
            view.startAnimation(clickScaleAnimation)

            // Sign out from Firebase
            auth.signOut()

            // Optional: Show a toast message
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

            // Navigate to LoginActivity and clear the task stack
            navigateTo(LoginActivity::class.java, true)
        }

        // If the entire logoutCardView is supposed to be the logout button:
        // Remove or comment out the block above

// Use this if the ENTIRE logoutCardView should be clickable for logout
        binding.logoutCardView?.setOnClickListener { view -> // Listener is now directly on logoutCardView
            view.startAnimation(clickScaleAnimation)

            // Sign out from Firebase
            auth.signOut()

            // Optional: Show a toast message
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

            // Navigate to LoginActivity and clear the task stack
            navigateTo(LoginActivity::class.java, true)
        }

    }

    private fun navigateTo(activityClass: Class<*>, clearTask: Boolean = false) {
        val intent = Intent(this, activityClass)
        if (clearTask) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        if (!clearTask) { // Only apply standard fade if not clearing task (clearing task often has its own feel)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        // If clearing task (like for logout), you might not need a specific exit animation for MainActivity,
        // as it will be removed from the stack. The LoginActivity will have its entrance.
    }
}
