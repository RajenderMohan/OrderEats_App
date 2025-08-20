package com.rajender.ordereats // Ensure this is your correct package

// Import your R file if it's not automatically resolved
// import com.rajender.ordereats.R
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.rajender.ordereats.Fragment.CongratsBottomSheet
import com.rajender.ordereats.databinding.ActivityPayOutBinding

class PayOutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPayOutBinding

    // Animation Delays (in milliseconds)
    private val DELAY_BACK_BUTTON = 100L
    private val DELAY_TITLE = 200L
    private val DELAY_NAME_FIELD = 300L
    private val DELAY_ADDRESS_FIELD = 370L
    private val DELAY_PHONE_FIELD = 440L
    private val DELAY_PAYMENT_FIELD = 510L
    private val DELAY_TOTAL_FIELD = 580L
    private val DELAY_PLACE_ORDER_BUTTON = 700L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- 1. Load Animations ---
        val slideInFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left_simple)
        val fadeInText = AnimationUtils.loadAnimation(this, R.anim.fade_in_text)
        // val slideInFormField = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right_form_field) // Loaded per view below
        val slideUpFromBottom = AnimationUtils.loadAnimation(this, R.anim.slide_up_from_bottom)
        val clickScale = AnimationUtils.loadAnimation(this, R.anim.click_scale)

        // --- 2. Prepare Views for Animation ---
        val viewsToAnimate = listOf(
            binding.buttonBack,
            binding.textView21, // This is your "Edit" title
            binding.nameFieldContainer,
            binding.addressFieldContainer,
            binding.phoneFieldContainer,
            binding.paymentMethodContainer,
            binding.totalAmountContainer,
            binding.placeMyOrder
        )
        viewsToAnimate.forEach { it.visibility = View.INVISIBLE }

        val handler = Handler(Looper.getMainLooper())

        // --- 3. Apply Staggered Entrance Animations ---
        handler.postDelayed({
            binding.buttonBack.visibility = View.VISIBLE
            binding.buttonBack.startAnimation(slideInFromLeft)
        }, DELAY_BACK_BUTTON)

        handler.postDelayed({
            binding.textView21.visibility = View.VISIBLE
            binding.textView21.startAnimation(fadeInText)
        }, DELAY_TITLE)

        // Form Fields
        handler.postDelayed({
            binding.nameFieldContainer.visibility = View.VISIBLE
            binding.nameFieldContainer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right_form_field))
        }, DELAY_NAME_FIELD)

        handler.postDelayed({
            binding.addressFieldContainer.visibility = View.VISIBLE
            binding.addressFieldContainer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right_form_field))
        }, DELAY_ADDRESS_FIELD)

        handler.postDelayed({
            binding.phoneFieldContainer.visibility = View.VISIBLE
            binding.phoneFieldContainer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right_form_field))
        }, DELAY_PHONE_FIELD)

        handler.postDelayed({
            binding.paymentMethodContainer.visibility = View.VISIBLE
            binding.paymentMethodContainer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right_form_field))
        }, DELAY_PAYMENT_FIELD)

        handler.postDelayed({
            binding.totalAmountContainer.visibility = View.VISIBLE
            binding.totalAmountContainer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right_form_field))
        }, DELAY_TOTAL_FIELD)

        // Place Order Button
        handler.postDelayed({
            binding.placeMyOrder.visibility = View.VISIBLE
            binding.placeMyOrder.startAnimation(slideUpFromBottom)
        }, DELAY_PLACE_ORDER_BUTTON)

        // --- 4. Setup Click Listeners (incorporating your original logic) ---
        binding.buttonBack.setOnClickListener {
            it.startAnimation(clickScale) // Apply click animation
            // Add a small delay for the click animation to be visible before finishing
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 150) // Adjust delay as needed
        }

        binding.placeMyOrder.setOnClickListener {
            it.startAnimation(clickScale) // Apply click animation
            // Your original logic for the "Place My Order" button:
            // Consider a small delay if the animation makes the bottom sheet appear abruptly
            Handler(Looper.getMainLooper()).postDelayed({
                val bottomSheetDialog = CongratsBottomSheet()
                bottomSheetDialog.show(supportFragmentManager, "CongratsDialog")
            }, 800) // Small delay, adjust or remove if not desired
        }

        // --- 5. Load/Prefill Data (if any) ---
        // loadPayOutData() // If you need to prefill data
    }

    // Optional function to load or prefill data
    // private fun loadPayOutData() {
    // binding.nameEditText.setText(intent.getStringExtra("USER_NAME") ?: "Admin")
    // ... and so on for other fields
    // }

    override fun finish() {
        super.finish()
        // Optional: Apply a slide-out animation when this activity finishes
        // overridePendingTransition(R.anim.stay_still, R.anim.slide_out_to_right) // Example animations
    }
}
