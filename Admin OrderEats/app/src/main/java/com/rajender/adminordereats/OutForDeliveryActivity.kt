package com.rajender.adminordereats

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajender.adminordereats.Adapter.DeliveryAdapter
import com.rajender.adminordereats.databinding.ActivityOutForDeliveryBinding

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding: ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // --- Animate Header ---
        val slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        binding.headerTitle.startAnimation(slideDown)
        binding.backButton.startAnimation(slideDown)

        // --- Prepare Data ---
        val customerNames = arrayListOf(
            "Rajender Mohan Verma",
            "Anushka Singh",
            "Shekhar Maurya",
            "Karuna Singh",
            "Harsh Verma"
        )

        val paymentStatuses = arrayListOf(
            "Received",
            "Not Received",
            "Pending",
            "Received",
            "Pending"
        )

        val deliveryStatuses = arrayListOf(
            "Out for Delivery",
            "Delivered",
            "Pending",
            "Out for Delivery",
            "Delivered"
        )

        val customerImages = arrayListOf(
            R.drawable.rajender,
            R.drawable.anu,
            R.drawable.shekhar1,
            R.drawable.karuna,
            R.drawable.harsh
        )

        // --- Initialize and Set Adapter ---
        val adapter = DeliveryAdapter(
            customerNames,
            paymentStatuses,
            deliveryStatuses,
            customerImages,
            this
        )
        binding.deliveryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.deliveryRecyclerView.adapter = adapter

        // --- Back Button Click Listener ---
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    override fun finish() {
        super.finish()
        // Apply activity exit transition
        overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity)
    }
}
