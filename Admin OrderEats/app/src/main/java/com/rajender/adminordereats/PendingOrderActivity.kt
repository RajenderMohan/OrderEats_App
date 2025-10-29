package com.rajender.adminordereats

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajender.adminordereats.Adapter.PendingOrderAdapter
import com.rajender.adminordereats.databinding.ActivityPendingOrderBinding

class PendingOrderActivity : AppCompatActivity() {

    private val binding: ActivityPendingOrderBinding by lazy {
        ActivityPendingOrderBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // --- Animate Header ---
        val slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        binding.headerTitle.startAnimation(slideDown)
        binding.backButton.startAnimation(slideDown)

        // --- Prepare Data (Your existing data setup) ---
        val orderCustomerName = arrayListOf(
            "Rajender Mohan Verma",
            "Anushka Singh",
            "Shekhar Maurya",
            "Karuna Singh",
            "Harsh Verma"
        )

        val orderQuantity = arrayListOf(
            "8",
            "6",
            "5",
            "7",
            "8"
        )
        val orderFoodImage = arrayListOf(
            R.drawable.rajender,
            R.drawable.anu,
            R.drawable.shekhar1,
            R.drawable.karuna,
            R.drawable.harsh
        )

        // --- Initialize and Set Adapter ---
        val adapter = PendingOrderAdapter(orderCustomerName, orderQuantity, orderFoodImage, this)
        binding.pendingOrderRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.pendingOrderRecyclerView.adapter = adapter

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
