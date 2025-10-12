package com.rajender.ordereats.Fragment // Ensure this is your correct package

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rajender.ordereats.R
import com.rajender.ordereats.adapter.MenuAdapter
import com.rajender.ordereats.databinding.FragmentMenuBottomSheetBinding

class MenuBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentMenuBottomSheetBinding

    private val menuFoodNames = listOf(
        "Cheese Burger", "Veggie Pizza", "Noodles", "Paneer Tikka", "Masala Dosa",
        "Chole Bhature", "Makta Kulfi", "Veg Sandwich", "Momo Platter", "Ice Cream",
        "Kachori", "Desi Jalebi", "Mojito", "Manchurian", "Desi Aalu Paratha"
    )
    private val menuItemPrices = listOf(
        "₹30", "₹120", "₹100", "₹99", "₹79", "₹58", "₹150", "₹50", "₹110",
        "₹99", "₹30", "₹50", "₹50", "₹70", "₹40"
    )
    private val menuImages = listOf(
        R.drawable.burger, R.drawable.banner_pizza, R.drawable.noddles,
        R.drawable.paneer_tikka, R.drawable.dosas, R.drawable.chole_kulche,
        R.drawable.matka_kulfi, R.drawable.sandwich, R.drawable.momos_2,
        R.drawable.ice_cream, R.drawable.kachori, R.drawable.jalebi,
        R.drawable.mojito, R.drawable.manchurian, R.drawable.paratha
    )

    // Animation Delays (in milliseconds)
    private val DELAY_BACK_BUTTON = 50L
    private val DELAY_TITLE = 150L
    private val DELAY_RECYCLER_VIEW_CONTAINER = 250L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        // Optional: Apply a custom style for the BottomSheetDialog if needed
        // e.g., setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")
        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

        // --- 1. Load All Necessary Animations ---
        val slideInFromLeft = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_from_left_simple)
        val fadeInText = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_text)
        val fadeInRecyclerContainer = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in_recycler)
        val clickScale = AnimationUtils.loadAnimation(requireContext(), R.anim.click_scale)

        // --- 2. Prepare Views for Animation (Set initial visibility to INVISIBLE) ---
        binding.buttonBack.visibility = View.INVISIBLE
        binding.menuTitleTextView.visibility = View.INVISIBLE // Ensure ID 'menuTitleTextView' exists in XML
        binding.menuRecyclerView.visibility = View.INVISIBLE

        val handler = Handler(Looper.getMainLooper())

        // --- 3. Apply Staggered Entrance Animations ---
        handler.postDelayed({
            if (isAdded) { // Check if fragment is still added to its activity
                binding.buttonBack.visibility = View.VISIBLE
                binding.buttonBack.startAnimation(slideInFromLeft)
            }
        }, DELAY_BACK_BUTTON)

        handler.postDelayed({
            if (isAdded) {
                binding.menuTitleTextView.visibility = View.VISIBLE
                binding.menuTitleTextView.startAnimation(fadeInText)
            }
        }, DELAY_TITLE)

        handler.postDelayed({
            if (isAdded) {
                binding.menuRecyclerView.visibility = View.VISIBLE
                binding.menuRecyclerView.startAnimation(fadeInRecyclerContainer)
                setupMenuRecyclerViewAndItemAnimations()
            }
        }, DELAY_RECYCLER_VIEW_CONTAINER)

        // --- 4. Setup Click Listener for Back Button ---
        binding.buttonBack.setOnClickListener {
            Log.d(TAG, "Back button clicked")
            it.startAnimation(clickScale)
            Handler(Looper.getMainLooper()).postDelayed({
                if (isAdded) {
                    dismiss()
                }
            }, 150)
        }
    }

    private fun setupMenuRecyclerViewAndItemAnimations() {
        Log.d(TAG, "setupMenuRecyclerViewAndItemAnimations")
        // Ensure adapter and layout manager are setup correctly
        // Ensure all drawable resources for images exist.
        if (menuFoodNames.size == menuItemPrices.size && menuFoodNames.size == menuImages.size) {
            val adapter = MenuAdapter(
                ArrayList(menuFoodNames),
                ArrayList(menuItemPrices),
                ArrayList(menuImages),
                requireContext()
            )
            binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.menuRecyclerView.adapter = adapter

            val itemLayoutAnimationController =
                AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_from_bottom)
            binding.menuRecyclerView.layoutAnimation = itemLayoutAnimationController
        } else {
            Log.e(TAG, "Data lists (food names, prices, images) have different sizes! Adapter cannot be created correctly.")
            // Optionally, show a toast or handle this error state in the UI
            // Toast.makeText(context, "Error loading menu items.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
        // Since 'binding' is lateinit and initialized in onCreateView,
        // and its root is returned, it will be managed by the view lifecycle.
        // Explicitly nullifying is not strictly necessary for this pattern,
        // but if you used a nullable _binding, you would do _binding = null here.
    }

    companion object {
        // TAG for identifying the fragment, used when showing
        const val TAG = "MenuBottomSheetFragment"

        // Factory method to create new instances of this fragment
        @JvmStatic // Good practice if calling from Java
        fun newInstance(): MenuBottomSheetFragment {
            // If you needed to pass arguments:
            // val fragment = MenuBottomSheetFragment()
            // val args = Bundle()
            // args.putString("SOME_KEY", "some_value")
            // fragment.arguments = args
            // return fragment
            return MenuBottomSheetFragment()
        }
    }
}
