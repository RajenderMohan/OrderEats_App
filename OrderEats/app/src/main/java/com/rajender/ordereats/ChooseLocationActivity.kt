package com.rajender.ordereats

// Removed: import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
// androidx.core.view.ViewCompat and WindowInsetsCompat are fine for enableEdgeToEdge
import com.rajender.ordereats.databinding.ActivityChooseLocationBinding
// Ensure this import is present if you use R.id.some_id, R.string.some_string from your app
// For android.R.layout.simple_list_item_1, this specific import is not strictly needed
// but it's good practice to have your project's R class imported if you use any of your own resources.
import com.rajender.ordereats.R

class ChooseLocationActivity : AppCompatActivity() {
    private val binding : ActivityChooseLocationBinding by lazy {
        ActivityChooseLocationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val locationList = arrayOf("Jaipur", "Delhi", "Uttar-Pradesh", "Odisha", "Punjab", "Haryana")
        // Corrected line:
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)

        val autoCompleteTextView: AutoCompleteTextView = binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)

        // It's good practice to also enable the dropdown to show on click
        // if it's meant to behave like a spinner
        autoCompleteTextView.isFocusable = false
        autoCompleteTextView.isClickable = true
        autoCompleteTextView.setOnClickListener {
            autoCompleteTextView.showDropDown()
        }
    }
}
