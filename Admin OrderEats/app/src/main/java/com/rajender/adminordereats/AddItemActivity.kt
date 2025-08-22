package com.rajender.adminordereats

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.Animation // Keep this if you use Animation type elsewhere, otherwise can be removed if not directly used
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest // Your existing import
import androidx.activity.result.contract.ActivityResultContracts // Your existing import
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rajender.adminordereats.databinding.ActivityAddItemBinding

class AddItemActivity : AppCompatActivity() {

    // Food Item Details
    private lateinit var foodName : String
    private lateinit var foodPrice : String
    private lateinit var foodDescription : String
    private lateinit var foodIngredient : String
    private var foodImageUri : Uri?= null
    // Firebase
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase

    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    // Your existing image picker launcher
    private val pickImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.selectedImage.setImageURI(uri)
                // Animate the image appearing
                val imagePopInAnim = AnimationUtils.loadAnimation(this, R.anim.image_pop_in)
                binding.cardView6.startAnimation(imagePopInAnim) // Animate the CardView
                binding.cardView6.visibility = View.VISIBLE
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Handles edge-to-edge display
        setContentView(binding.root)

            // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()

    binding.AddItemButton.setOnClickListener {
        // Get data from the form
        foodName = binding.foodName.text.toString().trim()
        foodPrice = binding.foodPrice.text.toString().trim()
        foodDescription = binding.description.text.toString().trim()
        foodIngredient = binding.ingredient.text.toString().trim()

        if(!(foodName.isBlank() || foodPrice.isBlank() || foodDescription.isBlank() || foodIngredient.isBlank())){
            uploadData()
            Toast.makeText(this, "Item Add Successfully", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            finish()
        }
    }


        // --- Load Animations (those used directly, not in the helper) ---
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        // slideUpInput is now loaded inside startSequentialAnimations, so no need to load it here for that purpose
        val clickScale = AnimationUtils.loadAnimation(this, R.anim.click_scale)

        // --- Entrance Animations for Form Elements ---
        binding.backButton.startAnimation(fadeIn)
        binding.textView13.startAnimation(fadeIn) // "Add Item" Title

        // Stagger the animation for input fields
        startSequentialAnimations(
            listOf(
                binding.foodName,
                binding.foodPrice,
                binding.selectImage,       // The TextView for "Select Image"
                binding.descriptionTextView,
                binding.description,
                binding.ingredientTextView,
                binding.ingredient,
                binding.AddItemButton      // Your "Add Item" button
            ),
            R.anim.slide_up_input, // *** CORRECTED: Pass the resource ID (Int) ***
            100L          // Stagger delay in milliseconds
        )

        // Initially hide the image preview CardView
        binding.cardView6.visibility = View.GONE

        // --- Click Listeners and Transforms/Transitions ---

        // 1. Back Button with Exit Transition
        binding.backButton.setOnClickListener {
            it.startAnimation(clickScale) // Optional: click feedback
            finish()
            // Standard slide-out transition (ensure these animation files exist in res/anim)
            overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity)
        }



        // 3. Click animation (transform) on "Add Item" Button
        binding.AddItemButton.setOnClickListener {
            it.startAnimation(clickScale)
            // TODO: Add your item saving logic here
            // Example:
            // if (allFieldsValid()) {
            //    saveItemToDatabase()
            //    Toast.makeText(this, "Item Added!", Toast.LENGTH_SHORT).show()
            //    finish()
            //    overridePendingTransition(R.anim.fade_in, R.anim.fade_out) // Or another exit transition
            // } else {
            //    Toast.makeText(this, "Please fill all required fields.", Toast.LENGTH_LONG).show()
            // }
        }

        // 4. (Optional) Transform on the selectedImage (ImageView inside CardView)
        binding.selectedImage.setOnClickListener {
            // Example: allow re-selection or just a visual feedback
            it.startAnimation(clickScale)
            // pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) // To re-select
        }
    }

    private fun uploadData() {
        // get a reference to the "menu" node in the database
        val menuRef : DatabaseReference = database.getReference("menu")

        // create a unique key for the new menu item
        val newItemKey: String? = menuRef.push().key



    }

    /**
     * Helper function to apply an animation to a list of views sequentially with a delay.
     * Takes the animation resource ID instead of a pre-loaded Animation object.
     */
    private fun startSequentialAnimations(views: List<View>, animResId: Int, staggerDelay: Long) { // *** CORRECTED: animResId is now Int ***
        views.forEachIndexed { index, view ->
            val animation = AnimationUtils.loadAnimation(view.context, animResId) // animResId is correctly an Int here
            animation.startOffset = index * staggerDelay
            view.startAnimation(animation)
        }
    }

    // Handle back press for consistent exit transition
    override fun finish() {
        super.finish()
        // Apply exit transition when activity is finishing for any reason (back button, programmatically)
        // Ensure this is called only if you want this specific transition when finishing.
        // If back press needs a different transition than programmatic finish(), handle it in onBackPressed().
        // For simplicity, applying it here covers most cases.
        if (isFinishing) { // Check if activity is actually finishing
            overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity)
        }
    }

    // You might want to override onBackPressed specifically if you need different logic/transition
    // than the generic finish()
    /*
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity)
    }
    */
}
