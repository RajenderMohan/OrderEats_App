package com.rajender.ordereats

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment // Import NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.rajender.ordereats.Fragment.Notification_Botton_Fragment
import com.rajender.ordereats.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Using lateinit for View Binding as it's initialized in onCreate
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // It's generally good practice to inflate binding before setContentView
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // enableEdgeToEdge() // If you are using this, call it before setContentView or right after super.onCreate()
        // And handle window insets if edge-to-edge is enabled:
        // ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
        //     val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        //     view.setPadding(insets.left, insets.top, insets.right, insets.bottom)
        //     WindowInsetsCompat.CONSUMED // Or return windowInsets if you want children to also consume
        // }

        // --- Correct way to get NavController ---
        // 1. Find the NavHostFragment using its ID from your layout XML
        // Ensure R.id.fragmentContainerView is the ID of your NavHostFragment
        // in activity_main.xml
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        // 2. Get the NavController from the NavHostFragment
        navController = navHostFragment.navController

        // 3. Setup BottomNavigationView with NavController
        // Using view binding to access bottomNavigationView for safety and conciseness
        binding.bottomNavigationView.setupWithNavController(navController)

        binding.notificationButton.setOnClickListener {
            // Consider using a more descriptive tag or a companion object constant for the tag
            val bottomSheetDialog = Notification_Botton_Fragment()
            bottomSheetDialog.show(supportFragmentManager, "NotificationBottomSheetTag")
        }
    }
}
