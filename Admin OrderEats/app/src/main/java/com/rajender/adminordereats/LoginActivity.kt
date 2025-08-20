package com.rajender.adminordereats

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.rajender.adminordereats.databinding.ActivityLoginBinding
import com.rajender.adminordereats.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String
    private var userName: String? = null
    private var nameOfRestaurant: String? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    // Define NEW animation start delays for the unique sequence
    private val DELAY_LOGO = 0L
    private val DELAY_MAIN_TITLE = 200L       // "OrderEats" title
    private val DELAY_SUB_HEADER = 350L     // "Login To Your Admin Dashboard"
    private val DELAY_EMAIL_FIELD = 500L
    private val DELAY_PASSWORD_FIELD = 600L // Slightly after email
    private val DELAY_OR_TEXT = 750L        // "Or" text
    private val DELAY_CONTINUE_TEXT = 750L  // "Continue With" - same time as "Or"
    private val DELAY_SOCIAL_BUTTONS_GROUP = 900L // Delay for the start of the social button group
    private val DELAY_LOGIN_BUTTON = 1050L
    private val DELAY_SIGNUP_TEXT = 1200L

    // Assuming textView12 (Dev Name) is also to be animated
    private val DELAY_DEV_NAME = 1350L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Call early
        setContentView(binding.root)
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        // Initialize Firebase Auth
        auth = Firebase.auth
        // Initialize Firebase Database
        database = Firebase.database.reference
        // Initialize Google Sign-In Client
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        // Activity Enter Transition (if LoginActivity is NOT the launcher)
        // Ensure fade_in_activity and fade_out_activity exist in res/anim
        overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity)

        // --- Load ALL Unique Animations (Ensure these are in res/anim) ---
        // These names must match your actual animation XML files
        val dropBounceLogo = AnimationUtils.loadAnimation(this, R.anim.drop_bounce_logo)
        val titleRevealUp = AnimationUtils.loadAnimation(this, R.anim.title_reveal_up)
        val slideInFromLeftOvershoot =
            AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left_overshoot)
        val slideInFromRightOvershoot =
            AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right_overshoot)
        val buttonEmergeRotate = AnimationUtils.loadAnimation(this, R.anim.button_emerge_rotate)
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in) // General purpose fade
        val subtleRise = AnimationUtils.loadAnimation(
            this,
            R.anim.subtle_rise_fade_in
        ) // For less prominent text
        val clickScale = AnimationUtils.loadAnimation(this, R.anim.click_scale) // For button clicks


        // --- Prepare Views for Staggered Animation (Set to INVISIBLE initially) ---
        val viewsToAnimate = listOf(
            binding.imageView3, binding.textView6, binding.textView8,
            binding.emailAddress, binding.passwordText,
            binding.textView9, binding.textView10, binding.googleButton, binding.facebookButton,
            binding.loginbutton, binding.donthavebutton, binding.textView12 // Added textView12
        )
        viewsToAnimate.forEach { it.visibility = View.INVISIBLE }

        val handler = Handler(Looper.getMainLooper())

        // --- Start UNIQUE Staggered Entrance Animations ---

        // Logo
        applyAnimationWithDelay(binding.imageView3, dropBounceLogo, DELAY_LOGO, handler)

        // Main Title
        applyAnimationWithDelay(binding.textView6, titleRevealUp, DELAY_MAIN_TITLE, handler)

        // Sub Header "Login To Your Admin Dashboard" (using a generic fadeIn or subtleRise)
        applyAnimationWithDelay(binding.textView8, subtleRise, DELAY_SUB_HEADER, handler)

        // Email Field
        applyAnimationWithDelay(
            binding.emailAddress,
            slideInFromLeftOvershoot,
            DELAY_EMAIL_FIELD,
            handler
        )

        // Password Field
        applyAnimationWithDelay(
            binding.passwordText,
            slideInFromRightOvershoot,
            DELAY_PASSWORD_FIELD,
            handler
        )

        // "Or" Text
        applyAnimationWithDelay(binding.textView9, fadeIn, DELAY_OR_TEXT, handler)

        // "Continue With" Text
        applyAnimationWithDelay(binding.textView10, fadeIn, DELAY_CONTINUE_TEXT, handler)

        // Social Login Buttons - Grouped with internal stagger
        handler.postDelayed({
            // Google Button
            binding.googleButton.visibility = View.VISIBLE
            binding.googleButton.startAnimation(buttonEmergeRotate)

            // Facebook Button - Staggered slightly after Google button
            Handler(Looper.getMainLooper()).postDelayed({
                binding.facebookButton.visibility = View.VISIBLE
                binding.facebookButton.startAnimation(buttonEmergeRotate)
            }, 150) // 150ms delay for the second social button relative to the first
        }, DELAY_SOCIAL_BUTTONS_GROUP)

        // Login Button
        applyAnimationWithDelay(
            binding.loginbutton,
            buttonEmergeRotate,
            DELAY_LOGIN_BUTTON,
            handler
        ) // Can reuse buttonEmergeRotate or have a specific one

        // "Don't have account?" Text
        applyAnimationWithDelay(binding.donthavebutton, subtleRise, DELAY_SIGNUP_TEXT, handler)

        // Developer Name (textView12)
        applyAnimationWithDelay(binding.textView12, subtleRise, DELAY_DEV_NAME, handler)


        // --- Click Listeners with Animations and Transitions ---
        binding.loginbutton.setOnClickListener {
            // Get text from Edit text
            email = binding.emailAddress.text.toString()
            password = binding.passwordText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                val toast =
                    Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT)
                        .show()
            } else {
                createUserAccount(email, password)
            }

            binding.googleButton.setOnClickListener {
                val signIntent = googleSignInClient.signInIntent
                launcher.launch(signIntent)
            }

            it.startAnimation(clickScale)
            // TODO: Add your actual admin login logic here

            // Example: Navigate to AdminMainActivity (replace with your actual target)
            // For testing shared element transition, temporarily point to SignUpActivity
            // if it has the 'logo_transition' name on its logo.
            // val intent = Intent(this, AdminMainActivity::class.java)
