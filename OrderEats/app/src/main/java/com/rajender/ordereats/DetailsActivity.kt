package com.rajender.ordereats // Make sure this is your correct package

// Import your R file if not automatically resolved, though usually it is
// import com.rajender.ordereats.R
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
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

        // --- 1. Load All Necessary Animations ---
        val slideInFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left_simple)
        val fadeInText = AnimationUtils.loadAnimation(this, R.anim.fade_in_text)
        val scaleFadeInCard = AnimationUtils.loadAnimation(this, R.anim.scale_fade_in_card)
        val slideUpFromBottom = AnimationUtils.loadAnimation(this, R.anim.slide_up_from_bottom)
        val clickScale = AnimationUtils.loadAnimation(this, R.anim.click_scale)


        // --- 2. Prepare Views for Animation (Set initial visibility to INVISIBLE) ---
        val viewsToAnimate = listOf(
            binding.imageButton,
            binding.detailFoodName,
            binding.cardView4,
            binding.textView24,
            binding.DescriptionTextView,
            binding.textView26,
            binding.IngredientsTextView,
            binding.button // This is your Add To Cart button
        )
        viewsToAnimate.forEach { it.visibility = View.INVISIBLE }

        val handler = Handler(Looper.getMainLooper())

        // --- 3. Apply Staggered Entrance Animations ---
        // Back Button
        handler.postDelayed({
            binding.imageButton.visibility = View.VISIBLE
            binding.imageButton.startAnimation(slideInFromLeft)
        }, DELAY_BACK_BUTTON)

        // Food Name
        handler.postDelayed({
            binding.detailFoodName.visibility = View.VISIBLE
            binding.detailFoodName.startAnimation(fadeInText)
        }, DELAY_FOOD_NAME)

        // Food Image CardView
        handler.postDelayed({
            binding.cardView4.visibility = View.VISIBLE
            binding.cardView4.startAnimation(scaleFadeInCard)
        }, DELAY_FOOD_IMAGE_CARD)

        // Short Description Title
        // Reusing slideInFromLeft for textView24
        val slideInDescTitle = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left_simple)
        handler.postDelayed({
            binding.textView24.visibility = View.VISIBLE
            binding.textView24.startAnimation(slideInDescTitle)
        }, DELAY_DESC_TITLE)

        // Description Text
        val fadeInDescText = AnimationUtils.loadAnimation(this, R.anim.fade_in_text)
        handler.postDelayed({
            binding.DescriptionTextView.visibility = View.VISIBLE
            binding.DescriptionTextView.startAnimation(fadeInDescText)
        }, DELAY_DESC_TEXT)

        // Ingredients Title
        // Reusing slideInFromLeft for textView26
        val slideInIngredientsTitle = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left_simple)
        handler.postDelayed({
            binding.textView26.visibility = View.VISIBLE
            binding.textView26.startAnimation(slideInIngredientsTitle)
        }, DELAY_INGREDIENTS_TITLE)

        // Ingredients Text
        val fadeInIngredientsText = AnimationUtils.loadAnimation(this, R.anim.fade_in_text)
        handler.postDelayed({
            binding.IngredientsTextView.visibility = View.VISIBLE
            binding.IngredientsTextView.startAnimation(fadeInIngredientsText)
        }, DELAY_INGREDIENTS_TEXT)


        // Add to Cart Button (binding.button)
        handler.postDelayed({
            binding.button.visibility = View.VISIBLE
            binding.button.startAnimation(slideUpFromBottom)
        }, DELAY_ADD_TO_CART_BUTTON)


        // --- 4. Setup Click Listeners (with optional click animation) ---
        binding.imageButton.setOnClickListener {
            it.startAnimation(clickScale)
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 150) // Delay to see click animation
        }

        binding.button.setOnClickListener {
            it.startAnimation(clickScale)
            // TODO: Implement your "Add To Cart" logic here
            // For example: Toast.makeText(this, "${binding.detailFoodName.text} added to cart", Toast.LENGTH_SHORT).show()
        }

        // --- 5. Load Actual Data into Views ---
        // It's generally better to load data before making views visible if animations
        // don't depend on the content size changing drastically.
        // However, for this example, data is loaded here.
        loadDetailsData()
    }

    private fun loadDetailsData() {
        val foodName = intent.getStringExtra("MenuItemName") // Using the key from your original code
        val foodImageResId = intent.getIntExtra("MenuItemImage", 0) // Using the key from your original code

        binding.detailFoodName.text = foodName ?: "Food Item" // Provide a default if null

        if (foodImageResId != 0) {
            binding.DetailFoodImage.setImageResource(foodImageResId)
        } else {
            binding.DetailFoodImage.setImageResource(R.drawable.burger) // Default image if 0 is passed
        }

        // You might want to pass description and ingredients via Intent as well
        // For now, they will use the default text from your XML or you can set defaults here.
        // Example:
        // binding.DescriptionTextView.text = intent.getStringExtra("MenuItemDescription") ?: "A delicious food item."
        // binding.IngredientsTextView.text = intent.getStringExtra("MenuItemIngredients")?.replace("\\n", "\n") ?: "Fresh ingredients."
    }

    override fun finish() {
        super.finish()
        // Optional: Apply a slide-out animation when this activity finishes
        // overridePendingTransition(R.anim.stay_still, R.anim.slide_out_to_right) // Example animations
    }
}
