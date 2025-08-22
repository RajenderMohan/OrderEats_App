package com.rajender.adminordereats


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rajender.adminordereats.databinding.ActivityLoginBinding
import com.rajender.adminordereats.model.UserModel


class LoginActivity : AppCompatActivity() {

    private lateinit var emailString: String // Renamed to avoid conflict with Email class if ever imported fully
    private lateinit var passwordString: String // Renamed for clarity
    // These seem unused, consider removing if not needed later
    // private var userName: String? = null
    // private var nameOfRestaurant: String? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    // Animation Delays
    private val DELAY_LOGO = 0L
    private val DELAY_MAIN_TITLE = 200L
    private val DELAY_SUB_HEADER = 350L
    private val DELAY_EMAIL_FIELD = 500L
    private val DELAY_PASSWORD_FIELD = 600L
    private val DELAY_OR_TEXT = 750L
    private val DELAY_CONTINUE_TEXT = 750L
    private val DELAY_SOCIAL_BUTTONS_GROUP = 900L
    private val DELAY_LOGIN_BUTTON = 1050L
    private val DELAY_SIGNUP_TEXT = 1200L
    private val DELAY_DEV_NAME = 1350L

    companion object {
        private const val TAG = "LoginActivityGoogle" // For Logcat filtering
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance() // Standard way
        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference // Standard way

        // Configure Google Sign-In
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity)

