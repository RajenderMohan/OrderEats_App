package com.rajender.adminordereats // Make sure this is your correct package

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajender.adminordereats.Adapter.AddItemAdapter
// Correct the binding class import if necessary, though it might auto-correct
// after you change the type below.
import com.rajender.adminordereats.databinding.ActivityAllItemBinding

class AllItemActivity : AppCompatActivity() {

    // Change the type of the binding variable
    private val binding : ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAllItemBinding.inflate(layoutInflater) // Assuming this is corrected
        setContentView(binding.root)
        val menuFoodName = listOf("Burger", "Sandwich", "Momo", "Item", "Sandwich", "Momo", "Item", "Sandwich", "Momo", "Item", "Sandwich", "Momo")
        val menuItemPrice = listOf("₹30", "₹50", "₹30", "₹30", "₹70", "₹30", "₹20", "₹30", "₹10", "₹60", "₹30", "₹80")
        val menuImage = listOf(
        R.drawable.logo_app,
        R.drawable.logo_app,
        R.drawable.logo_app,
        R.drawable.logo_app,
        R.drawable.logo_app,
        R.drawable.logo_app, R.drawable.logo_app,
        R.drawable.logo_app,
        R.drawable.logo_app,
        R.drawable.logo_app,
        R.drawable.logo_app,
        R.drawable.logo_app
    )

    val adapter = AddItemAdapter(
        ArrayList(menuFoodName),
        ArrayList(menuItemPrice),
        ArrayList(menuImage)
    )


        binding.MenuRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.MenuRecyclerView.adapter = adapter

        binding.backButton.setOnClickListener {
            finish()
        }
    }

}
