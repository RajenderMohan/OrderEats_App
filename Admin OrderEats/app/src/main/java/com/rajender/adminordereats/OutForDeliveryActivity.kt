package com.rajender.adminordereats

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajender.adminordereats.Adapter.DeliveryAdapter
import com.rajender.adminordereats.databinding.ActivityOutForDeliveryBinding

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding: ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }

    // Inside OutForDeliveryActivity.kt
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ensure binding is initialized before use
        // binding = ActivityOutForDeliveryBinding.inflate(layoutInflater) // Corrected from your initial code
        setContentView(binding.root)

        val customerName = arrayListOf(
            "Rajender Mohan",
            "Anushka Singh",
            "Shekhar Maurya",
            "Karuna Singh"
        )
        // MAKE SURE THIS LIST HAS THE SAME NUMBER OF ITEMS
        val notReceviedTextView = arrayListOf(
            "Recevied",
            "Not Recevied",
            "Pending",
            "Recevied" // EXAMPLE: Added a fourth item. Adjust to your actual data.
        )
        val adapter = DeliveryAdapter(customerName, notReceviedTextView)
        binding.deliveryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.deliveryRecyclerView.adapter = adapter

        binding.backButton.setOnClickListener {
            finish() // Standard back button behavior
        }
    }

}
