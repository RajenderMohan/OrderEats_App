package com.rajender.adminordereats

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rajender.adminordereats.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.addMenu.setOnClickListener {
            val i1 = Intent(this, AddItemActivity::class.java)
            startActivity(i1)
        }
        binding.allItemMenu.setOnClickListener {
            val i2 = Intent(this, AllItemActivity::class.java)
            startActivity(i2)
        }
        // In PreviousActivity.kt
        binding.outForDeliveryButton.setOnClickListener {
            val intent = Intent(this, OutForDeliveryActivity::class.java)
            startActivity(intent) // This line starts OutForDeliveryActivity
        }

    }
}