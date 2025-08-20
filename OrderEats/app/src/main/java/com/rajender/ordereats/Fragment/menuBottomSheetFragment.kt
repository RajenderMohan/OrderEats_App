package com.rajender.ordereats.Fragment // Ensure this is your correct package

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rajender.ordereats.R
import com.rajender.ordereats.adapter.MenuAdapter
import com.rajender.ordereats.databinding.FragmentMenuBottomSheetBinding

// It's a Kotlin convention to use PascalCase for class names
class MenuBottomSheetFragment : BottomSheetDialogFragment() {

    // Using lateinit for view binding as per previous discussions
    private lateinit var binding: FragmentMenuBottomSheetBinding

    // Sample Data - Replace with your actual data source logic
    // Make sure the number of items in each list is the same
    private val menuFoodNames = listOf(
        "Cheese Burger", "Veggie Pizza", "Chicken Noodles", "Paneer Tikka",
        "Masala Dosa", "Chole Bhature", "Spring Rolls", "Veg Sandwich", "Momo Platter"
    )
    private val menuItemPrices = listOf(
        "$8", "$12", "$10", "$9", "$7", "$8", "$6", "$5", "$11"
    )
    private val menuImages = listOf(
        R.drawable.burger,
        R.drawable.pizza,
        R.drawable.noodles,
        R.drawable.paneer, // Replace with your actual drawables
        R.drawable.dosa,
        R.drawable.chole_kulche,
        R.drawable.momo,
        R.drawable.logo_app,
        R.drawable.momos
    )

    // Animation Delays (in milliseconds)
    private val DELAY_BACK_BUTTON = 50L
    private val DELAY_TITLE = 150L
    private val DELAY_RECYCLER_VIEW_CONTAINER = 250L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Optional: Apply a custom style for the BottomSheetDialog if needed
        // e.g., setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // Return type should be non-nullable View
        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- 1. Load All Necessary Animations ---
        val slideInFromLeft =
            AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_left_simple)
        val fadeInText = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_text)
        val fadeInRecyclerContainer =
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_recycler)
        val clickScale = AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.click_scale
        ) // Optional click animation

        // --- 2. Prepare Views for Animation (Set initial visibility to INVISIBLE) ---
        // Ensure menuTitleTextView ID exists in your fragment_menu_bottom_sheet.xml
        binding.buttonBack.visibility = View.INVISIBLE
        binding.menuTitleTextView.visibility = View.INVISIBLE // This ID must be in your XML
        binding.menuRecyclerView.visibility = View.INVISIBLE

        val handler = Handler(Looper.getMainLooper())

        // --- 3. Apply Staggered Entrance Animations to individual UI elements ---
        handler.postDelayed({
            binding.buttonBack.visibility = View.VISIBLE
            binding.buttonBack.startAnimation(slideInFromLeft)
        }, DELAY_BACK_BUTTON)

        handler.postDelayed({
            binding.menuTitleTextView.visibility = View.VISIBLE // Ensure ID is correct
            binding.menuTitleTextView.startAnimation(fadeInText)
        }, DELAY_TITLE)

        handler.postDelayed({
            binding.menuRecyclerView.visibility =
                View.VISIBLE // Make RecyclerView container visible
            binding.menuRecyclerView.startAnimation(fadeInRecyclerContainer) // Animate the container
            setupMenuRecyclerViewAndItemAnimations() // Setup adapter and then item animations
        }, DELAY_RECYCLER_VIEW_CONTAINER)


        // --- 4. Setup Click Listener for Back Button (with optional click animation) ---
        binding.buttonBack.setOnClickListener {
            it.startAnimation(clickScale) // Apply click feedback
            // Add a small delay for the click animation to be visible before dismissing
            Handler(Looper.getMainLooper()).postDelayed({
                dismiss() // Close the bottom sheet
            }, 150) // Adjust delay as needed, or remove if not desired
        }
    }

    private fun setupMenuRecyclerViewAndItemAnimations() {
        // --- 5. Setup RecyclerView Adapter ---
        // Ensure you have a MenuAdapter and it's correctly configured
        val adapter = MenuAdapter(
            ArrayList(menuFoodNames),
            ArrayList(menuItemPrices),
            ArrayList(menuImages), requireContext()
        )
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter

        // --- 6. Apply LayoutAnimation for RecyclerView item entrance ---
        val itemLayoutAnimationController =
            AnimationUtils.loadLayoutAnimation(
                requireContext(),
                R.anim.layout_animation_from_bottom
            )
        binding.menuRecyclerView.layoutAnimation = itemLayoutAnimationController
    }

    companion object {
        const val TAG = "MenuBottomSheetFragment" // TAG for identifying the fragment

        // Factory method to create new instances of this fragment
        fun newInstance(): MenuBottomSheetFragment {
            return MenuBottomSheetFragment()
        }
    }
}
