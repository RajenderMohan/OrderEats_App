package com.rajender.adminordereats

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rajender.adminordereats.databinding.ActivityAddItemBinding

class AddItemActivity : AppCompatActivity() {

    // Food Item Details
    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private lateinit var foodIngredient: String
    private var foodImageUri: Uri? = null

    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                foodImageUri = uri
                binding.selectedImage.setImageURI(uri)
                binding.tapToSelectImage.visibility = View.GONE
                Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        setupAnimations()
        setupClickListeners()
    }

    private fun setupAnimations() {
        // Header Animation
        val slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        binding.headerTitle.startAnimation(slideDown)

        // Staggered slide-up animation for input fields
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        val fieldsToAnimate = listOf(
            binding.foodName,
            binding.foodPrice,
            binding.foodImageLabel,
            binding.selectImage,
            binding.description,
            binding.ingredient,
            binding.AddItemButton
        )

        fieldsToAnimate.forEachIndexed { index, view ->
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
            animation.startOffset = (index * 100L)
            view.startAnimation(animation)
        }

        // Pulsate animation for the Add Item button
        val pulsate = AnimationUtils.loadAnimation(this, R.anim.pulsate)
        binding.AddItemButton.startAnimation(pulsate)
    }

    private fun setupClickListeners() {
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.selectImage.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.AddItemButton.setOnClickListener {
            foodName = binding.foodName.text.toString().trim()
            foodPrice = binding.foodPrice.text.toString().trim()
            foodDescription = binding.description.text.toString().trim()
            foodIngredient = binding.ingredient.text.toString().trim()

            if (foodName.isNotBlank() && foodPrice.isNotBlank() && foodDescription.isNotBlank() && foodIngredient.isNotBlank()) {
                if (foodImageUri != null) {
                    uploadData()
                } else {
                    Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadData() {
        val menuRef: DatabaseReference = database.getReference("menu")
        val newItemKey: String? = menuRef.push().key

        if (newItemKey != null) {
            // Here you would upload the image to Firebase Storage and then save the item details
            Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_LONG).show()
            finish()
        } else {
            Toast.makeText(this, "Could not create new item key.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity)
    }
}
