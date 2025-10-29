package com.rajender.adminordereats

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajender.adminordereats.Adapter.AddItemAdapter
import com.rajender.adminordereats.databinding.ActivityAllItemBinding

class AllItemActivity : AppCompatActivity() {

    private val binding: ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // --- Animate Header ---
        val slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        binding.headerTitle.startAnimation(slideDown)
        binding.backButton.startAnimation(slideDown)

        // --- Back Button Click Listener ---
        binding.backButton.setOnClickListener {
            finish()
        }

        // --- Prepare Data for Adapter ---
        val menuFoodName = listOf("Burger", "bhel", "Chai", "brownie", "sandwich", "Roll", "Momo", "paratha", "Paneer_tikka",
            "Pasta", "Noddles", "Kachori", "jalebi","idli","Paneer_bun","Mojito","Cake","Chat","Dosa","Chicken","Chole_Kulche",)
        val menuItemPrice = listOf("₹39", "₹49", "₹20", "₹30", "₹30", "₹40", "₹50", "₹30", "₹30", "₹50", "₹60", "₹50", "₹30","₹40","₹30","₹49","₹199","₹40","₹60","₹259","₹40",)
        val menuImage = listOf(
            R.drawable.burger, R.drawable.bhel,
            R.drawable.chai, R.drawable.brownie,
            R.drawable.sandwich, R.drawable.roll, R.drawable.momos,
            R.drawable.paratha, R.drawable.paneer_tikka, R.drawable.pasta,
            R.drawable.noddles, R.drawable.kachori, R.drawable.jalebi,
            R.drawable.idli, R.drawable.paneer_bun, R.drawable.mojito,
            R.drawable.cake, R.drawable.chat, R.drawable.dosa,
            R.drawable.chicken, R.drawable.chole_kulche,
        )

        // --- Initialize and Set Adapter ---
        val adapter = AddItemAdapter(
            ArrayList(menuFoodName),
            ArrayList(menuItemPrice),
            ArrayList(menuImage)
        )

        binding.MenuRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.MenuRecyclerView.adapter = adapter
    }

    override fun finish() {
        super.finish()
        // Apply activity exit transition
        overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity)
    }
}
