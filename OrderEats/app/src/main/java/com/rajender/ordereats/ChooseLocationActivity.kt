package com.rajender.ordereats

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.rajender.ordereats.databinding.ActivityChooseLocationBinding

class ChooseLocationActivity : AppCompatActivity() {
    private val binding : ActivityChooseLocationBinding by lazy {
        ActivityChooseLocationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge() is fine, but for this simple layout, you might not need it.
        setContentView(binding.root)

        val locationList = arrayOf("Jaipur", "Delhi", "Uttar-Pradesh", "Odisha", "Punjab", "Haryana")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)

        // Use binding directly to access the view
        val autoCompleteTextView = binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)

        // Add this listener to handle the redirection
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            // 1. Get the selected location string
            val selectedLocation = parent.getItemAtPosition(position) as String

            // 2. Create an Intent to navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)

            // 3. (Optional but recommended) Pass the selected location to MainActivity
            intent.putExtra("SelectedLocation", selectedLocation)

            // 4. Start MainActivity
            startActivity(intent)

            // 5. Finish ChooseLocationActivity so the user can't go back to it
            finish()
        }
    }
}
