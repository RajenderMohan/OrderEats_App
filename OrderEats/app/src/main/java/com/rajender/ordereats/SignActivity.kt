package com.rajender.ordereats

import android.app.Activity
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
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.rajender.ordereats.databinding.ActivitySignBinding
import com.rajender.ordereats.model.UserModel

class SignActivity : AppCompatActivity() {

    private var inputUsername: String = ""
    private var inputEmail: String = ""
    private var inputPassword: String = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient
    private val binding: ActivitySignBinding by lazy {
        ActivitySignBinding.inflate(layoutInflater)
    }
    // Animation Delays
    private val DELAY_LOGO = 100L
    private val DELAY_APP_NAME = DELAY_LOGO + 150L
    private val DELAY_TAGLINE = DELAY_APP_NAME + 150L
    private val DELAY_SIGNUP_PROMPT = DELAY_TAGLINE + 150L
    private val DELAY_NAME_FIELD = DELAY_SIGNUP_PROMPT + 200L
    private val DELAY_EMAIL_FIELD = DELAY_NAME_FIELD + 100L
    private val DELAY_PASSWORD_FIELD = DELAY_EMAIL_FIELD + 100L
    private val DELAY_OR_TEXT = DELAY_PASSWORD_FIELD + 250L
    private val DELAY_SIGNUP_WITH_TEXT = DELAY_OR_TEXT
    private val DELAY_GOOGLE_BUTTON = DELAY_OR_TEXT + 200L
    private val DELAY_FACEBOOK_BUTTON = DELAY_GOOGLE_BUTTON
    private val DELAY_CREATE_ACCOUNT_BUTTON = DELAY_GOOGLE_BUTTON + 250L
    private val DELAY_ALREADY_HAVE_ACCOUNT_PROMPT = DELAY_CREATE_ACCOUNT_BUTTON + 200L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference // Using KTX

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // CRITICAL: Ensure this is correct
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.createAccountButton.setOnClickListener {
            inputUsername = binding.userName.text.toString().trim()
            inputEmail = binding.userEmail.text.toString().trim()
            inputPassword = binding.userPassword.text.toString().trim()

            if (inputEmail.isBlank() || inputPassword.isBlank() || inputUsername.isBlank()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else if (inputPassword.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(inputEmail, inputPassword)
            }
        }

        binding.alreadyhavebutton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            // finish() // Optional
        }

