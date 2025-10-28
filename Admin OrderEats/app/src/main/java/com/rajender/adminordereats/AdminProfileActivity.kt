package com.rajender.adminordereats

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rajender.adminordereats.databinding.ActivityAdminProfileBinding

class AdminProfileActivity : AppCompatActivity() {

    private val binding: ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }

    // ट्रैक करें कि क्या यूजर अभी एडिट कर रहा है
    private var isEditingEnabled = false

    // सभी EditTexts की लिस्ट, ताकि उन पर एक साथ काम कर सकें
    private lateinit var editableFields: List<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 1. Toolbar को एक्शन बार के रूप में सेट करें
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Toolbar के डिफ़ॉल्ट टाइटल को छिपाएं

        // 2. सभी एडिटेबल फील्ड्स को एक लिस्ट में डालें
        editableFields = listOf(binding.name, binding.address, binding.email, binding.phone, binding.password)

        // 3. शुरू में सभी फील्ड्स को नॉन-एडिटेबल रखें और सेव बटन को छिपाएं
        setFieldsEditable(false)
        binding.saveInfoButton.hide() // FAB को छिपाने का सही तरीका

        // 4. क्लिक लिसनर्स सेट करें
        setupClickListeners()

        // 5. एंट्री एनिमेशन चलाएं
        runEnterAnimation()
    }

    private fun setupClickListeners() {
        // बैक बटन का क्लिक लिसनर
        binding.backButton.setOnClickListener {
            finish() // एक्टिविटी बंद करें
        }

        // एडिट FAB का क्लिक लिसनर
        binding.editProfileButton.setOnClickListener {
            isEditingEnabled = !isEditingEnabled // एडिट मोड को टॉगल करें
            setFieldsEditable(isEditingEnabled)
        }

        // सेव बटन (saveInfoButton) का क्लिक लिसनर
        binding.saveInfoButton.setOnClickListener {
            // TODO: यहां डेटाबेस में जानकारी सेव करने का लॉजिक लिखें
            val name = binding.name.text.toString()
            val address = binding.address.text.toString()
            // ...

            // सेव करने के बाद, एडिट मोड को बंद कर दें
            isEditingEnabled = false
            setFieldsEditable(false)
            Toast.makeText(this, "Information Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * यह फंक्शन सभी इनपुट फील्ड्स की एडिटिंग को इनेबल या डिसेबल करता है।
     */
    private fun setFieldsEditable(isEditable: Boolean) {
        // सभी फील्ड्स पर isEnabled सेट करें
        editableFields.forEach { field ->
            field.isEnabled = isEditable
        }

        if (isEditable) {
            // जब एडिट मोड ऑन हो:
            // कर्सर को नाम वाले फील्ड पर ले जाएं और उसे सेलेक्ट करें
            binding.name.requestFocus()
            binding.name.setSelection(binding.name.text.length)

            // एडिट FAB के आइकॉन को 'close' में बदलें
            binding.editProfileButton.setImageResource(R.drawable.ic_close)

            // सेव बटन को एनिमेशन के साथ दिखाएं
            binding.saveInfoButton.show()

        } else {
            // जब एडिट मोड ऑफ हो:
            // एडिट FAB के आइकॉन को वापस 'edit' में बदलें
            binding.editProfileButton.setImageResource(R.drawable.edit_icon)

            // सेव बटन को एनिमेशन के साथ छिपाएं
            binding.saveInfoButton.hide()
        }
    }

    /**
     * स्क्रीन खुलने पर शुरुआती एनिमेशन चलाता है।
     */
    private fun runEnterAnimation() {
        // CollapsingToolbarLayout में एनिमेशन की ज़रूरत नहीं, यह खुद स्क्रॉल पर एनिमेट होता है।

        // LinearLayout में लेआउट एनिमेशन शुरू करें
        // यह XML में android:layoutAnimation से हैंडल हो रहा है
        binding.linearLayout.layoutAnimation = AnimationUtils.loadLayoutAnimation(this, R.anim.slide_up_animation)
        binding.linearLayout.scheduleLayoutAnimation()
    }

    override fun finish() {
        super.finish()
        // एक्टिविटी बंद होते समय ट्रांज़िशन लगाएं
        // (अगर आप चाहें तो यहां fade ट्रांज़िशन भी इस्तेमाल कर सकते हैं)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
