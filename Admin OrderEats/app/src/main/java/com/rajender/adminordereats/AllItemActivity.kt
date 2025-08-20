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
        val menuFoodName = listOf("Burger", "Sandwich", "Momo", "Item", "Sandwich", "Momo", "Item", "Sandwich", "Momo", "Item", "Sandwich", "Momo")
        val menuItemPrice = listOf("₹30", "₹50", "₹30", "₹30", "₹70", "₹30", "₹20", "₹30", "₹10", "₹60", "₹30", "₹80")
        val menuImage = listOf(
            R.drawable.logo_app, R.drawable.logo_app, R.drawable.logo_app,
            R.drawable.logo_app, R.drawable.logo_app, R.drawable.logo_app,
            R.drawable.logo_app, R.drawable.logo_app, R.drawable.logo_app,
            R.drawable.logo_app, R.drawable.logo_app, R.drawable.logo_app
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
