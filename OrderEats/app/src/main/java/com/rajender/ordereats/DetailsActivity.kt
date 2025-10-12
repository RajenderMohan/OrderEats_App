package com.rajender.ordereats

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rajender.ordereats.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    // Animation Delays (in milliseconds)
    private val DELAY_BACK_BUTTON = 100L
    private val DELAY_FOOD_NAME = 200L
    private val DELAY_FOOD_IMAGE_CARD = 300L
    private val DELAY_DESC_TITLE = 400L
    private val DELAY_DESC_TEXT = 500L
    private val DELAY_INGREDIENTS_TITLE = 600L
    private val DELAY_INGREDIENTS_TEXT = 700L
    private val DELAY_ADD_TO_CART_BUTTON = 800L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val slideInFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left_simple)
        val fadeInText = AnimationUtils.loadAnimation(this, R.anim.fade_in_text)
        val scaleFadeInCard = AnimationUtils.loadAnimation(this, R.anim.scale_fade_in_card)
        val slideUpFromBottom = AnimationUtils.loadAnimation(this, R.anim.slide_up_from_bottom)
        val clickScale = AnimationUtils.loadAnimation(this, R.anim.click_scale)

        val viewsToAnimate = listOf(
            binding.imageButton,
            binding.detailFoodName,
            binding.cardView4,
            binding.textView24,
            binding.DescriptionTextView,
            binding.textView26,
            binding.IngredientsTextView,
            binding.addToCartButton // This is your Add To Cart button (assuming its ID in XML is @+id/button)
            // If ID in XML is @+id/addToCartButton, this should be binding.addToCartButton
        )
        viewsToAnimate.forEach { it.visibility = View.INVISIBLE }

        val handler = Handler(Looper.getMainLooper())

        // --- 3. Apply Staggered Entrance Animations ---
        // ... (your existing animation code is fine)
        handler.postDelayed({
            binding.imageButton.visibility = View.VISIBLE
            binding.imageButton.startAnimation(slideInFromLeft)
        }, DELAY_BACK_BUTTON)

        handler.postDelayed({
            binding.detailFoodName.visibility = View.VISIBLE
            binding.detailFoodName.startAnimation(fadeInText)
        }, DELAY_FOOD_NAME)

        handler.postDelayed({
            binding.cardView4.visibility = View.VISIBLE
            binding.cardView4.startAnimation(scaleFadeInCard)
        }, DELAY_FOOD_IMAGE_CARD)

        val slideInDescTitle = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left_simple)
        handler.postDelayed({
            binding.textView24.visibility = View.VISIBLE
            binding.textView24.startAnimation(slideInDescTitle)
        }, DELAY_DESC_TITLE)

        val fadeInDescText = AnimationUtils.loadAnimation(this, R.anim.fade_in_text)
        handler.postDelayed({
            binding.DescriptionTextView.visibility = View.VISIBLE
            binding.DescriptionTextView.startAnimation(fadeInDescText)
        }, DELAY_DESC_TEXT)

        val slideInIngredientsTitle = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left_simple)
        handler.postDelayed({
            binding.textView26.visibility = View.VISIBLE
            binding.textView26.startAnimation(slideInIngredientsTitle)
        }, DELAY_INGREDIENTS_TITLE)

        val fadeInIngredientsText = AnimationUtils.loadAnimation(this, R.anim.fade_in_text)
        handler.postDelayed({
            binding.IngredientsTextView.visibility = View.VISIBLE
            binding.IngredientsTextView.startAnimation(fadeInIngredientsText)
        }, DELAY_INGREDIENTS_TEXT)

        // Add to Cart Button (binding.button or binding.addToCartButton)
        handler.postDelayed({
            binding.addToCartButton.visibility = View.VISIBLE // Or binding.addToCartButton
            binding.addToCartButton.startAnimation(slideUpFromBottom) // Or binding.addToCartButton
        }, DELAY_ADD_TO_CART_BUTTON)


        // --- 4. Setup Click Listeners (with optional click animation) ---
        binding.imageButton.setOnClickListener {
            it.startAnimation(clickScale)
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 150) // Delay to see click animation
        }

        // Assuming your "Add to Cart" button's ID in activity_details.xml is android:id="@+id/addToCartButton"
        // ViewBinding will generate a property named `binding.addToCartButton`.
        // If your XML has `android:id="@+id/button"`, then `binding.button` is correct.
        // Let's use `binding.addToCartButton` as it's more descriptive and matches the XML you provided earlier.
        // If you are certain it's `binding.button`, please adjust.
        // **The code you provided uses `binding.button` in the click listener, so I'll edit that part.**

        binding.addToCartButton.setOnClickListener { // Use binding.button if that's what you've used throughout
            it.startAnimation(clickScale) // Optional: click animation

            // --- THIS IS WHERE YOU ADD THE TOAST ---
            val foodName = binding.detailFoodName.text.toString()
            Toast.makeText(this, "$foodName added to cart", Toast.LENGTH_SHORT).show()
            // -----------------------------------------

            // You can add a slight delay for the Toast to be visible before other actions
            // if needed, but usually it's fine without for simple Toasts.
            // For example, if you were navigating away immediately:
            // Handler(Looper.getMainLooper()).postDelayed({
            //    // Navigate or do other things
            // }, 300) // Small delay
        }


        // --- 5. Load Actual Data into Views ---
        loadDetailsData()
    }

    private fun loadDetailsData() {
        val foodName = intent.getStringExtra("MenuItemName")
        val foodImageResId = intent.getIntExtra("MenuItemImage", 0)

        binding.detailFoodName.text = foodName ?: "Food Item"

        if (foodImageResId != 0) {
            binding.DetailFoodImage.setImageResource(foodImageResId)
        } else {
            // Consider having a placeholder image or handling this case more gracefully
            binding.DetailFoodImage.setImageResource(R.drawable.burger) // Default image if 0 is passed
        }

        // Example: If you also pass description and ingredients
        // val description = intent.getStringExtra("MenuItemDescription") ?: "No description available."
        // val ingredients = intent.getStringExtra("MenuItemIngredients") ?: "Ingredients not listed."
        // binding.DescriptionTextView.text = description
        // binding.IngredientsTextView.text = ingredients.replace("\\n", "\n")
    }

    override fun finish() {
        super.finish()
        // overridePendingTransition(R.anim.stay_still, R.anim.slide_out_to_right)
    }
}
