package com.rajender.adminordereats

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajender.adminordereats.Adapter.DeliveryAdapter
import com.rajender.adminordereats.databinding.ActivityOutForDeliveryBinding
import com.rajender.adminordereats.R // Import R for resources

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding: ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }

    private lateinit var adapter: DeliveryAdapter
    private val customerNames = ArrayList<String>()
    private val paymentStatuses = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // --- Load Animations ---
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val clickScale = AnimationUtils.loadAnimation(this, R.anim.click_scale)

        // --- Apply Entrance Animations ---
        binding.textView13.startAnimation(fadeIn) // Title: "Out For Delivery"
        binding.backButton.startAnimation(fadeIn)

        // --- Prepare Data ---
        prepareLocalData()

        // --- Initialize and Set Adapter ---
        adapter = DeliveryAdapter(customerNames, paymentStatuses, this)
        binding.deliveryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.deliveryRecyclerView.adapter = adapter
        // RecyclerView's layout animation is set in XML.

        // --- Back Button Click Transform & Exit Transition ---
        binding.backButton.setOnClickListener {
            it.startAnimation(clickScale)
            finish()
            overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity)
        }
    }

    private fun prepareLocalData() {
        customerNames.clear() // Clear previous data if any
        paymentStatuses.clear()

        customerNames.addAll(listOf(
            "Rajender Mohan",
            "Anushka Singh",
            "Shekhar Maurya",
            "Karuna Singh",
            "Amit Patel"
        ))

        paymentStatuses.addAll(listOf(
            "Received",    // Make sure these strings match keys in DeliveryAdapter's colorMap
            "Not Received",
            "Pending",
            "Received",
            "Pending"
        ))
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity)
    }
}
