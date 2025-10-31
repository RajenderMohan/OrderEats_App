package com.rajender.adminordereats

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.rajender.adminordereats.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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

        binding.pendingText.setOnClickListener { view ->
            view.startAnimation(clickScaleAnimation)
            navigateTo(PendingOrderActivity::class.java)
        }

        binding.logoutCardView?.setOnClickListener { view ->
            view.startAnimation(clickScaleAnimation)

            auth.signOut()

            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

            navigateTo(LoginActivity::class.java, true)
        }

    }

    private fun navigateTo(activityClass: Class<*>, clearTask: Boolean = false) {
        val intent = Intent(this, activityClass)
        if (clearTask) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        if (!clearTask) { 
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}