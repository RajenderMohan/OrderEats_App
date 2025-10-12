package com.rajender.ordereats

// import com.google.firebase.database.FirebaseDatabase // Only if specifically needed here
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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.rajender.ordereats.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    // private lateinit var database: FirebaseDatabase // Only initialize if you use it in LoginActivity
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

    // Define delays for staggering
    companion object { // Using companion object for constants
        private const val TAG = "LoginActivity" // For Logging
        private const val DELAY_LOGO = 100L
        private const val DELAY_APP_NAME = DELAY_LOGO + 200L
        private const val DELAY_TAGLINE = DELAY_APP_NAME + 200L
        private const val DELAY_LOGIN_PROMPT = DELAY_TAGLINE + 200L
        private const val DELAY_EMAIL_FIELD = DELAY_LOGIN_PROMPT + 250L
        private const val DELAY_PASSWORD_FIELD = DELAY_EMAIL_FIELD // Can start simultaneously or slightly after
        private const val DELAY_OR_TEXT = DELAY_PASSWORD_FIELD + 300L
        private const val DELAY_CONTINUE_TEXT = DELAY_OR_TEXT // Converging, start at same time as "Or"
        private const val DELAY_GOOGLE_BUTTON = DELAY_OR_TEXT + 250L
        private const val DELAY_FACEBOOK_BUTTON = DELAY_GOOGLE_BUTTON // Can start simultaneously
        private const val DELAY_LOGIN_BUTTON = DELAY_GOOGLE_BUTTON + 300L
        private const val DELAY_SIGNUP_PROMPT = DELAY_LOGIN_BUTTON + 200L
        private const val DELAY_DEV_NAME = DELAY_SIGNUP_PROMPT + 300L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Call before setContentView
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle insets if using enableEdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(insets.left, insets.top, insets.right, insets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configure Google Sign-In
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Ensure this string is correct
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        // Initialize the ActivityResultLauncher for Google Sign-In
        googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    if (account != null && account.idToken != null) {
                        firebaseAuthWithGoogle(account.idToken!!)
                    } else {
                        Log.w(TAG, "Google sign in failed: Account or ID Token is null.")
                        Toast.makeText(this, "Google Sign-In Failed: No account data.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: ApiException) {
                    Log.w(TAG, "Google sign in failed with ApiException", e)
                    val errorMessage = when (e.statusCode) {
                        com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.SIGN_IN_CANCELLED -> "Google Sign-In was cancelled. 🤔"
                        com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.NETWORK_ERROR -> "Network error. Please check connection. 🌐"
                        // Add other specific GoogleSignInStatusCodes as needed
                        else -> "Google Sign-In Failed. Error: ${e.localizedMessage} (Code: ${e.statusCode})"
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            } else {
                Log.w(TAG, "Google Sign-In cancelled or failed by user/system. Result Code: ${result.resultCode}")
                Toast.makeText(this, "Google Sign-In Cancelled or Failed", Toast.LENGTH_SHORT).show()
            }
        }

        setupClickListeners()
        setupAnimations()
    }

    private fun setupClickListeners() {
        binding.loginbutton.setOnClickListener {
            val email = binding.emailAddress.text.toString().trim()
            val password = binding.userPassword.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Email and Password can't be blank 😒", Toast.LENGTH_SHORT).show()
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address 🤔", Toast.LENGTH_SHORT).show()
                binding.emailAddress.error = "Invalid email"
                binding.emailAddress.requestFocus()
            }
            else {
                loginUserWithEmail(email, password)
            }
        }

        binding.googleButton.setOnClickListener {
            signInWithGoogle()
        }

        binding.donthavebutton.setOnClickListener {
            startActivity(Intent(this, SignActivity::class.java))
        }

        binding.facebookButton.setOnClickListener {
            Toast.makeText(this, "Facebook login not implemented yet.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun loginUserWithEmail(email: String, password: String) {
        // binding.loginProgressBar.visibility = View.VISIBLE // Show progress bar
        binding.loginbutton.isEnabled = false // Disable button

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                // binding.loginProgressBar.visibility = View.GONE // Hide progress bar
                binding.loginbutton.isEnabled = true // Re-enable button

                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(this, "Login Successful! 😊", Toast.LENGTH_SHORT).show()
                    updateUi(auth.currentUser)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Login failed: ${task.exception?.localizedMessage ?: "Unknown error"} 😥",
                        Toast.LENGTH_LONG,
                    ).show()
                }
            }
    }

    private fun signInWithGoogle() {
        // binding.googleProgressBar.visibility = View.VISIBLE // Show progress bar
        binding.googleButton.isEnabled = false // Disable button
        val signInIntent = googleSignInClient.signInIntent
        try {
            googleSignInLauncher.launch(signInIntent)
        } catch (e: Exception) {
            // binding.googleProgressBar.visibility = View.GONE
            binding.googleButton.isEnabled = true
            Log.e(TAG, "Google Sign-In launch failed", e)
            Toast.makeText(this, "Google Play Services error. Please update or enable.", Toast.LENGTH_LONG).show()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                // binding.googleProgressBar.visibility = View.GONE // Hide progress bar
                binding.googleButton.isEnabled = true // Re-enable button

                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithGoogleCredential:success")
                    Toast.makeText(this, "Google Sign-In Successful! 😊", Toast.LENGTH_SHORT).show()
                    updateUi(auth.currentUser)
                } else {
                    Log.w(TAG, "signInWithGoogleCredential:failure", task.exception)
                    Toast.makeText(
                        this,
                        "Google Authentication Failed: ${task.exception?.localizedMessage ?: "Unknown error"} 😥",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }


    private fun updateUi(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setupAnimations() {
        val viewsToMakeInvisible = listOf(
            binding.imageView3, binding.textView6, binding.textView7, binding.textView8,
            binding.emailAddress, binding.userPassword,
            binding.textView9, binding.textView10, binding.googleButton, binding.facebookButton,
            binding.loginbutton, binding.donthavebutton, binding.textView12
        )
        viewsToMakeInvisible.forEach { it.visibility = View.INVISIBLE }

        val dropSettleAnim = AnimationUtils.loadAnimation(this, R.anim.drop_settle_fade_in)
        val zoomCenterAnim = AnimationUtils.loadAnimation(this, R.anim.zoom_center_fade_in)
        val slideUpRotateAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up_rotate_fade_in)
        val focusPulseAnim = AnimationUtils.loadAnimation(this, R.anim.focus_pulse_fade_in)
        val slideInFromLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left)
        val slideInFromRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_right)
        val convergeLeftAnim = AnimationUtils.loadAnimation(this, R.anim.converge_fade_in_left)
        val convergeRightAnim = AnimationUtils.loadAnimation(this, R.anim.converge_fade_in_right)
        val flipInLeftAnim = AnimationUtils.loadAnimation(this, R.anim.flip_in_from_left_y)
        val flipInRightAnim = AnimationUtils.loadAnimation(this, R.anim.flip_in_from_right_y)
        val heroPulseAnim = AnimationUtils.loadAnimation(this, R.anim.hero_pulse_scale_fade_in)
        val subtleRiseAnim = AnimationUtils.loadAnimation(this, R.anim.subtle_rise_fade_in)
        val slideUpFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_slide_up_delayed)

        val handler = Handler(Looper.getMainLooper())

        applyAnimationWithDelay(binding.imageView3, dropSettleAnim, DELAY_LOGO, handler)
        applyAnimationWithDelay(binding.textView6, zoomCenterAnim, DELAY_APP_NAME, handler)
        applyAnimationWithDelay(binding.textView7, slideUpRotateAnim, DELAY_TAGLINE, handler)
        applyAnimationWithDelay(binding.textView8, focusPulseAnim, DELAY_LOGIN_PROMPT, handler)
        applyAnimationWithDelay(binding.emailAddress, slideInFromLeft, DELAY_EMAIL_FIELD, handler)
        applyAnimationWithDelay(binding.userPassword, slideInFromRight, DELAY_PASSWORD_FIELD, handler)
        applyAnimationWithDelay(binding.textView9, convergeLeftAnim, DELAY_OR_TEXT, handler)
        applyAnimationWithDelay(binding.textView10, convergeRightAnim, DELAY_CONTINUE_TEXT, handler)
        applyAnimationWithDelay(binding.googleButton, flipInLeftAnim, DELAY_GOOGLE_BUTTON, handler)
        applyAnimationWithDelay(binding.facebookButton, flipInRightAnim, DELAY_FACEBOOK_BUTTON, handler)
        applyAnimationWithDelay(binding.loginbutton, heroPulseAnim, DELAY_LOGIN_BUTTON, handler)
        applyAnimationWithDelay(binding.donthavebutton, slideUpFadeIn, DELAY_SIGNUP_PROMPT, handler)
        applyAnimationWithDelay(binding.textView12, subtleRiseAnim, DELAY_DEV_NAME, handler)
    }

    private fun applyAnimationWithDelay(
        view: View,
        animation: Animation,
        delay: Long,
        handler: Handler
    ) {
        handler.postDelayed({
            view.visibility = View.VISIBLE
            view.startAnimation(animation)
        }, delay)
    }

    override fun onStart() {
        super.onStart()
//         Optional: Check if user is already signed in and navigate to MainActivity
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
