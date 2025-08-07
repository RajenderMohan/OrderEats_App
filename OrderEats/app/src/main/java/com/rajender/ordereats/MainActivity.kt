package com.rajender.ordereats

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController // This is the NavController interface/class
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController // Import for setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView // Import for BottomNavigationView
import com.rajender.ordereats.Fragment.Notification_Botton_Fragment
import com.rajender.ordereats.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController: NavController = findNavController(R.id.fragmentContainerView)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setupWithNavController(navController)
        binding.notificationButton.setOnClickListener {
            val bottomSheetDialog = Notification_Botton_Fragment()
            bottomSheetDialog.show(supportFragmentManager, "TAG")
        }

    }
}
