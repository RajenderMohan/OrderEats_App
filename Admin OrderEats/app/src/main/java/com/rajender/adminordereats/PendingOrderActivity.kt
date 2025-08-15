package com.rajender.adminordereats

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
// import androidx.core.view.ViewCompat // Not used in this snippet
// import androidx.core.view.WindowInsetsCompat // Not used in this snippet
import androidx.recyclerview.widget.LinearLayoutManager
// import com.rajender.adminordereats.Adapter.DeliveryAdapter // Not used in this activity
import com.rajender.adminordereats.Adapter.PendingOrderAdapter
import com.rajender.adminordereats.databinding.ActivityPendingOrderBinding
// import com.rajender.adminordereats.databinding.PendingOrderItemBinding // Not used directly in activity

class PendingOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPendingOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Good to call this early
        binding = ActivityPendingOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- All this code should be INSIDE onCreate ---
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
            R.drawable.logo_app,
            R.drawable.logo_app,
            R.drawable.logo_app,
            R.drawable.logo_app
        )

        // Assuming PendingOrderAdapter constructor now takes three arguments
        val adapter = PendingOrderAdapter(orderCustomerName, orderQuantity, orderFoodImage, this)

        // Ensure your RecyclerView in activity_pending_order.xml has the id "pendingOrderRecyclerView"
        // If it's "pendingRecyclerView" (as per your initial XML), change this line accordingly.
        binding.pendingOrderRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.pendingOrderRecyclerView.adapter = adapter

        // Assuming your ActivityPendingOrderBinding has a view with id "backButton"
        // This also needs to be inside onCreate if 'binding' is to be used
        binding.backButton.setOnClickListener {
            finish()
        }
        // --- End of code that should be inside onCreate ---
    }
}

