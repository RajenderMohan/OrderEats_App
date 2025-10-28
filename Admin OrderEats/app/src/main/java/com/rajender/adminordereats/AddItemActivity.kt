package com.rajender.adminordereats

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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
    private var foodImageUri: Uri? = null //<- URI ko store karne ke liye

    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    // Gallery se image chunne ke liye launcher
    private val pickImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                // NEW: Jab image chuni jaye to toast dikhayein
                Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show()

                // Chuni hui image ka URI save karein
                foodImageUri = uri
                binding.selectedImage.setImageURI(uri)

                // Image preview ko animation ke saath dikhayein
                val imagePopInAnim = AnimationUtils.loadAnimation(this, R.anim.image_pop_in)
                binding.cardView6.startAnimation(imagePopInAnim)
                binding.cardView6.visibility = View.VISIBLE
            } else {
                // Agar user image nahi chunta hai to toast dikhayein
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Firebase ko initialize karein
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // --- Animations ---
        setupAnimations()

        // --- Click Listeners ---
        setupClickListeners()
    }

    private fun setupAnimations() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.backButton.startAnimation(fadeIn)
        binding.textView13.startAnimation(fadeIn) // "Add Item" Title

        // UI elements ko ek-ek karke animate karein
        startSequentialAnimations(
            listOf(
                binding.foodName,
                binding.foodPrice,
                binding.selectImage,
                binding.descriptionTextView,
                binding.description,
                binding.ingredientTextView,
                binding.ingredient,
                binding.AddItemButton
            ),
            R.anim.slide_up_input,
            100L
        )

        // Shuru me image preview ko chupayein
        binding.cardView6.visibility = View.GONE
    }

    private fun setupClickListeners() {
        val clickScale = AnimationUtils.loadAnimation(this, R.anim.click_scale)

        // 1. Back Button
        binding.backButton.setOnClickListener {
            it.startAnimation(clickScale)
            finish() // Activity ko band karega
        }
        // 2. Select Image TextView
        binding.selectImage.setOnClickListener {
            it.startAnimation(clickScale)
            // Gallery kholne ke liye launcher ko start karein
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // 3. Add Item Button
        binding.AddItemButton.setOnClickListener {
            it.startAnimation(clickScale)

            // Form se data get karein
            foodName = binding.foodName.text.toString().trim()
            foodPrice = binding.foodPrice.text.toString().trim()
            foodDescription = binding.description.text.toString().trim()
            foodIngredient = binding.ingredient.text.toString().trim()

            // Check karein ki sabhi fields bhari hain aur image chuni gayi hai
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
        // 4. Selected Image (re-select karne ke liye)
        binding.selectedImage.setOnClickListener {
            it.startAnimation(clickScale)
            // Dobara gallery kholne ke liye
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    // ✅ Sahi jagah: Yeh function ab class ka member hai
    private fun uploadData() {
        // Data upload shuru hone par toast
        Toast.makeText(this, "Uploading item, please wait...", Toast.LENGTH_SHORT).show()

        // "menu" node ka reference lein
        val menuRef: DatabaseReference = database.getReference("menu")
        // Naye item ke liye ek unique key generate karein
        val newItemKey: String? = menuRef.push().key

        if (newItemKey == null) {
            Toast.makeText(this, "Could not create new item key.", Toast.LENGTH_SHORT).show()
            return
        }

        // Yahan par aapko Firebase Storage me image upload karne ka code likhna hoga.
        // Upload hone ke baad, aap download URL ke saath data ko Realtime Database me save karenge.

        // For now, let's just show a success message and finish.
        Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_LONG).show() // Changed to LONG for better visibility
        finish() // Activity ko band karein
    }

    // ✅ Sahi jagah: Yeh function ab class ka member hai
    private fun startSequentialAnimations(views: List<View>, animResId: Int, staggerDelay: Long) {
        views.forEachIndexed { index, view ->
            val animation = AnimationUtils.loadAnimation(view.context, animResId)
            animation.startOffset = index * staggerDelay
            view.startAnimation(animation)
        }
    }

    // ✅ Sahi jagah: Yeh function ab class ka member hai
    override fun finish() {
        super.finish()
        // Activity band hote time transition apply karein
        overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity)
    }
}
