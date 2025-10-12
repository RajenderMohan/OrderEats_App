package com.rajender.ordereats

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rajender.ordereats.Fragment.CongratsBottomSheet
import com.rajender.ordereats.databinding.ActivityPayOutBinding
import java.text.NumberFormat
import java.util.Locale

class PayOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPayOutBinding
    private var selectedPaymentMethod: String = "Cash On Delivery"

    private val DELAY_BACK_BUTTON = 100L
    private val DELAY_TITLE = 200L
    private val DELAY_NAME_FIELD = 300L
    private val DELAY_ADDRESS_FIELD = 370L
    private val DELAY_PHONE_FIELD = 440L
    private val DELAY_PAYMENT_TITLE = 510L // Ensure this corresponds to a view if used
    private val DELAY_RADIO_GROUP = 580L
    private val DELAY_TOTAL_FIELD = 650L
    private val DELAY_PLACE_ORDER_BUTTON = 750L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAnimations()

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.paymentRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                val radioButton = group.findViewById<RadioButton>(checkedId)
                selectedPaymentMethod = radioButton.text.toString()
            }
        }

        binding.placeMyOrderButton.setOnClickListener {
            // Optional: it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.click_scale))
            val name = binding.nameEditText.text.toString().trim()
            val address = binding.addressEditText.text.toString().trim()
            val phone = binding.phoneEditText.text.toString().trim()

            if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.paymentRadioGroup.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Please select a payment method.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Handler(Looper.getMainLooper()).postDelayed({
                val bottomSheetDialog = CongratsBottomSheet()
                bottomSheetDialog.show(supportFragmentManager, "CongratsDialog")
                // TODO: Save order details, process payment, clear cart
            }, if (selectedPaymentMethod == "Cash On Delivery") 300L else 800L)
        }

        loadPayOutData()
    }

    private fun setupAnimations() {
        val slideInFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left_simple)
        val fadeInText = AnimationUtils.loadAnimation(this, R.anim.fade_in_text)
        val slideInFormField = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right_form_field)
        val slideUpFromBottom = AnimationUtils.loadAnimation(this, R.anim.slide_up_from_bottom)
        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)

        // Ensure these views exist and match your XML.
        // If you have a payment title TextView, add it here and adjust delays/animations.
        val viewsToAnimate = listOf(
            binding.buttonBack, binding.textView21, binding.nameFieldContainer,
            binding.addressFieldContainer, binding.phoneFieldContainer,
            binding.paymentRadioGroup, // Or animate a title TextView for payments here
            binding.totalAmountContainer, binding.placeMyOrderButton
        )
        viewsToAnimate.forEach { it.visibility = View.INVISIBLE }

        val handler = Handler(Looper.getMainLooper())

        // Ensure these delays and animations match the order and number of viewsToAnimate
        val delays = listOf(
            DELAY_BACK_BUTTON, DELAY_TITLE, DELAY_NAME_FIELD, DELAY_ADDRESS_FIELD,
            DELAY_PHONE_FIELD, DELAY_RADIO_GROUP, // Or DELAY_PAYMENT_TITLE then another for RadioGroup
            DELAY_TOTAL_FIELD, DELAY_PLACE_ORDER_BUTTON
        )
        val animations = listOf(
            slideInFromLeft, fadeInText, slideInFormField, slideInFormField,
            slideInFormField, fadeIn, // Or animation for payment title then another for RadioGroup
            slideInFormField, slideUpFromBottom
        )

        for (i in viewsToAnimate.indices) {
            val view = viewsToAnimate[i]
            // Ensure animation list is not shorter than view list
            val animation = animations.getOrElse(i) { fadeIn }
            // Ensure delay list is not shorter than view list
            val delay = delays.getOrElse(i) { 500L } // Default delay if lists mismatch

            handler.postDelayed({
                view.visibility = View.VISIBLE
                view.startAnimation(animation)
            }, delay)
        }
    }

    private fun loadPayOutData() {
        val totalAmountValue = intent.getDoubleExtra("TOTAL_AMOUNT_EXTRA", 686.0)

        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        val formattedTotal = currencyFormat.format(totalAmountValue)
        binding.totalAmountEditText.setText(formattedTotal)


        if (binding.paymentRadioGroup.checkedRadioButtonId == -1) {
            binding.radioCOD.isChecked = true // Default if nothing is checked
            selectedPaymentMethod = binding.radioCOD.text.toString()
        } else {
            val initialCheckedRadioButton = binding.paymentRadioGroup.findViewById<RadioButton>(binding.paymentRadioGroup.checkedRadioButtonId)
            selectedPaymentMethod = initialCheckedRadioButton?.text?.toString() ?: "Cash On Delivery"
        }
    }

    override fun finish() {
        super.finish()
        // overridePendingTransition(R.anim.stay_still, R.anim.slide_out_to_right)
    }
}
