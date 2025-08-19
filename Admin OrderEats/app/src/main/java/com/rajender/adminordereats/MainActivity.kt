package com.rajender.adminordereats

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.rajender.adminordereats.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // --- Load Animations ---
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val slideInBottom = AnimationUtils.loadAnimation(this, R.anim.fade_in_slide_up_delayed)
        val clickScaleAnimation = AnimationUtils.loadAnimation(this, R.anim.click_scale)
        val rotateOnceAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_once)

        // --- Entrance Animations ---
        binding.imageView.startAnimation(fadeIn)
        binding.textView2.startAnimation(fadeIn)
        binding.cardView.startAnimation(slideInBottom)

        // Staggered animations for menu CardViews
        // It's cleaner to load them once if they are the same type or configure offsets directly
        val slideInLeft1 = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left)
        binding.cardView2.startAnimation(slideInLeft1)

        val slideInRight1 = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right)
        slideInRight1.startOffset = 200 // Apply offset to the Animation object
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

        // Assuming you've added android:id="@+id/logoutCardView" to the logout CardView in XML
        binding.logoutCardView?.let { // Use safe call if ID might not exist
            val slideInRight3 = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right)
            slideInRight3.startOffset = 600
            it.startAnimation(slideInRight3)
        }
        // --- End Entrance Animations ---

        // --- Click Listeners with Animations and Activity Transitions ---

        // Logo click animation
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

        // Click listener for the parent CardView of "Pending Order"
        // (assuming you want the whole card to be clickable for this)
        // If you only want the text "Pending Order" to be clickable,
        // you'd set the listener on binding.cardView.pendingOrder (if that ID exists directly under binding.cardView)
        // Or, more likely, you mean the "Pending Orders" section within the first card (binding.cardView)
        // Let's assume you have a specific view for pending orders you want to click:
        // For example, if binding.pendingOrder is the TextView inside the first CardView
        binding.pendingOrder.setOnClickListener { view -> // This is likely the TextView as per your original code
            view.startAnimation(clickScaleAnimation) // Animate the TextView
            // If you want to animate the whole cardView instead:
            // binding.cardView.startAnimation(clickScaleAnimation)
            navigateTo(PendingOrderActivity::class.java)
        }

        // If you have a logout button inside the logoutCardView
        binding.logoutCardView?.findViewById<View>(R.id.logoutButton)?.setOnClickListener { view ->
            view.startAnimation(clickScaleAnimation)
            // Add your logout logic here
            // For example, navigate to a LoginActivity after logging out:
            // navigateTo(LoginActivity::class.java, true) // clear task if it's a login screen
        }
    }

    /**
     * Helper function to start an activity with a standard fade transition.
     * @param activityClass The class of the Activity to start.
     * @param clearTask If true, clears the activity stack before starting the new one (e.g., for logout).
     */
    private fun navigateTo(activityClass: Class<*>, clearTask: Boolean = false) {
        val intent = Intent(this, activityClass)
        if (clearTask) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
