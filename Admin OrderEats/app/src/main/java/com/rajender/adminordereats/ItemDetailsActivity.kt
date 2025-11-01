package com.rajender.adminordereats

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rajender.adminordereats.databinding.ActivityItemDetailsBinding
import com.rajender.adminordereats.model.AllMenu

class ItemDetailsActivity : AppCompatActivity() {

    private val binding: ActivityItemDetailsBinding by lazy {
        ActivityItemDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Retrieve the menu item data
        val menuItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("menuItem", AllMenu::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("menuItem")
        }

        // Populate the views
        menuItem?.let {
            binding.foodNameTextView.text = it.foodName
            binding.foodDescriptionTextView.text = it.foodDescription
            binding.ingredientsTextView.text = it.foodIngredient
            it.foodImage?.let { uri -> binding.foodImageView.setImageURI(uri) }
        }

        // Set up click listeners
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.plusButton.setOnClickListener {
            increaseQuantity()
        }

        binding.minusButton.setOnClickListener {
            decreaseQuantity()
        }

        binding.deleteButton.setOnClickListener {
            // In a real app, you would delete this from the database
            Toast.makeText(this, "Item Deleted (Placeholder)", Toast.LENGTH_SHORT).show()
            finish() // Go back to the list after deleting
        }
    }

    private fun increaseQuantity() {
        var quantity = binding.quantityTextView.text.toString().toIntOrNull() ?: 1
        if (quantity < 10) {
            quantity++
            binding.quantityTextView.text = quantity.toString()
        } else {
            Toast.makeText(this, "Maximum quantity reached", Toast.LENGTH_SHORT).show()
        }
    }

    private fun decreaseQuantity() {
        var quantity = binding.quantityTextView.text.toString().toIntOrNull() ?: 1
        if (quantity > 1) {
            quantity--
            binding.quantityTextView.text = quantity.toString()
        } else {
            Toast.makeText(this, "Minimum quantity reached", Toast.LENGTH_SHORT).show()
        }
    }
}
