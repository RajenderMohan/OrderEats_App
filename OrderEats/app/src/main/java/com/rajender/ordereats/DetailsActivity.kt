package com.rajender.ordereats

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rajender.ordereats.R
import com.rajender.ordereats.data.CartRepository
import com.rajender.ordereats.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    private var foodName: String? = null
    private var foodImageResId: Int = 0
    private var quantity = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        loadDetailsData()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.textView24.setOnClickListener { headerView ->
            val contentView = binding.DescriptionTextView
            if (contentView.visibility == View.VISIBLE) {
                contentView.visibility = View.GONE
                (headerView as TextView).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
            } else {
                contentView.visibility = View.VISIBLE
                (headerView as TextView).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)
            }
        }

        binding.textView26.setOnClickListener { headerView ->
            val contentView = binding.IngredientsTextView
            if (contentView.visibility == View.VISIBLE) {
                contentView.visibility = View.GONE
                (headerView as TextView).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
            } else {
                contentView.visibility = View.VISIBLE
                (headerView as TextView).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)
            }
        }

        binding.plusButton.setOnClickListener {
            quantity++
            binding.quantityTextView.text = quantity.toString()
        }

        binding.minusButton.setOnClickListener {
            if (quantity > 1) {
                quantity--
                binding.quantityTextView.text = quantity.toString()
            }
        }

        binding.addToCartButton.setOnClickListener { 
            foodName?.let { name ->
                val price = "₹100" // Placeholder price
                for (i in 1..quantity) {
                    CartRepository.addItem(name, price, foodImageResId)
                }
                Toast.makeText(this, "Added $quantity x $name to cart", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    private fun loadDetailsData() {
        foodName = intent.getStringExtra("MenuItemName")
        foodImageResId = intent.getIntExtra("MenuItemImage", 0)

        binding.collapsingToolbar.title = foodName
        binding.detailFoodName.text = foodName ?: "Food Item"

        if (foodImageResId != 0) {
            binding.DetailFoodImage.setImageResource(foodImageResId)
        } else {
            binding.DetailFoodImage.setImageResource(R.drawable.burger)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