        binding.googleButton.setOnClickListener {
            // Check if googleSignInClient is initialized (it should be by this point in onCreate)
            if (::googleSignInClient.isInitialized) {
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            } else {
                // This case should ideally not happen if initialization in onCreate is correct
                Toast.makeText(this, "Google Sign-In is not ready. Please try again.", Toast.LENGTH_LONG).show()
                Log.e("SignActivity", "GoogleSignInClient was not initialized before googleButton click.")
                // You might want to re-initialize it here or guide the user, though it points to an earlier setup issue.
            }
        }
        setupAnimations()
    }

    private fun setupAnimations() {
        val viewsToMakeInvisible = listOf(
            binding.imageView3, binding.textView6, binding.textView7, binding.textView8,
            binding.userName, binding.userEmail, binding.userPassword,
            binding.textView9, binding.textView10, binding.googleButton, binding.facebookButton,
            binding.createAccountButton, binding.alreadyhavebutton
        )
        viewsToMakeInvisible.forEach { it.visibility = View.INVISIBLE }

        val dropSettleAnim = AnimationUtils.loadAnimation(this, R.anim.drop_settle_fade_in)
        val zoomCenterAnim = AnimationUtils.loadAnimation(this, R.anim.zoom_center_fade_in)
        val slideUpRotateAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up_rotate_fade_in)
        val focusPulseAnim = AnimationUtils.loadAnimation(this, R.anim.focus_pulse_fade_in)
        val slideInFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left)
        val slideUpForEmail = AnimationUtils.loadAnimation(this, R.anim.fade_in_slide_up_delayed)
        val slideInFromRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right)
        val convergeLeftAnim = AnimationUtils.loadAnimation(this, R.anim.converge_fade_in_left)
        val convergeRightAnim = AnimationUtils.loadAnimation(this, R.anim.converge_fade_in_right)
        val flipInLeftAnim = AnimationUtils.loadAnimation(this, R.anim.flip_in_from_left_y)
        val flipInRightAnim = AnimationUtils.loadAnimation(this, R.anim.flip_in_from_right_y)
        val heroPulseAnim = AnimationUtils.loadAnimation(this, R.anim.hero_pulse_scale_fade_in)
        val subtleRiseAnim = AnimationUtils.loadAnimation(this, R.anim.subtle_rise_fade_in)

        val handler = Handler(Looper.getMainLooper())

        applyAnimationWithDelay(binding.imageView3, dropSettleAnim, DELAY_LOGO, handler)
        applyAnimationWithDelay(binding.textView6, zoomCenterAnim, DELAY_APP_NAME, handler)
        applyAnimationWithDelay(binding.textView7, slideUpRotateAnim, DELAY_TAGLINE, handler)
        applyAnimationWithDelay(binding.textView8, focusPulseAnim, DELAY_SIGNUP_PROMPT, handler)
        applyAnimationWithDelay(binding.userName, slideInFromLeft, DELAY_NAME_FIELD, handler)
        applyAnimationWithDelay(binding.userEmail, slideUpForEmail, DELAY_EMAIL_FIELD, handler)
        applyAnimationWithDelay(binding.userPassword, slideInFromRight, DELAY_PASSWORD_FIELD, handler)
        applyAnimationWithDelay(binding.textView9, convergeLeftAnim, DELAY_OR_TEXT, handler)
        applyAnimationWithDelay(binding.textView10, convergeRightAnim, DELAY_SIGNUP_WITH_TEXT, handler)
        applyAnimationWithDelay(binding.googleButton, flipInLeftAnim, DELAY_GOOGLE_BUTTON, handler)
        applyAnimationWithDelay(binding.facebookButton, flipInRightAnim, DELAY_FACEBOOK_BUTTON, handler)
        applyAnimationWithDelay(binding.createAccountButton, heroPulseAnim, DELAY_CREATE_ACCOUNT_BUTTON, handler)
        applyAnimationWithDelay(binding.alreadyhavebutton, subtleRiseAnim, DELAY_ALREADY_HAVE_ACCOUNT_PROMPT, handler)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(task)
            } else {
                Log.w("SignActivity", "Google Sign-In failed or was cancelled by user. Result Code: ${result.resultCode}")
                Toast.makeText(this, "Google Sign In Failed or Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            // Signed in successfully with Google, now authenticate with Firebase
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("SignActivity", "Firebase signInWithCredential success")
                        // Optional: Save additional Google user details if needed, beyond what Firebase user provides
                        // val firebaseUser = task.result?.user
                        // if (firebaseUser != null && account.displayName != null) {
                        //     saveGoogleSpecificUserData(firebaseUser, account.displayName!!, account.email ?: "", account.photoUrl?.toString() ?: "")
                        // }
                        startActivity(Intent(this, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        })
                        finish()
                    } else {
                        Log.w("SignActivity", "Firebase signInWithCredential failure", task.exception)
                        Toast.makeText(this, "Firebase Authentication Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        } catch (e: ApiException) {
            // Google Sign In failed. Refer to GoogleSignInStatusCodes class for more information.
            Log.w("SignActivity", "Google sign-in failed with ApiException code: " + e.statusCode, e)
            // Providing more specific error messages based on common status codes
            val errorMessage = when (e.statusCode) {
                com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.SIGN_IN_CANCELLED -> "Sign in cancelled by user."
                com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.NETWORK_ERROR -> "Network error. Please check your connection."
                com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.SIGN_IN_FAILED -> "Google Sign In failed. Please try again."
                // GoogleSignInStatusCodes.DEVELOPER_ERROR often means config issue (SHA1, client ID, API enabled)
                com.google.android.gms.common.api.CommonStatusCodes.DEVELOPER_ERROR -> "Developer error. Please check app configuration."
                com.google.android.gms.common.api.CommonStatusCodes.INTERNAL_ERROR -> "Internal error. Please try again later."
                else -> "Google Sign In Failed. Code: ${e.statusCode}"
            }
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            // Handle other types of exceptions
            Log.e("SignActivity", "Google sign-in result encountered an unexpected error", e)
            Toast.makeText(this, "An unexpected error occurred during Google Sign In.", Toast.LENGTH_LONG).show()
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val firebaseUser = task.result?.user
                    if (firebaseUser != null) {
                        Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                        saveUserData(firebaseUser, inputUsername, email, password)
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        Log.e("SignActivity", "Account creation successful but Firebase user is null")
                        Toast.makeText(this, "Account Creation Failed (User null)", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.w("SignActivity", "createAccountWithEmail:failure", task.exception)
                    Toast.makeText(this, "Account Creation Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun saveUserData(firebaseUser: FirebaseUser, name: String, email: String, pass: String) {
        val userId = firebaseUser.uid
        // Note: Storing plain text password in the database is generally NOT recommended.
        // If password is only for Firebase Auth, no need to store it again here.
        val user = UserModel(name = name, email = email, password = pass) // Consider UserModel(name, email)

        database.child("users").child(userId).setValue(user)
            .addOnSuccessListener {
                Log.d("SignActivity", "User data saved successfully for $userId")
            }
            .addOnFailureListener { e ->
                Log.e("SignActivity", "Failed to save user data for $userId", e)
                Toast.makeText(this, "Failed to save user details after account creation.", Toast.LENGTH_SHORT).show()
            }
    }

    // Optional: If you want to save specific details from Google Sign-In not covered by normal sign-up
    // private fun saveGoogleSpecificUserData(firebaseUser: FirebaseUser, googleName: String, googleEmail: String, googlePhotoUrl: String) {
    //     val userId = firebaseUser.uid
    //     // You might want a different data model or update existing fields
    //     val googleUserDetails = mapOf(
    //         "name" to googleName,
    //         "email" to googleEmail,
    //         "photoUrl" to googlePhotoUrl,
    //         "provider" to "google.com" // To indicate the sign-in method
    //     )
    //     database.child("users").child(userId).updateChildren(googleUserDetails)
    //         .addOnSuccessListener {
    //             Log.d("SignActivity", "Google specific user data updated for $userId")
    //         }
    //         .addOnFailureListener { e ->
    //             Log.e("SignActivity", "Failed to update Google specific user data for $userId", e)
    //         }
    // }

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
}
