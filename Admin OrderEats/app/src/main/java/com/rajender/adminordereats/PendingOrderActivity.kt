package com.rajender.adminordereats

import android.content.Context // Added for adapter
import android.os.Bundle
import android.view.animation.AnimationUtils // Added for animations
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajender.adminordereats.Adapter.PendingOrderAdapter
import com.rajender.adminordereats.databinding.ActivityPendingOrderBinding
import com.rajender.adminordereats.R // IMPORTANT: Import R for animations and drawables

class PendingOrderActivity : AppCompatActivity() {
    // Using lazy initialization for binding is a good practice
    private val binding: ActivityPendingOrderBinding by lazy {
        ActivityPendingOrderBinding.inflate(layoutInflater)
    }

    private lateinit var adapter: PendingOrderAdapter // Declare adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Call this early
        setContentView(binding.root) // Set content view after binding is initialized

        // --- Load Animations ---
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val clickScale = AnimationUtils.loadAnimation(this, R.anim.click_scale)

        // --- Apply Entrance Animations ---
        // Assuming your activity_pending_order.xml has a TextView with id "textView13" for the title
        binding.textView13.startAnimation(fadeIn)
        binding.backButton.startAnimation(fadeIn)


        // --- Prepare Data (Your existing data setup) ---
        val orderCustomerName = arrayListOf(
            "Rajender Mohan Verma",
            "Anushka Singh",
            "Shekhar Maurya",
            "Karuna Singh"
        )

        val orderQuantity = arrayListOf(
            "8",
            "6",
            "5",
            "7"
        )
        val orderFoodImage = arrayListOf(
            R.drawable.logo_app, // Ensure R is imported
            R.drawable.logo_app,
            R.drawable.logo_app,
            R.drawable.logo_app
        )

        // --- Initialize and Set Adapter ---
        adapter = PendingOrderAdapter(orderCustomerName, orderQuantity, orderFoodImage, this)
        binding.pendingOrderRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.pendingOrderRecyclerView.adapter = adapter
        // The RecyclerView's layout animation is set in XML (android:layoutAnimation)
        // and will trigger automatically when items are first laid out.


        // --- Back Button Click Transform & Exit Transition ---
        binding.backButton.setOnClickListener {
            it.startAnimation(clickScale) // Apply click animation
            finish()
            // Apply activity exit transition
            overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity)
        }
    }

    override fun finish() {
        super.finish()
        // Apply consistent activity exit transition when finish() is called elsewhere too
        overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity)
    }
}
