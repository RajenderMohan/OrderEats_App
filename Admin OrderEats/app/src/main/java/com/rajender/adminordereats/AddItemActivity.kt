package com.rajender.adminordereats

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rajender.adminordereats.databinding.ActivityAddItemBinding

class AddItemActivity : AppCompatActivity() {

    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    private var foodImageUri: Uri? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.selectedImage.setImageURI(uri)
            foodImageUri = uri
            binding.tapToSelectImage.visibility = View.GONE
        }
    }

    // Handler and Runnable for logo rotation
    private val rotateHandler = Handler(Looper.getMainLooper())
    private lateinit var rotateRunnable: Runnable
    private val ROTATE_DELAY = 5000L // 5 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Animate the layout on startup
        animateLayout()

        // Set up the category dropdown
        setupCategoryDropdown()

        // Set up click listeners
        setupClickListeners()
        
        // Start the marquee with a delay
        startMarqueeWithDelay()
    }

    override fun onResume() {
        super.onResume()
        // Start the automatic logo rotation when the screen is visible
        startLogoRotation()
    }

    override fun onPause() {
        super.onPause()
        // Stop the automatic logo rotation to save resources when the screen is not visible
        stopLogoRotation()
    }

    private fun startLogoRotation() {
        val rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_once)
        rotateRunnable = object : Runnable {
            override fun run() {
                binding.logoImage.startAnimation(rotateAnim)
                // Schedule the next rotation
                rotateHandler.postDelayed(this, ROTATE_DELAY)
            }
        }
        // Start the first rotation after the initial delay
        rotateHandler.postDelayed(rotateRunnable, ROTATE_DELAY)
    }

    private fun stopLogoRotation() {
        rotateHandler.removeCallbacks(rotateRunnable)
    }

    private fun animateLayout() {
        val unfoldEnter = AnimationUtils.loadAnimation(this, R.anim.unfold_enter)
        val jiggle = AnimationUtils.loadAnimation(this, R.anim.jiggle)
        val pulse = AnimationUtils.loadAnimation(this, R.anim.pulse)
        val container = binding.mainContainer
        val childCount = container.childCount
        val handler = Handler(Looper.getMainLooper())

        for (i in 0 until childCount) {
            val view = container.getChildAt(i)
            view.visibility = View.INVISIBLE // Hide views initially
            handler.postDelayed({
                view.visibility = View.VISIBLE
                view.startAnimation(unfoldEnter)

                // Add pulse animation to all CardViews
                if (view is CardView) {
                    view.startAnimation(pulse)
                }

                // Add jiggle animation to the button after the main animation
                if (view.id == R.id.AddItemButton) { 
                    handler.postDelayed({ view.startAnimation(jiggle) }, (childCount * 100L))
                }
            }, (i * 100L)) // Staggered delay
        }
    }

    private fun setupCategoryDropdown() {
        val categories = listOf("Fast Food",
                "Indian Cuisine",
                "Chinese",
                "Italian",
                "Desserts",
                "Beverages",
                "Healthy",
               " Street Food",
                "South Indian",
                "Snacks")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        binding.foodCategory.setAdapter(adapter)
    }

    private fun setupClickListeners() {
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.AddItemButton.setOnClickListener {
            if (areAllFieldsFilled()) {
                Toast.makeText(this, "item added successfully", Toast.LENGTH_SHORT).show()
                // Redirect to MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun areAllFieldsFilled(): Boolean {
        return binding.foodName.text.toString().isNotBlank() &&
                binding.foodPrice.text.toString().isNotBlank() &&
                binding.description.text.toString().isNotBlank() &&
                binding.ingredient.text.toString().isNotBlank() &&
                binding.foodCategory.text.toString().isNotBlank() &&
                foodImageUri != null
    }

    private fun startMarqueeWithDelay() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            binding.headerTitle.isSelected = true
        }, 2000) // 2-second delay
    }

    override fun finish() {
        super.finish()
        // Apply the consistent unfold transition on exit
        overridePendingTransition(R.anim.unfold_enter, R.anim.unfold_exit)
    }
}
