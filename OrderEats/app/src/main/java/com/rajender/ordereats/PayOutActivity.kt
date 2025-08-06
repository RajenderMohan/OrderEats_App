package com.rajender.ordereats

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rajender.ordereats.Fragment.CongratsBottomSheet
import com.rajender.ordereats.databinding.ActivityPayOutBinding

class PayOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPayOutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set OnClickListener for the Place My Order button
        binding.placeMyOrder.setOnClickListener {
            val bottomSheetDialog = CongratsBottomSheet()
            bottomSheetDialog.show(supportFragmentManager, "CongratsDialog") // Changed tag for clarity
        }

        // --- Add this block for the back button ---
        binding.buttonBack.setOnClickListener {
            finish()
        }


    }
}