        val dropBounceLogo = AnimationUtils.loadAnimation(this, R.anim.drop_bounce_logo)
        val titleRevealUp = AnimationUtils.loadAnimation(this, R.anim.title_reveal_up)
        val slideInFromLeftOvershoot =
            AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left_overshoot)
        val slideInFromRightOvershoot =
            AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right_overshoot)
        val buttonEmergeRotate = AnimationUtils.loadAnimation(this, R.anim.button_emerge_rotate)
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val subtleRise = AnimationUtils.loadAnimation(this, R.anim.subtle_rise_fade_in)
        val clickScale = AnimationUtils.loadAnimation(this, R.anim.click_scale)

        val viewsToAnimate = listOf(
            binding.imageView3, binding.textView6, binding.textView8,
            binding.emailAddress, binding.passwordText,
            binding.textView9, binding.textView10, binding.googleButton, binding.facebookButton,
            binding.loginbutton, binding.donthavebutton, binding.textView12
        )
        viewsToAnimate.forEach { it.visibility = View.INVISIBLE }

        val handler = Handler(Looper.getMainLooper())

        applyAnimationWithDelay(binding.imageView3, dropBounceLogo, DELAY_LOGO, handler)
        applyAnimationWithDelay(binding.textView6, titleRevealUp, DELAY_MAIN_TITLE, handler)
        applyAnimationWithDelay(binding.textView8, subtleRise, DELAY_SUB_HEADER, handler)
        applyAnimationWithDelay(
            binding.emailAddress,
            slideInFromLeftOvershoot,
            DELAY_EMAIL_FIELD,
            handler
        )
        applyAnimationWithDelay(
            binding.passwordText,
            slideInFromRightOvershoot,
            DELAY_PASSWORD_FIELD,
            handler
        )
        applyAnimationWithDelay(binding.textView9, fadeIn, DELAY_OR_TEXT, handler)
        applyAnimationWithDelay(binding.textView10, fadeIn, DELAY_CONTINUE_TEXT, handler)

        handler.postDelayed({
            binding.googleButton.visibility = View.VISIBLE
            binding.googleButton.startAnimation(buttonEmergeRotate)
            Handler(Looper.getMainLooper()).postDelayed({
                binding.facebookButton.visibility = View.VISIBLE
                binding.facebookButton.startAnimation(buttonEmergeRotate)
            }, 150)
        }, DELAY_SOCIAL_BUTTONS_GROUP)

        applyAnimationWithDelay(binding.loginbutton, buttonEmergeRotate, DELAY_LOGIN_BUTTON, handler)
        applyAnimationWithDelay(binding.donthavebutton, subtleRise, DELAY_SIGNUP_TEXT, handler)
        applyAnimationWithDelay(binding.textView12, subtleRise, DELAY_DEV_NAME, handler)


        // --- Click Listeners with Animations and Transitions ---

        // LOGIN WITH EMAIL AND PASSWORD BUTTON
        binding.loginbutton.setOnClickListener {
            it.startAnimation(clickScale)

            emailString = binding.emailAddress.text.toString().trim()
            passwordString = binding.passwordText.text.toString().trim()

            if (emailString.isEmpty() || passwordString.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                createUserAccount(emailString, passwordString)
            }
        }

        // "DON'T HAVE ACCOUNT?" (NAVIGATE TO SIGN UP) BUTTON
        binding.donthavebutton.setOnClickListener {
            it.startAnimation(clickScale)
            // TODO: Ensure SignUpActivity is defined and correctly handles admin sign-up
            // or if this button should navigate elsewhere or be removed for an admin app.
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            overridePendingTransition(
                R.anim.slide_in_right_activity,
                R.anim.slide_out_left_activity
            )
        }

        // GOOGLE SIGN-IN BUTTON
        binding.googleButton.setOnClickListener {
            it.startAnimation(clickScale)
            Log.d(TAG, "Google Sign-In button clicked. Launching intent.")
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }

        // FACEBOOK SIGN-IN BUTTON (Placeholder)
        binding.facebookButton.setOnClickListener {
            it.startAnimation(clickScale)
            Toast.makeText(this, "Facebook Sign-In not implemented yet.", Toast.LENGTH_SHORT).show()
            // TODO: Implement Facebook Sign-In for Admin
        }
    } // End of onCreate

    private fun createUserAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                updateUI(user)
            } else {
                // If sign-in fails, attempt to create a new user (admin context)
                // Consider if admin accounts should be created this way or pre-provisioned
                Log.w(TAG, "signInWithEmail:failure", task.exception)
                Toast.makeText(this, "Sign-in failed. Attempting to create account...", Toast.LENGTH_SHORT).show() // User feedback

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { creationTask ->
                    if (creationTask.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(this, "Admin Account Created Successfully", Toast.LENGTH_SHORT).show()
                        // Decide if saveUserData() is needed for admins or if it's for general users
                        // For admins, you might set custom claims or add to a specific 'admins' node in DB
                        saveUserData(user, email, password) // Pass user, email, password if needed by saveUserData
                        updateUI(user)
                    } else {
                        Toast.makeText(this, "Authentication Failed: ${creationTask.exception?.message}", Toast.LENGTH_LONG).show()
                        Log.e(TAG, "createUserWithEmail:failure", creationTask.exception)
                    }
                }
            }
        }
    }

    // Modified saveUserData to potentially take user object if needed for UID
    private fun saveUserData(firebaseUser: FirebaseUser?, email: String, passwordForModel: String) {
        val userId = firebaseUser?.uid
        if (userId == null) {
            Log.e(TAG, "Cannot save user data, FirebaseUser or UID is null.")
            return
        }

        // Using email as userName and restaurantName from password field is unconventional for admin.
        // Re-evaluate what data an admin user model should store.
        // For now, using the provided logic, but it needs review for an admin app.
        val userNameFromEmail = email // Or derive from email if needed
        val restaurantNameFromPasswordField = passwordForModel // Highly unusual, placeholder

        val userModel = UserModel(userNameFromEmail, restaurantNameFromPasswordField, email, passwordForModel)

        database.child("admins").child(userId).setValue(userModel) // Saving under "admins" node
            .addOnSuccessListener {
                Log.d(TAG, "Admin user data saved successfully for UID: $userId")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to save admin user data for UID: $userId", e)
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
        overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity)
    }

    // Google Sign-In launcher
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d(TAG, "Google Sign-In Activity Result: ${result.resultCode}")
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account: GoogleSignInAccount = task.getResult(ApiException::class.java)!! // Added !! as getResult can be null if no account
                    Log.d(TAG, "firebaseAuthWithGoogle. ID Token: ${account.idToken}")
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener(this) { authTask ->
                            if (authTask.isSuccessful) {
                                Log.d(TAG, "signInWithCredentialGoogle:success")
                                val user = auth.currentUser
                                Toast.makeText(this, "Google Sign-In Successful.", Toast.LENGTH_SHORT).show()
                                // Optionally, save Google user specific admin data here or check if they are an admin
                                // For now, directly updating UI
                                updateUI(user)
                                // finish() // updateUI already calls finish()
                            } else {
                                Log.w(TAG, "signInWithCredentialGoogle:failure", authTask.exception)
                                Toast.makeText(this, "Google Sign-In Failed (Firebase Auth). ${authTask.exception?.message}", Toast.LENGTH_LONG).show()
                                updateUI(null) // Or handle failure appropriately
                            }
                        }
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed (ApiException)", e)
                    Toast.makeText(this, "Google Sign In Failed. Code: ${e.statusCode}", Toast.LENGTH_LONG).show()
                    updateUI(null) // Or handle failure appropriately
                }
            } else {
                // Sign-in flow was cancelled or failed before Firebase step
                Log.w(TAG, "Google Sign-In flow cancelled or failed. Result code: ${result.resultCode}")
                Toast.makeText(this, "Google Sign-In Cancelled or Failed.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d(TAG, "User already signed in: ${currentUser.uid}. Navigating to MainActivity.")
            updateUI(currentUser) // Navigate to main if already logged in
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            // Optional: Pass user data to MainActivity if needed
            // intent.putExtra("USER_EMAIL", user.email)
            startActivity(intent)
            finish() // Finish LoginActivity so user can't go back to it
        } else {
            // Stay on LoginActivity or handle null user (e.g., show error)
            Log.d(TAG, "updateUI called with null user. Staying on LoginActivity.")
        }
    }
}