//            val intent = Intent(this, SignUpActivity::class.java) // FOR TESTING SHARED ELEMENT

            // Shared Element Transition for the logo
            // The ImageView in the target Activity must also have android:transitionName="logo_transition"
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                binding.imageView3, // The shared view in the current activity
                "logo_transition"   // The transitionName defined in XML
            )
//            startActivity(intent, options.toBundle())
            // finish() // Optional: Finish LoginActivity after navigating
        }

        binding.donthavebutton.setOnClickListener {
            it.startAnimation(clickScale)
            // TODO: Decide navigation. If admin sign-up exists, navigate there.
            // Otherwise, this button might not be needed for an admin app.
            // Assuming SignUpActivity exists for the admin app for now.
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            // Ensure slide_in_right_activity and slide_out_left_activity exist
            overridePendingTransition(
                R.anim.slide_in_right_activity,
                R.anim.slide_out_left_activity
            )
        }

        binding.googleButton.setOnClickListener { // Google Button
            it.startAnimation(clickScale)
            // TODO: Implement Google Sign-In for Admin
        }

        binding.facebookButton.setOnClickListener { // Facebook Button
            it.startAnimation(clickScale)
            // TODO: Implement Facebook Sign-In for Admin
        }
    }

    private fun createUserAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                updateUI(user)
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT)
                            .show()
                        saveUserData()
                        updateUI(user)
                    } else {
                        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                        Log.d("Account", "createUserAccount: Failure", task.exception)
                    }
                }
            }
        }
    }

    private fun saveUserData() {
        // Get text from Edittext
        val userName = binding.emailAddress.text.toString().trim()
        val nameOfRestaurant = binding.passwordText.text.toString().trim()

        val user = UserModel(userName, nameOfRestaurant, email, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        // Save user Data Firebase Database
        userId.let {
            database.child("users").child(it).setValue(user)
        }
    }


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
        // Activity Exit Transition (when back button is pressed or finish() is called)
        // Ensure fade_in_activity and fade_out_activity exist
        overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity)
    }

    // Google Sign-In launcher
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount = task.result
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            // Successfully sign in with Google
                            Toast.makeText(
                                this,
                                "Successfully Sign-In with Google",
                                Toast.LENGTH_SHORT
                            ).show()
                            updateUI(authTask.result?.user)
                            finish()
                        } else {
                            Toast.makeText(this, "Sign-In with Google Failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Sign-In with Google Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

    // Check if user is already logged in
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}



