package com.rajender.adminordereats // Make sure this is your correct package

import android.os.Bundle
import android.view.animation.AnimationUtils // Import for animations
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajender.adminordereats.Adapter.AddItemAdapter
import com.rajender.adminordereats.databinding.ActivityAllItemBinding
import com.rajender.adminordereats.R // Import R for resources (R.anim, R.drawable)

class AllItemActivity : AppCompatActivity() {

    // Class-level binding variable, initialized lazily
    private val binding: ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Use the class-level 'binding' variable for setContentView
        setContentView(binding.root)

        // --- Load Animations ---
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val clickScale = AnimationUtils.loadAnimation(this, R.anim.click_scale)

        // --- Apply Entrance Animations ---
        // Ensure your TextView for the title in activity_all_item.xml has the id "textView13"
        binding.textView13.startAnimation(fadeIn)
        binding.backButton.startAnimation(fadeIn)

        // --- Back Button Click Transform & Exit Transition ---
        binding.backButton.setOnClickListener {
            it.startAnimation(clickScale) // Apply click scale animation
            finish()
            // Transition for this activity exiting and the previous one entering
            overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity)
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

            // Ensure R.drawable.logo_app exists
        )

        // --- Initialize and Set Adapter ---
        val adapter = AddItemAdapter(
            ArrayList(menuFoodName),
            ArrayList(menuItemPrice),
            ArrayList(menuImage)
        )

        binding.MenuRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.MenuRecyclerView.adapter = adapter

        // The RecyclerView's layout animation (e.g., @anim/layout_animation_fall_down)
        // should be set in your activity_all_item.xml on the RecyclerView.
        // It will run automatically when the RecyclerView is first laid out with items.
    }

    override fun finish() {
        super.finish()
        // Ensure consistent exit transition
        // This applies when AllItemActivity exits and the PREVIOUS activity re-enters.
        overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity)
    }
}
