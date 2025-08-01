package com.rajender.ordereats

import android.content.Intent // <<--- YEH IMPORT ADD KAREIN
import android.os.Bundle
import android.os.Handler     // <<--- IS IMPORT KO BADLEIN
import android.os.Looper      // <<--- YEH IMPORT ADD KAREIN (Handler ke liye behtar practice)
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
// androidx.core.view.ViewCompat aur androidx.core.view.WindowInsetsCompat yahan istemal nahi ho rahe hain,
// lekin agar enableEdgeToEdge() ke liye zaroori hain toh rakhein.

@Suppress("DEPRECATION") // Agar aap android.os.Handler() ke purane constructor ko target kar rahe hain
class splash_screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        this.setContentView(R.layout.activity_splash_screen)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, SignActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000) // 3000 milliseconds = 3 seconds
    }
}
