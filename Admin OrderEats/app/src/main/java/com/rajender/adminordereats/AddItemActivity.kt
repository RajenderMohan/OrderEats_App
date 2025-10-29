package com.rajender.adminordereats

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rajender.adminordereats.databinding.ActivityAddItemBinding

class AddItemActivity : AppCompatActivity() {

    // Food Item Details
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
        setupInputValidation()
    }

    private fun setupAnimations() {
        // Header Animation
        val slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        binding.headerTitle.startAnimation(slideDown)

        // Staggered slide-up animation for the form
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        val controller = LayoutAnimationController(slideUp)
        controller.delay = 0.2f // a delay of 20% of the animation duration
        controller.order = LayoutAnimationController.ORDER_NORMAL

        binding.formContainer.layoutAnimation = controller
        binding.formContainer.startLayoutAnimation()
    }

    private fun setupClickListeners() {
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.selectImage.setOnClickListener {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.AddItemButton.setOnClickListener {
            if (validateInput()) {
                uploadData()
            }
        }
    }

    private fun setupInputValidation() {
        binding.foodName.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrBlank()) {
                binding.foodNameLayout.error = "Food name is required"
            } else {
                binding.foodNameLayout.error = null
            }
        }

        binding.foodPrice.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrBlank()) {
                binding.foodPriceLayout.error = "Food price is required"
            } else {
                binding.foodPriceLayout.error = null
            }
        }

        binding.description.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrBlank()) {
                binding.descriptionLayout.error = "Description is required"
            } else {
                binding.descriptionLayout.error = null
            }
        }

        binding.ingredient.doOnTextChanged { text, _, _, _ ->
            if (text.isNullOrBlank()) {
                binding.ingredientLayout.error = "Ingredients are required"
            } else {
                binding.ingredientLayout.error = null
            }
        }
    }

    private fun validateInput(): Boolean {
        val isFoodNameValid = !binding.foodName.text.isNullOrBlank()
        val isFoodPriceValid = !binding.foodPrice.text.isNullOrBlank()
        val isDescriptionValid = !binding.description.text.isNullOrBlank()
        val isIngredientValid = !binding.ingredient.text.isNullOrBlank()
        val isImageSelected = foodImageUri != null

        if (!isFoodNameValid) binding.foodNameLayout.error = "Food name is required"
        if (!isFoodPriceValid) binding.foodPriceLayout.error = "Food price is required"
        if (!isDescriptionValid) binding.descriptionLayout.error = "Description is required"
        if (!isIngredientValid) binding.ingredientLayout.error = "Ingredients are required"

        if (!isImageSelected) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
        }

        return isFoodNameValid && isFoodPriceValid && isDescriptionValid && isIngredientValid && isImageSelected
    }

    private fun uploadData() {
        setInProgress(true)

        val menuRef: DatabaseReference = database.getReference("menu")
        val newItemKey: String? = menuRef.push().key

        if (newItemKey != null) {
            // Here you would upload the image to Firebase Storage and then save the item details
            // For now, we'll just simulate a delay
            binding.root.postDelayed({
                setInProgress(false)
                Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_LONG).show()
                finish()
            }, 2000)
        } else {
            setInProgress(false)
            Toast.makeText(this, "Could not create new item key.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.progressBar.visibility = View.VISIBLE
            binding.AddItemButton.isEnabled = false
        } else {
            binding.progressBar.visibility = View.GONE
            binding.AddItemButton.isEnabled = true
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity)
    }
}
