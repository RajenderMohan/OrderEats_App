package com.rajender.adminordereats // Ensure this is your correct package name

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.rajender.adminordereats.databinding.ActivitySignUpBinding
import com.rajender.adminordereats.model.UserModel

class SignUpActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var nameOfRestaurant: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    // Define NEW animation start delays for the unique sequence
    private val DELAY_LOGO = 0L
    private val DELAY_MAIN_TITLE = 200L // Adjusted for potentially faster feel
    private val DELAY_SUB_HEADER = 350L
    private val DELAY_LOCATION_INPUT = 500L
    private val DELAY_OWNER_NAME = 650L         // Slide from left
    private val DELAY_RESTAURANT_NAME = 750L    // Slide from right
    private val DELAY_EMAIL_PHONE = 850L       // Slide from left
    private val DELAY_PASSWORD = 950L          // Slide from right
    private val DELAY_CREATE_BUTTON = 1100L
    private val DELAY_ALREADY_HAVE_ACCOUNT = 1250L
    private val DELAY_DEV_NAME = 1400L // Assuming textView12 is present and to be animated


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        //Initialize Firebase Auth
        auth = Firebase.auth
        // Initialize Firebase Database
        database = Firebase.database.reference


        // Activity Enter Transition (e.g., from LoginActivity)
        // Ensure slide_in_right_activity and slide_out_left_activity exist
        overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity)

        // Setup AutoCompleteTextView for Location
        // Ensure your locationList has appropriate values
        val locationList = arrayOf("Jaipur", "Mumbai", "Delhi", "Kolkata", "Chennai", "Bengaluru")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        binding.listOfLocation.setAdapter(adapter)

        // --- Load Unique Animations (Ensure these are in res/anim) ---
        val dropBounceLogo = AnimationUtils.loadAnimation(this, R.anim.drop_bounce_logo)
        val titleRevealUp = AnimationUtils.loadAnimation(this, R.anim.title_reveal_up)
        val slideInFromBottomOvershoot =
            AnimationUtils.loadAnimation(this, R.anim.slide_in_from_bottom_overshoot)
        val slideInLeftOvershoot =
            AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left_overshoot)
        val slideInRightOvershoot =
            AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right_overshoot)
        val buttonEmergeRotate = AnimationUtils.loadAnimation(this, R.anim.button_emerge_rotate)
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in) // For general purpose
        val subtleRise = AnimationUtils.loadAnimation(
            this,
            R.anim.subtle_rise_fade_in
        ) // For dev name or less prominent text
        val clickScale = AnimationUtils.loadAnimation(this, R.anim.click_scale)


        // --- Prepare Views for Staggered Animation (Set to INVISIBLE initially) ---
        // Assuming textView12 from your XML is for the developer name
        val viewsToAnimate = listOf(
            binding.imageView3, binding.textView6, binding.textView8,
            binding.textInputLayout, // Includes the "Choose your Location" label visually
            binding.nameofOwner, binding.nameOfRestaurant,
            binding.emailOrPhone, binding.passwordText,
            binding.createButton, binding.alreadyhavebutton,
            binding.textView12 // Added for dev name animation
        )
        viewsToAnimate.forEach { it.visibility = View.INVISIBLE }

        val handler = Handler(Looper.getMainLooper())

        // --- Start Unique Staggered Entrance Animations (Using Helper Function) ---

        applyAnimationWithDelay(binding.imageView3, dropBounceLogo, DELAY_LOGO, handler)
        applyAnimationWithDelay(binding.textView6, titleRevealUp, DELAY_MAIN_TITLE, handler)
        applyAnimationWithDelay(
            binding.textView8,
            fadeIn,
            DELAY_SUB_HEADER,
            handler
        ) // Or titleRevealUp, or subtleRise
        applyAnimationWithDelay(
            binding.textInputLayout,
            slideInFromBottomOvershoot,
            DELAY_LOCATION_INPUT,
            handler
        )
        applyAnimationWithDelay(
            binding.nameofOwner,
            slideInLeftOvershoot,
            DELAY_OWNER_NAME,
            handler
        )
        applyAnimationWithDelay(
            binding.nameOfRestaurant,
            slideInRightOvershoot,
            DELAY_RESTAURANT_NAME,
            handler
        )
        applyAnimationWithDelay(
            binding.emailOrPhone,
            slideInLeftOvershoot,
            DELAY_EMAIL_PHONE,
            handler
        )
        applyAnimationWithDelay(
            binding.passwordText,
            slideInRightOvershoot,
            DELAY_PASSWORD,
            handler
        )
        applyAnimationWithDelay(
            binding.createButton,
            buttonEmergeRotate,
            DELAY_CREATE_BUTTON,
            handler
        )
        applyAnimationWithDelay(
            binding.alreadyhavebutton,
            fadeIn,
            DELAY_ALREADY_HAVE_ACCOUNT,
            handler
        ) // Or subtleRise
        applyAnimationWithDelay(
            binding.textView12,
            subtleRise,
            DELAY_DEV_NAME,
            handler
        ) // Animating dev name


        // --- Click Listeners with Animations and Transitions ---
        binding.createButton.setOnClickListener {
            // Get text from Edittext
            userName = binding.nameofOwner.text.toString().trim()
            nameOfRestaurant = binding.nameOfRestaurant.text.toString().trim()
            email = binding.emailOrPhone.text.toString().trim()
            password = binding.passwordText.text.toString().trim()

            if (userName.isBlank() || nameOfRestaurant.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email, password)
            }

            it.startAnimation(clickScale)
            // TODO: Add your admin account creation logic here

            // Example: Navigate to MainActivity, potentially with Shared Element if logo_transition is set
            // Ensure com.rajender.adminordereats.MainActivity is the correct target for admin app
            // val intent = Intent(this, com.rajender.adminordereats.MainActivity::class.java)

            // If you want shared element transition for the logo FROM SignUpActivity TO MainActivity:
            if (binding.imageView3.transitionName != null && binding.imageView3.transitionName == "logo_transition") {
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    binding.imageView3,
                    "logo_transition" // Ensure MainActivity's logo ImageView has this transitionName
                )
                startActivity(intent, options.toBundle())
            } else {
                startActivity(intent)
                // Fallback transition if shared element is not used or not set up on target
                overridePendingTransition(
                    R.anim.slide_in_right_activity,
                    R.anim.slide_out_left_activity
                )
            }
            // finishAffinity() // Consider if signup is the end of a flow and you want to clear prior activities.
            // finish() // Or just finish SignUpActivity if appropriate
        }

        binding.alreadyhavebutton.setOnClickListener {
            it.startAnimation(clickScale)
            finish() // This will trigger the finish() override for exit animation, returning to LoginActivity
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: Failure", task.exception)
            }
        }
    }
// Save Data in to Database
    private fun saveUserData() {
        // Get text from Edittext
        userName = binding.nameofOwner.text.toString().trim()
        nameOfRestaurant = binding.nameOfRestaurant.text.toString().trim()
        email = binding.emailOrPhone.text.toString().trim()
        password = binding.passwordText.text.toString().trim()
        val user = UserModel(userName, nameOfRestaurant, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
            // Save user Data Firebase Database
        database.child("admins").child(userId).setValue(user)
    }

    /**
     * Helper function to apply an animation to a view after a delay.
     * Makes the view visible just before starting the animation.
     */
    private fun applyAnimationWithDelay(
        view: View,
        animationToApply: Animation,
        delay: Long,
        animHandler: Handler
    ) {
        animHandler.postDelayed({
            view.visibility = View.VISIBLE
            view.startAnimation(animationToApply)
        }, delay)
    }

    override fun finish() {
        super.finish()
        // Activity Exit Transition (back to LoginActivity)
        // Ensure slide_in_left_activity and slide_out_right_activity exist
        overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_right_activity)
    }
}
